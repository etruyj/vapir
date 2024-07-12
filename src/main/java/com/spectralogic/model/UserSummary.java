//===================================================================
// UserSummary.java
// Description:
//     Represents a summary of user information.
//
// Created by Sean Snyder.
//===================================================================

package com.spectralogic.vail.vapir.model;

import java.util.ArrayList;

public class UserSummary extends Summary {
    private ArrayList<String> groups;

    //=======================================
    // Getters
    //=======================================

    public ArrayList<String> getGroups() {
        return groups;
    }

    //=======================================
    // Setters
    //=======================================

    public void setGroups(ArrayList<String> groups) {
        this.groups = groups;
    }

    public void addGroup(String group) {
        if (groups == null) {
            groups = new ArrayList<String>();
        }
        groups.add(group);
    }

    public int groupCount() {
        return groups == null ? 0 : groups.size();
    }

    public String group(int i) {
        return groups.get(i);
    }
}
