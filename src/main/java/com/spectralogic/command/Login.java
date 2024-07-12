//===================================================================
// Login.java
//      Description:
//          Commands associated with Logging into the management pane
//          of the sphere.
//
// Created by Sean Snyder
//===================================================================

package com.spectralogic.vail.vapir.command;

import com.spectralogic.vail.vapir.api.VailConnector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Login {
    private static final Logger log = LoggerFactory.getLogger(Login.class);

    public static boolean toSphere(String ip_address, String user, String password, VailConnector sphere) {
        try {
            log.info("Connecting to Vail sphere at " + ip_address + " as user [" + user + "]");
            sphere.login(ip_address, user, password);
            log.info("Login successful.");
            return true;
        } catch(Exception e) {
            System.err.println(e.getMessage());
            log.error("Failed to log into the Vail sphere.");
            return false;
        }
    }
}
