//===================================================================
// UserKey.java
// Description:
//     Represents a user key with an ID, secret access key, and flags for initialization and activity status.
//
// Created by Sean Snyder.
//===================================================================

package com.spectralogic.vail.vapir.model;

public class UserKey {
    private String id;
    private String secretAccessKey;
    private boolean initialized = false;
    private boolean inactive = false;

    //===========================================
    // Getters
    //===========================================

    public String getId() { return id; }
    public String getSecretAccessKey() { return secretAccessKey; }
    public boolean isInitialized() { return initialized; }
    public boolean isInactive() { return inactive; }

    //===========================================
    // Setters
    //===========================================

    public void setId(String id) { this.id = id; }
    public void setSecretAccessKey(String secretAccessKey) { this.secretAccessKey = secretAccessKey; }
    public void setInitialized(boolean initialized) { this.initialized = initialized; }
    public void setInactive(boolean inactive) { this.inactive = inactive; }
}
