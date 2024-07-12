//===================================================================
// Capabilities.java
// Description:
//     This holds the ProtocolCapability values for the system.xml
//     file required by Veeam's SOSAPI. This is a separate class
//     as the XmlParser struggles with subclasses.
//
// Created by Sean Snyder.
//===================================================================

package com.spectralogic.vail.vapir.model.veeam;

public class Capabilities {
    public Capabilities() {
        CapacityInfo = true;
        UploadSessions = false;
        EnableOnPremArchiveTier = true;
    }

    //===========================================
    // Getters
    //===========================================

    public boolean capacityInfo() { return CapacityInfo; }
    public boolean uploadSessions() { return UploadSessions; }
    public boolean EnableOnPremArchiveTier() { return EnableOnPremArchiveTier; }

    //===========================================
    // Setters
    //===========================================

    public void setCapacityInfo(boolean b) { CapacityInfo = b; }
    public void setUploadSessions(boolean b) { UploadSessions = b; }
    public void setEnableOnPremArchiveTier(boolean enable) { EnableOnPremArchiveTier = enable; }

    //===========================================
    // Variables
    //===========================================

    public boolean CapacityInfo;
    public boolean UploadSessions;
    public boolean EnableOnPremArchiveTier;    
}
