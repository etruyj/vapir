//===================================================================
// Accounts.java
//      Description:
//          API function calls associated with the account API
//===================================================================

package com.spectralogic.vail.vapir.api;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import com.spectralogic.vail.vapir.model.Account;
import com.spectralogic.vail.vapir.model.Message;
import com.spectralogic.vail.vapir.util.http.RestClient;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Accounts {
    private static final Logger log = LoggerFactory.getLogger(Accounts.class);

    public static Account addAccount(String ip_address, String token, String json_body, RestClient rest_client) throws IOException, JsonParseException {
        Gson gson = new Gson();

        String api_url = URLs.accountsURL(ip_address);

        String response = rest_client.post(api_url, token, json_body); 

        return gson.fromJson(response, Account.class);
    }

    public static Account addAccount(String ip_address, String token, Account account, RestClient rest_client) throws IOException, JsonParseException {
        Gson gson = new Gson();

        String api_url = URLs.accountsURL(ip_address);
        String payload = gson.toJson(account);

        String response = rest_client.post(api_url, token, payload); 

        return gson.fromJson(response, Account.class);
    }

    public static Account addAccount(String ip_address, String token, String account_name, String email, String external_id, String role_arn, RestClient rest_client) throws IOException, JsonParseException {
		Gson gson = new Gson();

		String url = URLs.accountsURL(ip_address);

		Account account = new Account();

		if(email != null && !email.equals("none"))
		{
			account.setEmail(email);
		}
		if(external_id != null && !external_id.equals("none"))
		{
			account.setExternalId(external_id);
		}

		account.setUsername(account_name);
		account.setRoleArn(role_arn);

		String payload = gson.toJson(account, Account.class);

		String response = rest_client.post(url, token, payload);

	    return gson.fromJson(response, Account.class);
    }

    public static Account[] list(String ip_address, String token, RestClient rest_client) throws IOException, JsonParseException {
        Gson gson = new Gson();

        String url = URLs.accountsURL(ip_address);

        log.debug("API URL: GET " + url);
        String response = rest_client.get(url, token);
        log.debug("API Response: " + response);
      
        try { 
            return gson.fromJson(response, Account[].class);
        } catch(JsonParseException e) {
            log.error(e.getMessage());
            String error = ServerMessage.process(response);
            log.error(error);
            // Return an empty array to allow the script to recover.
            return new Account[0];
        }
    }
}
