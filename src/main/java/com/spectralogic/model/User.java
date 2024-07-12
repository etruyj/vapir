//===================================================================
// User.java
// Description:
//     Represents a user with a username and account ID.
//
// Created by Sean Snyder.
//===================================================================

package com.spectralogic.vail.vapir.model;

import com.google.gson.annotations.SerializedName;

public class User {
    private String username = "";
    @SerializedName("accountid")
    private String accountId = "";

    //=======================================
    // Getters
    //=======================================
    
    public String getUsername() { return username; }
    public String getAccountId() { return accountId; }

    //=======================================
    // Setters
    //=======================================

    public void setUsername(String username) { this.username = username; }
    public void setAccountId(String accountId) { this.accountId = accountId; }
}
