//===================================================================
// MapDataPolicies.java
//      Description:
//          This class provides the functionality to create maps of
//          identifiers to data policy information.
//
// Created by Sean Snyder
// Copyright Spectra Logic 2024
//===================================================================

package com.spectralogic.vail.vapir.util.map;

import com.spectralogic.vail.vapir.model.blackpearl.BpDataPolicy;

import java.util.ArrayList;
import java.util.HashMap;

public class MapDataPolicies {
    public static HashMap<String, String> createNameIdMap(ArrayList<BpDataPolicy> dp_list) {
        HashMap<String, String> dp_map = new HashMap<String, String>();
        for(BpDataPolicy policy : dp_list) {
            dp_map.put(policy.getName(), policy.getId());
        }

        return dp_map;
    }
}
