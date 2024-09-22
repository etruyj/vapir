//===================================================================
// NodeActivationPacket.java
//      Description:
//          This class models the node activation packet sent to
//          to the Vail server in order to activate the Vail node.
//
// Created by Sean Snyder
// Copyright Spectra Logic 2024
//===================================================================

package com.spectralogic.vail.vapir.model;

public class NodeActivationPacket {
    private String key;
    private String url;

    //===========================================
    // Getters
    //===========================================
    public String getKey() { return key; }
    public String getUrl() { return url; }

    //===========================================
    // Setters
    //===========================================
    public void setKey(String key) { this.key = key; }
    public void setUrl(String url) { this.url = url; }
}
