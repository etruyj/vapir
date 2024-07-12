//===================================================================
// ListLifecycles.java
//      Description:
//          Commands associated with listing Lifecycles in the Vail
//          sphere.
//
// Created by Sean Snyder
//===================================================================

package com.spectralogic.vail.vapir.command;

import com.spectralogic.vail.vapir.api.VailConnector;
import com.spectralogic.vail.vapir.model.Lifecycle;

import java.util.ArrayList;

public class ListLifecycles {
    public static Lifecycle[] all(String ip_address, VailConnector sphere) {
        try {
            return sphere.listLifecycles(ip_address);
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}
