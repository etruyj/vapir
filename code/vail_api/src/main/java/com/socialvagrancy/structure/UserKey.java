package com.socialvagrancy.vail.structures;

public class UserKey
{
	public String id;
    public String secretAccessKey;
    public boolean initialized = false;
	public boolean inactive = false;

    //===========================================
    // Getters
    //===========================================
    
    public String getId() { return id; }
    public String getSecretAccessKey() { return secretAccessKey; }
    public boolean getInitialized() { return initialized; }
    public boolean getInactive() { return inactive; }
    
    //===========================================
    // Setters
    //===========================================

    public void setId(String id) { this.id = id; }
    public void setSecretAccessKey(String secretAccessKey) { this.secretAccessKey = secretAccessKey; }
    public void setInitilized(boolean initialized) { this.initialized = initialized; }
    public void setInactive(boolean inactive) { this.inactive = inactive; }
}
