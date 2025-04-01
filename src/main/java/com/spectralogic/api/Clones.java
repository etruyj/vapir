//===================================================================
// Clones.java
//      Description:
//          Functions associated with API calls to the /clones
//
// Created by Sean Snyder
//===================================================================

package com.spectralogic.vail.vapir.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import com.spectralogic.vail.vapir.model.Clone;
import com.spectralogic.vail.vapir.util.http.RestClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Clones {
    private static final Logger log = LoggerFactory.getLogger(Objects.class);

    public static Clone get(String ip_address, String bucket_id, String key, String version_id, String token, RestClient rest_client) throws IOException, JsonParseException, UnsupportedEncodingException {
        Gson gson = new Gson();

        // Encode weird stuff in the key
        key = URLEncoder.encode(key, "UTF-8");
        key = key.replace(" ", "%20");

        String url = URLs.clonesURL(ip_address, bucket_id, key);

        // Add Query Parameters
        if(version_id != null) {
            url += "&version=" + version_id;
        }

        // Since we're dealing with human defined keys. Replace spaces with %20
        // also replace back ticks with %60
        url = url.replace(" ", "%20").replace("`","%60");

        log.debug("API URL: GET " + url);

        String response = rest_client.get(url, token);

        log.debug("API Response: " + response);

        return gson.fromJson(response, Clone.class);
    } 
}
