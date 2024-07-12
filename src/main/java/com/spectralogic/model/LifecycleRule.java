//===================================================================
// LifecycleRule.java
// Description:
//     Represents a lifecycle rule for managing bucket objects.
//
// Created by Sean Snyder.
//===================================================================

package com.spectralogic.vail.vapir.model;

import java.util.ArrayList;

public class LifecycleRule {
    // deletion and expiration are set to false to prevent
    // unexpected behavior (aka surprise deletions).
    private String name;
    private String apply;
    private String type;
    private boolean deletion = false;
    private Destination destinations;
    private boolean expiration = false;
    private Schedule schedule;

    public LifecycleRule() {
        destinations = new Destination();
        schedule = new Schedule();
    }
    
    @Deprecated // Storage[] was changed to an ArrayList<Storage> in v3.0.0
    public LifecycleRule(int location_count) {
        destinations = new Destination();
        destinations.setStorage(new ArrayList<String>());
        schedule = new Schedule();
    }

    // Copy constructor
    public LifecycleRule(LifecycleRule rule) { 
        this.name = rule.getName();
        this.apply = rule.getApply();
        this.type = rule.getType();
        this.deletion = rule.getDeletion();
        this.expiration = rule.getExpiration();
        
        this.destinations = new Destination();
        this.destinations.setStorage(rule.getDestinations().getStorage());

        this.schedule = new Schedule();
        this.schedule.setDays(rule.getSchedule().getDays()); 
    }

    //===========================================
    // Builders
    //===========================================
    
    public LifecycleRule defaultSettings() {
        this.setType("move");
        this.destinations = new Destination();
        this.schedule = new Schedule();
        return this;
    }

    //==============================================
    // GETTER FUNCTIONS
    //==============================================

    public String getName() { return name; }
    public String getApply() { return apply; }
    public String getType() { return type; }
    public boolean getDeletion() { return deletion; }
    public Destination getDestination() { return destinations; }
    public boolean getExpiration() { return expiration; }
    public Schedule getSchedule() { return schedule; }

    public Destination getDestinations() { return destinations; }
    public int storageCount() { return destinations.getStorage().size(); }
    public String storageID(int s) { return destinations.getStorage().get(s); }

    //=============================================
    // Setter Functions
    //=============================================

    public void addStorage(String sid) { destinations.addStorage(sid); }
    public void clearDestinationCount() { destinations.setCount(null); }
    public void setName(String name) { this.name = name; }
    public void setApply(String apply) { this.apply = apply; }
    public void setType(String type) { this.type = type; }
    public void setDeletion(boolean d) { deletion = d; }
    public void setDestinations(Destination destinations) { this.destinations = destinations; }
    public void setDestinationCount(int count) { destinations.setCount(count); }
    public void setExpiration(boolean e) { expiration = e; }
    public void setSchedule(Schedule schedule) { this.schedule = schedule; }
    public void setSchedule(int days) { this.schedule.setDays(days); }
    public void setStorage(int s, String sid) { destinations.getStorage().set(s, sid); }

    //==============================================
    // INTERNAL CLASSES
    //==============================================

    public class Destination {
        private Integer count;
        private ArrayList<String> storage;

        public Destination() {
            this.storage = new ArrayList<String>();
        }

        //=======================================
        // Getters
        //=======================================

        public ArrayList<String> getStorage() { return storage; }

        //=======================================
        // Setters
        //=======================================

        public void addStorage(String location) { this.storage.add(location); }
        public void setStorage(ArrayList<String> storage) { 
            if(storage != null) {
                for(String location : storage) {
                    this.storage.add(location);
                }
            } else {
                this.storage.clear(); 
            }
        }
        public void setStorage(int index, String storage_id) { storage.set(index, storage_id); }
        public void setCount(Integer count) { this.count = count; }
    }

    public class Schedule {
        private int days;

        //=======================================
        // Getters
        //=======================================
        public int getDays() { return days; }
        
        //=======================================
        // Setters
        //=======================================

        public void setDays(int days) { this.days = days; }
    }
}
