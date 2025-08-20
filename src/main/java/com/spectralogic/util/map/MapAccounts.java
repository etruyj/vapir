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
	
	public static HashMap<String, String> createNameCanonicalIDMap(Account[] accounts) {
		HashMap<String, String> account_map = new HashMap<String, String>();

		for(int i=0; i < accounts.length; i++) {
			account_map.put(accounts[i].getUsername(), accounts[i].getCanonicalId());
			
            // If the username for the account is "spectra" i.e. the primary local account,
            // also apply this account as the "sphere" account, which is an old configuration
            // parameter. Not really necessary, but preserved for backwards compatibility.
            if(accounts[i].getUsername().equals("spectra")) {
                account_map.put("sphere", accounts[i].getCanonicalId());
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
