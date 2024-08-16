//===================================================================
// Groups.java
//      Description:
//          Functions associated with API calls to /groups
//
// Created by Sean Snyder
//===================================================================

package com.spectralogic.vail.vapir.api;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import com.spectralogic.vail.vapir.model.Group;
import com.spectralogic.vail.vapir.model.GroupData;
import com.spectralogic.vail.vapir.util.http.RestClient;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Groups {
    private static final Logger log = LoggerFactory.getLogger(Groups.class);

    public static Group create(Group group, String ip_address, String token, RestClient rest_client) throws IOException, JsonParseException {
        Gson gson = new Gson();

        String url = URLs.createGroupURL(ip_address, group.getAccountId(), group.getName());
        String payload = gson.toJson(group);

        log.debug("API URL: PUT " + url);
        log.debug("API Payload: " + payload);

        String response = rest_client.put(url, token, payload);

        log.debug("API Response: " + response);

        return gson.fromJson(response, Group.class);
    }
    
    @Deprecated // doesn't work with current API
    public static Group createGroup(String ip_address, String account_id, String name, String token, RestClient rest_client) throws IOException, JsonParseException {
        Gson gson = new Gson();

        String url = URLs.createGroupURL(ip_address, account_id, name);

        log.debug("API URL: PUT " + url);

        String response = rest_client.put(url, token, "");

        log.debug("API Response: " + response);

        return gson.fromJson(response, Group.class);
    }

    public static GroupData list(String ip_address, String account_id, String token, RestClient rest_client) throws IOException, JsonParseException {
        Gson gson = new Gson();

        String url = URLs.groupsURL(ip_address, account_id);

        log.debug("API URL: GET " + url);

        String response = rest_client.get(url, token);

        log.debug("API Response: " + response);

        return gson.fromJson(response, GroupData.class);
    }

}
