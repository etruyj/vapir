//===================================================================
// CreateUser.java
// 	Description:
// 		Takes username and account id, name, or canonical id
// 		and sends the username and account id to the sphere
// 		to create the user.
//===================================================================

package com.spectralogic.vail.vapir.command;

import com.spectralogic.vail.vapir.api.VailConnector;
import com.spectralogic.vail.vapir.model.Account;
import com.spectralogic.vail.vapir.model.User;
import com.spectralogic.vail.vapir.util.search.SearchAccounts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateUser {
    private static final Logger log = LoggerFactory.getLogger(CreateUser.class);

	public static User create(String ip_address, String username, String account, VailConnector sphere) throws Exception {
	    log.info("Creating user [" + username + "] for account " + account);
    
        Account[] accounts = ListAccounts.all(ip_address, sphere);
        log.info("Found (" + accounts.length + ") AWS accounts attached to the sphere.");

		String account_id = SearchAccounts.findId(accounts, account);

		if(account_id == null || account_id.equals("none")) {
			    throw new Exception("Unabled to find account [" + account + "].");
		} else {
            log.info("Account " + account + " has id " + account_id);
	        return sphere.createUser(ip_address, account_id, username);
        }
    }
}

