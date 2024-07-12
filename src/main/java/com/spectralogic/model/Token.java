//===================================================================
// Token.java
// Description:
//     Represents a token with a status and value.
//
// Created by Sean Snyder.
//===================================================================

package com.spectralogic.vail.vapir.model;

public class Token {
    private String status;
    private String token;

    //=======================================
    // Getters
    //=======================================

    public String getStatus() { return status; }
    public String getToken() { return token; }

    //=======================================
    // Setters
    //=======================================

    public void setStatus(String status) { this.status = status; }
    public void setToken(String token) { this.token = token; }
}
