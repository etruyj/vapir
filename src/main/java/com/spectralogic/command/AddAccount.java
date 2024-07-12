//===================================================================
// AddAccount.java
//      Description:
//          Commands associated with adding an AWS account to a Vail
//          sphere.
//
// Created by Sean Snyder
//===================================================================

package com.spectralogic.vail.vapir.command;

import com.spectralogic.vail.vapir.api.VailConnector;
import com.spectralogic.vail.vapir.model.Account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddAccount {
    private static final Logger log = LoggerFactory.getLogger(AddAccount.class);

    public static Account fromFields(String ip_address, String roleArn, String externalId, String username, String description, VailConnector sphere) throws Exception {
        log.info("Adding AWS Account [" + roleArn + "] to Vail Sphere.");
        Account account = new Account();

        account.setRoleArn(roleArn);
        account.setUsername(username);
        account.setDescription(description);

        if(externalId != null && externalId != "") {
            account.setExternalId(externalId);
        }

        Account response = sphere.addAccount(ip_address, account);

        if(response != null) {
            log.info("Successfully added account to Vail Sphere.");
        } else {
            log.warn("Failed to add account to Vail Sphere.");
        }

        return response;
    }
}
