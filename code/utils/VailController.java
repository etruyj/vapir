//===================================================================
//	VailController.java
//		Description:
//			This is the controller for the Vail Sphere
//			commands defined in the commands/ directory.
//			This class abstracts the individual command
//			layer and performs necessary translations
//			between the interface layer and the command
//			layer.
//===================================================================

package com.socialvagrancy.vail.utils;

import com.socialvagrancy.vail.commands.AdvancedCommands;
import com.socialvagrancy.vail.commands.BasicCommands;
import com.socialvagrancy.vail.structures.Account;
import com.socialvagrancy.vail.structures.Bucket;
import com.socialvagrancy.vail.structures.OutputFormat;
import com.socialvagrancy.vail.structures.Summary;
import com.socialvagrancy.vail.structures.User;
import com.socialvagrancy.vail.structures.UserKey;
import com.socialvagrancy.utils.Logger;

import java.util.ArrayList;

public class VailController
{
	private BasicCommands sphere;
	private AdvancedCommands advanced;
	private String token;
	private Logger logbook;

	public VailController()
	{
		logbook = new Logger("../logs/vail_api.log", 102400, 1, 1);
		sphere = new BasicCommands(logbook);
		advanced = new AdvancedCommands(sphere, logbook);
	}

	public String clearCache(String ip_address)
	{
		return sphere.clearCache(ip_address);
	}

	public ArrayList<String> configureSphere(String ip_address, String file_path)
	{
		return advanced.configureSphere(ip_address, file_path);
	}

	public String createBucket(String ip_address, String bucket_name, String account)
	{
		if(!account.equals("none"))
		{
			if(advanced.createBucketForAccount(ip_address, bucket_name, account) != null)
			{
				return "Successfully created bucket [" + bucket_name + "] for account [" + account + "].";
			}	
			else
			{
				return "Unable to create bucket [" + bucket_name + "] for account [" + account + "].";
			}
		}

		return null;
	}

	public void findMinIAMPermissions(String ip_address)
	{
		advanced.minimumIAMPermissions(ip_address);
	}

	public Account[] listAccounts(String ip_address)
	{
		return sphere.listAccounts(ip_address);
	}

	public Bucket[] listBuckets(String ip_address)
	{
		return sphere.listBuckets(ip_address);
	}
	
	public ArrayList<Summary> listBucketSummary(String ip_address, String account)
	{
		return advanced.filteredBucketList(ip_address, account);
	}

	public User[] listUsers(String ip_address)
	{
		return sphere.listUsers(ip_address);
	}

	public ArrayList<Summary> listUserSummary(String ip_address, String account, boolean active_only)
	{
		return advanced.filteredUserList(ip_address, account, active_only);
	}

	public OutputFormat[] listKeys(String ip_address, String account, String user)
	{
		sphere.listUserKeys(ip_address, account, user);
		return null;
	}

	public boolean login(String ip_address, String username, String password)
	{
		return sphere.login(ip_address, username, password);
	}

	public String updateOwner(String ip_address, String bucket, String account_name)
	{
		return advanced.updateBucketOwner(ip_address, bucket, account_name);
	}
}
