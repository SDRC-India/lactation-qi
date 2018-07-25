/**
 * @author Naseem Akhtar (naseem@sdrc.co.in) on 12th February 2018 2057.
 * This service will handle the request coming from mobile to SynchronizationController.
 */
package org.sdrc.lactation.service;

import org.sdrc.lactation.model.SyncModel;
import org.sdrc.lactation.model.SyncResult;
import org.springframework.http.HttpRequest;

public interface SynchronizationService {

	/**
	 * This method will receive data from a device and sync it with the servers db.
	 * 
	 * @param synchronizationModel
	 * @return
	 */
	SyncResult synchronizeForms(SyncModel synchronizationModels, HttpRequest httpRequest);

	/**
	 * This method is used to set unique id, created date, updated date and other such things for legacy data.
	 * This method will make the legacy data format consistent with other records.
	 * 
	 * @return {@link Boolean} depending on the status of the operation
	 */
	Boolean setUniqueId();
	
}
