//===================================================================
// ListGroups.java
// 	Description:
// 		Provides an ArrayList of users associated with the
// 		Vail Sphere.
// 		This is now an advanced function as the API changed
// 		to require an account with the list users command.
//===================================================================

package com.spectralogic.vail.vapir.command;

import com.spectralogic.vail.vapir.api.VailConnector;
import com.spectralogic.vail.vapir.model.Account;
import com.spectralogic.vail.vapir.model.Summary;
import com.spectralogic.vail.vapir.model.Group;
import com.spectralogic.vail.vapir.model.GroupData;
import com.spectralogic.vail.vapir.model.UserData;
import com.spectralogic.vail.vapir.util.map.MapAccounts;
import com.spectralogic.vail.vapir.util.search.SearchAccounts;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ListGroups
{
    private static final Logger log = LoggerFactory.getLogger(ListGroups.class);

    public static Group[] inAccount(String ip_address, String account, VailConnector sphere) {
        try {
            Account[] accounts = ListAccounts.all(ip_address, sphere);

            String id = SearchAccounts.findId(accounts, account);

            return sphere.listGroups(ip_address, id).getData();
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

	public static ArrayList<Summary> summary(String ip, String account, VailConnector sphere) {
		log.info("Listing groups that belong to the account " + account);
        
        ArrayList<Summary> group_list = null;
    
        try {

            Account[] accounts = sphere.listAccounts(ip);
            log.info("Found (" + accounts.length + ") AWS accounts associated with Vail sphere.");
		
		    if(account.equalsIgnoreCase("sphere"))
		    {
			    boolean searching = true;
			    int itr = 0;

			    // Find the account id
			    while(searching)
			    {
				    if(accounts[itr].getRoleArn() == "")
				    {
			    		account = accounts[itr].getId();
			    		searching = false;
			    	}

			    	itr++;

			    	if(itr >= accounts.length)
			    	{
				    	searching = false;
				    }   
			    }
		    }   

		    if(account.equals("none") || account.equals("all"))
		    {
			    group_list = listAllGroups(sphere, ip, accounts);
		
		    }
		    else
		    {
			    group_list = listAccountGroups(sphere, ip, accounts, account);
		    }
        } catch(Exception e) {
            System.err.println(e.getMessage());
        }

        log.info("Returning a list of (" + group_list.size() + ") groups.");
		return group_list;
	}

	//=======================================
	// Private Functions
	//=======================================

	public static Summary buildGroupSummary(String name, String account_id, String account_name)
	{
		Summary sum = new Summary();
        
		sum.setType("user");
		sum.setName(name);
		sum.setAccountId(account_id);
		sum.setAccountName(account_name);

		return sum;
		
	}

	private static ArrayList<Summary> listAccountGroups(VailConnector sphere, String ip, Account[] accounts, String account) throws Exception {
        log.info("Searching for groups that belong to account " + account);
        ArrayList<Summary> group_list = new ArrayList<Summary>();
		GroupData groups;
		boolean searching = true;

		HashMap<String, String> account_id_map = MapAccounts.createNameIDMap(accounts);
		HashMap<String, Account> account_map = MapAccounts.createIDAccountMap(accounts);
		HashMap<String, String> account_canon_map = MapAccounts.createCanonicalIDMap(accounts);

		// Filter out account == sphere which is valid input
		if(account.equalsIgnoreCase("sphere"))
		{
			account = "";
		}	

		if(searching && account_id_map.get(account) == null)
		{
			throw new Exception("Account name [" + account + "] was not found in account list");
		}
		else if(searching)
		{
			account = account_id_map.get(account);
			searching = false;
		}

		if(searching && account_canon_map.get(account) == null)
		{
			throw new Exception("Account canonical ID [" + account + "] was not found in account list"); 
		}
		else if(searching)
		{
			account = account_canon_map.get(account);
			searching = false;
		}

		if(searching && account_map.get(account) == null)
		{
			throw new Exception("Invalid account [" + account + "] specified in parameters.");
		}
	
		// Else
		// 	The account name specified exists in one of the maps.
		//	We can find the users.

		groups = sphere.listGroups(ip, account);



		for(int i=0; i < groups.count(); i++)
		{
			group_list.add(buildGroupSummary(groups.name(i), groups.accountID(i), account_map.get(account).getUsername()));
		}

		return group_list;
	}

	private static ArrayList<Summary> listAllGroups(VailConnector sphere, String ip, Account[] accounts) throws Exception
	{
        log.info("Listing groups that belong to all accounts.");
		ArrayList<Summary> group_list = new ArrayList<Summary>();
		GroupData groups;

		for(int i=0; i < accounts.length; i++)
		{
			groups = sphere.listGroups(ip, accounts[i].getId());
			
			// Error Handling for pre-2.0.0 sphere 
			// and for the event where people delete all their groups.
			if(groups != null)
			{
				for(int j=0; j < groups.count(); j++)
				{
					group_list.add(buildGroupSummary(groups.name(j), groups.accountID(j), accounts[i].getUsername()));
				}
			}
		}

		return group_list;
	}

}

