//=============================================
// Storage.java
// Description:
//     Represents storage information including ID, name, type, etc.
//
// Variables:
// - id: Description of the ID.
// - name: Description of the name.
// - type: Description of the type.
// - forceDelete: Description of the forceDelete flag.
// - cloneRestore: Boolean. Are glacier restores cloned to another storage tier or served from cache.
// - cloudProvider: Description of the cloudProvider.
// - region: Description of the region.
// - endpoint: Description of the endpoint.
// - bucket: Description of the bucket.
// - link: Description of the linked Vail bucket.
// - cautionThreshold: Description of the cautionThreshold.
// - warningThreshold: Description of the warningThreshold.
// - status: Description of the status.
// - storageClass: Description of the storageClass.
// - target: What type of storage is being created. Options [ bp | bppolicy]
//          bp is tradational method of adding a bucket.
//          bppolicy is new and involves creating a Vail bucket on the BP with the chosen data policy.
//  - item: When specifying a bppolicy target, it's the data policy id to use when creating the bucket.
//  - recoverable: Boolean. Whether an additional packing list be included with the objects to allow
//          someone to be able to restore data from the tape without Vail.
//
// Created by Sean Snyder.
//=============================================

package com.spectralogic.vail.vapir.model;

public class Storage {
    private String access_key;
    private String id;
    private String name;
    private String type;
    private boolean forceDelete;
    private String cloudProvider;
    private String region;
    private String endpoint;
    private String bucket;
    private String link;
    private Integer cautionThreshold = null;
    private Integer warningThreshold = null;
    private String secret_key;
    private String status;
    private String storageClass;
    private String arn;
    private String podId;
    private String target;
    private String item;
    private boolean cloneRestore;
    private boolean recoverable;

    public Storage() {} // empty constructor to allow for copy constructor.
    
    // Copy Constructor
    public Storage(Storage location) {
        this.access_key = location.getAccessKey();
        this.id = location.getId();
        this.name = location.getName();
        this.type = location.getType();
        this.forceDelete = location.isForceDelete();
        this.cloudProvider = location.getCloudProvider();
        this.region = location.getRegion();
        this.endpoint = location.getEndpoint();
        this.bucket = location.getBucket();
        this.link = location.getLink();
        this.cautionThreshold = location.getCautionThreshold();
        this.warningThreshold = location.getWarningThreshold();
        this.secret_key = location.getSecretKey();
        this.status = location.getStatus();
        this.storageClass = location.getStorageClass();
        this.arn = location.getArn();
        this.podId = location.getPodId();
        this.target = location.getTarget();
        this.item = location.getItem();
        this.cloneRestore = location.isCloneRestore();
        this.recoverable = location.isRecoverable();
    }

    //===========================================
    // Getters
    //===========================================

    public String getAccessKey() { return access_key; }
    public String getArn() { return arn; }
    public String getBucket() { return bucket; }
    public Integer getCautionThreshold() { return cautionThreshold; }
    public String getCloudProvider() { return cloudProvider; }
    public String getEndpoint() { return endpoint; }
    public String getId() { return id; }
    public String getLink() { return link; }
    public String getName() { return name; }
    public String getPodId() { return podId; }
    public String getRegion() { return region; }
    public String getSecretKey() { return secret_key; }
    public String getStatus() { return status; }
    public String getStorageClass() { return storageClass; }
    public String getType() { return type; }
    public Integer getWarningThreshold() { return warningThreshold; }
    public boolean isForceDelete() { return forceDelete; }
    public String getTarget() { return target; }
    public String getItem() { return item; }
    public boolean isCloneRestore() { return cloneRestore; }
    public boolean isRecoverable() { return recoverable; }

    //===========================================
    // Setters
    //===========================================

    public void setAccessKey(String key) { this.access_key = key; }
    public void setArn(String arn) { this.arn = arn; }
    public void setBucket(String bucket) { this.bucket = bucket; }
    public void setCautionThreshold(Integer cautionThreshold) { this.cautionThreshold = cautionThreshold; }
    public void setCloudProvider(String cloudProvider) { this.cloudProvider = cloudProvider; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
    public void setForceDelete(boolean forceDelete) { this.forceDelete = forceDelete; }
    public void setId(String id) { this.id = id; }
    public void setLink(String link) { this.link = link; }
    public void setName(String name) { this.name = name; }
    public void setPodId(String podId) { this.podId = podId; }
    public void setRegion(String region) { this.region = region; }
    public void setSecretKey(String key) { this.secret_key = key; }
    public void setStatus(String status) { this.status = status; }
    public void setStorageClass(String storageClass) { this.storageClass = storageClass; }
    public void setType(String type) { this.type = type; }
    public void setWarningThreshold(Integer warningThreshold) { this.warningThreshold = warningThreshold; }
    public void setTarget(String target) { this.target = target; }
    public void setItem(String item) { this.item = item; }
    public void setCloneRestore(boolean cloneRestore) { this.cloneRestore = cloneRestore; }
    public void setRecoverable(boolean recoverable) { this.recoverable = recoverable; }
}
