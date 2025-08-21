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

    public static Group[] inAccount(String account, VailConnector sphere) {
        log.info("Listing all groups associated with account id: " + account);
        Group[] groups = null;

        try {
           groups = sphere.listGroups(account).getData();
           log.info("Found (" + groups.length + ") groups.");
        } catch(Exception e) {
            log.error(e.getMessage());
            log.error("Failed to list groups.");
        }

        return groups;
    }

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

    @Deprecated // removed ip address requirement.
	public static ArrayList<Summary> summary(String ip, String account, VailConnector sphere) {
	    return summary(account, sphere);
    }

	public static ArrayList<Summary> summary(String account, VailConnector sphere) {
		log.info("Listing groups that belong to the account " + account);
        
        ArrayList<Summary> group_list = null;
    
        try {

            Account[] accounts = sphere.listAccounts();
            log.info("Found (" + accounts.length + ") AWS accounts associated with Vail sphere.");
	
            // === Find Account Id ===
            // Parse the account list to determine the account
            // ID to provide to the API.
            // First convert "sphere" the old account name to spectra
		    if(account.equalsIgnoreCase("sphere")) {
                account = "spectra";
            }

            // Search as long as none or all are not specified.
            if(!(account.equals("none") || account.equals("all"))) {
                boolean searching = true;
                int itr = 0;
                String account_id = null;

			    while(searching) {
                    // Search for username or if the ID matches.
				    if(accounts[itr].getUsername().equalsIgnoreCase(account) || accounts[itr].getId().equals(account)) {
                        log.info("Account [" + account + "] has id " + accounts[itr].getId());
			    		account_id = accounts[itr].getId();
			    		searching = false;
			    	}

			    	itr++;

			    	if(searching && itr >= accounts.length) {
				    	// Account not found
                        // break the loop with a failure
                        throw new Exception("Failed to find account [" + account + "].");
				    }   
			    }
			    
                // search for groups associated with the specified account
                group_list = listAccountGroups(sphere, account_id, account);
		    } else {  
                // none or all was specified.
                // search for all groups.
			    group_list = listAllGroups(sphere, accounts);
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

	private static ArrayList<Summary> listAccountGroups(VailConnector sphere, String account_id, String account) throws Exception {
        log.info("Searching for groups that belong to account " + account + "[" + account_id + "]");
        ArrayList<Summary> group_list = new ArrayList<Summary>();
		GroupData groups;
		boolean searching = true;

		groups = sphere.listGroups(account);

		for(int i=0; i < groups.count(); i++) {
			group_list.add(buildGroupSummary(groups.name(i), account_id, account));
		}

		return group_list;
	}

	private static ArrayList<Summary> listAllGroups(VailConnector sphere, Account[] accounts) throws Exception
	{
        log.info("Listing groups that belong to all accounts.");
		ArrayList<Summary> group_list = new ArrayList<Summary>();
		GroupData groups;

		for(int i=0; i < accounts.length; i++)
		{
			groups = sphere.listGroups(accounts[i].getId());
			
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

