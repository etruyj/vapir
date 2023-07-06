//===================================================================
// CreateUser.java
// 	Description:
// 		Takes username and account id, name, or canonical id
// 		and sends the username and account id to the sphere
// 		to create the user.
//===================================================================

package com.socialvagrancy.vail.commands.sub;

import com.socialvagrancy.vail.commands.BasicCommands;
import com.socialvagrancy.vail.structures.Account;
import com.socialvagrancy.vail.structures.User;
import com.socialvagrancy.vail.utils.account.GetAccountID;
import com.socialvagrancy.utils.io.Logger;

public class CreateUser
{
	public static String createUser(BasicCommands sphere, String ip_address, String account, String username, Logger logbook)
	{
		Account[] accounts = sphere.listAccounts(ip_address);

		logbook.INFO("Finding account id for specified account " + account);

		String account_id = GetAccountID.findAccount(accounts, account);

		String message;

		if(account_id == null || account_id.equals("none"))
		{
			logbook.ERR("Unabled to find account [" + account + "].");
			message = ("Unabled to find account [" + account + "]. Please specify a valid account.");
		}
		else
		{
			if(sphere.createUser(ip_address, account_id, username) == null)
			{
				message = "Unable to create user " + username;
			}
			else
			{
				message = "User " + username + " created successfully.";
			}
		}

		return message;
	}
}
