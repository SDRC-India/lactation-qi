package org.sdrc.lactation.utils;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in)
 * 
 *  This class will contain as the name suggests all the constant variable names.
 *  We are assigning the externalized property files key to variables in this class.
 *
 */
public class Constants {
	
	// Email related configuration
	static final String EMAIL = "email";
	public static final String EMAIL_PASSWORD = "email.password";
	public static final String EMAIL_TO = "email.to";
	public static final String EMAIL_SUBJECT = "email.subject";
	public static final String EMAIL_TEXT = "email.text";
	public static final String EMAIL_INVALID_USER_SUBJECT = "email.invalid.user.subject";
	
	//Genrated report related configuration
	public static final String REPORT_PATH = "report.filepath";
	public static final String REPORT_BABY_SHEET = "report.sheet.baby";
	public static final String REPORT_USER_SHEET =  "report.sheet.user";
	public static final String SL_NO = "slno";
	public static final String BABY_LIST = "baby.list";
	public static final String BABY_CREATED_ON = "baby.created.on";
	public static final String USER_LIST = "user.list";
	public static final String USER_SYNCED_ON = "user.synced.on";
	
	//Data dump related configurations
	public static final String HEADER_SEPERATOR = "header.seperator";
	public static final String DATA_DUMP_PATH = "dataDump.filepath";
	//header of different sheets e.g - patient, bfsp
	public static final String SHEET_PATIENT_HEADING = "sheet.patient";
	public static final String SHEET_BFEXP_HEADING = "sheet.bfExp";
	public static final String SHEET_BFSP_HEADING = "sheet.bfsp";
	public static final String SHEET_LOGFEED_HEADING = "sheet.logfeed";
	public static final String SHEET_BFPD_HEADING = "sheet.bfpd";
	
	//Breastfeed expressions
	public static final String EXPRESSION_METHOD_OTHER = "expression.method.other";

}
