//====================================================================
// Object.java
//      Description
//          This class represents an object with metadata such as key, version ID,
//          last modified timestamp, ETag, size, owner information, and storage class.
//
// Created by Sean Syder
//====================================================================

package com.spectralogic.vail.vapir.model;

public class Object {
    private String key;
    private String versionId;
    private String lastModified;
    private Boolean isLatest;
    private String etag;
    private long size;
    private String ownerId;
    private String ownerName;
    private String storageClass;

    public Object() {}
    public Object(Object other) {this.key=other.key;this.versionId=other.versionId;this.lastModified=other.lastModified;this.isLatest=other.isLatest;this.etag=other.etag;this.size=other.size;this.ownerId=other.ownerId;this.ownerName=other.ownerName;this.storageClass=other.storageClass;}

    //===========================================    
    // Getters
    //===========================================    
    public String getKey() {return key;}
    public String getVersionId() {return versionId;}
    public String getLastModified() {return lastModified;}
    public Boolean isLatest() {return isLatest;}
    public String getEtag() {return etag;}
    public long getSize() {return size;}
    public String getOwnerId() {return ownerId;}
    public String getOwnerName() {return ownerName;}
    public String getStorageClass() {return storageClass;}

    //===========================================    
    // Setters
    //===========================================    
    public void setKey(String key) {this.key = key;}
    public void setVersionId(String versionId) {this.versionId = versionId;}
    public void setLastModified(String lastModified) {this.lastModified = lastModified;}
    public void setLatest(Boolean isLatest) {this.isLatest = isLatest;}
    public void setEtag(String etag) {this.etag = etag;}
    public void setSize(long size) {this.size = size;}
    public void setOwnerId(String ownerId) {this.ownerId = ownerId;}
    public void setOwnerName(String ownerName) {this.ownerName = ownerName;}
    public void setStorageClass(String storageClass) {this.storageClass = storageClass;}
}

