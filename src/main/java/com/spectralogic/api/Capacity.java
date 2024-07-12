//===================================================================
// Capacity.java
//      Description:
//          Functions associated with the /capacity API
//
// Created by Sean Snyder
//===================================================================

package com.spectralogic.vail.vapir.api;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import com.spectralogic.vail.vapir.model.CapacitySummary;
import com.spectralogic.vail.vapir.util.http.RestClient;

import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Capacity {
    private static final Logger log = LoggerFactory.getLogger(Capacity.class);

    public static ArrayList<CapacitySummary> getCapacitySummary(String ip_address, String token, RestClient rest_client) throws IOException, JsonParseException {
        Gson gson = new Gson();

        String url = URLs.capacitySummarySphereURL(ip_address);

        log.debug("API URL: GET " + url);
        String response = rest_client.get(url, token);

        log.debug("API Response: " + response);

        ArrayList<CapacitySummary> capacity_list = new ArrayList<CapacitySummary>();

        CapacitySummary[] summaries = gson.fromJson(response, CapacitySummary[].class);

        for(CapacitySummary summary : summaries) {
            capacity_list.add(summary);
        }

        return capacity_list;
    }
}
