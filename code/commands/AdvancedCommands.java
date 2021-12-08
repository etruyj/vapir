package com.socialvagrancy.vail.commands;

import com.socialvagrancy.vail.commands.sub.PolicyTest;
import com.socialvagrancy.vail.commands.sub.SumarizeUsers;
import com.socialvagrancy.vail.structures.Account;
import com.socialvagrancy.vail.structures.User;
import com.socialvagrancy.vail.structures.UserKey;
import com.socialvagrancy.vail.structures.UserSummary;
import com.socialvagrancy.utils.Logger;

import java.util.ArrayList;

public class AdvancedCommands
{
	BasicCommands sphere;
	Logger logbook;

	public AdvancedCommands(BasicCommands api, Logger logs)
	{
		sphere = api;
		logbook = logs;
	}

	public void minimumIAMPermissions(String ip_address)
	{
		PolicyTest.findMinIAMPermissions(sphere, ip_address, "../lib/permissions/iam.txt", "../output/MinIAMPolicy.txt", logbook);
	}

	public ArrayList<UserSummary> filteredUserList(String ip_address, String filter_account, boolean is_active)
	{
		logbook.logWithSizedLogRotation("Creating summarized user name list...", 1);

		Account[] accounts = sphere.listAccounts(ip_address);
		User[] users = sphere.listUsers(ip_address);

		logbook.logWithSizedLogRotation("Adding account names", 1);
		ArrayList<UserSummary> summary = SumarizeUsers.filterByAccount(users, accounts, filter_account, logbook);
		logbook.logWithSizedLogRotation("Found (" + summary.size() + ") users", 1);


		if(is_active)
		{
			logbook.logWithSizedLogRotation("Filtering out inactive users", 1);
		}
		else
		{
			logbook.logWithSizedLogRotation("Finding user status", 1);
		}
	
		summary = SumarizeUsers.filterByActive(sphere, ip_address, summary, is_active);

		logbook.logWithSizedLogRotation("Returning (" + summary.size() + ") users", 2);

		return summary;	
	}
}
