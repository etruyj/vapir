//===================================================================
// ListBuckets.java
// 	Description:
//		Provides a summarized list of buckets in the account.
//===================================================================

package com.socialvagrancy.vail.commands.sub;

import com.socialvagrancy.vail.structures.Account;
import com.socialvagrancy.vail.structures.Bucket;
import com.socialvagrancy.vail.structures.BucketSummary;
import com.socialvagrancy.vail.structures.Lifecycle;
import com.socialvagrancy.vail.utils.map.MapAccounts;
import com.socialvagrancy.vail.utils.map.MapLifecycle;

import java.util.ArrayList;
import java.util.HashMap;

public class ListBuckets
{
	public static ArrayList<BucketSummary> filterByAccount(Bucket[] buckets, Account[] accounts, Lifecycle[] lifecycles, String filter_account)
	{
		ArrayList<BucketSummary> bucket_list = new ArrayList<BucketSummary>();
		BucketSummary bucket;

		HashMap<String, String> canon_id_map = MapAccounts.createCanonicalIDMap(accounts);
		HashMap<String, String> canon_name_map = MapAccounts.createCanonicalIDNameMap(accounts);
		HashMap<String, String> name_canon_map = MapAccounts.createNameCanonicalIDMap(accounts);
		HashMap<String, String> id_canon_map = MapAccounts.createIDCanonicalIDMap(accounts);
		HashMap<String, String> lifecycle_id_map = MapLifecycle.createIDNameMap(lifecycles);

		String filter_id;
		
		// Determine which account we're searching for.
		
		if(filter_account.equals("none") || filter_account.equals("all"))
		{
			filter_id = "all";
		}
		else
		{
			if(id_canon_map.get(filter_account) != null)
			{
				filter_id = id_canon_map.get(filter_account);
			}
			else if(name_canon_map.get(filter_account) != null)
			{
				filter_id = name_canon_map.get(filter_account);
			}
			else if(canon_name_map.get(filter_account) != null)
			{
				filter_id = filter_account;
			}
			else
			{
				System.err.println("ERROR: Unable to locate account " + filter_account);
				filter_id = "none";
			}
		}

		// Build Summary
		// only add to list if filter_id = all or filter_id = bucket owner
		// if filter_id = none, invalid account in search parameters.

		if(buckets != null && !filter_id.equals("none"))
		{
			for(int i=0; i < buckets.length; i++)
			{
				if(filter_id.equals("all") || buckets[i].owner.equals(filter_id))
				{
					bucket = new BucketSummary();
					
					bucket.type = "bucket";
					bucket.name = buckets[i].name;
					bucket.account_name = canon_name_map.get(buckets[i].owner);
					bucket.account_id = canon_id_map.get(buckets[i].owner);
	
					if(lifecycle_id_map.get(buckets[i].lifecycle) == null)
					{
						bucket.lifecycle = "[UNKNOWN]";
					}
					else
					{
						bucket.lifecycle = lifecycle_id_map.get(buckets[i].lifecycle);
					}

					bucket_list.add(bucket);
				}
			}
		}

		return bucket_list;
	}
}
