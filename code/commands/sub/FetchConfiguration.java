//===================================================================
// FetchConfiguration.java
// 	Description:
// 		Queries the Vail Sphere for configuration specific
// 		information and outputs it to the shell in json format.
//===================================================================

package com.socialvagrancy.vail.commands.sub;

import com.socialvagrancy.vail.commands.BasicCommands;
import com.socialvagrancy.vail.structures.SphereConfig;
import com.socialvagrancy.vail.utils.map.MapAccounts;
import com.socialvagrancy.vail.utils.map.MapLifecycle;
import com.socialvagrancy.vail.utils.map.MapStorage;
import com.socialvagrancy.utils.Logger;

import java.util.HashMap;

public class FetchConfiguration
{
	public static SphereConfig fromSphere(BasicCommands sphere, String ip_address, Logger logbook)
	{
		logbook.INFO("Fetching Sphere configuration status...");

		SphereConfig config = new SphereConfig();
		config.accounts = sphere.listAccounts(ip_address);
		config.buckets = sphere.listBuckets(ip_address);
		config.lifecycles = sphere.listLifecycles(ip_address);
		config.storage = sphere.listStorage(ip_address);
		config.groups = ListGroups.inSphere(sphere, ip_address, "none", logbook); // none = all accounts.

		logbook.INFO("Cleaning JSON output...");

		HashMap<String, String> canon_account_map = MapAccounts.createCanonicalIDNameMap(config.accounts);
		HashMap<String, String> lifecycle_id_map = MapLifecycle.createIDNameMap(config.lifecycles);
		HashMap<String, String> storage_id_map = MapStorage.createIDNameMap(config.storage);

		config = cleanBuckets(config, canon_account_map, lifecycle_id_map, logbook);
		config = cleanLifecycles(config, storage_id_map, logbook);
		config = cleanStorage(config);
		config = cleanAccounts(config);
		config = cleanGroups(config);

		return config;
	}

	//=======================================
	// Private Functions
	//=======================================

	private static SphereConfig cleanAccounts(SphereConfig config)
	{
		for(int i=0; i<config.accountCount(); i++)
		{
			config.clearAccountID(i);
			config.clearAccountCanonicalID(i);
		}

		return config;
	}

	private static SphereConfig cleanBuckets(SphereConfig config, HashMap<String, String> canon_id_map, HashMap<String, String> id_name_map, Logger logbook)
	{
		//===============================
		// Clean the variable by deleting
		// unnecessary values to streamline
		// the output.
		//===============================

		String lifecycle;
		String account;

		for(int i=0; i < config.bucketCount(); i++)
		{
			if(id_name_map.get(config.bucketLifecycle(i)) == null)
			{
				logbook.WARN("Unable to find lifecycle with id: " + config.bucketLifecycle(i));
				System.err.println("WARN: Unable to find lifecycle with id: " + config.bucketLifecycle(i));
				lifecycle = "[UNKNOWN]";
			}
			else
			{
				lifecycle = id_name_map.get(config.bucketLifecycle(i));
			}

			if(canon_id_map.get(config.bucketOwner(i)) == null)
			{
				logbook.WARN("Unable to find account with id: " + config.bucketOwner(i));
				System.err.println("WARN: Unable to find account with id: " + config.bucketOwner(i));
				account = "[UNKNOWN]";
			}
			else
			{
				account = canon_id_map.get(config.bucketOwner(i));
			}

			config.setBucketOwner(i, account);
			config.setBucketLifecycle(i, lifecycle);
			config.clearBucketCreated(i);

			// Clear ACL info if ACLs are set to none.
			// ACLs Disabled or BucketOwnerEnforced

			if(config.buckets[i].control.equals("BucketOwnerEnforced"))
			{
				config.clearBucketAcls(i);
			}
		
		}

		return config;	
	}

	public static SphereConfig cleanGroups(SphereConfig config)
	{
		for(int i=0; i < config.groupCount(); i++)
		{
			config.clearGroupAccountID(i);
			config.clearGroupType(i);
		}

		return config;
	}

	public static SphereConfig cleanLifecycles(SphereConfig config, HashMap<String, String> storage_id_map, Logger logbook)
	{
		String storage_name;

		for(int i=0; i < config.lifecycleCount(); i++)
		{
			config.clearLifecycleId(i);
			config.clearLifecycleModified(i);

			for(int j=0; j < config.lifecycleRuleCount(i); j++)
			{
				config.clearLifecycleRuleDestinationCount(i, j);

				for(int k = 0; k < config.lifecycleRuleStorageCount(i, j); k++)
				{
					if(storage_id_map.get(config.lifecycleRuleStorageId(i, j, k)) == null)
					{
						logbook.WARN("Unable to find storage location with id " + config.lifecycleRuleStorageId(i, j, k));
						System.err.println("WARN: Unable to find storage location with id " + config.lifecycleRuleStorageId(i, j, k));
						storage_name = "[UNKNOWN]";
					}
					else
					{
						storage_name = storage_id_map.get(config.lifecycleRuleStorageId(i, j, k));
					}

					config.setLifecycleRuleStorageId(i, j, k, storage_name);
				}
			}

		}

		return config;
	}

	public static SphereConfig cleanStorage(SphereConfig config)
	{
		for(int i=0; i < config.storageCount(); i++)
		{
			config.clearStorageID(i);
			config.clearStorageStatus(i);
		}
		
		return config;
	}
}
