//===================================================================
// CapacitySummary.java
//      Description:
//          This class provides the variables returned by the 
//          sl/api/capacity/sphere/summary API call.
//===================================================================

package com.spectralogic.vail.vapir.model;

import java.math.BigInteger;

public class CapacitySummary {
    //===========================================
    // Getters
    //===========================================
    public String storageClass() { return storageClass; }
    public BigInteger used() { return used; }
    public BigInteger data() { return data; }
    public BigInteger total() { return total; }

    //===========================================
    // Setters
    //===========================================
    public void setStorageClass(String sClass) { storageClass = sClass; }
    public void setUsed(String used_capacity) { used = new BigInteger(used_capacity); }
    public void setData(String data_size) { data = new BigInteger(data_size); }
    public void setTotal(String total_size) { total = new BigInteger(total_size); } 


    //===========================================
    // Variables
    //===========================================
    private String storageClass;
    private BigInteger used;
    private BigInteger data;
    private BigInteger total;
}
