//===================================================================
// ListStorage.java
//      Description:
//          Commands associated with listing storage locations attached
//          to a Vail sphere.
//
// Created by Sean Snyder
//===================================================================

package com.spectralogic.vail.vapir.command;

import com.spectralogic.vail.vapir.model.Storage;
import com.spectralogic.vail.vapir.api.VailConnector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ListStorage {
    private static final Logger log = LoggerFactory.getLogger(ListStorage.class);

    public static Storage[] all(String ip_address, VailConnector sphere) {
        log.info("Listing all storage locations.");
        try {
            Storage[] storage = sphere.listStorage(ip_address);
            log.info("Found (" + storage.length + ") storage locations.");
            return storage;
        } catch(Exception e) {
            log.error(e.getMessage());
            System.err.println(e.getMessage());
            return null;
        }
    }
}
