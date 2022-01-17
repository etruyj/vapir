package com.socialvagrancy.vail.commands;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.socialvagrancy.vail.structures.Account;
import com.socialvagrancy.vail.structures.Bucket;
import com.socialvagrancy.vail.structures.Token;
import com.socialvagrancy.vail.structures.User;
import com.socialvagrancy.vail.structures.UserKey;
import com.socialvagrancy.vail.utils.Connector;

import com.socialvagrancy.utils.Logger;

public class BasicCommands
{
	private String token;
	private Logger logbook;

	public BasicCommands(Logger logs)
	{
		logbook = logs;
	}

	//===========================================================
	// Commands
	//===========================================================

	public String createAccount(String ipaddress, String account_name, String email, String external_id, String role_arn)
	{
		Gson gson = new Gson();
		Connector conn = new Connector();

		String url = URLs.accountsURL(ipaddress);

		logbook.logWithSizedLogRotation("Creating account [" + account_name + "] with role ARN: " + role_arn, 1);

		Account account = new Account();
		
		if(!email.equals("none"))
		{
			account.email = email;
		}
		if(!external_id.equals("none"))
		{
			account.externalId = external_id;	
		}

		account.username = account_name;
		account.roleArn = role_arn;

		String body = gson.toJson(account, Account.class);

		logbook.logWithSizedLogRotation("POST: " + url, 2);
		logbook.logWithSizedLogRotation("BODY: " + body, 2);

		String response = conn.POST(url, token, body);

		logbook.logWithSizedLogRotation(response, 2);

		return response;
	}

	public String clearCache(String ipaddress)
	{
		String url = URLs.clearCacheURL(ipaddress);

		logbook.logWithSizedLogRotation("Clearing cached user permissions...", 1);
		logbook.logWithSizedLogRotation("POST " + url, 2);

		Connector conn = new Connector();

		String response = conn.POST(url, token, "");
	
		if(response.length() > 0)
		{
			logbook.logWithSizedLogRotation(response, 3);
			return response;
		}
		else
		{
			logbook.logWithSizedLogRotation("Cache cleared successfully.", 2);
			return "Cache cleared successfully.";
		}
	}

	public Bucket createBucket(String ipaddress, String name, String json_body)
	{
		Gson gson = new Gson();
		
		String url = URLs.bucketsURL(ipaddress);

		logbook.logWithSizedLogRotation("Creating new bucket (" + name + ") ...", 1);
		logbook.logWithSizedLogRotation("POST " + url, 2);

		Connector conn = new Connector();
		
		String response = conn.POST(url, token, json_body);

		try
		{
			Bucket bucket = gson.fromJson(response, Bucket.class);

			logbook.logWithSizedLogRotation("Successfully created " + bucket.name, 2);

			return bucket;
		}
		catch(JsonParseException e)
		{
			logbook.logWithSizedLogRotation("ERROR: " + e.getMessage(), 3);
			logbook.logWithSizedLogRotation(response, 3);

			return null;
		}
	}

	public UserKey createUserKey(String ipaddress, String account, String user)
	{
		Gson gson = new Gson();
		
		String url = URLs.keysURL(ipaddress, account, user);

		logbook.logWithSizedLogRotation("Creating new S3 Access key for " + account + "/" + user + "...", 1);
		logbook.logWithSizedLogRotation("POST " + url, 2);

		Connector conn = new Connector();
		
		String response = conn.POST(url, token, "");

		try
		{
			UserKey key = gson.fromJson(response, UserKey.class);

			logbook.logWithSizedLogRotation("Created key: " + key.id, 2);

			return key;
		}
		catch(JsonParseException e)
		{
			logbook.logWithSizedLogRotation("ERROR: " + e.getMessage(), 3);
		
			return null;
		}
	}

	public boolean deleteUserKey(String ipaddress, String account, String user, String access_key)
	{
		Gson gson = new Gson();
		
		String url = URLs.keysDeleteURL(ipaddress, account, user, access_key);

		logbook.logWithSizedLogRotation("Deleting S3 Access key (" + access_key + ") for " + account + "/" + user + "...", 1);
		logbook.logWithSizedLogRotation("DELETE " + url, 2);

		Connector conn = new Connector();
		
		String response = conn.DELETE(url, token);

		System.out.println(response);

		try
		{
			if(Integer.valueOf(response)>=200 && Integer.valueOf(response)<=300)
			{
				logbook.logWithSizedLogRotation("Key deleted successfully with code " + response, 2);
				return true;
			}
			else
			{
				logbook.logWithSizedLogRotation("Keys deletion failed with code " + response, 3);
				return false;
			}

		}
		catch(JsonParseException e)
		{
			logbook.logWithSizedLogRotation("ERROR: " + e.getMessage(), 3);
		
			return false;
		}
	}

	public Account[] listAccounts(String ipaddress)
	{
		Gson gson = new Gson();
		
		String url = URLs.accountsURL(ipaddress);

		logbook.logWithSizedLogRotation("Querying Sphere for list of accounts...", 1);
		logbook.logWithSizedLogRotation("GET " + url, 2);

		Connector conn = new Connector();
		
		String response = conn.GET(url, token);

		try
		{
			Account[] accounts = gson.fromJson(response, Account[].class);

			logbook.logWithSizedLogRotation("Found (" + accounts.length + ") accounts", 2);

			return accounts;
		}
		catch(JsonParseException e)
		{
			logbook.logWithSizedLogRotation("ERROR: " + e.getMessage(), 3);
		
			return null;
		}
	}

	public Bucket[] listBuckets(String ipaddress)
	{
		Gson gson = new Gson();

		String url = URLs.bucketsURL(ipaddress);
	
		logbook.logWithSizedLogRotation("Querying Sphere for a full list of buckets...", 1);
		logbook.logWithSizedLogRotation("GET " + url, 2);

		Connector conn = new Connector();

		String response = conn.GET(url, token);

		try
		{
			Bucket[] buckets = gson.fromJson(response, Bucket[].class);
			logbook.logWithSizedLogRotation("Found (" + buckets.length + ")", 2);
			return buckets;
		}
		catch(JsonParseException e)
		{
			logbook.logWithSizedLogRotation("ERROR: " + e.getMessage(), 3);
			return null;
		}
	}

	public User[] listUsers(String ipaddress)
	{
		Gson gson = new Gson();
		
		String url = URLs.usersURL(ipaddress);

		logbook.logWithSizedLogRotation("Querying Sphere for a full list of users...", 1);
		logbook.logWithSizedLogRotation("GET " + url, 2);

		Connector conn = new Connector();
		
		String response = conn.GET(url, token);

		try
		{
			User[] users = gson.fromJson(response, User[].class);

			logbook.logWithSizedLogRotation("Found (" + users.length + ") users", 2);

			return users;
		}
		catch(JsonParseException e)
		{
			logbook.logWithSizedLogRotation("ERROR: " + e.getMessage(), 3);
		
			return null;
		}
	}

	public UserKey[] listUserKeys(String ipaddress, String account, String user)
	{
		Gson gson = new Gson();
		
		String url = URLs.keysURL(ipaddress, account, user);

		logbook.logWithSizedLogRotation("Querying Sphere for access keys associated with " + account + "/" + user + "...", 1);
		logbook.logWithSizedLogRotation("GET " + url, 2);

		Connector conn = new Connector();
		
		String response = conn.GET(url, token);

		try
		{
			UserKey[] keys = gson.fromJson(response, UserKey[].class);

			logbook.logWithSizedLogRotation("Found (" + keys.length + ") keys", 2);

			return keys;
		}
		catch(JsonParseException e)
		{
			logbook.logWithSizedLogRotation("ERROR: " + e.getMessage(), 3);
		
			return null;
		}
	}

	public boolean login(String ipaddress, String username, String password)
	{
		Gson gson = new Gson();

		String url = URLs.loginURL(ipaddress);

		logbook.logWithSizedLogRotation("Logging in...", 1);
		logbook.logWithSizedLogRotation("POST " + url, 2);
		logbook.logWithSizedLogRotation("Username: " + username, 2);

		String auth = "{ \"username\":\"" 
			+ username + "\", \"password\":\"" + password
			+ "\" }";

		Connector conn = new Connector();

		String response = conn.POST(url, auth);
		
		try
		{
			Token tok = gson.fromJson(response, Token.class);
		
			token = "Bearer " + tok.getToken();

			logbook.logWithSizedLogRotation("Login SUCCESSFUL", 2);

			return true;
		}
		catch(JsonParseException e)
		{
			logbook.logWithSizedLogRotation("ERROR: " + e.getMessage(), 3);
			logbook.logWithSizedLogRotation("Login FAILED", 2);

			return false;
		}
	}
}
