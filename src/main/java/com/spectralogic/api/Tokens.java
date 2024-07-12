//===================================================================
// Tokens.java
//      Description:
//          Functions associated with making API calls to the /tokens
//          endpoint.
//
// Created by Sean Snyder
//===================================================================

package com.spectralogic.vail.vapir.api;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import com.spectralogic.vail.vapir.model.Token;
import com.spectralogic.vail.vapir.util.http.RestClient;

import java.io.IOException;

public class Tokens {
    public static Token login(String ip_address, String username, String password, RestClient rest_client) throws IOException, JsonParseException {
        Gson gson = new Gson();

        String url = URLs.loginURL(ip_address);
        String auth = "{ \"username\":\"" 
			+ username + "\", \"password\":\"" + password
			+ "\" }";

        String response = rest_client.post(url, auth);

        return gson.fromJson(response, Token.class);
    }
}
