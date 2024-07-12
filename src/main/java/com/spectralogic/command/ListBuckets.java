//===================================================================
// ListBuckets.java
// 	Description:
//		Provides a summarized list of buckets in the account.
//===================================================================

package com.spectralogic.vail.vapir.command;

import com.spectralogic.vail.vapir.api.VailConnector;
import com.spectralogic.vail.vapir.model.Account;
import com.spectralogic.vail.vapir.model.Bucket;
import com.spectralogic.vail.vapir.model.BucketSummary;
import com.spectralogic.vail.vapir.model.Lifecycle;
import com.spectralogic.vail.vapir.util.map.MapAccounts;
import com.spectralogic.vail.vapir.util.map.MapLifecycle;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ListBuckets
{
    private static final Logger log = LoggerFactory.getLogger(ListBuckets.class);

    public static Bucket[] all(String ip_address, VailConnector sphere) {
        try {
            return sphere.listBuckets(ip_address);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public static ArrayList<Bucket> all(VailConnector sphere) {
        log.info("Listing all buckets associated with Vail sphere.");

        ArrayList<Bucket> bucket_list = new ArrayList<Bucket>();
        
        try {
            Bucket[] buckets = sphere.listBuckets();

            log.info("Found (" + buckets.length + ") buckets associated with the sphere.");

            for(Bucket bucket : buckets) {
                bucket_list.add(bucket);
            }
        } catch(Exception e) {
            log.error(e.getMessage());
            log.error("Failed to list buckets.");
        }
        return bucket_list;
    }

    public static ArrayList<BucketSummary> summary(String ip_address, String filter_account, VailConnector sphere) {
        log.info("Listing buckets associated with account " + filter_account);

        Bucket[] buckets = all(ip_address, sphere);
        log.info("Found (" + buckets.length + ") buckets.");
        
        Account[] accounts = ListAccounts.all(ip_address, sphere);
        log.info("Found (" + accounts.length + ") accounts.");

        Lifecycle[] lifecycles = ListLifecycles.all(ip_address, sphere);
        log.info("Found (" + lifecycles.length + ") lifecycles.");

        return filterByAccount(buckets, accounts, lifecycles, filter_account);
    }

    private static ArrayList<BucketSummary> filterByAccount(Bucket[] buckets, Account[] accounts, Lifecycle[] lifecycles, String filter_account)
	{
        log.info("Filter bucket list for buckets associated with account " + filter_account);
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
                log.warn("Unable to locate account " + filter_account);
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
				if(filter_id.equals("all") || buckets[i].getOwner().equals(filter_id))
				{
					bucket = new BucketSummary();
					
					bucket.setType("bucket");
					bucket.setName(buckets[i].getName());
					bucket.setAccountName(canon_name_map.get(buckets[i].getOwner()));
					bucket.setAccountId(canon_id_map.get(buckets[i].getOwner()));
					
					if(buckets[i].getLifecycle() == null)
					{
						bucket.setLifecycle("");
					}
					else if(lifecycle_id_map.get(buckets[i].getLifecycle()) == null)
					{
						bucket.setLifecycle("[UNKNOWN]");
					}
					else
					{
						bucket.setLifecycle(lifecycle_id_map.get(buckets[i].getLifecycle()));
					}

					bucket_list.add(bucket);
				}
			}
		}

        log.info("Returning a filtered list of (" + bucket_list.size() + ") buckets.");

		return bucket_list;
	}
}

