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

package com.spectralogic.vail.vapir.util.map;

import com.spectralogic.vail.vapir.model.Account;
import java.util.HashMap;

public class MapAccounts
{
	public static HashMap<String, String> createCanonicalIDNameMap(Account[] accounts)
	{
		HashMap<String, String> account_map = new HashMap<String, String>();

		for(int i=0; i < accounts.length; i++)
		{
			account_map.put(accounts[i].getCanonicalId(), accounts[i].getUsername());
		}

		return account_map;
	}

	public static HashMap<String, String> createCanonicalIDMap(Account[] accounts)
	{
		HashMap<String, String> account_map = new HashMap<String, String>();

		for(int i=0; i < accounts.length; i++)
		{
			account_map.put(accounts[i].getCanonicalId(), accounts[i].getId());
		}

		return account_map;
	}
	
	public static HashMap<String, Account> createIDAccountMap(Account[] accounts)
	{
		HashMap<String, Account> account_map = new HashMap<String, Account>();

		for(int i=0; i < accounts.length; i++)
		{
			account_map.put(accounts[i].getId(), accounts[i]);
		}

		return account_map;
	}

	public static HashMap<String, String> createIDCanonicalIDMap(Account[] accounts)
	{
		HashMap<String, String> account_map = new HashMap<String, String>();

		for(int i=0; i < accounts.length; i++)
		{
			account_map.put(accounts[i].getId(), accounts[i].getCanonicalId());
		}

		return account_map;	
	}

	public static HashMap<String, String> createIDNameMap(Account[] accounts)
	{
		HashMap<String, String> account_map = new HashMap<String, String>();

		for(int i=0; i < accounts.length; i++)
		{
			account_map.put(accounts[i].getId(), accounts[i].getUsername());
		}

		return account_map;
	}
	
	public static HashMap<String, String> createNameCanonicalIDMap(Account[] accounts)
	{
		HashMap<String, String> account_map = new HashMap<String, String>();

		for(int i=0; i < accounts.length; i++)
		{
			account_map.put(accounts[i].getUsername(), accounts[i].getCanonicalId());
			
			// Check for Sphere account
			// add account without ARN to name map as sphere as well.
			if(accounts[i].getRoleArn() == null || accounts[i].getRoleArn() == "")
			{
				account_map.put("sphere", accounts[i].getCanonicalId());
				account_map.put("spectra", accounts[i].getCanonicalId());
			}

		}

		return account_map;	
	}

	public static HashMap<String, String> createNameIDMap(Account[] accounts)
	{
		HashMap<String, String> account_map = new HashMap<String, String>();

		for(int i=0; i < accounts.length; i++)
		{
			account_map.put(accounts[i].getUsername(), accounts[i].getId());
		}

		return account_map;
	}
}
