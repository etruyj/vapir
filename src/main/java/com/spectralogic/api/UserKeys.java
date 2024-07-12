//===================================================================
// UserKeys.java
//      Description:
//          Functions associated with API calls to /user/:userId/keys
//
//  Created by Sean Snyder
//===================================================================

package com.spectralogic.vail.vapir.api;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import com.spectralogic.vail.vapir.model.UserKey;
import com.spectralogic.vail.vapir.util.http.RestClient;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserKeys {
    private static final Logger log = LoggerFactory.getLogger(UserKeys.class);

    public static UserKey createUserKey(String ip_address, String account_id, String user, String token, RestClient rest_client) throws IOException, JsonParseException {
        Gson gson = new Gson();

        String url = URLs.keysURL(ip_address, account_id, user);

        log.debug("API URL: POST " + url);

        String response = rest_client.post(url, token, "");

        log.debug("API Response: " + response);

        return gson.fromJson(response, UserKey.class);
    }

    public static void deleteUserKey(String ip_address, String account_id, String user, String access_key, String token, RestClient rest_client) throws IOException, JsonParseException {
        Gson gson = new Gson();

        String url = URLs.keysDeleteURL(ip_address, account_id, user, access_key);

        log.debug("API URL: DELETE " + url);

        String response = rest_client.delete(url, token);
        
        log.debug("API Response: " + response);
    }

    public static UserKey[] list(String ip_address, String account_id, String user, String token, RestClient rest_client) throws IOException, JsonParseException {
        Gson gson = new Gson();

        String url = URLs.keysURL(ip_address, account_id, user);

        log.debug("API URL: GET " + url);

        String response = rest_client.get(url, token);

        log.debug("API Response: " + response);

        return gson.fromJson(response, UserKey[].class);
    }

}
