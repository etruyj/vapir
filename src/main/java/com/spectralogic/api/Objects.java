//===================================================================
// Objects.java
//      Description:
//          Functions associated with API calls to the /objects
//
// Created by Sean Snyder
//===================================================================

package com.spectralogic.vail.vapir.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import com.spectralogic.vail.vapir.model.BucketObjects;
import com.spectralogic.vail.vapir.util.http.RestClient;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Objects {
    private static final Logger log = LoggerFactory.getLogger(Objects.class);

    public static BucketObjects list(String ip_address, String bucket_id, String marker, Integer max_keys, String token, RestClient rest_client) throws IOException, JsonParseException {
        Gson gson = new Gson();

        String url = URLs.objectsURL(ip_address, bucket_id);

        // Add Query Parameters
        //      Max Keys is required to control memory management.
        if(max_keys == null || max_keys == 0) {
            max_keys = 300; // Vail's default.
        }

        url += "?max-keys=" + max_keys;

        if(marker != null) {
            url += "&marker=" + marker;
        }
        log.debug("API URL: GET " + url);

        String response = rest_client.get(url, token);

        log.debug("API Response: " + response);

        return gson.fromJson(response, BucketObjects.class);
    } 
}
