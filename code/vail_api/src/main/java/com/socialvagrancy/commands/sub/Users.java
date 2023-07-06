//===================================================================
// Users.java
// 	Description: A collection of functions that provide information
// 	on Vail users.
//===================================================================

package com.socialvagrancy.vail.commands.sub;

import com.socialvagrancy.vail.commands.BasicCommands;
import com.socialvagrancy.vail.structures.Account;
import com.socialvagrancy.vail.structures.Summary;
import com.socialvagrancy.vail.structures.User;
import com.socialvagrancy.vail.structures.UserKey;
import com.socialvagrancy.vail.structures.json.UserData;
import com.socialvagrancy.utils.io.Logger;

import java.util.ArrayList;

public class Users
{
	public static ArrayList<Summary> generateFilteredList(BasicCommands sphere, String ip_address, String filter_account, boolean is_active, Logger logbook)
	{
		logbook.logWithSizedLogRotation("Creating summarized user name list...", 1);

		Account[] accounts = sphere.listAccounts(ip_address);
		UserData users = sphere.listUsers(ip_address, filter_account);
        ArrayList<Summary> summary = generateSummaryList(users);

		logbook.logWithSizedLogRotation("Found (" + summary.size() + ") users", 1);


		if(is_active)
		{
			logbook.logWithSizedLogRotation("Filtering out inactive users", 1);
		}
		else
		{
			logbook.logWithSizedLogRotation("Finding user status", 1);
		}
	
		summary = filterByActive(sphere, ip_address, summary, is_active);

		logbook.logWithSizedLogRotation("Returning (" + summary.size() + ") users", 2);

		return summary;	
	}

	public static Summary findUserWithSingleKey(BasicCommands sphere, String ip_address, ArrayList<Summary> user_list)
	{
		UserKey[] keys;

		for(int i=0; i<user_list.size(); i++)
		{
			keys = sphere.listUserKeys(ip_address, user_list.get(i).account_id, user_list.get(i).name);

			if(keys.length<2)
			{
				return user_list.get(i);
			}
		}

		return null;
	}


	//=======================================
	// Private Functions
	//=======================================

	private static String checkActive(BasicCommands sphere, String ip_address, String account_id, String username)
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

/* Depricated
 *      The 2.0 version of the Vail API requires the account number to be specified when making this call.
 *      remove in vail_api-2.1.0 update.
	private static ArrayList<Summary> filterByAccount(User[] users, Account[] accounts, String filter_account, Logger logbook)
	{
		ArrayList<Summary> summary;
		int account_index = -2;

		if(filter_account.equalsIgnoreCase("none") || filter_account.equalsIgnoreCase("all"))
		{
			logbook.logWithSizedLogRotation("Not filtering users by account name. (" + filter_account + ")", 1);
		}
		else
		{
			logbook.logWithSizedLogRotation("Searching for index for account (" + filter_account + ")", 1);

			account_index = Accounts.findIndex(filter_account, accounts);

			if(account_index<0)
			{
				logbook.logWithSizedLogRotation("Invalid account specified. (" + account_index + ")", 2);
			}
			else
			{
				logbook.logWithSizedLogRotation("Account (" + filter_account + ") found at index [" + account_index + "]", 2);
			}
		}

		if(account_index>=0)
		{
			logbook.logWithSizedLogRotation("Filtering user lists", 1);
			logbook.logWithSizedLogRotation("CALL: filterUsers(" + accounts[account_index].id + ")", 1); 
			summary = filterList(users, accounts[account_index].id);
			
			logbook.logWithSizedLogRotation("Getting account names", 1);
			summary = Accounts.attachNames(summary, accounts);
		}
		else
		{
			logbook.logWithSizedLogRotation("Getting account names", 1);
			summary = Accounts.attachNames(users, accounts);
		}

		return summary;
	}
*/

	private static ArrayList<Summary> filterByActive(BasicCommands sphere, String ip_address, ArrayList<Summary> summary, boolean filter_active)
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


	private static ArrayList<Summary> filterList(User[] users, String account_id)
	{
		// NEED A BETTER NAME FOR THIS ONE
		// Filters the user list by account name. 
		// Used by the filterByAccountName function to break out the code.
		// 
		// Takes the User[] array and converts it into an ArrayList<UserSummary>
		// This new variable includes username, account name, and account id.


		ArrayList<Summary> summary = new ArrayList<Summary>();
		Summary line;

		for(int i=0; i<users.length; i++)
		{
			if(users[i].accountid.equals(account_id))
			{
				line = new Summary();
				line.type = "user";		
				line.name = users[i].username;
				line.account_id = users[i].accountid;

				summary.add(line);
			}
		}
		
		return summary;
	}

    public static ArrayList<Summary> generateSummaryList(UserData data) {
        ArrayList<Summary> user_list = new ArrayList<Summary>();
        Summary user;

        for(int i=0; i<data.count(); i++) {
            user = new Summary();
        
            user.name = data.username(i);
            user.account_id = data.accountID(i);

            user_list.add(user);
        }

        return user_list;
    }
}
