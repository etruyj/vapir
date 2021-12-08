package com.socialvagrancy.vail.commands.sub;

import com.socialvagrancy.vail.structures.Account;
import com.socialvagrancy.vail.structures.Bucket;
import com.socialvagrancy.vail.structures.BucketSummary;

import java.util.ArrayList;

public class SummarizeBuckets
{
	public static ArrayList<BucketSummary> filterByAccount(Bucket[] buckets, Account[] accounts, String filter_account)
	{
		ArrayList<BucketSummary> bucket_list = new ArrayList<BucketSummary>();
		BucketSummary bucket;

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
					bucket = new BucketSummary();
					bucket.name = buckets[i].name;
					bucket.account_name = last_account_name;
					bucket.account_id = last_account_id;
					bucket_list.add(bucket);
				}
				else
				{
					bucket = new BucketSummary();
					last_canonical_id = buckets[i].owner;
					last_account_name = findAccountName(accounts, last_canonical_id);
					account_index = SumarizeUsers.findAccountIndex(last_account_name, accounts);
					last_account_id = accounts[account_index].id;
					bucket.name = buckets[i].name;
					bucket.account_name = last_account_name;
					bucket.account_id = last_account_id;
					bucket_list.add(bucket);
				}
			}
			else
			{
				account_index = SumarizeUsers.findAccountIndex(filter_account, accounts);
				if(buckets[i].owner.equals(accounts[account_index].canonicalId))
				{
					bucket = new BucketSummary();
					bucket.name = buckets[i].name;
					bucket.account_name = accounts[account_index].username;
					bucket.account_id = accounts[account_index].id;
					bucket_list.add(bucket);
				}
			}
		}

		return bucket_list;
	}

	public static String findAccountName(Account[] accounts, String canonicalId)
	{
		for(int i=0; i<accounts.length; i++)
		{
			if(accounts[i].canonicalId.equals(canonicalId))
			{
				if(accounts[i].roleArn.equals(""))
				{
					return "Sphere";
				}
				else
				{
					return accounts[i].username;
				}
			}
		}

		return "not found";
	}
}
