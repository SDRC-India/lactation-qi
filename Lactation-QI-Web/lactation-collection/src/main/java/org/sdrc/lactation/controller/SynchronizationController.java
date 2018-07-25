package org.sdrc.lactation.controller;

import org.sdrc.lactation.model.SyncModel;
import org.sdrc.lactation.model.SyncResult;
import org.sdrc.lactation.service.SynchronizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in) on 9th February 2018 17:10. This
 *         controller will be used to handle all the requests coming from the
 *         mobile application.
 */

@RestController
public class SynchronizationController {

	@Autowired
	private SynchronizationService synchronizationService;

	/**
	 * This API will receive data from a device and sync it with the servers db.
	 * 
	 * @param synchronizationModel
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value = "/sync", method = RequestMethod.POST)
	public SyncResult synchronize(@RequestBody SyncModel synchronizationModel) {
		return synchronizationService.synchronizeForms(synchronizationModel, null);
	}

	/**
	 * This API call is for checking whether the project is deployed successfully and also to check whether
	 * the server is up and running or not.
	 * 
	 * @return {@link Boolean} - status of the server.
	 */
	@CrossOrigin
	@RequestMapping(value = "/serverStatus", method = RequestMethod.GET)
	public Boolean serverStatus() {
		return true;
	}

	/**
	 * This API is used to set unique id, created date, updated date and other such things for legacy data.
	 * This API will make the legacy data format consistent with other records.
	 * 
	 * @return {@link Boolean} depending on the status of the operation
	 */
	@RequestMapping(value = "/setUniqueId", method = RequestMethod.GET)
	public Boolean setUniqueId() {
		return synchronizationService.setUniqueId();
	}

}
