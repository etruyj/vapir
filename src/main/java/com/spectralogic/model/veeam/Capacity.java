//===================================================================
// Capacity.java
// Description:
//     This class holds the values for the Capacity XML doc. The 
//     variables and classes are structured to resemble the desired
//     output to allow them to be output directly to an XML doc. The
//     structure of the output is dictated by Veeam's SOSAPI doc.
//
// Created by Sean Snyder
//===================================================================

package com.spectralogic.vail.vapir.model.veeam;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="CapacityInfo")
public class Capacity {
    private String Capacity;
    private String Available;
    private String Used;
        
    //=======================================
    // Getters
    //=======================================

    @XmlElement(name="Capacity")
    public String getCapacity() { return Capacity; }
    @XmlElement(name="Available")
    public String getAvailable() { return Available; }
    @XmlElement(name="Used")
    public String getUsed() { return Used; }

    //=======================================
    // Setters
    //=======================================

    public void setCapacity(String Capacity) { this.Capacity = Capacity; }
    public void setAvailable(String Available) { this.Available = Available; }
    public void setUsed(String Used) { this.Used = Used; }
}
