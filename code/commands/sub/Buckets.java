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
import java.util.HashMap;

public class Buckets
{
	/* MARK FOR DELETION 9/23/22
	 * Moved to CreateBucket
	public static Bucket createForAccount(BasicCommands sphere, String ip_address, Bucket bucket, Logger logbook)
	{
		logbook.logWithSizedLogRotation("Creating bucket [" + bucket.name + "] for account [" + bucket.owner + "]...", 1);

		Account[] accounts = sphere.listAccounts(ip_address);
		HashMap<String, String> account_map = Accounts.mapNameToCanonicalID(accounts);

		// Make some edits to the bucket parameters
		// otherwise Vail won't accept them.
		
		Bucket forJson = new Bucket();
		Gson gson = new Gson();

		forJson.name = bucket.name;
			
		if(!bucket.lifecycle.equals("none"))
		{
			forJson.lifecycle = bucket.lifecycle;
		}
			
		if(bucket.acls != null)
		{
			forJson.acls = bucket.acls;
		}
			
		if(bucket.permissionType != null)
		{
			forJson.permissionType = bucket.permissionType;
		}
		else
		{
			forJson.permissionType = "NONE";
		}
		
		// Find Canonical ID for bucket owner.
		// The Sphere's Canonical ID isn't recognized for some reason.
		// Testing if adding blank will work here.

		if(bucket.owner.equals("Sphere") || bucket.owner.equals("sphere"))
		{
			logbook.logWithSizedLogRotation("Setting owner to \"\" for Sphere account...", 1);

			forJson.owner = "";
		}	
		else if(account_map.get(bucket.owner) != null)
		{
			logbook.logWithSizedLogRotation("Canonical ID for account [" + bucket.owner + "] found: " + account_map.get(bucket.owner), 1);

			forJson.owner = account_map.get(bucket.owner);
		}
		else
		{
			logbook.logWithSizedLogRotation("ERROR: Unable to find Canonical ID for account [" + bucket.owner + "]", 3);

			return null;
		}
		
		String json_body = gson.toJson(forJson, Bucket.class);

		System.err.println(json_body);

		return sphere.createBucket(ip_address, bucket.name, json_body);
	}
	*/

	public static Bucket createForAccount(BasicCommands sphere, String ip_address, String bucket_name, String account_name, Logger logbook)
	{
		Gson gson = new Gson();

		logbook.logWithSizedLogRotation("Creating bucket [" + bucket_name + "] for account [" + account_name + "]...", 1);

		String canonicalID = findCanonicalID(sphere, ip_address, account_name, logbook);

		Bucket bucket = new Bucket();
		bucket.name = bucket_name;
		bucket.owner = canonicalID;
		bucket.permissionType = "NONE";

		String json_body = gson.toJson(bucket, Bucket.class);

		return sphere.createBucket(ip_address, bucket_name, json_body);
	}

	public static String createForAccountWithKeys(BasicCommands sphere, String ip_address, String bucket_name, String account, Logger logbook)
	{
		// Deprecated:
		//  Canonical ID can be passed as owner in the json body to create the bucket.
		// Still necessary to create buckets for the Sphere though.

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
					bucket.account_name = Accounts.findNameWithCanonicalID(buckets[i].owner, accounts);
					bucket.account_id = accounts[account_index].id;
					bucket_list.add(bucket);
				}
			}
		}

		return bucket_list;
	}

	public static int findIndex(String name, Bucket[] buckets)
	{
		for(int i=0; i<buckets.length; i++)
		{
			if(buckets[i].name.equals(name))
			{
				return i;
			}
		}

		return -1;
	}

	public static String findCanonicalID(BasicCommands sphere, String ip_address, String account_name, Logger logbook)
	{
		// Find Canonical ID for bucket owner.
		// The Sphere's Canonical ID isn't recognized for some reason.
		// Testing if adding blank will work here.

		Account[] accounts = sphere.listAccounts(ip_address);
		HashMap<String, String> account_map = Accounts.mapNameToCanonicalID(accounts);

		// Quick check to convert the account_id to account_name to allow both to be provided.
		account_name = Accounts.findName(account_name, accounts);

		String canonical_id;

		if(account_name.equalsIgnoreCase("sphere"))
		{
			logbook.logWithSizedLogRotation("Setting owner to \"\" for Sphere account...", 1);

			canonical_id = "";
		}	
		else if(account_map.get(account_name) != null)
		{
			logbook.logWithSizedLogRotation("Canonical ID for account [" + account_name + "] found: " + account_map.get(account_name), 1);

			canonical_id = account_map.get(account_name);
		}
		else
		{
			logbook.logWithSizedLogRotation("ERROR: Unable to find Canonical ID for account [" + account_name + "]", 3);

			return null;
		}

		return canonical_id;
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

	public static Bucket updateOwner(BasicCommands sphere, String ip_address, String bucket_name, String account_name, Logger logbook)
	{
		Gson gson = new Gson();

		logbook.logWithSizedLogRotation("Updating bucket ownership for bucket [" + bucket_name + "] to account [" + account_name + "]...", 1);

		Bucket[] buckets = sphere.listBuckets(ip_address);

		int index = findIndex(bucket_name, buckets);

		if(index >=0)
		{
			logbook.logWithSizedLogRotation("Found bucket [" + bucket_name + "] at index (" + index + ")", 1);
			logbook.logWithSizedLogRotation("Bucket owner: " + buckets[index].owner, 1);
			String owner = findCanonicalID(sphere, ip_address, account_name, logbook);

			if(owner != null)
			{
				logbook.logWithSizedLogRotation("Found canonical id for account [" + account_name + "]: " + owner, 1);

				Bucket temp_bucket = buckets[index];
				temp_bucket.owner = owner;

				String json_body = gson.toJson(temp_bucket, Bucket.class);

				temp_bucket = sphere.updateBucket(ip_address, bucket_name, json_body);

				if(temp_bucket != null)
				{
					logbook.logWithSizedLogRotation("Bucket owner updated to " + temp_bucket.owner, 1);
				}
				else
				{
					logbook.logWithSizedLogRotation("ERROR: Unable to update bucket owner.", 3);
				}

				return temp_bucket;
			}
			else
			{
				logbook.logWithSizedLogRotation("ERROR: Unable to find the specified account [" + account_name + "].", 3);

				return null;
			}

		}
		else
		{
			logbook.logWithSizedLogRotation("ERROR: Unable to find bucket in list.", 3);

			return null;
		}

	}
}
