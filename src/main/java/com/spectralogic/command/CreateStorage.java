//===================================================================
// CreateStorage.java
//      Description:
//          The purpose of this class is to handle the commands
//          associated with creating storage in the Vail Sphere.
//
// Created by Sean Snyder
// Copyright Spectra Logic 2024
//===================================================================

package com.spectralogic.vail.vapir.command;

import com.spectralogic.vail.vapir.api.VailConnector;
import com.spectralogic.vail.vapir.model.Storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateStorage {
    public static final Logger log = LoggerFactory.getLogger(CreateStorage.class);

    public static Storage createNew(String ip_address, Storage storage, VailConnector sphere) {
        log.info("Creating new storage location [" + storage.getName() + "]");

        try {
            Storage new_storage = sphere.createStorage(storage, ip_address);
            log.info("Successfully created new storage location.");
            return new_storage;
        } catch(Exception e) {
            log.error(e.getMessage());
            log.error("Failed to create storage [" + storage.getName() + "]");
            return null;
        }
    }
}
