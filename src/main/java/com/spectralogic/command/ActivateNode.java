//===================================================================
// ActivateNode.java
//      Description:
//          This command activates the Vail node with the sphere.
//
// Created by Sean Snyder
// Copyright Spectra Logic 2024
//===================================================================

package com.spectralogic.vail.vapir.command;

import com.spectralogic.vail.vapir.api.VailConnector;
import com.spectralogic.vail.vapir.model.NodeActivationPacket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActivateNode {
    private static final Logger log = LoggerFactory.getLogger(ActivateNode.class);

    public static String withKey(String key, String license_server, VailConnector sphere) {
        log.info("Activating the Vail node with activation key [" + key + "] and licensing server [" + license_server + "]");

        NodeActivationPacket packet = new NodeActivationPacket();

        packet.setKey(key);
        packet.setUrl(license_server);

        try {
            if(sphere.activateNode(packet)) {
                return "Node activated successfully.";
            } else {
                throw new Exception("Node activation failed.");
            }

        } catch(Exception e) {
            log.error(e.getMessage());
            return "Failed to activate node.";
        }
    }
}
