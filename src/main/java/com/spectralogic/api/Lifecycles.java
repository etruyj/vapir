//===================================================================
// Lifecycles.java
//      Description:
//          Functions associated with the /lifecycles API
//
// Created by Sean Snyder
//===================================================================

package com.spectralogic.vail.vapir.api;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import com.spectralogic.vail.vapir.model.Lifecycle;
import com.spectralogic.vail.vapir.util.http.RestClient;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lifecycles {
    private static final Logger log = LoggerFactory.getLogger(Lifecycles.class);

    public static Lifecycle createLifecycle(String ip_address, String name, String body, String token, RestClient rest_client) throws IOException, JsonParseException {
        String url = URLs.lifecycleURL(ip_address);

        log.debug("API URL: POST " + url);
        log.debug("API Payload: " + body);

        String response = rest_client.post(url, token, body);

        log.debug("API Response: " + response);

        return convertResponseToLifecycle(response);
    }

    public static Lifecycle createLifecycle(String ip_address, Lifecycle lifecycle, String token, RestClient rest_client) throws IOException, JsonParseException {
        Gson gson = new Gson();

        String url = URLs.lifecycleURL(ip_address);
        String payload = gson.toJson(lifecycle);

        log.debug("API URL: POST " + url);
        log.debug("API Payload: " + payload);

        String response = rest_client.post(url, token, payload);

        log.debug("API Response: " + response);

        return convertResponseToLifecycle(response);
    }

    public static Lifecycle getLifecycle(String ip_address, String lifecycle_id, String token, RestClient rest_client) throws IOException, JsonParseException {
        String url = URLs.getLifecycleURL(ip_address, lifecycle_id);

        log.debug("API URL: GET " + url);

        String response = rest_client.get(url, token);

        log.debug("API Response: " + response);

        return convertResponseToLifecycle(response);
    }

    public static Lifecycle[] list(String ip_address, String token, RestClient rest_client) throws IOException, JsonParseException {
        Gson gson = new Gson();

        String url = URLs.lifecycleURL(ip_address);

        log.debug("API URL: GET " + url);

        String response = rest_client.get(url, token);

        log.debug("API Response: " + response);

        return gson.fromJson(response, Lifecycle[].class);
    }

    //===========================================
    // Private Functions
    //===========================================

    private static Lifecycle convertResponseToLifecycle(String response) throws JsonParseException {
        // Force a check on the JsonParseException.
        // gson.fromJson() can't be trusted to trigger a parse exception on nested objects like
        // model.Lifecycle. The response is converted to a Lifecycle and then the name is checked
        // to see if it exists. As name is a required field for lifecycles, a null value implies
        // a failed response from the system. The server message is processed and thrown as a 
        // JsonParseException.
        Gson gson = new Gson();
        Lifecycle lifecycle = gson.fromJson(response, Lifecycle.class);

        if(lifecycle.getName() != null) {
            return lifecycle; 
        } else {
            String error = ServerMessage.process(response);
            throw new JsonParseException(error);
        }
    }
}
