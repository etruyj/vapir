//===================================================================
// Accounts.java
// 	Description: This class holds all the command surround account
// 	actions.
//===================================================================

package com.socialvagrancy.vail.commands.sub;

import com.socialvagrancy.vail.structures.Account;
import com.socialvagrancy.vail.structures.Summary;
import com.socialvagrancy.vail.structures.User;
import com.socialvagrancy.utils.Logger;

import java.util.ArrayList;
import java.util.HashMap;

public class Accounts
{

	public static ArrayList<Summary> attachNames(ArrayList<Summary> users, Account[] accounts)
	{
		String last_id = "0";
		String last_name = "not specified"; 

		for(int i=0; i<users.size(); i++)
		{
			// Quick check to save processing time
			if(users.get(i).account_id.equals(last_id))
			{
				users.get(i).account_name = last_name;
			}
			else
			{
				// If it's a different account, search the list for the name
				last_id = users.get(i).account_id;
				last_name = findName(users.get(i).account_id, accounts);
				users.get(i).account_name = last_name;
			}
		}

		return users;
	}

	public static ArrayList<Summary> attachNames(User[] users, Account[] accounts)
	{
		ArrayList<Summary> summary = new ArrayList<Summary>();
		Summary line;
		String last_id = "0";
		String last_name = "not specified"; 

		for(int i=0; i<users.length; i++)
		{
			line = new Summary();

			line.type = "user";
			line.name = users[i].username;
			line.account_id = users[i].accountid;

			// Quick check to save processing time
			if(users[i].accountid.equals(last_id))
			{
				line.account_name = last_name;
			}
			else
			{
				// If it's a different account, search the list for the name
				last_id = users[i].accountid;
				last_name = findName(users[i].accountid, accounts);
				line.account_name = last_name;
			}

			summary.add(line);
		}

		return summary;
	}

	public static int findIndex(String account, Account[] accounts)
	{
		Long test;
		int index = -1;
		int itr = 0;
		boolean searching = true;

		try
		{
			// Check to see if account number or name was supplied.
			// String should fail and drop to the catch statement.
			test = Long.valueOf(account);

			do
			{
				if(accounts[itr].id.equals(account))
				{
					index = itr;
				}
				itr++;
				
				if(index > 0 || itr >= accounts.length)
				{
					// Error handling in case an invalid account
					// filter was selected.
					searching = false;
				}
			} while (searching);
		}
		catch(Exception e)
		{
			if(account.equalsIgnoreCase("Sphere"))
			{
				// If the filter is for the sphere account 
				// this is designated by roleArn being blank.
				do
				{
					if(accounts[itr].roleArn.equals(""))
					{
						index = itr;
					}
					itr++;
				
					if(index > 0 || itr >= accounts.length)
					{
						// Error handling in case an invalid account
						// filter was selected.
						searching = false;
					}
				} while (searching);
			}
			else
			{
				// Otherwise we need to search for 
				do
				{
					if(accounts[itr].username.equalsIgnoreCase(account))
					{
						index = itr;
					}
					itr++;
				
					if(index > 0 || itr >= accounts.length)
					{
						// Error handling in case an invalid account
						// filter was selected.
						searching = false;
					}
				} while (searching);
			}

		}

		return index;
	}


	public static String findName(String account_id, Account[] accounts)
	{
		String account_name = "not found";

		try
		{
			long test = Long.valueOf(account_id);

			for(int i=0; i<accounts.length; i++)
			{
				if(accounts[i].id.equals(account_id))
				{
					if(accounts[i].roleArn.equals(""))
					{
						return "Sphere";
					}
					else
					{
						return accounts[i].username;
					}
				}
			}

			return account_name;
		}
		catch(Exception e)
		{
			// Assume an account name was passed instead of an id
			return account_id;
		}
	}

	public static String findNameWithCanonicalID(String canonicalId, Account[] accounts)
	{
		for(int i=0; i<accounts.length; i++)
		{
			if(accounts[i].canonicalId.equals(canonicalId))
			{
				if(accounts[i].roleArn.equals(""))
				{
					return "Sphere";
				}
				else
				{
					return accounts[i].username;
				}
			}
		}

		return "not found";
	}

	public static HashMap<String, String> mapNameToCanonicalID(Account[] accounts)
	{
		HashMap<String, String> map = new HashMap<String, String>();

		for(int i=0; i<accounts.length; i++)
		{
			map.put(accounts[i].username, accounts[i].canonicalId);
		}

		return map;
	}
}
