//===================================================================
// ListUsers.java
// 	Description:
// 		Provides an ArrayList of users associated with the
// 		Vail Sphere.
// 		This is now an advanced function as the API changed
// 		to require an account with the list users command.
//===================================================================

package com.socialvagrancy.vail.commands.sub;

import com.socialvagrancy.vail.commands.BasicCommands;
import com.socialvagrancy.vail.structures.Account;
import com.socialvagrancy.vail.structures.Summary;
import com.socialvagrancy.vail.structures.UserKey;
import com.socialvagrancy.vail.structures.json.Group;
import com.socialvagrancy.vail.structures.json.UserData;
import com.socialvagrancy.vail.utils.map.MapAccounts;
import com.socialvagrancy.utils.Logger;

import java.util.ArrayList;
import java.util.HashMap;

public class ListUsers
{
	public static ArrayList<Summary> inSphere(BasicCommands sphere, String ip, String account, boolean active_only, Logger logbook)
	{
		logbook.INFO("Fetching list of users...");

		Account[] accounts = sphere.listAccounts(ip);
		ArrayList<Summary> user_list;

		if(account.equalsIgnoreCase("sphere"))
		{
			boolean searching = true;
			int itr = 0;

			// Find the account id
			while(searching)
			{
				if(accounts[itr].roleArn == "")
				{
					account = accounts[itr].id;
					searching = false;
				}

				itr++;

				if(itr >= accounts.length)
				{
					searching = false;
				}
			}
		}

		if(account.equals("none") || account.equals("all"))
		{
			logbook.INFO("Calling listAllUsers(sphere, " + ip + ", Account[])...");
			user_list = listAllUsers(sphere, ip, accounts);
			logbook.INFO("Found (" + user_list.size() + ") users in the sphere.");
		}
		else
		{
			logbook.INFO("Calling listAccountUsers(sphere, " + ip + ", Account[], " + account + ")...");
			user_list = listAccountUsers(sphere, ip, accounts, account, logbook);
			logbook.INFO("Found (" + user_list.size() + ") users in the sphere.");
		}

		user_list = attachGroups(sphere, ip, user_list);
		user_list = determineStatus(sphere, ip, user_list);

		if(active_only)
		{
			logbook.INFO("Filtering out inactive users");

			user_list = filterInactive(user_list);
		}

		return user_list;
	}

	//=======================================
	// Private Functions
	//=======================================

	public static ArrayList<Summary> attachGroups(BasicCommands sphere, String ip, ArrayList<Summary> user_list)
	{
		for(int i=0; i < user_list.size(); i++)
		{
			Group[] groups = sphere.listUserGroups(ip, user_list.get(i).account_id, user_list.get(i).name);
		
			for(int j=0; j < groups.length; j++)
			{
				user_list.get(i).addGroup(groups[j].name());
			}
		}

		return user_list;
	}

	public static Summary buildUserSummary(String username, String account_id, String account_name)
	{
		Summary user_sum = new Summary();

		user_sum.type = "user";
		user_sum.name = username;
		user_sum.account_id = account_id;
		user_sum.account_name = account_name;

		return user_sum;
		
	}

	private static ArrayList<Summary> determineStatus(BasicCommands sphere, String ip, ArrayList<Summary> user_list)
	{
		boolean active_key = false;

		for(int i=0; i < user_list.size(); i++)
		{
			UserKey[] keys = sphere.listUserKeys(ip, user_list.get(i).account_id, user_list.get(i).name);

			if(keys != null)
			{
				for(int j=0; j < keys.length; j++)
				{
					if(keys[j].initialized)
					{
						active_key = true;
					}
				}
			}
			
			if(active_key)
			{
				user_list.get(i).status = "active";
			}
			else
			{
				user_list.get(i).status = "in-active";
			}
		}
		
		return user_list;
	}

	private static ArrayList<Summary> filterInactive(ArrayList<Summary> user_list)
	{
		for(int i = user_list.size() - 1; i >=0; i--)
		{
			if(user_list.get(i).status.equals("inactive"))
			{
				user_list.remove(i);
			}
		}

		return user_list;
	}

	private static ArrayList<Summary> listAccountUsers(BasicCommands sphere, String ip, Account[] accounts, String account, Logger logbook)
	{
		ArrayList<Summary> user_list = new ArrayList<Summary>();
		UserData users;
		boolean searching_account = true;

		HashMap<String, String> account_id_map = MapAccounts.createNameIDMap(accounts);
		HashMap<String, Account> account_map = MapAccounts.createIDAccountMap(accounts);
		HashMap<String, String> account_canon_map = MapAccounts.createCanonicalIDMap(accounts);

		// Filter out account == sphere which is valid input
		if(account.equalsIgnoreCase("sphere"))
		{
			account = "";
		}	

		if(searching_account && account_id_map.get(account) == null)
		{
			logbook.INFO("Account name [" + account + "] was not found in account list");
		}
		else if(searching_account)
		{
			account = account_id_map.get(account);
			searching_account = false;
		}

		if(searching_account && account_canon_map.get(account) == null)
		{
			logbook.INFO("Account canonical ID [" + account + "] was not found in account list"); 
		}
		else if(searching_account)
		{
			account = account_canon_map.get(account);
			searching_account = false;
		}

		if(searching_account && account_map.get(account) == null)
		{
			logbook.ERR("Invalid account [" + account + "] specified in parameters.");
			System.err.println("ERROR: Invalid account [" + account + "] specified in parameters.");
			return user_list; // an empty list.
		}
	
		// Else
		// 	The account name specified exists in one of the maps.
		//	We can find the users.

		users = sphere.listUsers(ip, account);

		for(int i=0; i < users.count(); i++)
		{
			user_list.add(buildUserSummary(users.username(i), users.accountID(i), account_map.get(account).username));
		}

		return user_list;
	}

	private static ArrayList<Summary> listAllUsers(BasicCommands sphere, String ip, Account[] accounts)
	{
		ArrayList<Summary> user_list = new ArrayList<Summary>();
		UserData users;

		for(int i=0; i < accounts.length; i++)
		{
			users = sphere.listUsers(ip, accounts[i].id);

			for(int j=0; j < users.count(); j++)
			{
				user_list.add(buildUserSummary(users.username(j), users.accountID(j), accounts[i].username));
			}
		}

		return user_list;
	}

}
