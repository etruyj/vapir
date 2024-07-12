//====================================================================
// GetCapacitySummary.java
//      Description:
//          Returns the capacity of different storage locations in the
//          Vail sphere.
//
// Created by Sean Snyder
//===================================================================

package com.spectralogic.vail.vapir.command;

import com.spectralogic.vail.vapir.model.CapacitySummary;
import com.spectralogic.vail.vapir.api.VailConnector;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetCapacitySummary {
    private static final Logger log = LoggerFactory.getLogger(GetCapacitySummary.class);

    public static ArrayList<CapacitySummary> fromSphere(String ip_address, VailConnector sphere) {
        try {
            log.info("Querying Vail sphere at " + ip_address + " for capacity summary.");
            return sphere.getCapacitySummary(ip_address);
        } catch (Exception e) {
            log.error(e.getMessage());
            System.err.println(e.getMessage());
            return new ArrayList<CapacitySummary>();
        }
    }
}
