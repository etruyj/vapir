//===================================================================
// ActivateNode.java
//      Description:
//          This class handles the API call necessary to activate
//          a Vail node within a sphere.
//
// Created by Sean Snyder
// Copyright Spectra Logic 2024
//===================================================================

package com.spectralogic.vail.vapir.api;

import com.spectralogic.vail.vapir.util.http.RestClient;
import com.spectralogic.vail.vapir.model.NodeActivationPacket;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActivateNode {
    private static final Logger log = LoggerFactory.getLogger(ActivateNode.class);

    public static boolean activate(NodeActivationPacket packet, String ip_address, String token, RestClient rest_client) throws IOException, JsonParseException {
         Gson gson = new Gson();

         String url = URLs.activateURL(ip_address);
         String payload = gson.toJson(packet);

         log.debug("API URL: POST " + url);
         log.debug("API Payload: " + payload);

         String response = rest_client.post(url, token, payload);

         log.debug("API Response: " + response);

         if(response.contains("204 No Content")) {
            // No response. Assuming http 204.
             return true;
         } else {
            // Error response was returned.    
            return false;
         }
    }
}
