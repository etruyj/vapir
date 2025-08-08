//===================================================================
// VapirConfigModel.java
//      Description:
//          This class models the configuration parameters for the 
//          vapir script.
//
// Created by Sean Snyder
//===================================================================

package com.spectralogic.vail.vapir.model;

import java.util.Map;

public class VapirConfigModel {
    private String apiPath; // path to the api.yaml
    private Map<String, String> apiMap; // map of commands to API calls.

    //===========================================
    // Getters
    //===========================================
    public String getApiPath() { return apiPath; }
    public Map<String, String> getApiMap() { return apiMap; }

    //===========================================
    // Setters
    //===========================================
    public void setApiPath(String path) { this.apiPath = path; }
    public void setApiMap(Map<String, String> cmds) { this.apiMap = cmds; }
}
