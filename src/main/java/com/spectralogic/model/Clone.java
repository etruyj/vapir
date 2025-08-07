//===================================================================
// Clone.java
//      Description:
//          This class models the information returned in the vail
//          /buckets/:bucketname/clone?object=name API call.
// 
// Created by Sean Snyder
//===================================================================

package com.spectralogic.vail.vapir.model;

import java.util.List;

public class Clone {
    private List<CloneInfo> storage;

    //===========================================
    // Getters
    //===========================================
    public List<CloneInfo> getStorage() { return storage; }

    //===========================================
    // Setters
    //===========================================
    public void setStorage(List<CloneInfo> storage) { this.storage = storage; }

    //===========================================
    // Inner Classes
    //===========================================
    public class CloneInfo {
        private String id;
        private Boolean archived;

        //=======================================
        // Getters
        //=======================================
        public String getId() { return id; }
        public Boolean isArchived() { return archived; }

        //=======================================
        // Setters
        //=======================================
        public void setId(String id) { this.id = id; }
        public void setArchived(Boolean archived) { this.archived = archived; }
    }
}
