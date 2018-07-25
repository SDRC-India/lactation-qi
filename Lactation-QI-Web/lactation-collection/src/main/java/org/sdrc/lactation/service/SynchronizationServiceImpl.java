package org.sdrc.lactation.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.sdrc.lactation.domain.Area;
import org.sdrc.lactation.domain.LactationUser;
import org.sdrc.lactation.domain.LogBreastFeedingPostDischarge;
import org.sdrc.lactation.domain.LogBreastFeedingSupportivePractice;
import org.sdrc.lactation.domain.LogExpressionBreastFeed;
import org.sdrc.lactation.domain.LogFeed;
import org.sdrc.lactation.domain.Patient;
import org.sdrc.lactation.domain.TypeDetails;
import org.sdrc.lactation.model.BFExpressionModel;
import org.sdrc.lactation.model.BFPDModel;
import org.sdrc.lactation.model.BFSPModel;
import org.sdrc.lactation.model.FeedExpressionModel;
import org.sdrc.lactation.model.PatientModel;
import org.sdrc.lactation.model.SyncModel;
import org.sdrc.lactation.model.SyncResult;
import org.sdrc.lactation.model.UserModel;
import org.sdrc.lactation.repository.AreaRepository;
import org.sdrc.lactation.repository.LactationUserRepository;
import org.sdrc.lactation.repository.LogBreastFeedingPostDischargeRepository;
import org.sdrc.lactation.repository.LogBreastFeedingSupportivePracticeRepository;
import org.sdrc.lactation.repository.LogExpressionBreastFeedRepository;
import org.sdrc.lactation.repository.LogFeedRepository;
import org.sdrc.lactation.repository.PatientRepository;
import org.sdrc.lactation.repository.TypeDetailsRepository;
import org.sdrc.lactation.utils.Constants;
import org.sdrc.lactation.utils.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in) on 9th February 2018 17:10. This
 *         service will be used to write synchronization logic which will handle
 *         data coming from the mobile.
 * @author Ratikanta        
 */

@Service
public class SynchronizationServiceImpl implements SynchronizationService {

	private static final Logger log = LogManager.getLogger(SynchronizationServiceImpl.class);
	
	@Autowired
	private LactationUserRepository lactationUserRepository;

	@Autowired
	private PatientRepository patientRepository;

	@Autowired
	private LogExpressionBreastFeedRepository logExpressionBreastFeedRepository;

	@Autowired
	private LogBreastFeedingPostDischargeRepository logBreastFeedingPostDischargeRepository;

	@Autowired
	private LogFeedRepository logFeedRepository;
	
	@Autowired
	private AreaRepository areaRepository;
	
	@Autowired
	private TypeDetailsRepository typeDetailsRepository;
	
	@Autowired
	private LogBreastFeedingSupportivePracticeRepository logBreastFeedingSupportivePracticeRepository;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	ConfigurableEnvironment configurableEnvironment;
	
	private SimpleDateFormat sdfDateOnly = new SimpleDateFormat("dd-MM-yyyy");
	
	private SimpleDateFormat sdfTimeOnly = new SimpleDateFormat("HH:mm");
	
	private SimpleDateFormat sdfDateTimeWithSeconds = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private SimpleDateFormat sdfDateInteger = new SimpleDateFormat("ddMMyyyyHHmmssSSS");

	/**
	 * @author Naseem Akhtar (naseem@sdrc.co.in) on 12th February 2018 1548.
	 *         This method will receive the forms in form of
	 *         SynchronizationModel. This method will accept List of
	 *         SynchronizationModel which would contain list of users for
	 *         registration purpose and list of data related to a particular
	 *         baby.
	 *  @author Ratikanta
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public SyncResult synchronizeForms(SyncModel syncModel, HttpRequest httpRequest) {

		SyncResult syncResult = new SyncResult();

		//getting area 
		Map<Integer, Area> areaMap = new HashMap<>();
		areaRepository.findAll().forEach(area->areaMap.put(area.getId(), area));
		
		//getting type details
		Map<Integer, TypeDetails> typeDetailsMap = new HashMap<>();
		typeDetailsRepository.findAll().forEach(typeDetails->typeDetailsMap.put(typeDetails.getId(), typeDetails));
		
		// for changing values inside lambda expressions atomic boolean is used.
		AtomicBoolean userFromDifferentInstitution = new AtomicBoolean(false);
		
		List<String> userEmailsByInstitution = new ArrayList<>();
		
		JSONObject userWithDifferentInstituteId = new JSONObject();
		
		
		//Saving users
		if (syncModel.getUsers() != null
				&& !syncModel.getUsers().isEmpty()) {
			
			List<LactationUser> users = new ArrayList<>();
			syncModel.getUsers().forEach(user -> {
				LactationUser existingUser = lactationUserRepository.findByEmail(user.getEmail());
				if(existingUser != null && existingUser.getInstitution().getId() != user.getInstitution()){
					userWithDifferentInstituteId.put("name", existingUser.getFirstName());
					userWithDifferentInstituteId.put("email", user.getEmail());
					userWithDifferentInstituteId.put("oldState", existingUser.getState().getName());
					userWithDifferentInstituteId.put("oldDistrict", existingUser.getDistrict().getName());
					userWithDifferentInstituteId.put("oldInstitute", existingUser.getInstitution().getName());
					userWithDifferentInstituteId.put("newState", areaMap.get(user.getState()).getName());
					userWithDifferentInstituteId.put("newDistrict", areaMap.get(user.getDistrict()).getName());
					userWithDifferentInstituteId.put("newInstitute", areaMap.get(user.getInstitution()).getName());
					userFromDifferentInstitution.set(true);
				}else if(existingUser == null && !userFromDifferentInstitution.get()){
					LactationUser lactationUser = new LactationUser();
					lactationUser.setCountry(new Area(user.getCountry()));
					lactationUser.setCreatedDate(getTimestampFromString(user.getCreatedDate()));
					lactationUser.setDistrict(new Area(user.getDistrict()));
					lactationUser.setEmail(user.getEmail());
					lactationUser.setFirstName(user.getFirstName());
					lactationUser.setInstitution(new Area(user.getInstitution()));
					lactationUser.setLastName(user.getLastName());
					lactationUser.setState(new Area(user.getState()));
					lactationUser.setUpdatedDate(getTimestampFromString(user.getUpdatedDate()));
					lactationUser.setUuidNumber(user.getUuidNumber() == null ? null : user.getUuidNumber());
					
					users.add(lactationUser);
				}
			});
			
			if(!userFromDifferentInstitution.get() && !users.isEmpty()){
				lactationUserRepository.save(users);
			}
			
		}
		
		
		if(!userFromDifferentInstitution.get()){
			
			List<UserModel> userByInstitutionList = new ArrayList<>();
			
			// Fetching user institution wise and iterating through them
			for (LactationUser user : lactationUserRepository.findByInstitutionId(syncModel.getInstituteId())) {
				UserModel userModel = new UserModel();
				userModel.setCountry(user.getCountry().getId());
				userModel.setDistrict(user.getDistrict().getId());
				userModel.setEmail(user.getEmail());
				userModel.setFirstName(user.getFirstName());
				userModel.setInstitution(user.getInstitution().getId());
				userModel.setIsSynced(true);
				userModel.setLastName(user.getLastName());
				userModel.setState(user.getState().getId());
				userModel.setCreatedDate(sdfDateTimeWithSeconds.format(user.getCreatedDate()));
				userModel.setUpdatedDate(sdfDateTimeWithSeconds.format(user.getUpdatedDate()));
				userModel.setUuidNumber(user.getUuidNumber() == null ? null : user.getUuidNumber());
				
				userEmailsByInstitution.add(user.getEmail());
				
				userByInstitutionList.add(userModel);
			}
			
			//getting patients from database
			Map<String, Patient> patientMap = new HashMap<>();
			Set<String> babyCodeList = new HashSet<>();
			
			if (syncModel.getPatients() != null
					&& !syncModel.getPatients().isEmpty()) {
				syncModel.getPatients().forEach(patient -> babyCodeList.add(patient.getBabyCode()));
			}
			if (syncModel.getBfExpressions() != null
					&& !syncModel.getBfExpressions().isEmpty()) {
				syncModel.getBfExpressions().forEach(bfExpression -> babyCodeList.add(bfExpression.getBabyCode()));					
			}
			
			if (syncModel.getFeedExpressions() != null
					&& !syncModel.getFeedExpressions().isEmpty()) {
				syncModel.getFeedExpressions().forEach(feedExpression -> babyCodeList.add(feedExpression.getBabyCode()));					
			}
			
			if (syncModel.getBfsps() != null
					&& !syncModel.getBfsps().isEmpty()) {
				syncModel.getBfsps().forEach(bfsp -> babyCodeList.add(bfsp.getBabyCode()));					
			}
			
			if (syncModel.getBfpds() != null
					&& !syncModel.getBfpds().isEmpty()) {
				syncModel.getBfpds().forEach(bfpd -> babyCodeList.add(bfpd.getBabyCode()));					
			}
			if(!babyCodeList.isEmpty()){
				List<Patient> existingPatients = patientRepository.findByBabyCodeIn(babyCodeList);
				existingPatients.forEach(patient->patientMap.put(patient.getBabyCode(), patient));
			}
			
			
			//Saving patients
			if (syncModel.getPatients() != null
					&& !syncModel.getPatients().isEmpty()) {
				
				List<Patient> patients = new ArrayList<>();
				syncModel.getPatients().forEach(patient -> {
					Patient existingPatient = patientMap.get(patient.getBabyCode());
					
					if(existingPatient != null){
						existingPatient.setAdmissionDateForOutdoorPatients((patient.getAdmissionDateForOutdoorPatients() == null 
								|| patient.getAdmissionDateForOutdoorPatients() == "") ? null :	getDateFromString(patient.getAdmissionDateForOutdoorPatients()));
						existingPatient.setBabyAdmittedTo(patient.getBabyAdmittedTo() == null ? null : new TypeDetails(patient.getBabyAdmittedTo()));
						existingPatient.setBabyCodeHospital((patient.getBabyCodeHospital() == null || patient.getBabyCodeHospital() == "") ? null : patient.getBabyCodeHospital());
						existingPatient.setBabyOf((patient.getBabyOf() == null || patient.getBabyOf() == "") ? null : patient.getBabyOf());
						existingPatient.setBabyWeight(patient.getBabyWeight() == null ? null :patient.getBabyWeight());
						existingPatient.setDeliveryDateAndTime(getTimestampFromDateAndTime(patient.getDeliveryDate(), patient.getDeliveryTime()));
						existingPatient.setDeliveryMethod(patient.getDeliveryMethod() == null ? null : new TypeDetails(patient.getDeliveryMethod()));
						existingPatient.setDischargeDate((patient.getDischargeDate() == null || patient.getDischargeDate() == "") ? null : getDateFromString(patient.getDischargeDate()));
						existingPatient.setGestationalAgeInWeek(patient.getGestationalAgeInWeek() == null ? null : patient.getGestationalAgeInWeek());
						existingPatient.setInpatientOrOutPatient(patient.getInpatientOrOutPatient() == null ? null : new TypeDetails(patient.getInpatientOrOutPatient()));
						existingPatient.setMothersAge(patient.getMothersAge() == null ? null : patient.getMothersAge());
						existingPatient.setMothersPrenatalIntent(patient.getMothersPrenatalIntent() == null ? null : new TypeDetails(patient.getMothersPrenatalIntent()));
						
						if(patient.getNicuAdmissionReason() != null && patient.getNicuAdmissionReason().length != 0) {
							existingPatient.setNicuAdmissionReason(arrayToString(patient.getNicuAdmissionReason()));
						}else {
							existingPatient.setNicuAdmissionReason(null);
						}
						
						existingPatient.setParentsKnowledgeOnHmAndLactation(patient.getParentsKnowledgeOnHmAndLactation() == null ? null : 
							new TypeDetails(patient.getParentsKnowledgeOnHmAndLactation()));
						
						if((patient.getTimeTillFirstExpressionInHour() != null && patient.getTimeTillFirstExpressionInHour() != "") && 
								(patient.getTimeTillFirstExpressionInMinute() != null && patient.getTimeTillFirstExpressionInMinute() != "")) {
							existingPatient.setTimeTillFirstExpression(patient.getTimeTillFirstExpressionInHour() + ":" + patient.getTimeTillFirstExpressionInMinute());
						}else{
							existingPatient.setTimeTillFirstExpression(null);
						}
						
						existingPatient.setUpdatedBy(patient.getUserId());
						existingPatient.setUpdatedDate(getTimestampFromString(patient.getUpdatedDate()));
						existingPatient.setDischargeDate(patient.getDischargeDate() != null && patient.getDischargeDate() != "" ? getDateFromString(patient.getDischargeDate()) : null);
						existingPatient.setUuidNumber(patient.getUuidNumber() == null ? null : patient.getUuidNumber());
					}else {
						Patient newPatient = new Patient();
						
						newPatient.setAdmissionDateForOutdoorPatients((patient.getAdmissionDateForOutdoorPatients() == null 
								|| patient.getAdmissionDateForOutdoorPatients() == "") ? null :	getDateFromString(patient.getAdmissionDateForOutdoorPatients()));
						newPatient.setBabyAdmittedTo(patient.getBabyAdmittedTo() == null ? null : new TypeDetails(patient.getBabyAdmittedTo()));
						newPatient.setBabyCode(patient.getBabyCode());
						newPatient.setBabyCodeHospital((patient.getBabyCodeHospital() == null || patient.getBabyCodeHospital() == "") ? null : patient.getBabyCodeHospital());
						newPatient.setBabyOf((patient.getBabyOf() == null || patient.getBabyOf() == "") ? null : patient.getBabyOf());
						newPatient.setBabyWeight(patient.getBabyWeight() == null ? null :patient.getBabyWeight());
						newPatient.setCreatedBy(patient.getUserId());
						newPatient.setUpdatedBy(patient.getUserId());
						newPatient.setCreatedDate(getTimestampFromString(patient.getCreatedDate()));
						newPatient.setUpdatedDate(getTimestampFromString(patient.getUpdatedDate()));
						newPatient.setDeliveryDateAndTime(getTimestampFromDateAndTime(patient.getDeliveryDate(), patient.getDeliveryTime()));
						newPatient.setDeliveryMethod(patient.getDeliveryMethod() == null ? null : new TypeDetails(patient.getDeliveryMethod()));
						newPatient.setDischargeDate((patient.getDischargeDate() == null || patient.getDischargeDate() == "") ? null : getDateFromString(patient.getDischargeDate()));
						newPatient.setGestationalAgeInWeek(patient.getGestationalAgeInWeek() == null ? null : patient.getGestationalAgeInWeek());
						newPatient.setInpatientOrOutPatient(patient.getInpatientOrOutPatient() == null ? null : new TypeDetails(patient.getInpatientOrOutPatient()));
						newPatient.setMothersAge(patient.getMothersAge() == null ? null : patient.getMothersAge());
						newPatient.setMothersPrenatalIntent(patient.getMothersPrenatalIntent() == null ? null : new TypeDetails(patient.getMothersPrenatalIntent()));
						
						if(patient.getNicuAdmissionReason() != null && patient.getNicuAdmissionReason().length != 0){
							newPatient.setNicuAdmissionReason(arrayToString(patient.getNicuAdmissionReason()));
						}
						
						newPatient.setParentsKnowledgeOnHmAndLactation(patient.getParentsKnowledgeOnHmAndLactation() == null ? null : 
							new TypeDetails(patient.getParentsKnowledgeOnHmAndLactation()));
						
						if((patient.getTimeTillFirstExpressionInHour() != null && patient.getTimeTillFirstExpressionInHour() != "") && 
								(patient.getTimeTillFirstExpressionInMinute() != null && patient.getTimeTillFirstExpressionInMinute() != "")){
							newPatient.setTimeTillFirstExpression(patient.getTimeTillFirstExpressionInHour() + ":" + patient.getTimeTillFirstExpressionInMinute());
						}
						
						newPatient.setUuidNumber(patient.getUuidNumber() == null ? null : patient.getUuidNumber());
						
						patients.add(newPatient);
					}
				});
				List<Patient> savedPatient = patientRepository.save(patients);
				savedPatient.forEach(patient-> patientMap.put(patient.getBabyCode(), patient));
				
			}
			
			
			//Saving BF expression
			if (syncModel.getBfExpressions() != null
					&& !syncModel.getBfExpressions().isEmpty()) {
				
				List<String> uniqueIdList = new ArrayList<>();
				Map<String, LogExpressionBreastFeed> bFEXpressionMap = new HashMap<>();
				syncModel.getBfExpressions().forEach(bfExpression -> uniqueIdList.add(bfExpression.getId()));
				
				List<LogExpressionBreastFeed> existingBFEXpressions = logExpressionBreastFeedRepository.findByUniqueFormIdIn(uniqueIdList);
				existingBFEXpressions.forEach(bFEXpression->bFEXpressionMap.put(bFEXpression.getUniqueFormId(), bFEXpression));
				
				List<LogExpressionBreastFeed> bfExpressions = new ArrayList<>();
				
				syncModel.getBfExpressions().forEach(bFEXpression -> {
					LogExpressionBreastFeed existingBFEXpression = bFEXpressionMap.get(bFEXpression.getId());
					if(existingBFEXpression != null){
						existingBFEXpression.setPatientId(patientMap.get(bFEXpression.getBabyCode()));
						existingBFEXpression.setDateAndTimeOfExpression(getTimestampFromDateAndTime(bFEXpression.getDateOfExpression(),
								bFEXpression.getTimeOfExpression()));
						existingBFEXpression.setMethodOfExpression(bFEXpression.getMethodOfExpression() == null ? null : new TypeDetails(bFEXpression.getMethodOfExpression()));
						existingBFEXpression.setExpressionOccuredLocation(bFEXpression.getLocationOfExpression() == null ? null : new TypeDetails(bFEXpression.getLocationOfExpression()));
						existingBFEXpression.setMilkExpressedFromLeftAndRightBreast(bFEXpression.getVolOfMilkExpressedFromLR() == null ? null : bFEXpression.getVolOfMilkExpressedFromLR());
						existingBFEXpression.setUniqueFormId(bFEXpression.getId());
						existingBFEXpression.setUpdatedDate(getTimestampFromString((bFEXpression.getUpdatedDate())));
						existingBFEXpression.setUpdatedBy(bFEXpression.getUserId());
						existingBFEXpression.setUuidNumber(bFEXpression.getUuidNumber() == null ? null : bFEXpression.getUuidNumber());
						
						if(bFEXpression.getMethodOfExpression() != null && bFEXpression.getMethodOfExpression() == Integer.parseInt(configurableEnvironment.getProperty(Constants.EXPRESSION_METHOD_OTHER))){
							existingBFEXpression.setMethodOfExpressionOthers(bFEXpression.getMethodOfExpressionOthers() == null ? null : 
								(bFEXpression.getMethodOfExpressionOthers().trim()).equals("")? null: bFEXpression.getMethodOfExpressionOthers());
						}
					}else{
						LogExpressionBreastFeed newBFEXpression = new LogExpressionBreastFeed();
						
						newBFEXpression.setPatientId(patientMap.get(bFEXpression.getBabyCode()));
						newBFEXpression.setDateAndTimeOfExpression(getTimestampFromDateAndTime(bFEXpression.getDateOfExpression(),
								bFEXpression.getTimeOfExpression()));
						newBFEXpression.setMethodOfExpression(bFEXpression.getMethodOfExpression() == null ? null : new TypeDetails(bFEXpression.getMethodOfExpression()));
						newBFEXpression.setExpressionOccuredLocation(bFEXpression.getLocationOfExpression() == null ? null : new TypeDetails(bFEXpression.getLocationOfExpression()));
						newBFEXpression.setMilkExpressedFromLeftAndRightBreast(bFEXpression.getVolOfMilkExpressedFromLR() == null ? null : bFEXpression.getVolOfMilkExpressedFromLR());
						newBFEXpression.setUniqueFormId(bFEXpression.getId());
						newBFEXpression.setCreatedDate(getTimestampFromString(bFEXpression.getCreatedDate()));
						newBFEXpression.setCreatedBy(bFEXpression.getUserId());
						newBFEXpression.setUpdatedBy(bFEXpression.getUserId());
						newBFEXpression.setUuidNumber(bFEXpression.getUuidNumber() == null ? null : bFEXpression.getUuidNumber());
						newBFEXpression.setUpdatedDate(getTimestampFromString((bFEXpression.getUpdatedDate())));
						
						if(bFEXpression.getMethodOfExpression() != null && bFEXpression.getMethodOfExpression() == Integer.parseInt(configurableEnvironment.getProperty(Constants.EXPRESSION_METHOD_OTHER))){							
							newBFEXpression.setMethodOfExpressionOthers(bFEXpression.getMethodOfExpressionOthers() == null ? null : 
								(bFEXpression.getMethodOfExpressionOthers().trim()).equals("")? null: bFEXpression.getMethodOfExpressionOthers());
						}
						
						bfExpressions.add(newBFEXpression);
					}
				});
				logExpressionBreastFeedRepository.save(bfExpressions);
				
			}
			
			//Saving feed expression
			if (syncModel.getFeedExpressions() != null
					&& !syncModel.getFeedExpressions().isEmpty()) {
				
				List<LogFeed> feeds = new ArrayList<>();
				List<String> uniqueIdList = new ArrayList<>();
				Map<String, LogFeed> logFeedMap = new HashMap<>();
				syncModel.getFeedExpressions().forEach(feedExpression -> uniqueIdList.add(feedExpression.getId()));
				
				List<LogFeed> existingFeeds = logFeedRepository.findByUniqueFormIdIn(uniqueIdList);
				existingFeeds.forEach(logFeed->logFeedMap.put(logFeed.getUniqueFormId(), logFeed));
				
				syncModel.getFeedExpressions().forEach(logFeed -> {
					LogFeed existingFeed = logFeedMap.get(logFeed.getId());
					if(existingFeed != null){
						existingFeed.setPatientId(patientMap.get(logFeed.getBabyCode()));
						existingFeed.setDateAndTimeOfFeed(getTimestampFromDateAndTime(logFeed.getDateOfFeed(), logFeed.getTimeOfFeed()));
						existingFeed.setFeedMethod(logFeed.getMethodOfFeed() == null ? null : new TypeDetails(logFeed.getMethodOfFeed()));
						existingFeed.setOmmVolume(logFeed.getOmmVolume() == null ? null :logFeed.getOmmVolume());
						existingFeed.setDhmVolume(logFeed.getDhmVolume() == null ? null : logFeed.getDhmVolume());
						existingFeed.setFormulaVolume(logFeed.getFormulaVolume() == null ? null : logFeed.getFormulaVolume());
						existingFeed.setAnimalMilkVolume(logFeed.getAnimalMilkVolume() == null ? null : logFeed.getAnimalMilkVolume());
						existingFeed.setOtherVolume(logFeed.getOtherVolume() == null ? null : logFeed.getOtherVolume());
						existingFeed.setLocationOfFeeding(logFeed.getLocationOfFeeding() == null ? null : new TypeDetails(logFeed.getLocationOfFeeding()));
						existingFeed.setWeightOfBaby(logFeed.getBabyWeight() == null ? null : logFeed.getBabyWeight());
						existingFeed.setUpdatedBy(logFeed.getUserId());
						existingFeed.setUniqueFormId(logFeed.getId());
						existingFeed.setUpdatedDate(getTimestampFromString(logFeed.getUpdatedDate()));
						existingFeed.setUuidNumber(logFeed.getUuidNumber() == null ? null : logFeed.getUuidNumber());
					}else{
						LogFeed newFeed = new LogFeed();
						
						newFeed.setPatientId(patientMap.get(logFeed.getBabyCode()));
						newFeed.setDateAndTimeOfFeed(getTimestampFromDateAndTime(logFeed.getDateOfFeed(), logFeed.getTimeOfFeed()));
						newFeed.setFeedMethod(logFeed.getMethodOfFeed() == null ? null : new TypeDetails(logFeed.getMethodOfFeed()));
						newFeed.setOmmVolume(logFeed.getOmmVolume() == null ? null :logFeed.getOmmVolume());
						newFeed.setDhmVolume(logFeed.getDhmVolume() == null ? null : logFeed.getDhmVolume());
						newFeed.setFormulaVolume(logFeed.getFormulaVolume() == null ? null : logFeed.getFormulaVolume());
						newFeed.setAnimalMilkVolume(logFeed.getAnimalMilkVolume() == null ? null : logFeed.getAnimalMilkVolume());
						newFeed.setOtherVolume(logFeed.getOtherVolume() == null ? null : logFeed.getOtherVolume());
						newFeed.setLocationOfFeeding(logFeed.getLocationOfFeeding() == null ? null : new TypeDetails(logFeed.getLocationOfFeeding()));
						newFeed.setWeightOfBaby(logFeed.getBabyWeight() == null ? null : logFeed.getBabyWeight());
						newFeed.setCreatedBy(logFeed.getUserId());
						newFeed.setUpdatedBy(logFeed.getUserId());
						newFeed.setUniqueFormId(logFeed.getId());
						newFeed.setCreatedDate(getTimestampFromString(logFeed.getCreatedDate()));
						newFeed.setUuidNumber(logFeed.getUuidNumber() == null ? null : logFeed.getUuidNumber());
						newFeed.setUpdatedDate(getTimestampFromString(logFeed.getUpdatedDate()));
						
						feeds.add(newFeed);					
					}
				});
				logFeedRepository.save(feeds);
				
			}
			
			//Saving BFSP
			if (syncModel.getBfsps() != null
					&& !syncModel.getBfsps().isEmpty()) {
				List<String> uniqueIdList = new ArrayList<>();
				List<LogBreastFeedingSupportivePractice> bFSPs = new ArrayList<>();
				Map<String, LogBreastFeedingSupportivePractice> bFSPMap = new HashMap<>();
				
				syncModel.getBfsps().forEach(bfsp -> uniqueIdList.add(bfsp.getId()));
				List<LogBreastFeedingSupportivePractice> existingBFSPs = logBreastFeedingSupportivePracticeRepository.findByUniqueFormIdIn(uniqueIdList);
				existingBFSPs.forEach(bFSP->bFSPMap.put(bFSP.getUniqueFormId(), bFSP));
				
				syncModel.getBfsps().forEach(bFSP -> {
					LogBreastFeedingSupportivePractice existingBFSP = bFSPMap.get(bFSP.getId());
					if(existingBFSP != null){
						existingBFSP.setPatientId(patientMap.get(bFSP.getBabyCode()));
						existingBFSP.setDateAndTimeOfBFSP(getTimestampFromDateAndTime(bFSP.getDateOfBFSP(), bFSP.getTimeOfBFSP()));
						existingBFSP.setBfspPerformed(bFSP.getBfspPerformed() == null ? null : new TypeDetails(bFSP.getBfspPerformed()));
						existingBFSP.setPersonWhoPerformedBFSP(bFSP.getPersonWhoPerformedBFSP() == null ? null : new TypeDetails(bFSP.getPersonWhoPerformedBFSP()));
						existingBFSP.setBfspDuration(bFSP.getBfspDuration() == null ? null : bFSP.getBfspDuration());
						existingBFSP.setUpdatedBy(bFSP.getUserId());
						existingBFSP.setUniqueFormId(bFSP.getId());
						existingBFSP.setUpdatedDate(getTimestampFromString(bFSP.getUpdatedDate()));
						existingBFSP.setUuidNumber(bFSP.getUuidNumber() == null ? null : bFSP.getUuidNumber());
					}else{
						LogBreastFeedingSupportivePractice newBFSP = new LogBreastFeedingSupportivePractice();
						
						newBFSP.setPatientId(patientMap.get(bFSP.getBabyCode()));
						newBFSP.setDateAndTimeOfBFSP(getTimestampFromDateAndTime(bFSP.getDateOfBFSP(), bFSP.getTimeOfBFSP()));
						newBFSP.setBfspPerformed(bFSP.getBfspPerformed() == null ? null : new TypeDetails(bFSP.getBfspPerformed()));
						newBFSP.setPersonWhoPerformedBFSP(bFSP.getPersonWhoPerformedBFSP() == null ? null : new TypeDetails(bFSP.getPersonWhoPerformedBFSP()));
						newBFSP.setBfspDuration(bFSP.getBfspDuration() == null ? null : bFSP.getBfspDuration());
						newBFSP.setCreatedBy(bFSP.getUserId());
						newBFSP.setUpdatedBy(bFSP.getUserId());
						newBFSP.setUniqueFormId(bFSP.getId());
						newBFSP.setCreatedDate(getTimestampFromString(bFSP.getCreatedDate()));
						newBFSP.setUuidNumber(bFSP.getUuidNumber() == null ? null : bFSP.getUuidNumber());
						newBFSP.setUpdatedDate(getTimestampFromString(bFSP.getUpdatedDate()));
						
						bFSPs.add(newBFSP);					
					}
				});
				logBreastFeedingSupportivePracticeRepository.save(bFSPs);
			}
			
			//Saving BFPD
			if (syncModel.getBfpds() != null
					&& !syncModel.getBfpds().isEmpty()) {
				
				List<String> uniqueIdList = new ArrayList<>();
				Map<String, LogBreastFeedingPostDischarge> bFPDMap = new HashMap<>();
				List<LogBreastFeedingPostDischarge> bFPDs = new ArrayList<>();
				
				syncModel.getBfpds().forEach(bfpd -> uniqueIdList.add(bfpd.getId()));
				List<LogBreastFeedingPostDischarge> existingBFPDs = logBreastFeedingPostDischargeRepository.findByUniqueFormIdIn(uniqueIdList);
				existingBFPDs.forEach(bFPD->bFPDMap.put(bFPD.getUniqueFormId(), bFPD));
				
				syncModel.getBfpds().forEach(bFPD -> {
					LogBreastFeedingPostDischarge existingBFPD = bFPDMap.get(bFPD.getId());
					if(existingBFPD != null){
						existingBFPD.setPatientId(patientMap.get(bFPD.getBabyCode()));
						existingBFPD.setDateOfBreastFeeding(getTimestampFromDateAndTime(bFPD.getDateOfBreastFeeding(), "00:00"));
						existingBFPD.setTimeOfBreastFeeding(bFPD.getTimeOfBreastFeeding() == null ? null : new TypeDetails(bFPD.getTimeOfBreastFeeding()));
						existingBFPD.setBreastFeedingStatus(bFPD.getBreastFeedingStatus() == null ? null : new TypeDetails(bFPD.getBreastFeedingStatus()));
						existingBFPD.setUniqueFormId(bFPD.getId());
						existingBFPD.setUuidNumber(bFPD.getUuidNumber() == null ? null : bFPD.getUuidNumber());
						existingBFPD.setUpdatedDate(getTimestampFromString(bFPD.getUpdatedDate()));
						existingBFPD.setUpdatedBy(bFPD.getUserId());
					}else{
						LogBreastFeedingPostDischarge newBFPD = new LogBreastFeedingPostDischarge();
						
						newBFPD.setPatientId(patientMap.get(bFPD.getBabyCode()));
						newBFPD.setDateOfBreastFeeding(getTimestampFromDateAndTime(bFPD.getDateOfBreastFeeding(), "00:00"));
						newBFPD.setTimeOfBreastFeeding(bFPD.getTimeOfBreastFeeding() == null ? null : new TypeDetails(bFPD.getTimeOfBreastFeeding()));
						newBFPD.setBreastFeedingStatus(bFPD.getBreastFeedingStatus() == null ? null : new TypeDetails(bFPD.getBreastFeedingStatus()));
						newBFPD.setUniqueFormId(bFPD.getId());
						newBFPD.setUuidNumber(bFPD.getUuidNumber() == null ? null : bFPD.getUuidNumber());
						newBFPD.setCreatedDate(getTimestampFromString(bFPD.getCreatedDate()));
						newBFPD.setUpdatedDate(getTimestampFromString(bFPD.getUpdatedDate()));
						newBFPD.setCreatedBy(bFPD.getUserId());
						newBFPD.setUpdatedBy(bFPD.getUserId());
						
						bFPDs.add(newBFPD);					
					}
				});
				logBreastFeedingPostDischargeRepository.save(bFPDs);
			}
			
//=================================================================================================================================================================//
//=================================================================================================================================================================//
//=================================================================================================================================================================//
//=================================================================================================================================================================//
			
			/*
			 * @author - Naseem Akhtar on 22nd March 2018 2045
			 * From here the reverse sync logic starts
			 */
			
			List<Patient> patientByInstituteId = patientRepository.findByCreatedByIn(userEmailsByInstitution);
			List<PatientModel> patientByInstituteIdList = new ArrayList<>();
			
			patientByInstituteId.forEach(patient -> {
				PatientModel patientModel = new PatientModel();
				patientModel.setAdmissionDateForOutdoorPatients(patient.getAdmissionDateForOutdoorPatients() == null ? null : sdfDateOnly.format(patient.getAdmissionDateForOutdoorPatients()));
				patientModel.setBabyAdmittedTo(patient.getBabyAdmittedTo() == null ? null : patient.getBabyAdmittedTo().getId());
				patientModel.setBabyCode(patient.getBabyCode());
				patientModel.setBabyCodeHospital(patient.getBabyCodeHospital() == null ? null : patient.getBabyCodeHospital());
				patientModel.setBabyOf(patient.getBabyOf() == null ? null : patient.getBabyOf());
				patientModel.setBabyWeight(patient.getBabyWeight() == null ? null : patient.getBabyWeight());
				patientModel.setDeliveryDate(sdfDateOnly.format(patient.getDeliveryDateAndTime()));
				patientModel.setDeliveryMethod(patient.getDeliveryMethod() == null ? null : patient.getDeliveryMethod().getId());
				patientModel.setDeliveryTime(sdfTimeOnly.format(patient.getDeliveryDateAndTime()));
				patientModel.setDischargeDate(patient.getDischargeDate() == null ? null : sdfDateOnly.format(patient.getDischargeDate()));
				patientModel.setGestationalAgeInWeek(patient.getGestationalAgeInWeek() == null ? null : patient.getGestationalAgeInWeek());
				patientModel.setInpatientOrOutPatient(patient.getInpatientOrOutPatient() == null ? null : patient.getInpatientOrOutPatient().getId());
				patientModel.setIsSynced(true);
				patientModel.setMothersAge(patient.getMothersAge() == null ? null : patient.getMothersAge());
				patientModel.setMothersPrenatalIntent(patient.getMothersPrenatalIntent() == null ? null : patient.getMothersPrenatalIntent().getId());
				
				String[] nicuReasons = patient.getNicuAdmissionReason() == null ? new String[0] : (patient.getNicuAdmissionReason().split(","));
				Integer[] nicuAdmissionReasons = new Integer[nicuReasons.length];
				
				for (int i = 0; i < nicuReasons.length; i++) {
					nicuAdmissionReasons[i] = Integer.parseInt(nicuReasons[i]);
				}
				
				patientModel.setNicuAdmissionReason(nicuAdmissionReasons.length == 0 ? null : nicuAdmissionReasons);
				patientModel.setParentsKnowledgeOnHmAndLactation(patient.getParentsKnowledgeOnHmAndLactation() == null ? null : patient.getParentsKnowledgeOnHmAndLactation().getId());
				if(patient.getTimeTillFirstExpression() != null){
					patientModel.setTimeTillFirstExpressionInHour(patient.getTimeTillFirstExpression().split(":")[0]);
					patientModel.setTimeTillFirstExpressionInMinute(patient.getTimeTillFirstExpression().split(":")[1]);
				}
				patientModel.setCreatedDate(sdfDateTimeWithSeconds.format(patient.getCreatedDate()));
				patientModel.setUpdatedDate(sdfDateTimeWithSeconds.format(patient.getUpdatedDate()));
				patientModel.setUuidNumber(patient.getUuidNumber() == null ? null : patient.getUuidNumber());
				patientModel.setUserId(patient.getUpdatedBy());
				
				patientByInstituteIdList.add(patientModel);
			});
			
			List<LogExpressionBreastFeed> bfExpressionByInstitute = logExpressionBreastFeedRepository.findByCreatedByIn(userEmailsByInstitution);
			List<BFExpressionModel> bfExpressionByInstituteList = new ArrayList<>();
			
			bfExpressionByInstitute.forEach(bfExp -> {
				BFExpressionModel bfExpressionModel = new BFExpressionModel();
				bfExpressionModel.setBabyCode(bfExp.getPatientId().getBabyCode());
				bfExpressionModel.setDateOfExpression(sdfDateOnly.format(bfExp.getDateAndTimeOfExpression()));
				bfExpressionModel.setId(bfExp.getUniqueFormId());
				bfExpressionModel.setIsSynced(true);
				bfExpressionModel.setLocationOfExpression(bfExp.getExpressionOccuredLocation() == null ? null : bfExp.getExpressionOccuredLocation().getId());
				bfExpressionModel.setMethodOfExpression(bfExp.getMethodOfExpression() == null ? null : bfExp.getMethodOfExpression().getId());
				bfExpressionModel.setTimeOfExpression(sdfTimeOnly.format(bfExp.getDateAndTimeOfExpression()));
				bfExpressionModel.setVolOfMilkExpressedFromLR(bfExp.getMilkExpressedFromLeftAndRightBreast() == null ? null : bfExp.getMilkExpressedFromLeftAndRightBreast());
				bfExpressionModel.setCreatedDate(sdfDateTimeWithSeconds.format(bfExp.getCreatedDate()));
				bfExpressionModel.setUpdatedDate(sdfDateTimeWithSeconds.format(bfExp.getUpdatedDate()));
				bfExpressionModel.setUuidNumber(bfExp.getUuidNumber() == null ? null : bfExp.getUuidNumber());
				bfExpressionModel.setUserId(bfExp.getUpdatedBy());
				bfExpressionModel.setMethodOfExpressionOthers(bfExp.getMethodOfExpressionOthers() == null ? null : bfExp.getMethodOfExpressionOthers());
				
				bfExpressionByInstituteList.add(bfExpressionModel);
			});
			
			List<LogBreastFeedingPostDischarge> bfpdByInstitute = logBreastFeedingPostDischargeRepository.findByCreatedByIn(userEmailsByInstitution);
			List<BFPDModel> bfpdByInstituteList = new ArrayList<>();
			
			bfpdByInstitute.forEach(bfpd -> {
				BFPDModel bfpdModel = new BFPDModel();
				bfpdModel.setBabyCode(bfpd.getPatientId().getBabyCode());
				bfpdModel.setBreastFeedingStatus(bfpd.getBreastFeedingStatus() == null ? null : bfpd.getBreastFeedingStatus().getId());
				bfpdModel.setDateOfBreastFeeding(sdfDateOnly.format(bfpd.getDateOfBreastFeeding()));
				bfpdModel.setId(bfpd.getUniqueFormId());
				bfpdModel.setIsSynced(true);
				bfpdModel.setTimeOfBreastFeeding(bfpd.getTimeOfBreastFeeding() == null ? null : bfpd.getTimeOfBreastFeeding().getId());
				bfpdModel.setCreatedDate(sdfDateTimeWithSeconds.format(bfpd.getCreatedDate()));
				bfpdModel.setUpdatedDate(sdfDateTimeWithSeconds.format(bfpd.getUpdatedDate()));
				bfpdModel.setUuidNumber(bfpd.getUuidNumber() == null ? null : bfpd.getUuidNumber());
				bfpdModel.setUserId(bfpd.getUpdatedBy());
				
				bfpdByInstituteList.add(bfpdModel);
			});
			
			List<LogBreastFeedingSupportivePractice> bfspByInstitute = logBreastFeedingSupportivePracticeRepository.findByCreatedByIn(userEmailsByInstitution);
			List<BFSPModel> bfspByInstituteList = new ArrayList<>();
			
			bfspByInstitute.forEach(bfsp -> {
				BFSPModel bfspModel = new BFSPModel();
				bfspModel.setBabyCode(bfsp.getPatientId().getBabyCode());
				bfspModel.setBfspDuration(bfsp.getBfspDuration() == null ? null : bfsp.getBfspDuration());
				bfspModel.setBfspPerformed(bfsp.getBfspPerformed() == null ? null : bfsp.getBfspPerformed().getId());
				bfspModel.setDateOfBFSP(sdfDateOnly.format(bfsp.getDateAndTimeOfBFSP()));
				bfspModel.setId(bfsp.getUniqueFormId());
				bfspModel.setIsSynced(true);
				bfspModel.setPersonWhoPerformedBFSP(bfsp.getPersonWhoPerformedBFSP() == null ? null : bfsp.getPersonWhoPerformedBFSP().getId());
				bfspModel.setTimeOfBFSP(sdfTimeOnly.format(bfsp.getDateAndTimeOfBFSP()));
				bfspModel.setCreatedDate(sdfDateTimeWithSeconds.format(bfsp.getCreatedDate()));
				bfspModel.setUpdatedDate(sdfDateTimeWithSeconds.format(bfsp.getUpdatedDate()));
				bfspModel.setUuidNumber(bfsp.getUuidNumber() == null ? null : bfsp.getUuidNumber());
				bfspModel.setUserId(bfsp.getUpdatedBy());
				
				bfspByInstituteList.add(bfspModel);
			});
			
			List<LogFeed> feedByInstitute = logFeedRepository.findByCreatedByIn(userEmailsByInstitution);
			List<FeedExpressionModel> feedByInstituteList = new ArrayList<>();
			
			feedByInstitute.forEach(feed -> {
				FeedExpressionModel feedModel = new FeedExpressionModel();
				feedModel.setAnimalMilkVolume(feed.getAnimalMilkVolume() == null ? null : feed.getAnimalMilkVolume());
				feedModel.setBabyCode(feed.getPatientId().getBabyCode());
				feedModel.setBabyWeight(feed.getWeightOfBaby() == null ? null : feed.getWeightOfBaby());
				feedModel.setDateOfFeed(sdfDateOnly.format(feed.getDateAndTimeOfFeed()));
				feedModel.setDhmVolume(feed.getDhmVolume() == null ? null : feed.getDhmVolume());
				feedModel.setFormulaVolume(feed.getFormulaVolume() == null ? null : feed.getFormulaVolume());
				feedModel.setId(feed.getUniqueFormId());
				feedModel.setIsSynced(true);
				feedModel.setLocationOfFeeding(feed.getLocationOfFeeding() == null ? null : feed.getLocationOfFeeding().getId());
				feedModel.setMethodOfFeed(feed.getFeedMethod() == null ? null : feed.getFeedMethod().getId());
				feedModel.setOmmVolume(feed.getOmmVolume() == null ? null : feed.getOmmVolume());
				feedModel.setOtherVolume(feed.getOtherVolume() == null ? null : feed.getOtherVolume());
				feedModel.setTimeOfFeed(sdfTimeOnly.format(feed.getDateAndTimeOfFeed()));
				feedModel.setCreatedDate(sdfDateTimeWithSeconds.format(feed.getCreatedDate()));
				feedModel.setUpdatedDate(sdfDateTimeWithSeconds.format(feed.getUpdatedDate()));
				feedModel.setUuidNumber(feed.getUuidNumber() == null ? null : feed.getUuidNumber());
				feedModel.setUserId(feed.getUpdatedBy());
				
				feedByInstituteList.add(feedModel);
			});
			
			
			
			syncResult.setBfExpressions(bfExpressionByInstituteList);
			syncResult.setBfpds(bfpdByInstituteList);
			syncResult.setBfsps(bfspByInstituteList);
			syncResult.setFeedExpressions(feedByInstituteList);
			syncResult.setPatients(patientByInstituteIdList);
			syncResult.setUsers(userByInstitutionList);
			syncResult.setSyncStatus(1);
			
		}else{
			//-----------------------------Send email to lactation group, conveying that existing user trying to register with different institute----------------------//
			
			syncResult.setSyncStatus(-1);
			
			Thread thread = new Thread(){
				@Override
				public void run(){
					final String text = "A user - "+ userWithDifferentInstituteId.get("name") + " (" + userWithDifferentInstituteId.get("email") +"), registered with "+ 
							userWithDifferentInstituteId.get("oldInstitute") + " (" +userWithDifferentInstituteId.get("oldState") + ", " + userWithDifferentInstituteId.get("oldDistrict") + 
							"), is trying to register " + "himself again with "+ userWithDifferentInstituteId.get("newInstitute") + 
							" (" + userWithDifferentInstituteId.get("newState") + ", " + userWithDifferentInstituteId.get("newDistrict") + ")";
					
					emailService.sendEmail(configurableEnvironment.getProperty(Constants.EMAIL_TO), null,
							configurableEnvironment.getProperty(Constants.EMAIL_INVALID_USER_SUBJECT), text, null);
				}
			};
			
			thread.start();
		}
		return syncResult;
	}
	
	private Timestamp getTimestampFromDateAndTime(String date, String time){
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		try {
			return new Timestamp(sdf.parse(date+ " " + time).getTime());
		} catch (ParseException e) {
			log.error("Error in method getTimestampFromDateAndTime - " + e);
			return null;
		}
	}
	
	private Timestamp getTimestampFromString(String date){
		return Timestamp.valueOf(date);
	}
	
	private Date getDateFromString(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		try{
			return new Date(sdf.parse(date).getTime());
		} catch(ParseException e){
			log.error("Error in method getDateFromString - " + e);
			return null;
		}
	}
	
	private String arrayToString(Integer[] integerArray){
		StringBuilder arrayAsString = new StringBuilder();
		for (int i = 0; i < integerArray.length; i++) {
			arrayAsString.append(integerArray[i].toString()+",");
		}
		return arrayAsString.substring(0, arrayAsString.length() - 1);
	}

	//=================================================================================================================================================================//
	//=================================================================================================================================================================//
	//=================================================================================================================================================================//
	//=================================================================================================================================================================//
	
	/**
	 * @author Naseem Akhtar (naseem@sdrc.co.in)
	 * 
	 * This method will be basically used to validate and restructure the legacy records.
	 * This code can be removed when the legacy data import is over.
	 */
	@Override
	@Transactional
	public Boolean setUniqueId() {
		List<Patient> patients = patientRepository.findByUuidNumberIsNull();
		List<LogBreastFeedingPostDischarge> bfpd = logBreastFeedingPostDischargeRepository.findByUniqueFormIdIsNull();
		List<LogBreastFeedingSupportivePractice> bfsp = logBreastFeedingSupportivePracticeRepository.findByUniqueFormIdIsNull();
		List<LogExpressionBreastFeed> bfExpression = logExpressionBreastFeedRepository.findByUniqueFormIdIsNull();
		List<LogFeed> feeds = logFeedRepository.findByUniqueFormIdIsNull();
		
		final String legacyData = "LegacyData";
		final int timeInMili = 400;
		
		patients.forEach(d -> {
			if(d.getUpdatedDate() == null)
				d.setUpdatedDate(d.getCreatedDate());
			
			if(d.getUuidNumber() == null)
				d.setUuidNumber(legacyData);
			
			if(d.getUpdatedBy() == null)
				d.setUpdatedBy(d.getCreatedBy());
		});
		
		setBfpdData(bfpd, legacyData, timeInMili);
		setBfspData(bfsp, legacyData, timeInMili);
		setBfExpressionData(bfExpression, legacyData, timeInMili);
		setFeedData(feeds, legacyData, timeInMili);
		
		return true;
	}
	
	/**
	 * @author Naseem Akhtar (naseem@sdrc.co.in)
	 * @param bfpd
	 * @throws InterruptedException
	 */
	private void setBfpdData(List<LogBreastFeedingPostDischarge> bfpd, String legacyData, Integer time) {
			bfpd.forEach(d -> {
				try {
					Thread.sleep(time);
					
					String date = sdfDateTimeWithSeconds.format(new Date());
					String dateForUniqueId = sdfDateInteger.format(new Date());
					
					if(d.getUniqueFormId() == null)
						d.setUniqueFormId(d.getPatientId().getBabyCode() + "bfpd" + dateForUniqueId);
					
					if(d.getUuidNumber() == null)
						d.setUuidNumber(legacyData);

					if(d.getCreatedDate() == null)
						d.setCreatedDate(Timestamp.valueOf(date));
						
					if(d.getUpdatedBy() == null)
						d.setUpdatedBy(d.getCreatedBy());
					
					if(d.getUpdatedDate() == null)
						d.setUpdatedDate(d.getCreatedDate());
					
				} catch (InterruptedException e) {
					log.error("BFPD - " + e);
				}
			});
	}
	
	/**
	 * @author Naseem Akhtar (naseem@sdrc.co.in)
	 * @param bfsp
	 * @throws InterruptedException
	 */
	private void setBfspData(List<LogBreastFeedingSupportivePractice> bfsp, String legacyData, Integer time) {
		bfsp.forEach(d -> {
			try {
				Thread.sleep(time);
				
				String date = sdfDateTimeWithSeconds.format(new Date());
				String dateForUniqueId = sdfDateInteger.format(new Date());
				
				if(d.getUniqueFormId() == null)
					d.setUniqueFormId(d.getPatientId().getBabyCode() + "bfps" + dateForUniqueId);
				
				if(d.getUuidNumber() == null)
					d.setUuidNumber(legacyData);
				
				if(d.getCreatedDate() == null)
					d.setCreatedDate(Timestamp.valueOf(date));
				
				if(d.getUpdatedBy() == null)
					d.setUpdatedBy(d.getCreatedBy());
				
				if(d.getUpdatedDate() == null)
					d.setUpdatedDate(d.getCreatedDate());
			} catch (InterruptedException e) {
				log.error("BFSP - " + e);
			}
		});
	}
	
	/**
	 * @author Naseem Akhtar (naseem@sdrc.co.in)
	 * @param bfExpression
	 * @throws InterruptedException
	 */
	private void setBfExpressionData(List<LogExpressionBreastFeed> bfExpression, String legacyData, Integer time) {
		bfExpression.forEach(d -> {
			try {
				Thread.sleep(time);
				String date = sdfDateTimeWithSeconds.format(new Date());
				String dateForUniqueId = sdfDateInteger.format(new Date());
				
				if(d.getUniqueFormId() == null)
					d.setUniqueFormId(d.getPatientId().getBabyCode() + "bfid" + dateForUniqueId);
				
				if(d.getUuidNumber() == null)
					d.setUuidNumber(legacyData);
				
				if(d.getCreatedDate() == null)
					d.setCreatedDate(Timestamp.valueOf(date));
				
				if(d.getUpdatedBy() == null)
					d.setUpdatedBy(d.getCreatedBy());
				
				if(d.getUpdatedDate() == null)
					d.setUpdatedDate(d.getCreatedDate());
			} catch (InterruptedException e) {
				log.error("BFExp - " + e);
			}
		});
	}
	
	/**
	 * @author Naseem Akhtar (naseem@sdrc.co.in)
	 * @param feeds
	 * @throws InterruptedException
	 */
	private void setFeedData(List<LogFeed> feeds, String legacyData, Integer time) {
		feeds.forEach(d -> {
			try {
				Thread.sleep(time);
				String date = sdfDateTimeWithSeconds.format(new Date());
				String dateForUniqueId = sdfDateInteger.format(new Date());
				
				if(d.getUniqueFormId() == null)
					d.setUniqueFormId(d.getPatientId().getBabyCode() + "feid" + dateForUniqueId);
				
				if(d.getUuidNumber() == null)
					d.setUuidNumber(legacyData);
				
				if(d.getCreatedDate() == null)
					d.setCreatedDate(Timestamp.valueOf(date));
				
				if(d.getUpdatedBy() == null)
					d.setUpdatedBy(d.getCreatedBy());
				
				if(d.getUpdatedDate() == null)
					d.setUpdatedDate(d.getCreatedDate());
			} catch (InterruptedException e) {
				log.error("Feed - " + e);
			}
		});
	}
	
}
