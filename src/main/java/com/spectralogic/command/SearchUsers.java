//===================================================================
// SearchUsers.java
//      Description:
//          This function produces a user list based on the desired
//          search parameters. As it leverages the existing ListUsers
//          command, it does inefficiently do the get groups call
//          before then culling the list.
//
// Created by Sean Snyder
//===================================================================

package com.spectralogic.vail.vapir.command;

import com.spectralogic.vail.vapir.api.VailConnector;
import com.spectralogic.vail.vapir.model.UserKey;
import com.spectralogic.vail.vapir.model.UserSummary;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchUsers {
    private static final Logger log = LoggerFactory.getLogger(SearchUsers.class);

    public static ArrayList<UserSummary> withAccessKey(String ip, String account, String access_key, boolean active_only, VailConnector vail) {
        log.info("Searching for user associated with access key: " + access_key);

        ArrayList<UserSummary> search_results = new ArrayList<UserSummary>();

        try {
            List<UserSummary> all_users = ListUsers.summary(ip, account, active_only, vail);

            // Query the user list for the keys.
            UserKey[] user_keys = null;

            for(UserSummary user : all_users) {
                log.info("Retrieving user keys associated with username: " + user.getName()); 

                user_keys = vail.listUserKeys(user.getAccountId(), user.getName());

                if(user_keys == null || user_keys.length == 0) {
                    log.info("No user keys associated with user.");
                } else {
                    log.info("Found (" + user_keys.length + ") access keys associated with user.");
                    for(int i=0; i<user_keys.length; i++) {
                        if(user_keys[i].getId().equals(access_key)) {
                            log.info("User " + user.getName() + " has access key " + user_keys[i].getId());
                            search_results.add(user);
                        }                    
                    }        
                }
            }
        } catch(Exception e) {
            log.error(e.getMessage());
            log.error("Failed to search users.");
        }
        log.info("Returning (" + search_results.size() + ") search results.");

        return search_results;
    }
}
