package org.sdrc.lactation.controller;

import org.sdrc.lactation.service.LegacyDataImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LegacyDataImportController {
	
	@Autowired
	private LegacyDataImportService legacyDataImportService;
	
	@RequestMapping(value = "/importData", method = RequestMethod.GET)
	public Boolean importLegacyData() {
		return legacyDataImportService.importLegacyData();
	}

}
