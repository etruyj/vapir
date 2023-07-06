package com.socialvagrancy.vail.commands;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.socialvagrancy.vail.structures.Account;
import com.socialvagrancy.vail.structures.Bucket;
import com.socialvagrancy.vail.structures.Endpoint;
import com.socialvagrancy.vail.structures.Lifecycle;
import com.socialvagrancy.vail.structures.Message;
import com.socialvagrancy.vail.structures.Storage;
import com.socialvagrancy.vail.structures.Token;
import com.socialvagrancy.vail.structures.json.Group;
import com.socialvagrancy.vail.structures.json.GroupData;
import com.socialvagrancy.vail.structures.json.UserData;
import com.socialvagrancy.vail.structures.User;
import com.socialvagrancy.vail.structures.UserKey;
import com.socialvagrancy.vail.structures.blackpearl.BPUser;
import com.socialvagrancy.vail.structures.blackpearl.Ds3KeyPair;
import com.socialvagrancy.vail.utils.Connector;
import com.socialvagrancy.utils.io.Logger;

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
	
	public Account addAccount(String ipaddress, String account_name, String role_arn, String json_body)
	{
		Gson gson = new Gson();
		Connector conn = new Connector();

		String url = URLs.accountsURL(ipaddress);

		logbook.logWithSizedLogRotation("Creating account [" + account_name + "] with role ARN: " + role_arn, 1);

		logbook.logWithSizedLogRotation("POST: " + url, 2);

		String response = conn.POST(url, token, json_body);

		try
		{
			Account account = gson.fromJson(response, Account.class);
		
			return account;
		}
		catch(Exception e)
		{
			logbook.logWithSizedLogRotation(e.getMessage(), 2);
			logbook.logWithSizedLogRotation("BODY: " + json_body, 1);
			logbook.logWithSizedLogRotation(response, 3);
			logbook.logWithSizedLogRotation("FAILED: addAccount(" + account_name + ")", 3);
			
			return null;
		}
	}

	public Account addAccount(String ipaddress, String account_name, String email, String external_id, String role_arn)
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

		try
		{
			account = gson.fromJson(response, Account.class);
		
			return account;
		}
		catch(Exception e)
		{
			logbook.logWithSizedLogRotation(e.getMessage(), 3);
			logbook.logWithSizedLogRotation("BODY: " + body, 1);
			logbook.logWithSizedLogRotation(response, 3);
			logbook.logWithSizedLogRotation("FAILED: addAccount(" + account_name + ")", 3);

			return null;
		}
	}

	public Storage addStorage(String ipaddress, String name, String body)
	{
		Gson gson = new Gson();

		String url = URLs.storageURL(ipaddress);

		logbook.logWithSizedLogRotation("Adding storage location [" + name + "]...", 1);
		logbook.logWithSizedLogRotation("POST " + url, 2);

		Connector conn = new Connector();

		String response = conn.POST(url, token, body);
	
		try
		{
			Storage location = gson.fromJson(response, Storage.class);

			logbook.logWithSizedLogRotation("Storage created successfully.", 2);

			return location;
		}
		catch(Exception e)
		{
			logbook.logWithSizedLogRotation(e.getMessage(), 2);
			logbook.logWithSizedLogRotation("BODY: " + body, 1);
			logbook.logWithSizedLogRotation(response, 3);
			logbook.logWithSizedLogRotation("Unable to create storage location.", 2);

			return null;
		}
	
	}

	public String blackpearlLogin(String ipaddress, String username, char[] password)
	{
		Gson gson = new Gson();

		String url = URLs.blackpearlLoginURL(ipaddress);

		logbook.INFO("Connecting to BlackPearl endpoint [" + ipaddress +"]...");
		logbook.INFO("POST " + url);

		String body = "{ \"username\":\"" + username +"\", \"password\": \"";
		
		for(int i=0; i < password.length; i++)
		{
			body += password[i];
		}
	
		body += "\" }";

		Connector conn = new Connector();

		String response = conn.POST(url, body);

		if(response != null || response.length() > 0)
		{
			try
			{
				Token token = gson.fromJson(response, Token.class);
				return token.getToken();
			}
			catch(JsonParseException e)
			{
				logbook.WARN(e.getMessage());
				logbook.WARN("Server response: " + response);
				return null;
			}
		}
		else
		{
			logbook.ERR("Unabled to connect to the BlackPearl.");
			return null;
		}
	}

	public BPUser blackpearlUserList(String ipaddress, String token)
	{
		Gson gson = new Gson();
		String url = URLs.blackpearlUsersListURL(ipaddress);
		
		logbook.INFO("Gathering users from BlackPearl...");
		logbook.INFO("GET " + url);

		Connector conn = new Connector();

		String response = conn.GET(url, token);

		try
		{
			BPUser users = gson.fromJson(response, BPUser.class);
			logbook.INFO("Found (" + users.count() + ") users.");

			return users;
		}
		catch(JsonParseException e)
		{
			logbook.WARN(e.getMessage());
			return null;
		}
	}

	public Ds3KeyPair blackpearlUserKeys(String ipaddress, String id, String token)
	{
		Gson gson = new Gson();
		String url = URLs.blackpearlUserKeyURL(ipaddress, id);

		logbook.INFO("Gathering DS3 keys from BlackPearl...");
		logbook.INFO("GET " + url);

		Connector conn = new Connector();

		String response = conn.GET(url, token);

		try
		{
			Ds3KeyPair keys = gson.fromJson(response, Ds3KeyPair.class);
			
			logbook.INFO("Found DS3 credentials.");
			return keys;
		}
		catch(JsonParseException e)
		{
			logbook.WARN(e.getMessage());
			return null;
		}
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
			logbook.ERR(e.getMessage());

			logbook.ERR("BODY: " + json_body);
			logbook.ERR(response);

			return null;
		}
	}

	public boolean createGroup(String ipaddress, String name, String account_id)
	{
		// This call is different in that it returns an empty set for a successful
		// creation call and an error message for a failure.

		String url = URLs.createGroupURL(ipaddress, account_id, name);

		logbook.INFO("Creating new group [" + name + "] for account " + account_id + "...");
		logbook.INFO("PUT " + url);

		Connector conn = new Connector();

		String response = conn.PUT(url, token, "");

		if(response.length() == 0)
		{
			logbook.INFO("Group [" + name + "] created successfully.");
			return true;
		}
		else
		{
			logbook.ERR(response);
			return false;
		}
	}

	public Lifecycle createLifecycle(String ipaddress, String name, String json_body)
	{
		Gson gson = new Gson();
		
		String url = URLs.lifecycleURL(ipaddress);

		logbook.logWithSizedLogRotation("Creating new lifecycle rule: " + name + "...", 1);
		logbook.logWithSizedLogRotation("POST " + url, 2);

		Connector conn = new Connector();
		
		String response = conn.POST(url, token, json_body);

		try
		{
			Lifecycle rule = gson.fromJson(response, Lifecycle.class);

			logbook.logWithSizedLogRotation("Created lifecycle: " + rule.name, 2);

			return rule;
		}
		catch(JsonParseException e)
		{
			logbook.logWithSizedLogRotation("ERROR: " + e.getMessage(), 3);
			logbook.logWithSizedLogRotation("BODY: " + json_body, 1);
			logbook.logWithSizedLogRotation(response, 3);
		
			return null;
		}

	}

	public User createUser(String ipaddress, String account_id, String username)
	{
		Gson gson = new Gson();

		String url = URLs.userCreateURL(ipaddress, account_id, username);
		
		logbook.INFO("Creating user [" + username + "] for account " + account_id);
		logbook.INFO("PUT " + url);

		Connector conn = new Connector();

		String response = conn.PUT(url, token, "");

		try
		{
			User user =  gson.fromJson(response, User.class);

			logbook.INFO("User [" + username + "] creation successful.");

			return user;
		}
		catch(JsonParseException e)
		{
			logbook.ERR(e.getMessage());
			logbook.ERR("Failed to create user [" + username + "].");
			logbook.ERR(response);

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
			logbook.logWithSizedLogRotation(response, 3);

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
	
		logbook.INFO("Querying Sphere for a full list of buckets...");
		logbook.INFO("GET " + url);

		Connector conn = new Connector();

		String response = conn.GET(url, token);

		try
		{
			Bucket[] buckets = gson.fromJson(response, Bucket[].class);
			logbook.INFO("Found (" + buckets.length + ") buckets");
			return buckets;
		}
		catch(JsonParseException e)
		{
			logbook.ERR(e.getMessage());
			return null;
		}
	}

	public Endpoint[] listEndpoints(String ipaddress)
	{
		// Return a list endpoints associated with the sphere.
		Gson gson = new Gson();

		String url = URLs.endpointsURL(ipaddress);

		logbook.INFO("Querying Sphere for associated endpoints...");
		logbook.INFO("GET " + url);

		Connector conn = new Connector();

		String response = conn.GET(url, token);

		try
		{
			Endpoint[] endpoints = gson.fromJson(response, Endpoint[].class);

			logbook.INFO("Found (" + endpoints.length + ") endpoints");
			return endpoints;
		}
		catch(JsonParseException e)
		{
			logbook.ERR(e.getMessage());
			return null;
		}
	}

	public GroupData listGroups(String ipaddress, String account)
	{
		// The format of the groups JSON is identical to the
		// Users JSON, so this command will use the same
		// variable type.
		
		Gson gson = new Gson();

		String url = URLs.groupsURL(ipaddress, account);

		logbook.INFO("Querying Sphere for groups associated with account " + account + "...");
		logbook.INFO("GET " + url);

		Connector conn = new Connector();

		String response = conn.GET(url, token);

		if(response.length() == 0)
		{
			logbook.WARN("No results returned from query. May be running a pre-version 2.0.0 sphere.");
			return null;
		}
		else
		{
			try
			{
				GroupData groups = gson.fromJson(response, GroupData.class);

				logbook.INFO("Found (" + groups.count() + ") groups.");

				return groups;
			}
			catch(JsonParseException e)
			{
				logbook.ERR(e.getMessage());

				return null;
			}
		}

	}

	public Lifecycle[] listLifecycles(String ipaddress)
	{
		Gson gson = new Gson();
		
		String url = URLs.lifecycleURL(ipaddress);

		logbook.logWithSizedLogRotation("Querying Sphere for lifecycle rules...", 1);
		logbook.logWithSizedLogRotation("GET " + url, 2);

		Connector conn = new Connector();
		
		String response = conn.GET(url, token);

		try
		{
			Lifecycle[] lifecycles = gson.fromJson(response, Lifecycle[].class);

			logbook.logWithSizedLogRotation("Found (" + lifecycles.length + ") lifecycles", 2);

			return lifecycles;
		}
		catch(JsonParseException e)
		{
			logbook.logWithSizedLogRotation("ERROR: " + e.getMessage(), 3);
		
			return null;
		}
	}

	public Storage[] listStorage(String ipaddress)
	{
		Gson gson = new Gson();
		
		String url = URLs.storageURL(ipaddress);

		logbook.logWithSizedLogRotation("Querying Sphere for storage locations...", 1);
		logbook.logWithSizedLogRotation("GET " + url, 2);

		Connector conn = new Connector();
		
		String response = conn.GET(url, token);

		try
		{
			Storage[] locations = gson.fromJson(response, Storage[].class);

			logbook.logWithSizedLogRotation("Found (" + locations.length + ") storage locations", 2);

			return locations;
		}
		catch(JsonParseException e)
		{
			logbook.logWithSizedLogRotation("ERROR: " + e.getMessage(), 3);
		
			return null;
		}
	}

	public Group[] listUserGroups(String ipaddress, String account_id, String username)
	{
		Gson gson = new Gson();

		String url = URLs.userGroupsURL(ipaddress, account_id, username);

		logbook.INFO("Querying Sphere for a list of groups user [" + username + "] belongs to....");
		logbook.INFO("GET " + url);

		Connector conn = new Connector();

		String response = conn.GET(url, token);

		try
		{
			Group[] groups = gson.fromJson(response, Group[].class);

			logbook.INFO("Found (" + groups.length + ") groups");

			return groups;
		}
		catch(JsonParseException e)
		{
			logbook.ERR(e.getMessage());
			return null;
		}
	}

	public UserData listUsers(String ipaddress, String account_id)
	{
		Gson gson = new Gson();

		String url = URLs.usersURL(ipaddress, account_id);

		logbook.logWithSizedLogRotation("Querying Sphere for a list of users associated with account [" + account_id + "]...", 1);
		logbook.logWithSizedLogRotation("GET " + url, 2);

		Connector conn = new Connector();
		
		String response = conn.GET(url, token);

		try
		{
			UserData users = gson.fromJson(response, UserData.class);

			logbook.logWithSizedLogRotation("Found (" + users.count() + ") users", 2);

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
	
			if(tok == null)
			{
				logbook.ERR("Login FAILED");
				return false;
			}

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

	public Bucket updateBucket(String ipaddress, String name, String json_body)
	{
		Gson gson = new Gson();
		
		String url = URLs.bucketsURL(ipaddress);

		logbook.logWithSizedLogRotation("Updating bucket (" + name + ") ...", 1);
		logbook.logWithSizedLogRotation("PUT " + url, 2);

		Connector conn = new Connector();
		
		String response = conn.PUT(url, token, json_body);

		try
		{
			Bucket bucket = gson.fromJson(response, Bucket.class);

			logbook.logWithSizedLogRotation("Successfully updated " + bucket.name, 2);

			return bucket;
		}
		catch(JsonParseException e)
		{
			logbook.logWithSizedLogRotation("ERROR: " + e.getMessage(), 3);
			logbook.logWithSizedLogRotation("BODY: " + json_body, 1);
			logbook.logWithSizedLogRotation(response, 3);

			return null;
		}
	}
}
