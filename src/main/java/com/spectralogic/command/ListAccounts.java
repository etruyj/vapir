//===================================================================
// ListAccounts.java
//      Description:
//          Commands associated with listing AWS accounts in Vail.
//
// Created by Sean Snyder
//===================================================================

package com.spectralogic.vail.vapir.command;

import com.spectralogic.vail.vapir.model.Account;
import com.spectralogic.vail.vapir.api.VailConnector;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ListAccounts {
    private static final Logger log = LoggerFactory.getLogger(ListAccounts.class);

    public static Account[] all(String ip_address, VailConnector sphere) {
        try {
            log.info("Querying sphere at " + ip_address + " for a list of all AWS accounts.");
            Account[] accounts = sphere.listAccounts(ip_address);
            log.info("Found (" + accounts.length + ") AWS accounts.");
            return accounts;
        } catch(Exception e) {
            log.error(e.getMessage());
            System.err.println(e.getMessage());
            return null;
        }
    }

    public static ArrayList<Account> all(VailConnector sphere) {
        ArrayList<Account> account_list = new ArrayList<Account>();

        try {
            log.info("Querying sphere for a list of all AWS accounts.");
            Account[] accounts = sphere.listAccounts();
            log.info("Found (" + accounts.length + ") AWS accounts.");
        
            for(Account account : accounts) {
                account_list.add(account);
            }    
        } catch(Exception e) {
            log.error(e.getMessage());
            log.error("Failed to list AWS accounts.");
        }

        return account_list;
    }
}
