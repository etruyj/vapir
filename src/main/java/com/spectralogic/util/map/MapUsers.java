//===================================================================
// MapUsers.java
//      Description:
//          This class creates maps of users.
//
// Created by Sean Snyder
// Copyright Spectra Logic 2024
//===================================================================

package com.spectralogic.vail.vapir.util.map;

import com.spectralogic.vail.vapir.model.User;

import java.util.HashMap;

public class MapUsers {
    public static HashMap<String, String> createNameArnMap(User[] users) {
        HashMap<String, String> name_arn_map = new HashMap<String, String>();
        String arn_prefix = "arn:aws:iam::";
        String arn;

        for(User user : users) {
            arn = arn_prefix + user.getAccountId() + ":user/" + user.getUsername();
            name_arn_map.put(user.getUsername(), arn);
        }

        return name_arn_map;
    }
}
