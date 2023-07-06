package com.socialvagrancy.vail.commands;

import com.socialvagrancy.vail.commands.sub.Buckets;
import com.socialvagrancy.vail.commands.sub.ConfigureSphere;
import com.socialvagrancy.vail.commands.sub.CreateGroup;
import com.socialvagrancy.vail.commands.sub.CreateUser;
import com.socialvagrancy.vail.commands.sub.FetchConfiguration;
import com.socialvagrancy.vail.commands.sub.ListGroups;
import com.socialvagrancy.vail.commands.sub.ListUsers;
import com.socialvagrancy.vail.commands.sub.PolicyTest;
import com.socialvagrancy.vail.commands.sub.ListBuckets;
import com.socialvagrancy.vail.commands.sub.Users;
import com.socialvagrancy.vail.structures.Account;
import com.socialvagrancy.vail.structures.Bucket;
import com.socialvagrancy.vail.structures.BucketSummary;
import com.socialvagrancy.vail.structures.Lifecycle;
import com.socialvagrancy.vail.structures.SphereConfig;
import com.socialvagrancy.vail.structures.Summary;
import com.socialvagrancy.vail.structures.User;
import com.socialvagrancy.vail.structures.UserKey;
import com.socialvagrancy.vail.structures.UserSummary;
import com.socialvagrancy.utils.io.Logger;

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

	public ArrayList<String> createUser(String ipaddress, String account, String username)
	{
		ArrayList<String> response = new ArrayList<String>();
		response.add(CreateUser.createUser(sphere, ipaddress, account, username, logbook));

		return response;
	}

	public ArrayList<String> createGroup(String ip_address, String group_name, String account)
	{
		return CreateGroup.findAccountID(sphere, ip_address, group_name, account, logbook);
	}

	public ArrayList<String> configureSphere(String ip_address, String file_path)
	{
		return ConfigureSphere.start(sphere, ip_address, file_path, logbook);
	}

	public SphereConfig fetchConfiguration(String ip_address)
	{
		return FetchConfiguration.fromSphere(sphere, ip_address, logbook);
	}

	public void minimumIAMPermissions(String ip_address)
	{
		PolicyTest.findMinIAMPermissions(sphere, ip_address, "../lib/permissions/iam.txt", "../output/MinIAMPolicy.txt", logbook);
	}

	public ArrayList<BucketSummary> filteredBucketList(String ip_address, String filter_account)
	{
		logbook.logWithSizedLogRotation("Summarizing buckets...", 1);

		Account[] accounts = sphere.listAccounts(ip_address);
		Bucket[] buckets = sphere.listBuckets(ip_address);
		Lifecycle[] lifecycles = sphere.listLifecycles(ip_address);

		ArrayList<BucketSummary> bucket_list = ListBuckets.filterByAccount(buckets, accounts, lifecycles, filter_account);

		return bucket_list;
	}

	public ArrayList<Summary> listGroups(String ip, String account)
	{
		return ListGroups.inSphere(sphere, ip, account, logbook);
	}

	public ArrayList<UserSummary> listUsers(String ip, String account, boolean active_only)
	{
		return ListUsers.inSphere(sphere, ip, account, active_only, logbook);
	}

	public String updateBucketOwner(String ip_address, String bucket_name, String account_name)
	{
		Buckets.updateOwner(sphere, ip_address, bucket_name, account_name, logbook);
	
		return "yes";
	}
}
