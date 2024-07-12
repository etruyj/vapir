//===================================================================
// BpDataPolicy.java
//      Description:
//          A container class for BlackPearl Vail-compliant data
//          policies. This just contains the id and the name to 
//          allow a user mapping for storage configuration.
//
// Created by Sean Snyder
// Copyright Spectra Logic 2024
//===================================================================

package com.spectralogic.vail.vapir.model.blackpearl;

public class BpDataPolicy {
    private String id;
    private String name;
    
    //===========================================
    // Getters
    //===========================================
    public String getId() { return id; }
    public String getName() { return name; }

    //===========================================
    // Getters
    //===========================================
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
}
