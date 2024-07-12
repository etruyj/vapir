//===================================================================
// ListEndpoints.java
//      Description:
//          Pulls a list of endpoints attached to the sphere.
//
// Created by Sean Snyder
// Copyright Spectra Logic 2024
//===================================================================

package com.spectralogic.vail.vapir.command;

import com.spectralogic.vail.vapir.api.VailConnector;
import com.spectralogic.vail.vapir.model.Endpoint;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ListEndpoints {
    private static final Logger log = LoggerFactory.getLogger(ListEndpoints.class);

    public static ArrayList<Endpoint> all(VailConnector sphere) {
        log.info("Pulling a list of all endpoints connected to the sphere.");

        ArrayList<Endpoint> endpoint_list = new ArrayList<Endpoint>();

        try {
            Endpoint[] endpoints = sphere.listEndpoints();

            for(Endpoint endpoint : endpoints) {
                endpoint_list.add(endpoint);
            }

            log.info("Found (" + endpoint_list.size() + ") endpoints attached to the sphere.");
        } catch(Exception e) {
            log.error(e.getMessage());
            log.error("Failed to retrieve endpoint list.");
        }

        return endpoint_list;
    }

    public static ArrayList<Endpoint> blackpearl(VailConnector sphere) {
        log.info("Pulling a list of blackpearl endpoints connected to the sphere.");

        ArrayList<Endpoint> endpoint_list = new ArrayList<Endpoint>();

        try {
            Endpoint[] endpoints = sphere.listEndpoints();

            for(Endpoint endpoint : endpoints) {
                if(endpoint.getType().equals("bp")) {
                    endpoint_list.add(endpoint);
                }
            }

            log.info("Found (" + endpoint_list.size() + ") endpoints attached to the sphere.");
        } catch(Exception e) {
            log.error(e.getMessage());
            log.error("Failed to retrieve endpoint list.");
        }

        return endpoint_list;
    }
}
