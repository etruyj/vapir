//=============================================
// Summary.java
// Description:
//     Represents a summary with type, name, account name, account ID, and status.
//
// Variables:
// - type: Description of the type.
// - name: Description of the name.
// - account_name: Description of the account name.
// - account_id: Description of the account ID.
// - status: Description of the status.
//
// Created by Sean Snyder.
//=============================================

package com.spectralogic.vail.vapir.model;

public class Summary {
    // Private variables
    private String type;
    private String name;
    private String account_name;
    private String account_id;
    private String status = null;

    // Getters
    public String getType() { return type; }
    public String getName() { return name; }
    public String getAccountName() { return account_name; }
    public String getAccountId() { return account_id; }
    public String getStatus() { return status; }

    // Setters
    public void setType(String type) { this.type = type; }
    public void setName(String name) { this.name = name; }
    public void setAccountName(String account_name) { this.account_name = account_name; }
    public void setAccountId(String account_id) { this.account_id = account_id; }
    public void setStatus(String status) { this.status = status; }
}
