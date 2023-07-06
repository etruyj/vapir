//===================================================================
// CreateGroup.java
// 	Description:
// 		Takes a group name and an account [id | name | canonical id]
// 		and issues the create group command.
//===================================================================

package com.socialvagrancy.vail.commands.sub;

import com.socialvagrancy.vail.commands.BasicCommands;
import com.socialvagrancy.vail.structures.Account;
import com.socialvagrancy.vail.structures.json.Group;
import com.socialvagrancy.vail.utils.account.GetAccountID;
import com.socialvagrancy.utils.io.Logger;

import java.util.ArrayList;

public class CreateGroup
{
	public static ArrayList<String> findAccountID(BasicCommands sphere, String ip, String name, String account, Logger logbook)
	{
		ArrayList<String> response = new ArrayList<String>();

		logbook.INFO("Creating group for account " + account);

		Account[] accounts = sphere.listAccounts(ip);
		String account_id = GetAccountID.findAccount(accounts, account);
	
		String message;

		if(!account_id.equals("none"))
		{
			message = withAccountID(sphere, ip, name, account_id, logbook);
		}
		else
		{
			message = "Invalid account name [" + account + "] specified.";
			logbook.ERR(message);
		}

		response.add(message);

		return response;
	}

	public static String withAccountID(BasicCommands sphere, String ip, String name, String account_id, Logger logbook)
	{
		String message; 

		if(sphere.createGroup(ip, name, account_id))
		{
			message = "Group [" + name + "] created successfully.";
		}
		else
		{
			message = "Unable to create group [" + name + "].";
		}

		return message;
	}
}
