//===================================================================
// ListGroups.java
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
import com.socialvagrancy.vail.structures.json.GroupData;
import com.socialvagrancy.vail.structures.json.UserData;
import com.socialvagrancy.vail.utils.map.MapAccounts;
import com.socialvagrancy.utils.Logger;

import java.util.ArrayList;
import java.util.HashMap;

public class ListGroups
{
	public static ArrayList<Summary> inSphere(BasicCommands sphere, String ip, String account, Logger logbook)
	{
		logbook.INFO("Fetching list of groups...");

		Account[] accounts = sphere.listAccounts(ip);
		ArrayList<Summary> group_list;
		
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
			logbook.INFO("Calling listAllGroups(sphere, " + ip + ", Account[])...");
			group_list = listAllGroups(sphere, ip, accounts);
			logbook.INFO("Found (" + group_list.size() + ") groups in the sphere.");
		}
		else
		{
			logbook.INFO("Calling listAccountGroups(sphere, " + ip + ", Account[], " + account + ")...");
			group_list = listAccountGroups(sphere, ip, accounts, account, logbook);
			logbook.INFO("Found (" + group_list.size() + ") groups in the sphere.");
		}

		return group_list;
	}

	//=======================================
	// Private Functions
	//=======================================

	public static Summary buildGroupSummary(String name, String account_id, String account_name)
	{
		Summary sum = new Summary();

		sum.type = "user";
		sum.name = name;
		sum.account_id = account_id;
		sum.account_name = account_name;

		return sum;
		
	}

	private static ArrayList<Summary> listAccountGroups(BasicCommands sphere, String ip, Account[] accounts, String account, Logger logbook)
	{
		ArrayList<Summary> group_list = new ArrayList<Summary>();
		GroupData groups;
		boolean searching = true;

		HashMap<String, String> account_id_map = MapAccounts.createNameIDMap(accounts);
		HashMap<String, Account> account_map = MapAccounts.createIDAccountMap(accounts);
		HashMap<String, String> account_canon_map = MapAccounts.createCanonicalIDMap(accounts);

		// Filter out account == sphere which is valid input
		if(account.equalsIgnoreCase("sphere"))
		{
			account = "";
		}	

		if(searching && account_id_map.get(account) == null)
		{
			logbook.INFO("Account name [" + account + "] was not found in account list");
		}
		else if(searching)
		{
			account = account_id_map.get(account);
			searching = false;
		}

		if(searching && account_canon_map.get(account) == null)
		{
			logbook.INFO("Account canonical ID [" + account + "] was not found in account list"); 
		}
		else if(searching)
		{
			account = account_canon_map.get(account);
			searching = false;
		}

		if(searching && account_map.get(account) == null)
		{
			logbook.ERR("Invalid account [" + account + "] specified in parameters.");
			System.err.println("ERROR: Invalid account [" + account + "] specified in parameters.");
			return group_list; // an empty list.
		}
	
		// Else
		// 	The account name specified exists in one of the maps.
		//	We can find the users.

		groups = sphere.listGroups(ip, account);

		for(int i=0; i < groups.count(); i++)
		{
			group_list.add(buildGroupSummary(groups.name(i), groups.accountID(i), account_map.get(account).username));
		}

		return group_list;
	}

	private static ArrayList<Summary> listAllGroups(BasicCommands sphere, String ip, Account[] accounts)
	{
		ArrayList<Summary> group_list = new ArrayList<Summary>();
		GroupData groups;

		for(int i=0; i < accounts.length; i++)
		{
			groups = sphere.listGroups(ip, accounts[i].id);

			for(int j=0; j < groups.count(); j++)
			{
				group_list.add(buildGroupSummary(groups.name(j), groups.accountID(j), accounts[i].username));
			}
		}

		return group_list;
	}

}
