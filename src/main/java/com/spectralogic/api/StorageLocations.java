//===================================================================
// StorageLocations.java
//      Description:
//          Functions associated with API calls to storage locations.
//
// Created by Sean Snyder
//===================================================================

package com.spectralogic.vail.vapir.api;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import com.spectralogic.vail.vapir.model.Storage;
import com.spectralogic.vail.vapir.util.http.RestClient;

import java.io.IOException; 

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StorageLocations {
    private static final Logger log = LoggerFactory.getLogger(StorageLocations.class);

   	public static Storage addStorage(String ip_address, String name, String body, String token, RestClient rest_client) throws IOException, JsonParseException {
		String url = URLs.storageURL(ip_address);

        log.debug("API URL: POST " + url);
        log.debug("API Payload: " + body);

		String response = rest_client.post(url, token, body);
	
        log.debug("API Response: " + response);

        return convertResponseToStorage(response);	
	}

   	public static Storage addStorage(String ip_address, Storage location, String token, RestClient rest_client) throws IOException, JsonParseException {
        Gson gson = new Gson();

        String url = URLs.storageURL(ip_address);
        String payload = gson.toJson(location);

        log.debug("API URL: POST " + url);
        log.debug("API Payload: " + payload);

        String response = rest_client.post(url, token, payload);

        log.debug("API Response: " + response);

        return convertResponseToStorage(response);
    }

    public static Storage createStorage(Storage storage, String ip_address, String token, RestClient rest_client) throws IOException, JsonParseException {
        Gson gson = new Gson();

        String url = URLs.storageURL(ip_address);
        String payload = gson.toJson(storage);

        log.debug("API URL: POST " + url);
        log.debug("API Payload: " + payload);

        String response = rest_client.post(url, token, payload);

        log.debug("API Response: " + response);

        return convertResponseToStorage(response);
    }

    public static Storage[] list(String ip_address, String token, RestClient rest_client) throws IOException, JsonParseException {
        Gson gson = new Gson();

        String url = URLs.storageURL(ip_address);

        String response = rest_client.get(url, token);

        log.debug("API Response: " + response);

        return gson.fromJson(response, Storage[].class);
    }

    //===========================================
    // Private Functions
    //===========================================

    private static Storage convertResponseToStorage(String response) throws JsonParseException {
        // Manually test for JsonParseExceptions.
        // Objects with nested classes are still created even if the response doesn't
        // fit the object. This class tests if there is a name associated with the
        // storage location, if not, parsing failed and an error is returned.
        
        Gson gson = new Gson();

        Storage storage = gson.fromJson(response, Storage.class);

        if(storage.getName() != null) {
            return storage;
        } else {
            String error = ServerMessage.process(response);
            throw new JsonParseException(error);
        }
    }
}
