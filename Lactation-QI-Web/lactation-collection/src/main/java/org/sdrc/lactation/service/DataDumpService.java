package org.sdrc.lactation.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in)
 *
 * This service will be used to handle all the requests related to export of data through API call
 */

public interface DataDumpService {

	/**
	 * Export data in excel. 
	 * 
	 * @param request
	 * @param response
	 * @return path of the excel file that is generated in the server
	 */
	String exportDataInExcel(HttpServletRequest request, HttpServletResponse response);

	/**
	 * Export data in json format.
	 * 
	 * @param request
	 * @param response
	 * @return JSONObject which contains patients and patient related forms.
	 */
	JSONObject exportDataInJson(HttpServletRequest request, HttpServletResponse response);

}
