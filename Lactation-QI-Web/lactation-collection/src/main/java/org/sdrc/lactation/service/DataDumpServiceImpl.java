package org.sdrc.lactation.service;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.sdrc.lactation.domain.ApiCallMeta;
import org.sdrc.lactation.domain.ApiUser;
import org.sdrc.lactation.domain.LogBreastFeedingPostDischarge;
import org.sdrc.lactation.domain.LogBreastFeedingSupportivePractice;
import org.sdrc.lactation.domain.LogExpressionBreastFeed;
import org.sdrc.lactation.domain.LogFeed;
import org.sdrc.lactation.domain.Patient;
import org.sdrc.lactation.domain.TypeDetails;
import org.sdrc.lactation.repository.ApiCallMetaRepository;
import org.sdrc.lactation.repository.ApiUserRepository;
import org.sdrc.lactation.repository.LogBreastFeedingPostDischargeRepository;
import org.sdrc.lactation.repository.LogBreastFeedingSupportivePracticeRepository;
import org.sdrc.lactation.repository.LogExpressionBreastFeedRepository;
import org.sdrc.lactation.repository.LogFeedRepository;
import org.sdrc.lactation.repository.PatientRepository;
import org.sdrc.lactation.repository.TypeDetailsRepository;
import org.sdrc.lactation.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in) on 5th April 2018 17:00
 * 
 * This service class wil handle all the requests related to data dump.
 *
 */

@Service
public class DataDumpServiceImpl implements DataDumpService {

	private static final Logger log = LogManager.getLogger(DataDumpServiceImpl.class);
	
	@Autowired
	private PatientRepository patientRepository;

	@Autowired
	private LogExpressionBreastFeedRepository logExpressionBreastFeedRepository;

	@Autowired
	private LogBreastFeedingPostDischargeRepository logBreastFeedingPostDischargeRepository;

	@Autowired
	private LogFeedRepository logFeedRepository;

	@Autowired
	private LogBreastFeedingSupportivePracticeRepository logBreastFeedingSupportivePracticeRepository;
	
	@Autowired
	private TypeDetailsRepository typeDetailsRepository;
	
	@Autowired
	private ApiUserRepository apiUserRepository;
	
	@Autowired
	private MessageDigestPasswordEncoder passwordEncoder;
	
	@Autowired
	private ApiCallMetaRepository apiCallMetaRepository;
	
	@Autowired
	ConfigurableEnvironment configurableEnvironment;

	private SimpleDateFormat sdfDateInteger = new SimpleDateFormat("ddMMyyyyHHmmssSSS");
	
	private SimpleDateFormat sdfDateOnly = new SimpleDateFormat("yyyy-MM-dd");
	
	private SimpleDateFormat sdfDateTimeWithSeconds = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/***
	 * The following method will make calls to patient and patient related forms and then 
	 * write them in an excel file. Different sheets are created for different types of forms.
	 * 
	 * @return - String - filepath is returned.
	 */
	@Override
	@Transactional
	public String exportDataInExcel(HttpServletRequest request, HttpServletResponse response) {
		
		String filePath;

		if(validateUserForExportApi(request, response)) {
			
			filePath = configurableEnvironment.getProperty(Constants.DATA_DUMP_PATH) + sdfDateInteger.format(new Date()) + ".xlsx";
			
			try (XSSFWorkbook workbook = new XSSFWorkbook(); FileOutputStream fileOut = new FileOutputStream(filePath);) {
				
				Map<Integer, TypeDetails> typeDetailsMap = new HashMap<>();
				typeDetailsRepository.findAll().forEach(typeDetails->typeDetailsMap.put(typeDetails.getId(), typeDetails));
				
				List<Patient> patients = patientRepository.findAll();
				List<LogExpressionBreastFeed> bfExpressions = logExpressionBreastFeedRepository.findAll();
				List<LogBreastFeedingSupportivePractice> bfsps = logBreastFeedingSupportivePracticeRepository.findAll();
				List<LogFeed> feeds = logFeedRepository.findAll();
				List<LogBreastFeedingPostDischarge> bfpds = logBreastFeedingPostDischargeRepository.findAll();
	
				XSSFSheet patientSheet = workbook.createSheet("Patients");
				XSSFSheet bfExpressionsSheet = workbook.createSheet("Bf Expressions");
				XSSFSheet bfspSheet = workbook.createSheet("BFSP");
				XSSFSheet logFeedSheet = workbook.createSheet("Log Feed");
				XSSFSheet bfpdSheet = workbook.createSheet("BFPD");
	
				// for styling the first row of every sheet
				XSSFFont font = workbook.createFont();
				font.setBold(true);
				font.setItalic(false);
	
				CellStyle cellStyle = workbook.createCellStyle();
				cellStyle.setFont(font);
	
				int slNo = 1;
				int rowNum = 0;
				Row headingRow = patientSheet.createRow(rowNum++);
	
				headingRow.setRowStyle(cellStyle);
				
				// setting heading of patient sheet
				headingRow = setHeaderForExcelFiles(headingRow, configurableEnvironment.getProperty(Constants.SHEET_PATIENT_HEADING));
				
	
				// Iterating patient records and writing in the excel sheet
				for (Patient patient : patients) {
					Row row = patientSheet.createRow(rowNum++);
					int colNum = 0;
	
					row.createCell(colNum).setCellValue(slNo++);
					colNum++;
					row.createCell(colNum).setCellValue(patient.getBabyCode());
					colNum++;
					row.createCell(colNum).setCellValue(patient.getBabyCodeHospital() == null ? "" : patient.getBabyCodeHospital());
					colNum++;
					row.createCell(colNum).setCellValue(patient.getBabyOf() == null ? "" : patient.getBabyOf());
					colNum++;
					row.createCell(colNum).setCellValue(patient.getBabyWeight() == null ? "" : patient.getBabyWeight().toString());
					colNum++;
					row.createCell(colNum).setCellValue(patient.getBabyAdmittedTo() == null ? "" : patient.getBabyAdmittedTo().getName());
					colNum++;
					row.createCell(colNum).setCellValue(patient.getAdmissionDateForOutdoorPatients() ==  null ? "" : sdfDateOnly.format(patient.getAdmissionDateForOutdoorPatients()));
					colNum++;
					row.createCell(colNum).setCellValue(patient.getCreatedBy());
					colNum++;
					row.createCell(colNum).setCellValue(patient.getDischargeDate() == null ? "" : sdfDateOnly.format(patient.getDischargeDate()));
					colNum++;
					row.createCell(colNum).setCellValue(patient.getNicuAdmissionReason().length() == 0 ? "" : arrayToString(patient.getNicuAdmissionReason(), typeDetailsMap));
					colNum++;
					row.createCell(colNum).setCellValue(patient.getTimeTillFirstExpression() == null ? "" : patient.getTimeTillFirstExpression());
					colNum++;
					row.createCell(colNum).setCellValue(patient.getUpdatedBy());
					colNum++;
					row.createCell(colNum).setCellValue(patient.getUuidNumber() == null ? "" : patient.getUuidNumber());
					colNum++;
					row.createCell(colNum).setCellValue(sdfDateTimeWithSeconds.format(patient.getCreatedDate()));
					colNum++;
					row.createCell(colNum).setCellValue(sdfDateTimeWithSeconds.format(patient.getDeliveryDateAndTime()));
					colNum++;
					row.createCell(colNum).setCellValue(patient.getGestationalAgeInWeek() == null ? "" : patient.getGestationalAgeInWeek().toString());
					colNum++;
					row.createCell(colNum).setCellValue(patient.getMothersAge() == null ? "" : patient.getMothersAge().toString());
					colNum++;
					row.createCell(colNum).setCellValue(sdfDateTimeWithSeconds.format(patient.getUpdatedDate()));
					colNum++;
					row.createCell(colNum).setCellValue(patient.getDeliveryMethod() == null ? "" : patient.getDeliveryMethod().getName());
					colNum++;
					row.createCell(colNum).setCellValue(patient.getInpatientOrOutPatient() == null ? "" : patient.getInpatientOrOutPatient().getName());
					colNum++;
					row.createCell(colNum).setCellValue(patient.getMothersPrenatalIntent() == null ? "" : patient.getMothersPrenatalIntent().getName());
					colNum++;
					row.createCell(colNum).setCellValue(patient.getParentsKnowledgeOnHmAndLactation() == null ? "" : patient.getParentsKnowledgeOnHmAndLactation().getName());
				}
	
				rowNum = 0;
				slNo = 1;
				headingRow = bfExpressionsSheet.createRow(rowNum++);
	
				// setting heading of breastfeed expression sheet
				headingRow = setHeaderForExcelFiles(headingRow, configurableEnvironment.getProperty(Constants.SHEET_BFEXP_HEADING));
	
				// iterating through breastfeed expressions
				for (LogExpressionBreastFeed bfExpression : bfExpressions) {
					Row row = bfExpressionsSheet.createRow(rowNum++);
					int colNum = 0;
	
					row.createCell(colNum).setCellValue(slNo++);
					colNum++;
					row.createCell(colNum).setCellValue(bfExpression.getCreatedBy());
					colNum++;
					row.createCell(colNum).setCellValue(bfExpression.getUniqueFormId());
					colNum++;
					row.createCell(colNum).setCellValue(bfExpression.getUpdatedBy());
					colNum++;
					row.createCell(colNum).setCellValue(bfExpression.getUuidNumber() == null ? "" : bfExpression.getUuidNumber());
					colNum++;
					row.createCell(colNum).setCellValue(sdfDateTimeWithSeconds.format(bfExpression.getCreatedDate()));
					colNum++;
					row.createCell(colNum).setCellValue(sdfDateTimeWithSeconds.format(bfExpression.getDateAndTimeOfExpression()));
					colNum++;
					row.createCell(colNum).setCellValue(bfExpression.getMilkExpressedFromLeftAndRightBreast() == null ? "" : bfExpression.getMilkExpressedFromLeftAndRightBreast().toString());
					colNum++;
					row.createCell(colNum).setCellValue(sdfDateTimeWithSeconds.format(bfExpression.getUpdatedDate()));
					colNum++;
					row.createCell(colNum).setCellValue(bfExpression.getExpressionOccuredLocation() == null ? "" : bfExpression.getExpressionOccuredLocation().getName());
					colNum++;
					row.createCell(colNum).setCellValue(bfExpression.getMethodOfExpression() == null ? "" : bfExpression.getMethodOfExpression().getName());
					colNum++;
					row.createCell(colNum).setCellValue(bfExpression.getMethodOfExpressionOthers() == null ? "" : bfExpression.getMethodOfExpressionOthers());
					colNum++;
					row.createCell(colNum).setCellValue(bfExpression.getPatientId().getBabyCode());
				}
	
				rowNum = 0;
				slNo = 1;
				headingRow = bfspSheet.createRow(rowNum++);
	
				// setting heading of bfsp sheet
				headingRow = setHeaderForExcelFiles(headingRow, configurableEnvironment.getProperty(Constants.SHEET_BFSP_HEADING));
	
				// iterating through bfsp entries and writing them in excel
				for (LogBreastFeedingSupportivePractice bfsp : bfsps) {
					Row row = bfspSheet.createRow(rowNum++);
					int colNum = 0;
	
					row.createCell(colNum).setCellValue(slNo++);
					colNum++;
					row.createCell(colNum).setCellValue(bfsp.getCreatedBy());
					colNum++;
					row.createCell(colNum).setCellValue(bfsp.getUniqueFormId());
					colNum++;
					row.createCell(colNum).setCellValue(bfsp.getUpdatedBy());
					colNum++;
					row.createCell(colNum).setCellValue(bfsp.getUuidNumber() == null ? "" : bfsp.getUuidNumber());
					colNum++;
					row.createCell(colNum).setCellValue(bfsp.getBfspDuration() == null ? "" : bfsp.getBfspDuration().toString());
					colNum++;
					row.createCell(colNum).setCellValue(sdfDateTimeWithSeconds.format(bfsp.getCreatedDate()));
					colNum++;
					row.createCell(colNum).setCellValue(sdfDateTimeWithSeconds.format(bfsp.getDateAndTimeOfBFSP()));
					colNum++;
					row.createCell(colNum).setCellValue(sdfDateTimeWithSeconds.format(bfsp.getUpdatedDate()));
					colNum++;
					row.createCell(colNum).setCellValue(bfsp.getBfspPerformed() == null ? "" : bfsp.getBfspPerformed().getName());
					colNum++;
					row.createCell(colNum).setCellValue(bfsp.getPatientId().getBabyCode());
					colNum++;
					row.createCell(colNum).setCellValue(bfsp.getPersonWhoPerformedBFSP() == null ? "" : bfsp.getPersonWhoPerformedBFSP().getName());
				}
	
				rowNum = 0;
				slNo = 1;
				headingRow = logFeedSheet.createRow(rowNum++);
	
				// setting heading of feed sheet
				headingRow = setHeaderForExcelFiles(headingRow, configurableEnvironment.getProperty(Constants.SHEET_LOGFEED_HEADING));
	
				// iterating through feed entries and writing them in excel
				for (LogFeed feed : feeds) {
					Row row = logFeedSheet.createRow(rowNum++);
					int colNum = 0;
	
					row.createCell(colNum).setCellValue(slNo++);
					colNum++;
					row.createCell(colNum).setCellValue(feed.getCreatedBy());
					colNum++;
					row.createCell(colNum).setCellValue(feed.getUniqueFormId());
					colNum++;
					row.createCell(colNum).setCellValue(feed.getUpdatedBy());
					colNum++;
					row.createCell(colNum).setCellValue(feed.getUuidNumber() == null ? "" : feed.getUuidNumber());
					colNum++;
					row.createCell(colNum)
							.setCellValue(feed.getAnimalMilkVolume() == null ? "" : feed.getAnimalMilkVolume().toString());
					colNum++;
					row.createCell(colNum).setCellValue(sdfDateTimeWithSeconds.format(feed.getCreatedDate()));
					colNum++;
					row.createCell(colNum).setCellValue(sdfDateTimeWithSeconds.format(feed.getDateAndTimeOfFeed()));
					colNum++;
					row.createCell(colNum).setCellValue(feed.getDhmVolume() == null ? "" : feed.getDhmVolume().toString());
					colNum++;
					row.createCell(colNum)
							.setCellValue(feed.getFormulaVolume() == null ? "" : feed.getFormulaVolume().toString());
					colNum++;
					row.createCell(colNum).setCellValue(feed.getOmmVolume() == null ? "" : feed.getOmmVolume().toString());
					colNum++;
					row.createCell(colNum)
							.setCellValue(feed.getOtherVolume() == null ? "" : feed.getOtherVolume().toString());
					colNum++;
					row.createCell(colNum).setCellValue(sdfDateTimeWithSeconds.format(feed.getUpdatedDate()));
					colNum++;
					row.createCell(colNum).setCellValue(feed.getWeightOfBaby() == null ? "" : feed.getWeightOfBaby().toString());
					colNum++;
					row.createCell(colNum).setCellValue(feed.getFeedMethod() == null ? "" : feed.getFeedMethod().getName());
					colNum++;
					row.createCell(colNum).setCellValue(feed.getLocationOfFeeding() == null ? "" : feed.getLocationOfFeeding().getName());
					colNum++;
					row.createCell(colNum).setCellValue(feed.getPatientId().getBabyCode());
				}
	
				rowNum = 0;
				slNo = 1;
				headingRow = bfpdSheet.createRow(rowNum++);
				// setting heading of bfpd sheet
				headingRow = setHeaderForExcelFiles(headingRow, configurableEnvironment.getProperty(Constants.SHEET_BFPD_HEADING));
	
				// iterating through bfpd entries and writing them in excel
				for (LogBreastFeedingPostDischarge bfpd : bfpds) {
					Row row = bfpdSheet.createRow(rowNum++);
					int colNum = 0;
	
					row.createCell(colNum).setCellValue(slNo++);
					colNum++;
					row.createCell(colNum).setCellValue(bfpd.getCreatedBy());
					colNum++;
					row.createCell(colNum).setCellValue(bfpd.getUniqueFormId());
					colNum++;
					row.createCell(colNum).setCellValue(bfpd.getUpdatedBy());
					colNum++;
					row.createCell(colNum).setCellValue(bfpd.getUuidNumber() == null ? "" : bfpd.getUuidNumber());
					colNum++;
					row.createCell(colNum).setCellValue(sdfDateTimeWithSeconds.format(bfpd.getCreatedDate()));
					colNum++;
					row.createCell(colNum).setCellValue(sdfDateOnly.format(bfpd.getDateOfBreastFeeding()));
					colNum++;
					row.createCell(colNum).setCellValue(sdfDateTimeWithSeconds.format(bfpd.getUpdatedDate()));
					colNum++;
					row.createCell(colNum).setCellValue(bfpd.getBreastFeedingStatus() == null ? "" : bfpd.getBreastFeedingStatus().getName());
					colNum++;
					row.createCell(colNum).setCellValue(bfpd.getPatientId().getBabyCode());
					colNum++;
					row.createCell(colNum).setCellValue(bfpd.getTimeOfBreastFeeding().getName());
				}
	
				workbook.write(fileOut);
			} catch (Exception e) {
				log.error("Error - DataDumpServiceImpl - exportDataInExcel - " + e);
			}
		}else{
			filePath = null;
		}

		return filePath;
	}
	

	/***
	 *@author Naseem Akhtar (naseem@sdrc.co.in) on 7th April 2018 1641.
	 *
	 * This method will accpet the request for data dump in json format.
	 * After validating the request the data is being extracted and then returned.
	 * 
	 * @return {@link JSONObject}
	 */
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public JSONObject exportDataInJson(HttpServletRequest request, HttpServletResponse response) {
		
		//JSONObject which will be returned at last.
		JSONObject data = new JSONObject();
		
		//validating the user
		if(validateUserForExportApi(request, response)){
			try{
				//map to keep the type details table data.
				Map<Integer, TypeDetails> typeDetailsMap = new HashMap<>();
				typeDetailsRepository.findAll().forEach(typeDetails->typeDetailsMap.put(typeDetails.getId(), typeDetails));
				
				//querying multiple tables as per requirement.
				List<Patient> patients = patientRepository.findAll();
				List<LogExpressionBreastFeed> bfExpressions = logExpressionBreastFeedRepository.findAll();
				List<LogBreastFeedingSupportivePractice> bfsps = logBreastFeedingSupportivePracticeRepository.findAll();
				List<LogFeed> feeds = logFeedRepository.findAll();
				List<LogBreastFeedingPostDischarge> bfpds = logBreastFeedingPostDischargeRepository.findAll();
				
				//declaring some of the common JSON keys as final.
				final String babyCode = "babyCode";
				final String createdBy = "createdBy";
				final String updatedBy = "updatedBy";
				final String createdDate = "createdDate";
				final String updatedDate = "updatedDate";
				final String uniqueFormId = "uniqueFormId";
				
				//creating a JSONArray to store the patient entries received through the query call
				JSONArray patientList = new JSONArray();
				patients.forEach(d -> {
					JSONObject patient = new JSONObject();
					patient.put(babyCode, d.getBabyCode());
					patient.put("babyCodeHospital", d.getBabyCodeHospital() == null ? null : d.getBabyCodeHospital());
					patient.put("babyOf", d.getBabyOf() == null ? null : d.getBabyOf());
					patient.put("babyWeight", d.getBabyWeight() == null ? null : d.getBabyWeight().toString());
					patient.put("babyAdmittedTo", d.getBabyAdmittedTo() == null ? null : d.getBabyAdmittedTo().getName());
					patient.put("admissionDateForOutdoorPatients", d.getAdmissionDateForOutdoorPatients() ==  null ? null : sdfDateOnly.format(d.getAdmissionDateForOutdoorPatients()));
					patient.put(createdBy, d.getCreatedBy());
					patient.put("dischargeDate", d.getDischargeDate() == null ? null : sdfDateOnly.format(d.getDischargeDate()));
					patient.put("nicuAdmissionReason", d.getNicuAdmissionReason() == null ? null : arrayToString(d.getNicuAdmissionReason(), typeDetailsMap));
					patient.put("timeTillFirstExpression", d.getTimeTillFirstExpression() == null ? null : d.getTimeTillFirstExpression());
					patient.put(updatedBy, d.getUpdatedBy());
					patient.put("uuid", d.getUuidNumber() == null ? null : d.getUuidNumber());
					patient.put(createdDate, sdfDateTimeWithSeconds.format(d.getCreatedDate()));
					patient.put("deliveryDateAndTime", sdfDateTimeWithSeconds.format(d.getDeliveryDateAndTime()));
					patient.put("gestationAgeInWeek", d.getGestationalAgeInWeek() == null ? null : d.getGestationalAgeInWeek().toString());
					patient.put("mothersAge", d.getMothersAge() == null ? null : d.getMothersAge().toString());
					patient.put(updatedDate, sdfDateTimeWithSeconds.format(d.getUpdatedDate()));
					patient.put("deliveryMethod", d.getDeliveryMethod() == null ? null : d.getDeliveryMethod().getName());
					patient.put("inpatientOrOutPatient", d.getInpatientOrOutPatient() == null ? null : d.getInpatientOrOutPatient().getName());
					patient.put("mothersPrenatalIntent", d.getMothersPrenatalIntent() == null ? null : d.getMothersPrenatalIntent().getName());
					patient.put("parentsKnowledgeOnHmAndLactation", d.getParentsKnowledgeOnHmAndLactation() == null ? null : d.getParentsKnowledgeOnHmAndLactation().getName());
					
					patientList.add(patient);
				});
				
				//iterating through log expression breastfeed entries and storing them in bfExpressionList
				JSONArray bfExpressionList = new JSONArray();
				bfExpressions.forEach(d -> {
					JSONObject bfExp = new JSONObject();
					bfExp.put(createdBy, d.getCreatedBy());
					bfExp.put(uniqueFormId, d.getUniqueFormId());
					bfExp.put(updatedBy, d.getUpdatedBy());
					bfExp.put("uuid", d.getUuidNumber() == null ? null : d.getUuidNumber());
					bfExp.put(createdDate, sdfDateTimeWithSeconds.format(d.getCreatedDate()));
					bfExp.put("dateAndTimeOfExpression", sdfDateTimeWithSeconds.format(d.getDateAndTimeOfExpression()));
					bfExp.put("milkExpressedFromLeftAndRightBreast", d.getMilkExpressedFromLeftAndRightBreast() == null ? null : d.getMilkExpressedFromLeftAndRightBreast().toString());
					bfExp.put(updatedDate, sdfDateTimeWithSeconds.format(d.getUpdatedDate()));
					bfExp.put("locationWhereExpressionOccured", d.getExpressionOccuredLocation() == null ? null : d.getExpressionOccuredLocation().getName());
					bfExp.put("methodOfExpression", d.getMethodOfExpression() == null ? null : d.getMethodOfExpression().getName());
					bfExp.put(babyCode, d.getPatientId().getBabyCode());
					
					bfExpressionList.add(bfExp);
				});
				
				//iterating through breast feeding supportive practice entries and storing them in bfspList
				JSONArray bfspList = new JSONArray();
				bfsps.forEach(d -> {
					JSONObject bfsp = new JSONObject();
					bfsp.put(createdBy, d.getCreatedBy());
					bfsp.put(uniqueFormId, d.getUniqueFormId());
					bfsp.put(updatedBy, d.getUpdatedBy());
					bfsp.put("uuid", d.getUuidNumber() == null ? null : d.getUuidNumber());
					bfsp.put("bfspDuration", d.getBfspDuration() == null ? null : d.getBfspDuration().toString());
					bfsp.put(createdDate, sdfDateTimeWithSeconds.format(d.getCreatedDate()));
					bfsp.put("dateAndTimeOfBfsp", sdfDateTimeWithSeconds.format(d.getDateAndTimeOfBFSP()));
					bfsp.put(updatedDate, sdfDateTimeWithSeconds.format(d.getUpdatedDate()));
					bfsp.put("bfspPerformed", d.getBfspPerformed() == null ? null : d.getBfspPerformed().getName());
					bfsp.put(babyCode, d.getPatientId().getBabyCode());
					bfsp.put("personWhoPerformedBfsp", d.getPersonWhoPerformedBFSP() == null ? null : d.getPersonWhoPerformedBFSP().getName());
					
					bfspList.add(bfsp);
				});
				
				//iterating through feed entries and storing them in feedList
				JSONArray feedList = new JSONArray();
				feeds.forEach(d -> {
					JSONObject feed = new JSONObject();
					feed.put(createdBy, d.getCreatedBy());
					feed.put(uniqueFormId, d.getUniqueFormId());
					feed.put(updatedBy, d.getUpdatedBy());
					feed.put("uuid", d.getUuidNumber() == null ? null : d.getUuidNumber());
					feed.put("animalMilkVolume", d.getAnimalMilkVolume() == null ? null : d.getAnimalMilkVolume().toString());
					feed.put(createdDate, sdfDateTimeWithSeconds.format(d.getCreatedDate()));
					feed.put("dateAndTimeOfFeed", sdfDateTimeWithSeconds.format(d.getDateAndTimeOfFeed()));
					feed.put("dhmVolume", d.getDhmVolume() == null ? null : d.getDhmVolume().toString());
					feed.put("formulaVolume", d.getFormulaVolume() == null ? null : d.getFormulaVolume().toString());
					feed.put("ommVolume", d.getOmmVolume() == null ? null : d.getOmmVolume().toString());
					feed.put("otherVolume", d.getOtherVolume() == null ? null : d.getOtherVolume().toString());
					feed.put(updatedDate, sdfDateTimeWithSeconds.format(d.getUpdatedDate()));
					feed.put("babyWeight", d.getWeightOfBaby() == null ? null : d.getWeightOfBaby().toString());
					feed.put("methodOfFeed", d.getFeedMethod() == null ? null : d.getFeedMethod().getName());
					feed.put("loactionOfFeeding", d.getLocationOfFeeding() == null ? null : d.getLocationOfFeeding().getName());
					feed.put(babyCode, d.getPatientId().getBabyCode());
					
					feedList.add(feed);
				});
				
				//iterating through breast feeding post discharge entries and storing them in bfpdList
				JSONArray bfpdList = new JSONArray();
				bfpds.forEach(d -> {
					JSONObject bfpd = new JSONObject();
					bfpd.put(createdBy, d.getCreatedBy());
					bfpd.put(uniqueFormId, d.getUniqueFormId());
					bfpd.put(updatedBy, d.getUpdatedBy());
					bfpd.put("uuid", d.getUuidNumber() == null ? null : d.getUuidNumber());
					bfpd.put(createdDate, sdfDateTimeWithSeconds.format(d.getCreatedDate()));
					bfpd.put("dateOfBreastFeeding", sdfDateOnly.format(d.getDateOfBreastFeeding()));
					bfpd.put(updatedDate, sdfDateTimeWithSeconds.format(d.getUpdatedDate()));
					bfpd.put("breastFeedingStatus", d.getBreastFeedingStatus() == null ? null : d.getBreastFeedingStatus().getName());
					bfpd.put(babyCode, d.getPatientId().getBabyCode());
					bfpd.put("timeOfBreastFeeding", d.getTimeOfBreastFeeding().getName());
					
					bfpdList.add(bfpd);
				});
				
				//finally assigning all the list to data object.
				data.put("patients", patientList);
				data.put("breastFeedExpressions", bfExpressionList);
				data.put("bfsps", bfspList);
				data.put("feeds", feedList);
				data.put("bfpds", bfpdList);
				
			}catch (Exception e) {
				log.error("Error - DataDumpServiceImpl - exportDataInJson - " + e);
			}
		}else{
			//if user is not valid then send null
			data = null;
		}
		
		return data;
	}
	
	/**
	 * @author Naseem Akhtar (naseem@sdrc.co.in) on 7th April 2018 16:31
	 * 
	 * This method will be used to validate the user who has sent request for data dump (excel/json).
	 * 
	 * @param request - will be retrieving the encrypted username and password from the header.
	 * @return {@link Boolean} which will indicate whether the user is a valid one or not.
	 */
	private Boolean validateUserForExportApi(HttpServletRequest request, HttpServletResponse response) {
		
		//initializing this variable to send the status of the user which has sent request.
		Boolean validUser = false;
		Map<String, String> map = new HashMap<>();
		Enumeration<String> headerNames = request.getHeaderNames();
		
		//iterating through header names
		while(headerNames.hasMoreElements()){
			String key = headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
		}
			
		try{
			//Logging info about the api call
			ApiCallMeta apiCallMeta = new ApiCallMeta();
			apiCallMeta.setIpAddress(getIpAddr(request));
			apiCallMeta.setUserAgent(request.getHeader("User-Agent"));
			
			if(map.get("username") != null && map.get("password") != null){
				//decoding the username and password
				String username = new String(Base64.getDecoder().decode(map.get("username")));
				String password = new String(Base64.getDecoder().decode(map.get("password")));
				apiCallMeta.setUsername(username);
				
				//generating salt password useing md5
				String encodedPassword = passwordEncoder.encodePassword(username, password);
				
				//DB call to be made here
				ApiUser apiUser = apiUserRepository.findByUsername(username);
				
				if(apiUser != null && apiUser.getPassword().equals(encodedPassword))
					validUser = true;
				else
					response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			}else{
				response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
			}
			
			//saving the api info
			apiCallMetaRepository.save(apiCallMeta);
		}catch (Exception e) {
			log.error("Error occured while validating the user who made the API call", e);
		}
		
		return validUser;
		
	}
	
	
	/**
	 * @author Naseem Akhtar (naseem@sdrc.co.in) on 6th April 2018 13:08
	 * 
	 * This method will receive NICU admission reason id in string and a map with admission reasons 
	 * NICU Admission reason id will be extracted from the string and passed to the type detail map to extract
	 * the actual reason and append it on a string.
	 * 
	 * @param admissionReason
	 * @param typeDetailsMap
	 * @return comma seperated nicu admission reasons.
	 */
	private String arrayToString(String admissionReason, Map<Integer, TypeDetails> typeDetailsMap) {
		String[] nicuAdmissionReasons = admissionReason.split(",");
		StringBuilder reasonNameList = new StringBuilder();
		for(String reason : nicuAdmissionReasons){
			reasonNameList.append(typeDetailsMap.get(Integer.parseInt(reason)).getName() + ",");
		}
		return reasonNameList.substring(0, reasonNameList.length() - 1);
	}
	
	
	/**
	 * @author Naseem Akhtar (naseem@sdrc.co.in)
	 * @param headingRow - the row for which we need to iterate
	 * @param headers - Headers for the sheets with ~ seperated value. 
	 * @return - Row - the row for which we are setting the headings.
	 */
	private Row setHeaderForExcelFiles(Row headingRow, String allHeader) {
		int col = 0;
		String[] headers = allHeader.split("~");
		for(String header : headers){
			headingRow.createCell(col).setCellValue(header);
			col++;
		}
		return headingRow;
	}
	
	
	/** 
	 * @author Naseem Akhtar (naseem@sdrc.co.in)
	 * @param request
	 * @return
	 * This method will return the ip details of the logged in user
	 */
	private String getIpAddr(HttpServletRequest request) {
		final String unkwown = "unknown";
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || unkwown.equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || unkwown.equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || unkwown.equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	

}
