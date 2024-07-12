//===================================================================
// SystemInfo.java
// Description:
//     This holds the values for the system.xml file required by
//     Veeam's SOSAPI.
//
// Created by Sean Snyder.
//===================================================================

package com.spectralogic.vail.vapir.model.veeam;

import com.spectralogic.vail.vapir.util.xml.XmlQuotedStringAdapter;

import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="SystemInfo")
public class SystemInfo {
    public SystemInfo() {
        ProtocolVersion = "1.0";
        ModelName = "Spectra Logic Corporation";
        ProtocolCapabilities = new Capabilities();
    }

    //===========================================
    // Getters
    //===========================================
    
    public String protocolVersion() { return ProtocolVersion; }
    public String ModelName() { return ModelName; }
    public boolean EnableOnPremArchiveTier() { return ProtocolCapabilities.EnableOnPremArchiveTier; }
    public boolean monitorCapacityInfo() { return ProtocolCapabilities.capacityInfo(); }
    public boolean monitorUploadSessions() { return ProtocolCapabilities.uploadSessions(); }

    //===========================================
    // Settors
    //===========================================

    public void setProtocolVersion(String version) { ProtocolVersion = version; }
    public void setModelName(String model) { ModelName = model; }
    public void setEnableOnPremArchiveTier(boolean archive) { ProtocolCapabilities.setEnableOnPremArchiveTier(archive); }
    public void setCapacityInfo(boolean b) { ProtocolCapabilities.setCapacityInfo(b); }
    public void setUploadSessions(boolean b) { ProtocolCapabilities.setUploadSessions(b); }

    //===========================================
    // Variables
    //===========================================

    @XmlJavaTypeAdapter(XmlQuotedStringAdapter.class)
    public String ProtocolVersion;
    @XmlJavaTypeAdapter(XmlQuotedStringAdapter.class)
    public String ModelName;
    public Capabilities ProtocolCapabilities;    
}
