//===================================================================
// ListUsers.java
// 	Description:
// 		Provides an ArrayList of users associated with the
// 		Vail Sphere.
// 		This is now an advanced function as the API changed
// 		to require an account with the list users command.
//===================================================================

package com.spectralogic.vail.vapir.command;

import com.spectralogic.vail.vapir.api.VailConnector;
import com.spectralogic.vail.vapir.model.Account;
import com.spectralogic.vail.vapir.model.User;
import com.spectralogic.vail.vapir.model.UserSummary;
import com.spectralogic.vail.vapir.model.UserKey;
import com.spectralogic.vail.vapir.model.Group;
import com.spectralogic.vail.vapir.model.UserData;
import com.spectralogic.vail.vapir.util.map.MapAccounts;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ListUsers
{
    private static final Logger log = LoggerFactory.getLogger(ListUsers.class);

	public static User[] all(String ip_address, String account_id, VailConnector sphere) {
        log.info("List all users associated with account " + account_id);
        try {
            User[] users = sphere.listUsers(ip_address, account_id).getData();
            log.info("Found (" + users.length + ") users.");
            return users;
        } catch(Exception e) {
            log.error(e.getMessage());
            System.err.println(e.getMessage());
            return null;
        }
    }
    
    public static ArrayList<UserSummary> summary(String ip, String account, boolean active_only, VailConnector sphere) throws Exception {
		log.info("Creating user summary for users associated with account " + account);
        Account[] accounts = ListAccounts.all(ip, sphere);
		ArrayList<UserSummary> user_list;

		if(account.equalsIgnoreCase("sphere"))
		{
			boolean searching = true;
			int itr = 0;

			// Find the account id
			while(searching)
			{
				if(accounts[itr].getRoleArn().equals("")) {
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
			user_list = listAllUsers(sphere, ip, accounts);
		}
		else
		{
			user_list = listAccountUsers(sphere, ip, accounts, account);
		}

		user_list = attachGroups(sphere, ip, user_list);
		user_list = determineStatus(sphere, ip, user_list);

		if(active_only)
		{
			user_list = filterInactive(user_list);
		}
        
        log.info("Returning list of (" + user_list.size() + ") users.");
		return user_list;
	}

	//=======================================
	// Private Functions
	//=======================================

	private static ArrayList<UserSummary> attachGroups(VailConnector sphere, String ip, ArrayList<UserSummary> user_list) throws Exception {
        log.info("Finding groups associated with users.");
        for(int i=0; i < user_list.size(); i++)
		{
            System.out.println("SySTEM: " + user_list.get(i).getAccountId());
			Group[] groups = sphere.listUserGroups(ip, user_list.get(i).getAccountId(), user_list.get(i).getName());
	
            if(groups == null) {
                log.debug("User [" + user_list.get(i).getName() + "] does not belong to any groups.");
            } else {
                log.debug("User [" + user_list.get(i).getName() + "] belongs to (" + groups.length + ") groups.");    
			    for(int j=0; j < groups.length; j++)
			    {
				    user_list.get(i).addGroup(groups[j].getName());
			    }
            }
		}

		return user_list;
	}

	private static UserSummary buildUserSummary(String username, String account_id, String account_name) throws Exception {
		UserSummary user_sum = new UserSummary();

		user_sum.setType("user");
		user_sum.setName(username);
		user_sum.setAccountId(account_id);
		user_sum.setAccountName(account_name);

		return user_sum;
		
	}

	private static ArrayList<UserSummary> determineStatus(VailConnector sphere, String ip, ArrayList<UserSummary> user_list) throws Exception
	{
        log.info("Determining whether users have active access keys.");


		for(int i=0; i < user_list.size(); i++)
		{
		    boolean active_key = false;
			UserKey[] keys = sphere.listUserKeys(ip, user_list.get(i).getAccountId(), user_list.get(i).getName());

			if(keys != null)
			{
				for(int j=0; j < keys.length; j++)
				{
					if(keys[j].isInitialized())
					{
						active_key = true;
					}
				}
			}
			
			if(active_key)
			{
                log.debug("User [" + user_list.get(i).getName() + "] is active");
				user_list.get(i).setStatus("active");
			}
			else
			{
                log.debug("User [" + user_list.get(i).getName() + "] is in-active");
				user_list.get(i).setStatus("in-active");
			}
		}
		
		return user_list;
	}

	private static ArrayList<UserSummary> filterInactive(ArrayList<UserSummary> user_list)
	{
	    log.info("Filtering out inactive users from list.");
        for(int i = user_list.size() - 1; i >=0; i--)
		{
			if(user_list.get(i).getStatus().equals("in-active"))
			{
				user_list.remove(i);
			}
		}

        log.info("There are (" + user_list.size() + ") active users.");
		return user_list;
	}

	private static ArrayList<UserSummary> listAccountUsers(VailConnector sphere, String ip, Account[] accounts, String account) throws Exception
	{
        log.info("Listing users associated with account " + account);
		ArrayList<UserSummary> user_list = new ArrayList<UserSummary>();
		UserData users;
		boolean searching_account = true;

		HashMap<String, String> account_id_map = MapAccounts.createNameIDMap(accounts);
		HashMap<String, Account> account_map = MapAccounts.createIDAccountMap(accounts);
		HashMap<String, String> account_canon_map = MapAccounts.createCanonicalIDMap(accounts);

		// Filter out account == sphere which is valid input
		if(account.equalsIgnoreCase("sphere"))
		{
			account = "";
		}	

		if(searching_account && account_id_map.get(account) == null)
		{
			throw new Exception("Account name [" + account + "] was not found in account list");
		}
		else if(searching_account)
		{
			account = account_id_map.get(account);
			searching_account = false;
		}

		if(searching_account && account_canon_map.get(account) == null)
		{
			throw new Exception("Account canonical ID [" + account + "] was not found in account list"); 
		}
		else if(searching_account)
		{
			account = account_canon_map.get(account);
			searching_account = false;
		}

		if(searching_account && account_map.get(account) == null)
		{
			throw new Exception("Invalid account [" + account + "] specified in parameters.");
		}
	
		// Else
		// 	The account name specified exists in one of the maps.
		//	We can find the users.

		users = sphere.listUsers(ip, account);
        log.info("Found (" + users.count() + ") users.");

		for(int i=0; i < users.count(); i++)
		{
			user_list.add(buildUserSummary(users.username(i), users.accountID(i), account_map.get(account).getUsername()));
		}

        log.debug("Returning (" + user_list.size() + ") users");
		return user_list;
	}

	private static ArrayList<UserSummary> listAllUsers(VailConnector sphere, String ip, Account[] accounts) throws Exception
	{
        log.info("Listing all users in the Vail Sphere.");
		ArrayList<UserSummary> user_list = new ArrayList<UserSummary>();
		UserData users;

		for(int i=0; i < accounts.length; i++)
		{
			users = sphere.listUsers(ip, accounts[i].getId());
     
			for(int j=0; j < users.count(); j++)
			{
				user_list.add(buildUserSummary(users.username(j), users.accountID(j), accounts[i].getUsername()));
			}
		}

        log.debug("Returning (" + user_list.size() + ") users");
		return user_list;
	}

}

