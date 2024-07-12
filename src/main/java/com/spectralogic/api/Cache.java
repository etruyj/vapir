//===================================================================
// Cache.java
//      Description:
//          Functions associated with the /cache API
//
// Created by Sean Snyder
//===================================================================

package com.spectralogic.vail.vapir.api;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import com.spectralogic.vail.vapir.util.http.RestClient;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cache {
    private static final Logger log = LoggerFactory.getLogger(Cache.class);

    public static void clearCache(String ip_address, String token, RestClient rest_client) throws IOException {
        String url = URLs.clearCacheURL(ip_address);

        log.debug("API URL: POST " + url);
        String response = rest_client.post(url, token, "");

        log.debug("API Response: " + response);
    } 
}
