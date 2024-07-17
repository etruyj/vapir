//===================================================================
// MapGroups.java
//      Description:
//          This class creates maps of groups.
//
// Created by Sean Snyder
// Copyright Spectra Logic 2024
//===================================================================

package com.spectralogic.vail.vapir.util.map;

import com.spectralogic.vail.vapir.model.Group;

import java.util.HashMap;

public class MapGroups {
    public static HashMap<String, String> createNameArnMap(Group[] groups) {
        HashMap<String, String> name_arn_map = new HashMap<String, String>();
        String arn_prefix = "arn:aws:iam::";
        String arn;

        for(Group group : groups) {
            arn = arn_prefix + group.getAccountId() + ":group/" + group.getName();
            name_arn_map.put(group.getName(), arn);
        }

        return name_arn_map;
    }
}
