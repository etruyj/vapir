//*******************************************************************
// DEFUNCT: FUNCTIONALITY MOVED TO COMMANDS/SUB/USERS.JAVA ON JAN 17
// DELETE IF NO ISSUES FOUND
//*******************************************************************

package com.socialvagrancy.vail.commands.sub;

import com.socialvagrancy.vail.commands.BasicCommands;
import com.socialvagrancy.vail.structures.Account;
import com.socialvagrancy.vail.structures.User;
import com.socialvagrancy.vail.structures.UserKey;
import com.socialvagrancy.vail.structures.UserSummary;
import com.socialvagrancy.utils.io.Logger;
import java.util.ArrayList;

public class SumarizeUsers
{
	public static ArrayList<UserSummary> filterByAccount(User[] users, Account[] accounts, String filter_account, Logger logbook)
	{
		ArrayList<UserSummary> summary;
		int account_index = -2;

		if(filter_account.equalsIgnoreCase("none") || filter_account.equalsIgnoreCase("all"))
		{
			logbook.logWithSizedLogRotation("Not filtering users by account name. (" + filter_account + ")", 1);
		}
		else
		{
			logbook.logWithSizedLogRotation("Searching for index for account (" + filter_account + ")", 1);

			account_index = findAccountIndex(filter_account, accounts);

			if(account_index<0)
			{
				logbook.logWithSizedLogRotation("Invalid account specified. (" + account_index + ")", 2);
			}
			else
			{
				logbook.logWithSizedLogRotation("Account (" + filter_account + ") found at index [" + account_index + "]", 2);
			}
		}

		if(account_index>0)
		{
			logbook.logWithSizedLogRotation("Filtering user lists", 1);
			summary = filterList(users, accounts[account_index].username, accounts[account_index].id);
		}
		else
		{
			logbook.logWithSizedLogRotation("Getting account names", 1);
			summary = getAccountNames(users, accounts);
		}

		return summary;
	}

	public static ArrayList<UserSummary> filterByActive(BasicCommands sphere, String ip_address, ArrayList<UserSummary> summary, boolean filter_active)
	{
		int itr = 0;
		String status;

		while(itr<summary.size())
		{
			status = checkActive(sphere, ip_address, summary.get(itr).account_id, summary.get(itr).name);

			if(filter_active)
			{
				if(status.equals("active"))
				{
					summary.get(itr).status = status;
					itr++;
				}
				else
				{
					summary.remove(itr);
				}
			}
			else
			{
				summary.get(itr).status = status;
				itr++;
			}

		}

		return summary;
	}


	//==============================================
	// Private Functions
	//==============================================
	
	public static String checkActive(BasicCommands sphere, String ip_address, String account_id, String username)
	{
		UserKey[] keys = sphere.listUserKeys(ip_address, account_id, username);

		for(int i=0; i<keys.length; i++)
		{
			if(keys[i].initialized)
			{
				return "active";
			}
		}
		
		return "inactive";
	}

	public static ArrayList<UserSummary> filterList(User[] users, String account_name, String account_id)
	{
		ArrayList<UserSummary> summary = new ArrayList<UserSummary>();
		UserSummary line;

		for(int i=0; i<users.length; i++)
		{
			if(users[i].accountid.equals(account_id))
			{
				line = new UserSummary();
				
				line.name = users[i].username;
				line.account_id = users[i].accountid;
				line.account_name = account_name;

				summary.add(line);
			}
		}

		return summary;
	}

	public static int findAccountIndex(String account, Account[] accounts)
	{
		int test;
		int index = -1;
		int itr = 0;
		boolean searching = true;

		try
		{
			// Check to see if account number or name was supplied.
			// String should fail and drop to the catch statement.
			test = Integer.valueOf(account);

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
	
	public static String findAccountName(String account_id, Account[] accounts)
	{
		String account_name = "not found";

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

	public static ArrayList<UserSummary> getAccountNames(User[] users, Account[] accounts)
	{
		ArrayList<UserSummary> summary = new ArrayList<UserSummary>();
		UserSummary line;
		String last_id = "0";
		String last_name = "not specified"; 

		for(int i=0; i<users.length; i++)
		{
			line = new UserSummary();

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
				last_name = findAccountName(users[i].accountid, accounts);
				line.account_name = last_name;
			}

			summary.add(line);
		}

		return summary;
	}
}
