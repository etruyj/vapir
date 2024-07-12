//================================ Account =================================
// Description:
// This class represents an account entity.
// 
// Variables:
// - id: The ID of the account.
// - canonicalId: The canonical ID of the account.
// - externalId: The external ID of the account.
// - roleArn: The role ARN of the account.
// - username: The username of the account.
// - email: The email address associated with the account.
// 
//Created by Sean Snyder.
//===========================================================================

package com.spectralogic.vail.vapir.model;

public class Account {
    private String id = "";
    private String canonicalId = "";
    private String externalId = "";
    private String roleArn = "";
    private String username = "";
    private String email = "";
    private String description;
    private boolean default_account;

    // Constructors
    public Account() {
        default_account = false;
    }

    public Account(String id, String canonicalId, String externalId, String roleArn, String username, String email) {
        this.id = id;
        this.canonicalId = canonicalId;
        this.externalId = externalId;
        this.roleArn = roleArn;
        this.username = username;
        this.email = email;
    }

    //=============================
    // Getters
    //=============================

    public String getId() { return id; }
    public String getCanonicalId() { return canonicalId; }
    public boolean isDefault() { return default_account; }
    public String getDescription() { return description; }
    public String getExternalId() { return externalId; }
    public String getRoleArn() { return roleArn; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }

    //=============================
    // Setters
    //=============================

    public void setId(String id) { this.id = id; }
    public void setCanonicalId(String canonicalId) { this.canonicalId = canonicalId; }
    public void setDefault(boolean isDefault) { this.default_account = isDefault; }
    public void setDescription(String desc) { this.description = desc; } 
    public void setExternalId(String externalId) { this.externalId = externalId; }
    public void setRoleArn(String roleArn) { this.roleArn = roleArn; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
}
