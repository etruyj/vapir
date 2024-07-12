//===================================================================
// BlackPearl.java
//      Description:
//          Functions associated with making API calls to the underlying
//          BlackPearl.
//
// Created by Sean Snyder
//===================================================================

package com.spectralogic.vail.vapir.api;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import com.spectralogic.vail.vapir.model.blackpearl.BpDataPolicy;
import com.spectralogic.vail.vapir.model.blackpearl.BPUser;
import com.spectralogic.vail.vapir.model.blackpearl.Ds3KeyPair;
import com.spectralogic.vail.vapir.model.Token;
import com.spectralogic.vail.vapir.util.http.RestClient;

import java.io.IOException;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlackPearl {
    private static final Logger log = LoggerFactory.getLogger(BlackPearl.class);

    public static Token login(String ip_address, String username, char[] password, RestClient rest_client) throws IOException, JsonParseException {
        Gson gson = new Gson();

        String url = URLs.blackpearlLoginURL(ip_address);

        String body = "{ \"username\":\"" + username +"\", \"password\": \"";

        log.debug("API URL: POST " + url);
        log.debug("Username: " + username);

		for(int i=0; i < password.length; i++)
		{
			body += password[i];
		}

		body += "\" }";

        String response = rest_client.post(url, body);

        log.debug("API Response: " + response);

        return gson.fromJson(response, Token.class);
    }

    public static ArrayList<BpDataPolicy> listDataPolicies(String ip_address, String endpoint_id, String token, RestClient rest_client) throws IOException, JsonParseException {
        Gson gson = new Gson();

        String url = URLs.endpointDataPoliciesURL(ip_address, endpoint_id);

        log.debug("API URL: GET " + url);

        String response = rest_client.get(url, token);

        log.debug("API Response: " + response);

        return gson.fromJson(response, new TypeToken<ArrayList<BpDataPolicy>>(){}.getType());
    }

    public static BPUser listUsers(String ip_address, String token, RestClient rest_client) throws IOException, JsonParseException {
        Gson gson = new Gson();

        String url = URLs.blackpearlUsersListURL(ip_address);

        log.debug("API URL: GET " + url);

        String response = rest_client.get(url, token);

        log.debug("API Response: " + response);

        return gson.fromJson(response, BPUser.class);
    }

    public static Ds3KeyPair getUserKeys(String ip_address, String user_id, String token, RestClient rest_client) throws IOException, JsonParseException {
        Gson gson = new Gson();

        String url = URLs.blackpearlUserKeyURL(ip_address, user_id);

        log.debug("API URL: GET " + url);

        String response = rest_client.get(url, token);

        log.debug("API Response: " + response);

        return gson.fromJson(response, Ds3KeyPair.class);
    }
}
