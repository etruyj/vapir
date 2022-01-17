//===================================================================
// Bucket.java
//	Description: Commands associated with Vail buckets, such as
//		formatting the create bucket json.
//==================================================================

package com.socialvagrancy.vail.commands.sub;

import com.socialvagrancy.vail.commands.BasicCommands;
import com.socialvagrancy.vail.structures.Account;
import com.socialvagrancy.vail.structures.ACL;
import com.socialvagrancy.vail.structures.Bucket;
import com.socialvagrancy.vail.structures.Summary;
import com.socialvagrancy.vail.structures.UserKey;
import com.socialvagrancy.utils.Logger;

import com.google.gson.Gson;

import java.util.ArrayList;

public class Buckets
{
	public static String createForAccount(BasicCommands sphere, String ip_address, String bucket_name, String account, Logger logbook)
	{
		logbook.logWithSizedLogRotation("Creating bucket (" + bucket_name + ") for account..." + account, 1);

		ArrayList<Summary> account_users = Users.generateFilteredList(sphere, ip_address, account, false, logbook);

		Summary user = Users.findUserWithSingleKey(sphere, ip_address, account_users);

		String result;

		if(user != null)
		{
			logbook.logWithSizedLogRotation("Found user [" + user.name + "] with available access_key slot.", 2);

			// A user key has to be created. 
			// The account with the user that has the most-recently created key will own the bucket.
			// Selecting the bucket we want to own is as simple (or complex) as creating a key for a 
			// user on that account, creating the bucket, then deleting the key.
			UserKey key = sphere.createUserKey(ip_address, user.account_id, user.name);

			if(key != null && !key.id.equals("none"))
			{
				logbook.logWithSizedLogRotation("Created access key: " + key.id, 2);

				String body = Buckets.formatBucketJson(bucket_name, "none", null, null);
				
				Bucket bucket = sphere.createBucket(ip_address, bucket_name, body);

				if(bucket != null)
				{
					logbook.logWithSizedLogRotation("Created bucket (" + bucket.name + ") for account " + bucket.owner, 2);
					result = "Created bucket (" + bucket.name + ") for account " + bucket.owner;
				}
				else
				{
					logbook.logWithSizedLogRotation("ERROR: Unable to create bucket.", 3);
					result = "ERROR: Unable to create bucket.";
				}
			
				// Clean-up. Delete the key I created so there is no evidence.
				sphere.deleteUserKey(ip_address, user.account_id, user.name, key.id);
			}
			else
			{
				logbook.logWithSizedLogRotation("Unable to create access key for user [" + user + "]", 3);

				result = "ERROR: Unable to create access key for user [" + user + "]";
			}
		}
		else
		{
			logbook.logWithSizedLogRotation("ERROR: Unable to created bucket.", 3);
			logbook.logWithSizedLogRotation("All users for account " + account + " have two access keys. Cannot create a third.", 3);
	
			result = "ERROR: Unable to create bucket. All users for account " + account + " have two access keys. Cannot create a third."; 
		}
		
		return result;

	}	

	public static ArrayList<Summary> filterByAccount(Bucket[] buckets, Account[] accounts, String filter_account)
	{
		ArrayList<Summary> bucket_list = new ArrayList<Summary>();
		Summary bucket;

		String last_account_name = "none selected";
		String last_account_id = "0";
		String last_canonical_id = "0";
		int account_index = -1;

		for(int i=0; i<buckets.length; i++)
		{
			if(filter_account.equals("none") || filter_account.equals("all"))
			{
				if(buckets[i].owner.equals(last_canonical_id))
				{
					bucket = new Summary();
					bucket.type = "bucket";
					bucket.name = buckets[i].name;
					bucket.account_name = last_account_name;
					bucket.account_id = last_account_id;
					bucket_list.add(bucket);
				}
				else
				{
					bucket = new Summary();
					bucket.type = "bucket";
					last_canonical_id = buckets[i].owner;
					last_account_name = Accounts.findNameWithCanonicalID(last_canonical_id, accounts);
					account_index = Accounts.findIndex(last_account_name, accounts);
					last_account_id = accounts[account_index].id;
					bucket.name = buckets[i].name;
					bucket.account_name = last_account_name;
					bucket.account_id = last_account_id;
					bucket_list.add(bucket);
				}
			}
			else
			{
				account_index = Accounts.findIndex(filter_account, accounts);
				if(buckets[i].owner.equals(accounts[account_index].canonicalId))
				{
					bucket = new Summary();
					bucket.type = "bucket";
					bucket.name = buckets[i].name;
					bucket.account_name = accounts[account_index].username;
					bucket.account_id = accounts[account_index].id;
					bucket_list.add(bucket);
				}
			}
		}

		return bucket_list;
	}

	public static String formatBucketJson(String name, String permission_type, String lifecycle, ACL[] acl)
	{
		Bucket bucket = new Bucket();
		bucket.name = name;
		bucket.permissionType = permission_type;
		bucket.lifecycle = lifecycle;
		bucket.acls = acl;

		Gson gson = new Gson();

		return gson.toJson(bucket, Bucket.class);
	}

	
}
