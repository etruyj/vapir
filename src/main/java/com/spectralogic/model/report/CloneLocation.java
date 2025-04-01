//===================================================================
// CloneLocation
//      Description:
//          This class holds the information on clones in a specific
//          storage location.
//===================================================================
package com.spectralogic.vail.vapir.model.report;

import java.util.HashMap;

public class CloneLocation {
    private long clones;
    private long size;

    //=======================================
    // Getters
    //=======================================
    public long getClones() { return clones; }
    public long getSize() { return size; }

    //=======================================
    // Setters
    //=======================================
    public void setClones(long count) { this.clones = count; }
    public void setSize(long bytes) { this.size = bytes; }
}
