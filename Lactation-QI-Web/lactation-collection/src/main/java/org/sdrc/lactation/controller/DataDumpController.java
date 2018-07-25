package org.sdrc.lactation.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.sdrc.lactation.service.DataDumpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in) on 5th March 2018 20:23
 * 
 * This controller will be used to handle the api that we are using to dump the DB data in excel file
 * as well as in json format.
 *
 */

@RestController
public class DataDumpController {
	
	private static final Logger log = LogManager.getLogger(DataDumpController.class);
	
	@Autowired
	private DataDumpService dataDumpService;
	
	// for data dump in excel file
	@CrossOrigin
	@RequestMapping(value = "/exportDataInFile", method=RequestMethod.POST)
	public void exportDataInFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String fileName = dataDumpService.exportDataInExcel(request, response);
		
		if(fileName != null){
			try(InputStream inputStream = new FileInputStream(fileName)) {
				String headerKey = "Content-Disposition";
				String headerValue = String.format("attachment; filename=\"%s\"",
						new java.io.File(fileName).getName());
				response.setHeader(headerKey, headerValue);
				response.setContentType("application/octet-stream"); //for all file type
				ServletOutputStream outputStream = response.getOutputStream();
				FileCopyUtils.copy(inputStream, outputStream);
				outputStream.close();
			} catch (FileNotFoundException e) {
				log.error("DataDumpController - exportDataInFile - File Not Found Exception - " + e.getMessage());
			} catch (IOException e) {
				log.error("DataDumpController - exportDataInFile - Input/output exception - " + e.getMessage());
			}
			finally{
				Path path = Paths.get(fileName);
				Files.delete(path);
			}
		}
	}
	
	//for data dump in json format
	@CrossOrigin
	@RequestMapping(value = "/exportDataInJson", method=RequestMethod.POST)
	public JSONObject exportDataInJson(HttpServletRequest request, HttpServletResponse response) {
		return dataDumpService.exportDataInJson(request, response);
	}


}
