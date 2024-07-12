//===================================================================
// Lifecycle.java
// Description:
//     Represents a lifecycle configuration for managing bucket objects.
//
// Variables:
//  - afterPut: [boolean]
//  - ignoreClass: [boolean] ignore the storage class specified by
//          the back-up software (e.g. if the software says glacier
//          the object still lands in standard and then follows the
//          lifecycle rule).
// Created by Sean Snyder.
//===================================================================

package com.spectralogic.vail.vapir.model;

import java.util.ArrayList;

public class Lifecycle {
    private String id;
    private String name;
    private String modified;
    // Setting markers to true to be in-line with sphere defaults.
    private boolean markers = true;
    private ArrayList<LifecycleRule> rules;
    private String description;
    // Setting uploads to 7 to be in-line with sphere defaults.
    private int uploads = 7;
    private boolean afterPut;
    private boolean ignoreClass;

    public Lifecycle() {
        rules = new ArrayList<LifecycleRule>();
    }

    // Copy Constructor
    public Lifecycle(Lifecycle lifecycle) {
        this.id = lifecycle.getId();
        this.name = lifecycle.getName();
        this.modified = lifecycle.getModified();
        this.markers = lifecycle.getMarkers();
        this.description = lifecycle.getDescription();
        this.uploads = lifecycle.getUploads();
        this.afterPut = lifecycle.isAfterPut();
        this.ignoreClass = lifecycle.isIgnoreClass();

        this.rules = new ArrayList<LifecycleRule>();
        LifecycleRule lr = null;

        for(LifecycleRule rule : lifecycle.getRules()) {
            lr = new LifecycleRule(rule);
            this.rules.add(lr);
        }
    }

    //=======================================
    // Getters
    //=======================================

    public String getId() { return id; }
    public String getModified() { return modified; }
    public String getName() { return name; }
    public int ruleCount() { return rules.size(); }
    public boolean getMarkers() { return markers; }
    public ArrayList<LifecycleRule> getRules() { return rules; }
    public LifecycleRule getRule(int r) { return rules.get(r); }
    public String getDescription() { return description; }
    public int getUploads() { return uploads; }
    public int storageCount(int r) { return rules.get(r).storageCount(); }
    public String storageID(int r, int s) { return rules.get(r).storageID(s); }
    public boolean isAfterPut() { return afterPut; }
    public boolean isIgnoreClass() { return ignoreClass; }

    //=======================================
    // Setters
    //=======================================

    public void addStorage(int rule, String storage_id) { rules.get(rule).addStorage(storage_id); }
    public void addLifecycleRule(LifecycleRule rule) { this.rules.add(rule); }
    public void clearDestinationCount(int r) { rules.get(r).clearDestinationCount(); }
    public void setId(String id) { this.id = id; }
    public void setMarkers(boolean markers) { this.markers = markers; }
    public void setModified(String modified) { this.modified = modified; }
    public void setName(String name) { this.name = name; }
    public void setRules(ArrayList<LifecycleRule> rules) { this.rules = rules; }
    public void setDescription(String description) { this.description = description; }
    public void setUploads(int uploads) { this.uploads = uploads; }
//    public void setStorage(int r, int s, String sid) { rules.get(r).setStorage(s, sid); }

    public void setTypeMarkers() {
        // Sets the deletion flag based on the rule type.
        // The structure of the json is what actually determines
        // the rule type, not the string type.
        for (int i = 0; i < rules.size(); i++) {
            if (rules.get(i).getType().equals("clone")) {
                rules.get(i).setDeletion(false);
            } else if (rules.get(i).getType().equals("move")) {
                rules.get(i).setDeletion(true);
            } else if (rules.get(i).getType().equals("expire")) {
                rules.get(i).setExpiration(true);
                rules.get(i).setDestinations(null);
            }
        }
    }

    public void setAfterPut(boolean afterPut) { this.afterPut = afterPut; }
    public void setIgnoreClass(boolean ignoreClass) { this.ignoreClass = ignoreClass; }
}
