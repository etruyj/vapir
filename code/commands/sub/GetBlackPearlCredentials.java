//===================================================================
// GetBlackPearlCredentials.java
// 	Description:
// 		Gets the access key and secret key for the BlackPearl
//===================================================================

package com.socialvagrancy.vail.commands.sub;

import com.socialvagrancy.vail.commands.BasicCommands;
import com.socialvagrancy.vail.structures.Endpoint;
import com.socialvagrancy.vail.structures.blackpearl.BPUser;
import com.socialvagrancy.vail.structures.blackpearl.Ds3KeyPair;
import com.socialvagrancy.utils.Logger;
import java.io.Console;

public class GetBlackPearlCredentials
{
	public static Endpoint addDs3Keys(BasicCommands sphere, Endpoint endpoint, Logger logbook)
	{
		logbook.INFO("Retrieving DS3 access key and secret key...");

		Console console = System.console();
		int login_attempts = 3;	
		boolean login_successful = false;

		String user = "";
		String token = "";

		System.out.println("Logging into BlackPearl at " + endpoint.managementAddress() + "...");
		
		// Log in attempts.
		while(!login_successful && login_attempts > 0)
		{
			System.out.print("username: ");
			user = console.readLine();

			System.out.print("password: ");
			char[] password = console.readPassword();
			
			token = sphere.blackpearlLogin(endpoint.managementAddress(), user, password);
		
			if(token == null)
			{
				logbook.WARN("Failed to connect to BlackPearl.");
				login_attempts--;
			}
			else
			{
				token = "Bearer " + token;
				logbook.INFO("Connection successful.");
				login_successful = true;
			}
		}

		if(login_successful)
		{
			BPUser users = sphere.blackpearlUserList(endpoint.managementAddress(), token);
		
			if(users != null)
			{
				int id = findID(users, user);
				endpoint = findKeys(sphere, endpoint, id, token);
				return endpoint;
			}
		}

		return null;
	}

	//======================================
	// Private Functions
	//======================================

	private static int findID(BPUser users, String user)
	{
		boolean searching = true;
		int counter = 0;
		int user_id = -1;

		while(searching && counter < users.count())
		{
			if(users.username(counter).equals(user))
			{
				user_id = users.id(counter);
				searching = false;
			}

			counter++;
		}

		return user_id;
	}

	private static Endpoint findKeys(BasicCommands sphere, Endpoint endpoint, int id, String token)
	{
		Ds3KeyPair keys = sphere.blackpearlUserKeys(endpoint.managementAddress(), String.valueOf(id), token);

		if(keys != null)
		{
			endpoint.setAccessKey(keys.accessKey(0));
			endpoint.setSecretKey(keys.secretKey(0));

			return endpoint;
		}
		else
		{
			return null;
		}
	}
}
