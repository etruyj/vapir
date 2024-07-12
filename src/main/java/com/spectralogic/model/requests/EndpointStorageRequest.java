//===================================================================
// EndpointStorageRequest.java
// Description:
//     Holds the information necessary to add an endpoint to
//     a Vail Sphere.
//
// Variables:
// - accessKey: The access key for the endpoint.
// - bucket: The bucket associated with the endpoint.
// - cautionThreshold: The caution threshold for the endpoint.
// - link: The link associated with the endpoint.
// - name: The name of the endpoint.
// - secretKey: The secret key for the endpoint.
// - system: The system associated with the endpoint.
// - type: The type of the endpoint.
// - warningThreshold: The warning threshold for the endpoint.
//
// Created by Sean Snyder.
//===================================================================

package com.spectralogic.vail.vapir.model.requests;

public class EndpointStorageRequest {
    private String accessKey;
    private String bucket;
    private int cautionThreshold;
    private String item; 
    private String link;
    private String name;
    private String secretKey;
    private String system;
    private String target;
    private String type;
    private int warningThreshold;

    //=============================
    // Getters
    //=============================
    public String getAccessKey() { return accessKey; }
    public String getBucket() { return bucket; }
    public int getCautionThreshold() { return cautionThreshold; }
    public String getItem() { return item; }
    public String getLink() { return link; }
    public String getName() { return name; }
    public String getSecretKey() { return secretKey; }
    public String getSystem() { return system; }
    public String getTarget() { return target; }
    public String getType() { return type; }
    public int getWarningThreshold() { return warningThreshold; }

    //=============================
    // Setters
    //=============================
    public void setAccessKey(String accessKey) { this.accessKey = accessKey; }
    public void setBucket(String bucket) { this.bucket = bucket; }
    public void setCautionThreshold(int cautionThreshold) { this.cautionThreshold = cautionThreshold; }
    public void setItem(String item) { this.item = item; }
    public void setLink(String link) { this.link = link; }
    public void setName(String name) { this.name = name; }
    public void setSecretKey(String secretKey) { this.secretKey = secretKey; }
    public void setSystem(String system) { this.system = system; }
    public void setTarget(String target) { this.target = target; }
    public void setType(String type) { this.type = type; }
    public void setWarningThreshold(int warningThreshold) { this.warningThreshold = warningThreshold; }
}
