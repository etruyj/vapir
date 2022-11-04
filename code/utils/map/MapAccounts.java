//===================================================================
// MapAccounts.java
// 	Description:
// 		These functions create HashMaps of IDs and names for
// 		referencing between classes.
//
// 	Functions:
// 		- createCanonicalIDNameMap
// 		- createNameIDMap
//===================================================================

package com.socialvagrancy.vail.utils.map;

import com.socialvagrancy.vail.structures.Account;
import java.util.HashMap;

public class MapAccounts
{
	public static HashMap<String, String> createCanonicalIDNameMap(Account[] accounts)
	{
		HashMap<String, String> account_map = new HashMap<String, String>();

		for(int i=0; i < accounts.length; i++)
		{
			account_map.put(accounts[i].canonicalId, accounts[i].username);
		}

		return account_map;
	}

	public static HashMap<String, String> createCanonicalIDMap(Account[] accounts)
	{
		HashMap<String, String> account_map = new HashMap<String, String>();

		for(int i=0; i < accounts.length; i++)
		{
			account_map.put(accounts[i].canonicalId, accounts[i].id);
		}

		return account_map;
	}
	
	public static HashMap<String, Account> createIDAccountMap(Account[] accounts)
	{
		HashMap<String, Account> account_map = new HashMap<String, Account>();

		for(int i=0; i < accounts.length; i++)
		{
			account_map.put(accounts[i].id, accounts[i]);
		}

		return account_map;
	}

	public static HashMap<String, String> createIDCanonicalIDMap(Account[] accounts)
	{
		HashMap<String, String> account_map = new HashMap<String, String>();

		for(int i=0; i < accounts.length; i++)
		{
			account_map.put(accounts[i].id, accounts[i].canonicalId);
		}

		return account_map;	
	}

	public static HashMap<String, String> createIDNameMap(Account[] accounts)
	{
		HashMap<String, String> account_map = new HashMap<String, String>();

		for(int i=0; i < accounts.length; i++)
		{
			account_map.put(accounts[i].id, accounts[i].username);
		}

		return account_map;
	}
	
	public static HashMap<String, String> createNameCanonicalIDMap(Account[] accounts)
	{
		HashMap<String, String> account_map = new HashMap<String, String>();

		for(int i=0; i < accounts.length; i++)
		{
			account_map.put(accounts[i].username, accounts[i].canonicalId);
			
			// Check for Sphere account
			// add account without ARN to name map as sphere as well.
			if(accounts[i].roleArn == null || accounts[i].roleArn == "")
			{
				account_map.put("sphere", accounts[i].canonicalId);
			}

		}

		return account_map;	
	}

	public static HashMap<String, String> createNameIDMap(Account[] accounts)
	{
		HashMap<String, String> account_map = new HashMap<String, String>();

		for(int i=0; i < accounts.length; i++)
		{
			account_map.put(accounts[i].username, accounts[i].id);
		}

		return account_map;
	}
}
