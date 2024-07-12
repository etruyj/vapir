//===================================================================
// Users.java
//      Description:
//          Functions associated with API calls to /users
//===================================================================

package com.spectralogic.vail.vapir.api;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import com.spectralogic.vail.vapir.model.User;
import com.spectralogic.vail.vapir.model.UserData;
import com.spectralogic.vail.vapir.util.http.RestClient;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Users {
    private static final Logger log = LoggerFactory.getLogger(Users.class);

    public static User createUser(String ip_address, String account_id, String username, String token, RestClient rest_client) throws IOException, JsonParseException {
        Gson gson = new Gson();

        String url = URLs.getUserURL(ip_address, account_id, username);

        log.debug("API URL: PUT " + url);

        String response = rest_client.put(url, token, "");

        log.debug("API Response: " + response);

        User new_user = gson.fromJson(response, User.class);

        if(new_user.getUsername() == null || new_user.getUsername() == "") {
            String error = ServerMessage.process(response);
            log.error(error);
            return null;
        } else {
            return new_user;
        }
    }

    public static void deleteUser(String ip_address, String account_id, String username, String token, RestClient rest_client) throws IOException, JsonParseException {
        Gson gson = new Gson();

        String url = URLs.getUserURL(ip_address, account_id, username);

        log.debug("API URL: DELETE " + url);

        String response = rest_client.delete(url, token);
        
        log.debug("API Response: " + response);

    }

    public static UserData list(String ip_address, String account_id, String token, RestClient rest_client) throws IOException, JsonParseException {
        Gson gson = new Gson();

        String url = URLs.usersURL(ip_address, account_id);

        log.debug("API URL: GET " + url);

        String response = rest_client.get(url, token);

        log.debug("API Response: " + response);

        return gson.fromJson(response, UserData.class);
    }

}
