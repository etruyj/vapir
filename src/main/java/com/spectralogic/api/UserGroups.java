//===================================================================
// UserGroups.java
//      Description:
//          Functions for making API calls to /users/:user/groups
//
// Created by Sean Snyder
//===================================================================

package com.spectralogic.vail.vapir.api;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import com.spectralogic.vail.vapir.model.Group;
import com.spectralogic.vail.vapir.util.http.RestClient;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserGroups {
    private static final Logger log = LoggerFactory.getLogger(UserGroups.class);

    public static Group[] list(String ip_address, String account_id, String user, String token, RestClient rest_client) throws IOException, JsonParseException {
        Gson gson = new Gson();

        String url = URLs.userGroupsURL(ip_address, account_id, user);

        log.info("API URL: GET " + url);

        String response = rest_client.get(url, token);

        log.info("API Response: " + response);

        return gson.fromJson(response, Group[].class);
    }
}
