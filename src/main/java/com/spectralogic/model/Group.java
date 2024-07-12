//===================================================================
// Group.java
// Description:
//     Holds basic group information returned from api calls
//     Also referenced as the sub-class for the data field 
//     in GroupData.java, which couches this information in
//     a data variable.
//
// Created by Sean Snyder
//===================================================================

package com.spectralogic.vail.vapir.model;

import com.google.gson.annotations.SerializedName;

public class Group {
    private String name;
    @SerializedName("accountid")
    private String accountId;

    //=====================================
    // Getters
    //=====================================

    public String getName() { return name; }
    public String getAccountId() { return accountId; }

    
    //=====================================
    // Setters
    //=====================================

    public void setName(String name) { this.name = name; }
    public void setAccountId(String accountId) { this.accountId = accountId; }
}
