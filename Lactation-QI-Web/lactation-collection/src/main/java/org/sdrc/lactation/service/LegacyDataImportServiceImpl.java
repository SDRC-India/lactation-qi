package org.sdrc.lactation.service;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.util.DateFormatConverter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.sdrc.lactation.domain.Area;
import org.sdrc.lactation.domain.LogBreastFeedingPostDischarge;
import org.sdrc.lactation.domain.LogBreastFeedingSupportivePractice;
import org.sdrc.lactation.domain.LogExpressionBreastFeed;
import org.sdrc.lactation.domain.LogFeed;
import org.sdrc.lactation.domain.Patient;
import org.sdrc.lactation.domain.TypeDetails;
import org.sdrc.lactation.repository.AreaRepository;
import org.sdrc.lactation.repository.LogBreastFeedingPostDischargeRepository;
import org.sdrc.lactation.repository.LogBreastFeedingSupportivePracticeRepository;
import org.sdrc.lactation.repository.LogExpressionBreastFeedRepository;
import org.sdrc.lactation.repository.LogFeedRepository;
import org.sdrc.lactation.repository.PatientRepository;
import org.sdrc.lactation.repository.TypeDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LegacyDataImportServiceImpl implements LegacyDataImportService {

	private SimpleDateFormat sdfDateOnly = new SimpleDateFormat("dd-MM-yyyy");
	private SimpleDateFormat sdfDateTimeWithSeconds = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat sdfDateInteger = new SimpleDateFormat("ddMMyyyyHHmmssSSS");
	private SimpleDateFormat sdfDateTimeDDFirst = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

	private static final String FILE_NAME = "/opt/lactation/data_dump/Rechecked_102Babies_r1.xlsx";
	private static final Logger log = LogManager.getLogger(SynchronizationServiceImpl.class);

	@Autowired
	private TypeDetailsRepository typeDetailsRepository;

	@Autowired
	private PatientRepository patientRepository;

	@Autowired
	private LogExpressionBreastFeedRepository logExpressionBreastFeedRepository;
	
	@Autowired
	private LogBreastFeedingSupportivePracticeRepository logBreastFeedingSupportivePracticeRepository;
	
	@Autowired
	private LogFeedRepository logFeedRepository;
	
	@Autowired
	private LogBreastFeedingPostDischargeRepository logBreastFeedingPostDischargeRepository;
	
	@Autowired
	private AreaRepository areaRepository;

	@Override
	public Boolean importLegacyData() {
		final String username = "lactation_kem@medela.co.in";
		final String uuid = "Legacy Data";
		final String sysOutFragment = "Inserting row ---> ";

		try (FileInputStream excelFile = new FileInputStream(new File(FILE_NAME));
				XSSFWorkbook workbook = new XSSFWorkbook(excelFile);) {

			//getting area
			Map<String, Area> areaMap = new HashMap<>();
			// getting type details
			Map<String, TypeDetails> typeDetailsMapPatient = new HashMap<>();
			Map<String, TypeDetails> typeDetailsMapBfExp = new HashMap<>();
			Map<String, TypeDetails> typeDetailsMapBfsp = new HashMap<>();
			Map<String, TypeDetails> typeDetailsMapLogFeed = new HashMap<>();
			Map<String, TypeDetails> typeDetailsMapBfpd = new HashMap<>();
			
			areaRepository.findAll().forEach(area -> {
				areaMap.put(area.getName(), area);
			});
			
			typeDetailsRepository.findAll().forEach(typeDetails -> {
				if(typeDetails.getTypeId().getId() < 7)
					typeDetailsMapPatient.put(typeDetails.getName(), typeDetails);
				
				if(typeDetails.getTypeId().getId() == 7 || typeDetails.getTypeId().getId() == 8)
					typeDetailsMapBfExp.put(typeDetails.getName(), typeDetails);
				
				if(typeDetails.getTypeId().getId() == 9 || typeDetails.getTypeId().getId() == 10)
					typeDetailsMapBfsp.put(typeDetails.getName(), typeDetails);
				
				if(typeDetails.getTypeId().getId() == 5 || typeDetails.getTypeId().getId() == 11)
					typeDetailsMapLogFeed.put(typeDetails.getName(), typeDetails);
				
				if(typeDetails.getTypeId().getId() == 12 || typeDetails.getTypeId().getId() == 13)
					typeDetailsMapBfpd.put(typeDetails.getName(), typeDetails);
			});
			
			Map<String, Patient> patientMap = new HashMap<>();

			XSSFSheet patientSheet = workbook.getSheetAt(0);
			XSSFSheet logExpressionBfSheet = workbook.getSheetAt(1);
			XSSFSheet bfspSheet = workbook.getSheetAt(2);
			XSSFSheet logFeedSheet = workbook.getSheetAt(3);
			XSSFSheet bfpdSheet = workbook.getSheetAt(4);

			importPatientData(patientSheet, typeDetailsMapPatient, username, uuid, sysOutFragment, areaMap);
			patientRepository.findAll().forEach(patient -> patientMap.put(patient.getBabyCode(), patient));
			
			importLogExpBfData(logExpressionBfSheet, typeDetailsMapBfExp, patientMap, uuid, sysOutFragment);
			importBfspData(bfspSheet, typeDetailsMapBfsp, patientMap, uuid, sysOutFragment);
			importLogFeedData(logFeedSheet, typeDetailsMapLogFeed, patientMap, uuid, sysOutFragment);
			importBfpdData(bfpdSheet, typeDetailsMapBfpd, patientMap, uuid, sysOutFragment);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}

		return true;
	}

	private void importBfpdData(XSSFSheet bfpdSheet, Map<String, TypeDetails> typeDetailsMapBfpd,
			Map<String, Patient> patientMap, String uuid, String sysOutFragment) throws InterruptedException, ParseException {
		
		List<LogBreastFeedingPostDischarge> bfpdList = new ArrayList<>();
		
		for (int row = 1; row <= bfpdSheet.getLastRowNum(); row++) {
			Thread.sleep(100);
			System.out.println(sysOutFragment + (row+1) + " for BFPD");
			
			LogBreastFeedingPostDischarge bfpd = new LogBreastFeedingPostDischarge();
			bfpd.setUuidNumber(uuid);
			bfpd.setUpdatedDate(new Timestamp(new Date().getTime()));
			bfpd.setCreatedDate(new Timestamp(new Date().getTime()));
			
			XSSFRow xssfRow = bfpdSheet.getRow(row);
			for (int cols = 0; cols < xssfRow.getLastCellNum(); cols++) {
				Cell cell = xssfRow.getCell(cols);

				if (cell != null) {
					switch (cols) {
					case 0:
						Patient patient = patientMap.get(cell.getStringCellValue());
						bfpd.setPatientId(patient);
						bfpd.setCreatedBy(patient.getCreatedBy());
						bfpd.setUpdatedBy(patient.getUpdatedBy());
						System.out.println(sysOutFragment + (row+1) + " column --> PatientId");
						break;
					case 1:
						if(cell.getCellType() == Cell.CELL_TYPE_STRING)
							bfpd.setDateOfBreastFeeding(getTimestampFromString(cell.getStringCellValue()));
						else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
							Date date = cell.getDateCellValue();
							bfpd.setDateOfBreastFeeding(getTimestampFromString(sdfDateOnly.format(date)));
						}
						System.out.println(sysOutFragment + (row+1) + " column --> DateOfBreastFeeding");
						break;
					case 2:
						bfpd.setTimeOfBreastFeeding(typeDetailsMapBfpd.get(cell.getStringCellValue()));
						System.out.println(sysOutFragment + (row+1) + " column --> TimeOfBreastFeeding");
						break;
					case 3:
						bfpd.setBreastFeedingStatus(typeDetailsMapBfpd.get(cell.getStringCellValue()));
						System.out.println(sysOutFragment + (row+1) + " column --> BreastFeedingStatus");
						break;
					default:
						log.error("Error occured in switch case in BFPD in row no --> " + (row+1) + " column no --> " + (cols+1));
						break;
					}
				}
			}
			
			if(bfpd.getPatientId() != null && bfpd.getDateOfBreastFeeding() != null && bfpd.getTimeOfBreastFeeding() != null) {
				bfpd.setUniqueFormId(bfpd.getPatientId().getBabyCode() + "bfpd" + sdfDateInteger.format(new Date()));
				bfpdList.add(bfpd);
			}else {
				log.warn("BFPD --> patient id or date and time of exp is missing or incorrect for row no --->>>" + (row+1));
			}
		}
		
		if(bfpdList != null && !bfpdList.isEmpty())
			logBreastFeedingPostDischargeRepository.save(bfpdList);
	}

	private void importLogFeedData(XSSFSheet logFeedSheet, Map<String, TypeDetails> typeDetailsMapLogFeed,
			Map<String, Patient> patientMap, String uuid, String sysOutFragment) throws InterruptedException, ParseException {
		
		List<LogFeed> logFeedList = new ArrayList<>();
		
		for (int row = 1; row <= logFeedSheet.getLastRowNum(); row++) {
			Thread.sleep(100);
			System.out.println(sysOutFragment + (row+1) + " for Log Feed");
			
			LogFeed logFeed = new LogFeed();
			logFeed.setUuidNumber(uuid);
			logFeed.setUpdatedDate(new Timestamp(new Date().getTime()));
			logFeed.setCreatedDate(new Timestamp(new Date().getTime()));
			XSSFRow xssfRow = logFeedSheet.getRow(row);

			for (int cols = 0; cols < xssfRow.getLastCellNum(); cols++) {
				Cell cell = xssfRow.getCell(cols);

				if (cell != null) {
					switch (cols) {
					case 0:
						Patient patient = cell.getStringCellValue() == null ? null : patientMap.get(cell.getStringCellValue());
						logFeed.setPatientId(patient);
						
						if(patient != null) {
							logFeed.setCreatedBy(patient.getCreatedBy());
							logFeed.setUpdatedBy(patient.getCreatedBy());
						}
						System.out.println(sysOutFragment + (row+1) + " column --> PatientId");
						break;
					case 3:
						logFeed.setDateAndTimeOfFeed(getTimestampFromStringWithDateAndTime(cell.getStringCellValue()));
						System.out.println(sysOutFragment + (row+1) + " column --> Date and time");
						break;
					case 4:
						logFeed.setFeedMethod(typeDetailsMapLogFeed.get(cell.getStringCellValue()));
						System.out.println(sysOutFragment + (row+1) + " column --> feed method");
						break;
					case 5:
						if(cell.getCellType() == Cell.CELL_TYPE_BLANK)
							logFeed.setOmmVolume(null);
						else
							logFeed.setOmmVolume(cell.getNumericCellValue());
						
						System.out.println(sysOutFragment + (row+1) + " column --> omm volume");
						break;
					case 6:
						if(cell.getCellType() == Cell.CELL_TYPE_BLANK)
							logFeed.setDhmVolume(null);
						else
							logFeed.setDhmVolume(cell.getNumericCellValue());
						
						System.out.println(sysOutFragment + (row+1) + " column --> dhm volume");
						break;
					case 7:
						if(cell.getCellType() == Cell.CELL_TYPE_BLANK)
							logFeed.setFormulaVolume(null);
						else
							logFeed.setFormulaVolume(cell.getNumericCellValue());
						
						System.out.println(sysOutFragment + (row+1) + " column --> formula volume");
						break;
					case 8:
						if(cell.getCellType() == Cell.CELL_TYPE_BLANK)
							logFeed.setAnimalMilkVolume(null);
						else
							logFeed.setAnimalMilkVolume(cell.getNumericCellValue());
						
						System.out.println(sysOutFragment + (row+1) + " column --> animal milk volume");
						break;
					case 9:
						if(cell.getCellType() == Cell.CELL_TYPE_BLANK)
							logFeed.setOtherVolume(null);
						else
							logFeed.setOtherVolume(cell.getNumericCellValue());
						
						System.out.println(sysOutFragment + (row+1) + " column --> other volume");
						break;
					case 10:
						logFeed.setLocationOfFeeding(typeDetailsMapLogFeed.get(cell.getStringCellValue()));
						System.out.println(sysOutFragment + (row+1) + " column --> location of feeding");
						break;
					case 11:
						if(cell.getCellType() == Cell.CELL_TYPE_BLANK)
							logFeed.setWeightOfBaby(null);
						else
							logFeed.setWeightOfBaby(cell.getNumericCellValue());
						System.out.println(sysOutFragment + (row+1) + " column --> weight of baby");
						break;
					default:
						log.error("Error occured in switch case in LogFeed in row no --> " + (row+1) + " column no --> " + (cols+1));
						break;
					}
				}
			}
			
			if(logFeed.getPatientId() != null && logFeed.getDateAndTimeOfFeed() != null){
				logFeed.setUniqueFormId(logFeed.getPatientId().getBabyCode() + "feid" + sdfDateInteger.format(new Date()));
				logFeedList.add(logFeed);
			}else {
				log.warn("LogFeed --> patient id or date and time of exp is missing or incorrect for row no --->>>" + (row+1));
			}
		}
		
		if(logFeedList != null && !logFeedList.isEmpty())
			logFeedRepository.save(logFeedList);
	}

	private void importBfspData(XSSFSheet bfspSheet, Map<String, TypeDetails> typeDetailsMap,
			Map<String, Patient> patientMap, String uuid, String sysOutFragment) throws InterruptedException, ParseException {
		
		List<LogBreastFeedingSupportivePractice> bfspList = new ArrayList<>();
		
		for (int row = 1; row <= bfspSheet.getLastRowNum(); row++) {
			Thread.sleep(100);
			System.out.println(sysOutFragment + (row+1) + " for BFSP");
			
			LogBreastFeedingSupportivePractice bfsp = new LogBreastFeedingSupportivePractice();
			bfsp.setUuidNumber(uuid);
			bfsp.setUpdatedDate(new Timestamp(new Date().getTime()));
			bfsp.setCreatedDate(new Timestamp(new Date().getTime()));
			XSSFRow xssfRow = bfspSheet.getRow(row);

			for (int cols = 0; cols < xssfRow.getLastCellNum(); cols++) {
				Cell cell = xssfRow.getCell(cols);

				if (cell != null) {
					switch (cols) {
					case 0:
						Patient patient = patientMap.get(cell.getStringCellValue());
						bfsp.setPatientId(patient);
						bfsp.setCreatedBy(patient.getCreatedBy());
						bfsp.setUpdatedBy(patient.getCreatedBy());
						System.out.println(sysOutFragment + (row+1) + " column --> PatientId");
						break;
					case 3:
						bfsp.setDateAndTimeOfBFSP(getTimestampFromStringWithDateAndTime(cell.getStringCellValue()));
						System.out.println(sysOutFragment + (row+1) + " column --> DateAndTimeOfBFSP");
						break;
					case 4:
						bfsp.setBfspPerformed(typeDetailsMap.get(cell.getStringCellValue()));
						System.out.println(sysOutFragment + (row+1) + " column --> BfspPerformed");
						break;
					case 5:
						bfsp.setPersonWhoPerformedBFSP(typeDetailsMap.get(cell.getStringCellValue()));
						System.out.println(sysOutFragment + (row+1) + " column --> PersonWhoPerformedBFSP");
						break;
					case 6:
						bfsp.setBfspDuration((int) cell.getNumericCellValue());
						System.out.println(sysOutFragment + (row+1) + " column --> BfspDuration");
						break;
					default:
						log.error("Error occured in switch case in BFSP in row no --> " + (row+1) + " column no --> " + (cols+1));
						break;
					}
				}
			}
			if(bfsp.getPatientId() != null && bfsp.getDateAndTimeOfBFSP() != null) {
				bfsp.setUniqueFormId(bfsp.getPatientId().getBabyCode() + "bfps" + sdfDateInteger.format(new Date()));
				bfspList.add(bfsp);
			}else {
				log.warn("BFSP --> patient id or date and time of exp is missing or incorrect for row no --->>>" + (row+1));
			}
		}
		
		if(bfspList != null && !bfspList.isEmpty())
			logBreastFeedingSupportivePracticeRepository.save(bfspList);
	}

	private void importLogExpBfData(XSSFSheet logExpressionBfSheet, Map<String, TypeDetails> typeDetailsMap,
			Map<String, Patient> patientMap, String uuid, String sysOutFragment) throws ParseException, InterruptedException {
		List<LogExpressionBreastFeed> bfExps = new ArrayList<>();
		for (int row = 1; row <= logExpressionBfSheet.getLastRowNum(); row++) {
			Thread.sleep(100);
			System.out.println(sysOutFragment + (row+1) + " for BF Expressions");
			LogExpressionBreastFeed bfExp = new LogExpressionBreastFeed();
			bfExp.setUuidNumber(uuid);
			bfExp.setUpdatedDate(new Timestamp(new Date().getTime()));
			bfExp.setCreatedDate(new Timestamp(new Date().getTime()));
			XSSFRow xssfRow = logExpressionBfSheet.getRow(row);

			if(xssfRow != null) {
				for (int cols = 0; cols < xssfRow.getLastCellNum(); cols++) {
					Cell cell = xssfRow.getCell(cols);
	
					if (cell != null) {
						switch (cols) {
						case 0:
							Patient patient = patientMap.get(cell.getStringCellValue());
							bfExp.setPatientId(patient);
							bfExp.setCreatedBy(patient.getCreatedBy());
							bfExp.setUpdatedBy(patient.getCreatedBy());
							System.out.println(sysOutFragment + (row+1) + " column --> patient id");
							break;
						case 3:
							bfExp.setDateAndTimeOfExpression(getTimestampFromStringWithDateAndTime(cell.getStringCellValue()));
							System.out.println(sysOutFragment + (row+1) + " column --> DateAndTimeOfExpression");
							break;
						case 4:
							bfExp.setMethodOfExpression(typeDetailsMap.get(cell.getStringCellValue()));
							System.out.println(sysOutFragment + (row+1) + " column --> MethodOfExpression");
							break;
						case 5:
							if(cell.getCellType() == Cell.CELL_TYPE_BLANK)
								bfExp.setMethodOfExpressionOthers(null);
							else
								bfExp.setMethodOfExpressionOthers(cell.getStringCellValue());
							System.out.println(sysOutFragment + (row+1) + " column --> MethodOfExpressionOthers");
							break;
						case 6:
							bfExp.setExpressionOccuredLocation(typeDetailsMap.get(cell.getStringCellValue()));
							System.out.println(sysOutFragment + (row+1) + " column --> ExpressionOccuredLocation");
							break;
						case 7:
							if(cell.getCellType() == Cell.CELL_TYPE_BLANK)
								bfExp.setMilkExpressedFromLeftAndRightBreast(null);
							else
								bfExp.setMilkExpressedFromLeftAndRightBreast(cell.getNumericCellValue());
							System.out.println(sysOutFragment + (row+1) + " column --> MilkExpressedFromLeftAndRightBreast");
							break;
						}
					}
				}

				if (bfExp.getPatientId() != null && bfExp.getDateAndTimeOfExpression() != null) {
					bfExp.setUniqueFormId(bfExp.getPatientId().getBabyCode() + "bfid" + sdfDateInteger.format(new Date()));
					bfExps.add(bfExp);
				} else {
					log.warn("BFExpression: patient id or date and time of exp is missing or incorrect for row no --->>>" + (row+1));
				}
			}
		}

		if(bfExps != null && !bfExps.isEmpty())
			logExpressionBreastFeedRepository.save(bfExps);
	}

	private void importPatientData(XSSFSheet patientSheet, Map<String, TypeDetails> typeDetailsMap, String username, String uuid, String sysOutFragment, Map<String, Area> areaMap)
			throws ParseException {
		List<Patient> patientList = new ArrayList<>();
		for (int row = 1; row <= patientSheet.getLastRowNum(); row++) {
			
			System.out.println(sysOutFragment + (row+1));
			
			Patient patient = new Patient();
			patient.setCreatedBy(username);
			patient.setUpdatedBy(username);
			patient.setUuidNumber(uuid);
			patient.setUpdatedDate(new Timestamp(new Date().getTime()));
			
			XSSFRow xssfRow = patientSheet.getRow(row);

			for (int cols = 0; cols < xssfRow.getLastCellNum(); cols++) {
				Cell cell = xssfRow.getCell(cols);
				String userName = null;

				if (cell != null) {
					switch (cols) {
					case 1:
						Date date = cell.getDateCellValue();
//						String cellStringValue = dataFormatter.formatCellValue(cell);
						patient.setCreatedDate(getTimestampFromString(sdfDateOnly.format(date)));
						System.out.println(sysOutFragment + (row+1) + " column --> created date");
						break;
					case 4:
						userName = generateUserNameForLegacyDataImport(areaMap.get(cell.getStringCellValue()));
						patient.setCreatedBy(userName);
						patient.setUpdatedBy(userName);
						System.out.println("Fetching the hospital to which the baby belongs");
						break;
					case 6:
						patient.setBabyCode(cell.getStringCellValue());
						System.out.println(sysOutFragment + (row+1) + " column --> baby code");
						break;
					case 7:
						if(cell.getCellType() == 0)
							patient.setBabyCodeHospital(String.valueOf((int)cell.getNumericCellValue()));
						else if(cell.getCellType() == 1)
							patient.setBabyCodeHospital(cell.getStringCellValue());
						 
						System.out.println(sysOutFragment + (row+1) + " column --> baby code hospital");
						break;
					case 8:
						patient.setBabyOf(cell.getStringCellValue());
						System.out.println(sysOutFragment + (row+1) + " column --> baby of");
						break;
					case 9:
						patient.setMothersAge((int) cell.getNumericCellValue());
						System.out.println(sysOutFragment + (row+1) + " column --> mothers age");
						break;
					case 12:
						patient.setDeliveryDateAndTime(getTimestampFromStringWithDateAndTime(cell.getStringCellValue()));
						System.out.println(sysOutFragment + (row+1) + " column --> deilvery date and time");
						break;
					case 13:
						patient.setDeliveryMethod(typeDetailsMap.get(cell.getStringCellValue()));
						System.out.println(sysOutFragment + (row+1) + " column --> deilvery method");
						break;
					case 14:
						patient.setBabyWeight(cell.getNumericCellValue());
						System.out.println(sysOutFragment + (row+1) + " column --> baby weight");
						break;
					case 15:
						patient.setGestationalAgeInWeek((int) cell.getNumericCellValue());
						System.out.println(sysOutFragment + (row+1) + " column --> gestational age in weeks");
						break;
					case 16:
						patient.setMothersPrenatalIntent(typeDetailsMap.get(cell.getStringCellValue()));
						System.out.println(sysOutFragment + (row+1) + " column --> mothers prenatal intent");
						break;
					case 17:
						patient.setParentsKnowledgeOnHmAndLactation(typeDetailsMap.get(cell.getStringCellValue()));
						System.out.println(
								sysOutFragment + (row+1) + " column --> Parents Knowledge On Hm And Lactation");
						break;
					case 18:
						patient.setTimeTillFirstExpression(String.valueOf(cell.getNumericCellValue()));
						System.out.println(cell.getNumericCellValue());
						System.out.println(sysOutFragment + (row+1) + " column --> Time till first expression");
						break;
					case 19:
						patient.setInpatientOrOutPatient(typeDetailsMap.get(cell.getStringCellValue()));
						System.out.println(sysOutFragment + (row+1) + " column --> Inpatient or outpatient");
						break;
					case 20:
						if (patient != null && patient.getInpatientOrOutPatient() != null && patient.getInpatientOrOutPatient().getId() == 13) {
							Date admissionDate = cell.getDateCellValue();
							patient.setAdmissionDateForOutdoorPatients(getTimestampFromString(sdfDateOnly.format(admissionDate)));
							System.out.println(sysOutFragment + (row+1) + " column --> admission date for outdoor patients");
						}
						break;
					case 21:
						patient.setBabyAdmittedTo(typeDetailsMap.get(cell.getStringCellValue()));
						System.out.println(sysOutFragment + (row+1) + " column --> baby admitted to");
						break;
					case 22:
						patient.setNicuAdmissionReason(reasonsToIds(cell.getStringCellValue(), typeDetailsMap));
						System.out.println(sysOutFragment + (row+1) + " column --> nicu admission reason");
						break;
					case 23:
						if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
							Date dischargeDate = cell.getDateCellValue();
							patient.setDischargeDate(getTimestampFromString(sdfDateOnly.format(dischargeDate)));
						}else if(cell.getCellType() == Cell.CELL_TYPE_STRING)
							patient.setDischargeDate(getTimestampFromString(cell.getStringCellValue()));
							
						System.out.println(sysOutFragment + (row+1) + " column --> discharge date");
						break;
					default:
						log.error("Patient: error occured in switch case for row --> " + (row+1) + " column ---> " + (cols+1));
					}
				}
			}
			
			if(patient.getBabyCode() != null)
				patientList.add(patient);
		}

		if(patientList != null && !patientList.isEmpty())
			patientRepository.save(patientList);
		System.out.println("Add patient import completed");
	}

	private String generateUserNameForLegacyDataImport(Area area) {
		String userName;
		if(area != null) {
			if(area.getShortName() != null)
				userName = "lactation_" + area.getShortName().toLowerCase() + "@medela.co.in";
			else
				userName = "lactation_" + area.getName().toLowerCase().substring(0, 3) + "@medela.co.in";
		}else
			userName = "lactation_legacy@medela.co.in";
			
		
		return userName;
	}

	private Timestamp getTimestampFromString(String date) throws ParseException {
		return new Timestamp(sdfDateOnly.parse(date).getTime());
	}
	
	private Timestamp getTimestampFromStringWithDateAndTime(String date) throws ParseException {
		return new Timestamp(sdfDateTimeWithSeconds.parse(sdfDateTimeWithSeconds.format(sdfDateTimeDDFirst.parse(date))).getTime());
//		return new Timestamp(sdfDateTimeWithSeconds.parse(date).getTime());
	}
	
	private String reasonsToIds(String reasons, Map<String, TypeDetails> typeDetailsMap) {
		StringBuilder arrayAsString = new StringBuilder();
		String[] reasonArray = reasons.split(",");

		for (int i = 0; i < reasonArray.length; i++) {
			arrayAsString.append(typeDetailsMap.get(reasonArray[i].trim()).getId() + ",");
		}
		return arrayAsString.substring(0, arrayAsString.length() - 1);
	}

}
