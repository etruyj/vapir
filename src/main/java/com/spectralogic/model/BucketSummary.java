//===================================================================
// BucketSummary.java
// Description:
//     Represents a summary of a bucket.
//===================================================================

package com.spectralogic.vail.vapir.model;

public class BucketSummary extends Summary {
    private String lifecycle;

    //=============================
    // Getters
    //=============================
    public String getLifecycle() { return lifecycle; }
    //=============================

    //=============================
    // Setters
    //=============================
    public void setLifecycle(String lifecycle) { this.lifecycle = lifecycle; }
    //=============================
}
