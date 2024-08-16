//===================================================================
// CreateGroup.java
// 	Description:
// 		Takes a group name and an account [id | name | canonical id]
// 		and issues the create group command.
//===================================================================

package com.spectralogic.vail.vapir.command;

import com.spectralogic.vail.vapir.api.VailConnector;
import com.spectralogic.vail.vapir.model.Account;
import com.spectralogic.vail.vapir.model.Group;
import com.spectralogic.vail.vapir.util.search.SearchAccounts;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateGroup {
    private static final Logger log = LoggerFactory.getLogger(CreateGroup.class);

	public static Group create(String ip, String name, String account_id, VailConnector sphere) {
        log.info("Creating group [" + name + "] for account " + account_id);

        Account[] accounts = ListAccounts.all(ip, sphere);
        log.info("Found (" + accounts.length + ") AWS accounts attached to the sphere.");

		String id = SearchAccounts.findId(accounts, account_id); 

        if(id == null || id.equals("none")) {
            log.error("Cannot create group. Invalid account [" + account_id + "] specified.");
            return null;
        }

        log.info("Account " + account_id + " belongs to " + id);

        try {
		    return sphere.createGroup(ip, name, id);
        } catch(Exception e) {
            log.error(e.getMessage());
            log.error("Failed to create group [" + name + "]");
            return null;
        }
	}

    public static Group fromObject(Group group, VailConnector sphere) {
        log.info("Creating group [" + group.getName() + "] for account " + group.getAccountId());

        Group new_group = null;
        try {
            new_group = sphere.createGroup(group);

            log.info("Successfully created group " + group.getName());
        } catch(Exception e) {
            log.error(e.getMessage());
            log.error("Failed to create group.");
        }

        return new_group;
    }
}

