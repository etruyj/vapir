//===================================================================
// BucketObjects.java
//      Description:
//          This class models the response returned from Vail's 
//          sl/api/buckets/:bucketId/objects API call.
//
// Created by Sean Snyder
//===================================================================

package com.spectralogic.vail.vapir.model;

import java.util.List;

public class BucketObjects {
    private String name;
    private String nextMarker;
    private String marker;
    private Integer maxKeys;
    private List<Object> contents;

    //===========================================
    // Getters
    //===========================================
    public String getName() { return name; }
    public String getMarker() { return marker; }
    public String getNextMarker() { return nextMarker; }
    public Integer getMaxKeys() { return maxKeys; }
    public List<Object> getContents() { return contents; }

    //===========================================
    // Setters
    //===========================================
    public void setName(String name) { this.name = name; }
    public void setMarker(String key) { this.marker = key; }
    public void setNextMarker(String key) { this.nextMarker = key; }
    public void setMaxKeys(Integer maxKeys) { this.maxKeys = maxKeys; }
    public void setContents(List<Object> objects) { this.contents = objects; }
}
