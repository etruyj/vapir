//===================================================================
// Endpoint.java
// Description:
//     This holds the endpoint information that is returned
//     from the sl/api/endpoints command as well as credential
//     information to reduce the number of times a password
//     has to be entered when configuring storage.
//===================================================================

package com.spectralogic.vail.vapir.model;

public class Endpoint {
    private String id;
    private String type;
    private String name;
    private String location;
    private String currentVersion;
    private String preferredVersion;
    private String endpoint;
    private String managementEndpoint;
    private String status;

    // Credential for creating storage
    private String login;
    private String access_key;
    private String secret_key;
    private String arn;
    private String pod;

    //=============================
    // Getters
    //=============================
    public String getId() { return id; }
    public String getType() { return type; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public String getCurrentVersion() { return currentVersion; }
    public String getPreferredVersion() { return preferredVersion; }
    public String getEndpoint() { return endpoint; }
    public String getManagementEndpoint() { return managementEndpoint; }
    public String getStatus() { return status; }
    public String getLogin() { return login; }
    public String getAccessKey() { return access_key; }
    public String getSecretKey() { return secret_key; }
    public String getArn() { return arn; }
    public String getPod() { return pod; }
    //=============================

    //=============================
    // Setters
    //=============================
    public void setId(String id) { this.id = id; }
    public void setType(String type) { this.type = type; }
    public void setName(String name) { this.name = name; }
    public void setLocation(String location) { this.location = location; }
    public void setCurrentVersion(String currentVersion) { this.currentVersion = currentVersion; }
    public void setPreferredVersion(String preferredVersion) { this.preferredVersion = preferredVersion; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
    public void setManagementEndpoint(String managementEndpoint) { this.managementEndpoint = managementEndpoint; }
    public void setStatus(String status) { this.status = status; }
    public void setLogin(String login) { this.login = login; }
    public void setAccessKey(String key) { this.access_key = key; }
    public void setSecretKey(String key) { this.secret_key = key; }
    public void setArn(String arn) { this.arn = arn; }
    public void setPod(String pod) { this.pod = pod; }
    //=============================
}
