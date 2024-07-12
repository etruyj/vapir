//===================================================================
// GetBlackPearlCredentials.java
// 	Description:
// 		Gets the access key and secret key for the BlackPearl
//===================================================================

package com.spectralogic.vail.vapir.command;

import com.spectralogic.vail.vapir.api.VailConnector;
import com.spectralogic.vail.vapir.model.Endpoint;
import com.spectralogic.vail.vapir.model.blackpearl.BPUser;
import com.spectralogic.vail.vapir.model.blackpearl.Ds3KeyPair;

import java.io.Console;

public class GetBlackPearlCredentials
{
	public static Endpoint addDs3KeysWithManualLogin(Endpoint endpoint, VailConnector sphere)
	{
        try {
            Console console = System.console();
		    int login_attempts = 3;	
		    boolean login_successful = false;

		    String user = "";
		    String token = "";

		    System.out.println("Logging into BlackPearl at " + endpoint.getManagementEndpoint() + "...");
		
		    // Log in attempts.
		    while(!login_successful && login_attempts > 0)
		    {
		    	System.out.print("username: ");
			    user = console.readLine();

			    System.out.print("password: ");
			    char[] password = console.readPassword();
			
			    token = sphere.blackpearlLogin(endpoint.getManagementEndpoint(), user, password);
		
			    if(token == null)
			    {
				    System.out.println("Failed to connect to BlackPearl.");
				    login_attempts--;
			    }
			    else
			    {
				    token = "Bearer " + token;
				    System.out.println("Connection successful.");
				    login_successful = true;
			    }
		    }

		    if(login_successful)
		    {
			    BPUser users = sphere.blackpearlUserList(endpoint.getManagementEndpoint(), token);
		
			    if(users != null)
			    {
				    int id = findID(users, user);
				    endpoint = findKeys(sphere, endpoint, id, token);
				    return endpoint;
			    }
		    }
        } catch(Exception e) {
            System.err.println(e.getMessage());
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

		while(searching && counter < users.getUserCount())
		{
			if(users.getUserUsername(counter).equals(user))
			{
				user_id = users.getUserId(counter);
				searching = false;
			}

			counter++;
		}

		return user_id;
	}

	private static Endpoint findKeys(VailConnector sphere, Endpoint endpoint, int id, String token) throws Exception
	{
		Ds3KeyPair keys = sphere.blackpearlUserKeys(endpoint.getManagementEndpoint(), String.valueOf(id), token);

		if(keys != null)
		{
			endpoint.setAccessKey(keys.getAccessKey(0));
			endpoint.setSecretKey(keys.getSecretKey(0));

			return endpoint;
		}
		else
		{
			return null;
		}
	}
}

