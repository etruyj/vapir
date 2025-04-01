//===================================================================
// BucketDetails.java
//      Description:
//          This class models a bucket details report. The model 
//          holds information on the number of objects in the bucket,
//          the total size of the objects, and the location of the clones.
//
// Created by Sean Snyder
//===================================================================

package com.spectralogic.vail.vapir.model.report;

import java.util.HashMap;

public class BucketDetails {
    private String name; 
    private long objects;
    private long size; 
    private HashMap<String, CloneLocation> clones;

    //===========================================
    // Getters
    //===========================================
    public String getName() { return name; }
    public long getObjects() { return objects; }
    public long getSize() { return size; }
    public HashMap<String, CloneLocation> getClones() { return clones; }

    //===========================================
    // Setters
    //===========================================
    public void setName(String bucket) { this.name = bucket; }
    public void setObjects(long count) { this.objects = count; }
    public void setSize(long bytes) { this.size = bytes; }
    public void setClones(HashMap<String, CloneLocation> clone_map) { this.clones = clone_map; }
}
