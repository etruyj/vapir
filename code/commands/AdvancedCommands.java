package com.socialvagrancy.vail.commands;

import com.socialvagrancy.vail.commands.sub.Buckets;
import com.socialvagrancy.vail.commands.sub.ConfigureSphere;
import com.socialvagrancy.vail.commands.sub.PolicyTest;
import com.socialvagrancy.vail.commands.sub.Users;
import com.socialvagrancy.vail.structures.Account;
import com.socialvagrancy.vail.structures.Bucket;
import com.socialvagrancy.vail.structures.Summary;
import com.socialvagrancy.vail.structures.User;
import com.socialvagrancy.vail.structures.UserKey;
import com.socialvagrancy.utils.Logger;

import java.util.ArrayList;

//*******************************************************************
// COMMENTED CODE REMOVED ON JAN 17
// DELETE IF NO EFFECT FOUND
//*******************************************************************

public class AdvancedCommands
{
	BasicCommands sphere;
	Logger logbook;

	public AdvancedCommands(BasicCommands api, Logger logs)
	{
		sphere = api;
		logbook = logs;
	}

	public Bucket createBucketForAccount(String ip_address, String bucket_name, String account)
	{
		return Buckets.createForAccount(sphere, ip_address, bucket_name, account, logbook);
	}

	public ArrayList<String> configureSphere(String ip_address, String file_path)
	{
		return ConfigureSphere.start(sphere, ip_address, file_path, logbook);
	}

	public void minimumIAMPermissions(String ip_address)
	{
		PolicyTest.findMinIAMPermissions(sphere, ip_address, "../lib/permissions/iam.txt", "../output/MinIAMPolicy.txt", logbook);
	}

	public ArrayList<Summary> filteredBucketList(String ip_address, String filter_account)
	{
		logbook.logWithSizedLogRotation("Summarizing buckets...", 1);

		Account[] accounts = sphere.listAccounts(ip_address);
		Bucket[] buckets = sphere.listBuckets(ip_address);

		ArrayList<Summary> bucket_list = Buckets.filterByAccount(buckets, accounts, filter_account);

		logbook.logWithSizedLogRotation("Found (" + bucket_list.size() + ")", 2);

		return bucket_list;
	}

	public ArrayList<Summary> filteredUserList(String ip_address, String filter_account, boolean is_active)
	{
		return Users.generateFilteredList(sphere, ip_address, filter_account, is_active, logbook);
	/*
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
	*/
	}

	public String updateBucketOwner(String ip_address, String bucket_name, String account_name)
	{
		Buckets.updateOwner(sphere, ip_address, bucket_name, account_name, logbook);
	
		return "yes";
	}
}
