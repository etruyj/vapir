//===================================================================
// Endpoints.java
//      Description:
//          Functions associated with the /endpoints API
//
// Create by Sean Snyder
//===================================================================

package com.spectralogic.vail.vapir.api;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import com.spectralogic.vail.vapir.model.Endpoint;
import com.spectralogic.vail.vapir.model.blackpearl.BpDataPolicy;
import com.spectralogic.vail.vapir.util.http.RestClient;

import java.io.IOException;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Endpoints {
    private static final Logger log = LoggerFactory.getLogger(Endpoints.class);

    public static Endpoint[] list(String ip_address, String token, RestClient rest_client) throws IOException, JsonParseException {
        Gson gson = new Gson();

        String url = URLs.endpointsURL(ip_address);

        log.debug("API URL: GET " + url);

        String response = rest_client.get(url, token);

        log.debug("API Response: " + response);

        return gson.fromJson(response, Endpoint[].class);
    }

    public static ArrayList<BpDataPolicy> listDataPolicies(String ip_address, String endpoint_id, String token, RestClient rest_client) throws IOException, JsonParseException {
        Gson gson = new Gson();

        String url = URLs.endpointDataPoliciesURL(ip_address, endpoint_id);

        log.debug("API URL: GET " + url);

        String response = rest_client.get(url, token);

        log.debug("API Response: " + response);

        return gson.fromJson(response, new TypeToken<ArrayList<BpDataPolicy>>(){}.getType());
    }


}
