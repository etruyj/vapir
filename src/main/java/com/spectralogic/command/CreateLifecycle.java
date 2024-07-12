//===================================================================
// CreateLifecycle.java
// 	    Description: 
//          This class holds all the functions related to
// 	        lifecycles.
//
// Created by Sean Snyder
//===================================================================

package com.spectralogic.vail.vapir.command;

import com.spectralogic.vail.vapir.api.VailConnector;
import com.spectralogic.vail.vapir.model.Lifecycle;
import com.spectralogic.vail.vapir.model.LifecycleRule;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateLifecycle {
    private static final Logger log = LoggerFactory.getLogger(CreateLifecycle.class);

	public static ArrayList<LifecycleRule> buildRule(String name, String type, String apply, int destination_count, String[] storage_locations, int days, ArrayList<LifecycleRule> existing)
	{
	    log.info("Adding rule [" + name + "] to lifecycle.");

        /* Commenting out to convert this to an array list.
           Delete if all works well.
        log.info("There are (" + existing.length + ") rules already on the lifecycle.");

        LifecycleRule new_rule = new LifecycleRule[existing.length+1];
	       	
		for(int i=0; i<existing.length; i++)
		{
			new_rule[i].setName(existing[i].getName());
			new_rule[i].setApply(existing[i].getApply());
			new_rule[i].setType(existing[i].getType());
			new_rule[i].setDestinations(existing[i].getDestinations());
			new_rule[i].setSchedule(existing[i].getSchedule());
		}

		int last = existing.length; // no need to modify as the actual last index is existing.length - 1
		new_rule[last] = new LifecycleRule(storage_locations.length);	
		new_rule[last].setName(name);
		new_rule[last].setApply(apply);
		new_rule[last].setType(type);
		new_rule[last].setDestinationCount(destination_count);
		
		for(int j=0; j<storage_locations.length; j++)
		{
			new_rule[last].getDestinations().setStorage(j, storage_locations[j]);
		}

		new_rule[last].getSchedule().setDays(days);

		return new_rule;	
        */

        // New code
        log.info("There are (" + existing.size() + ") rules already on the lifecycle");
        LifecycleRule new_rule = new LifecycleRule();
        
        new_rule.setName(name);
        new_rule.setApply(apply);
        new_rule.setType(type);
        new_rule.setDestinationCount(destination_count);

        for(int j=0; j<storage_locations.length; j++) {
            new_rule.getDestinations().setStorage(j, storage_locations[j]);
        }

        existing.add(new_rule);

        return existing;

	}
	
	public static Lifecycle create(VailConnector sphere, String ip_address, String name, String description, boolean markers, int uploads, ArrayList<LifecycleRule> rules) {
		log.info("Creating lifecycle [" + name + "]");

        Lifecycle lifecycle = populate(name, description, markers, uploads, rules);
		// Using same var to catch response

        try {
		    lifecycle = sphere.createLifecycle(ip_address, lifecycle);
            log.info("Successfully created lifecycle [" + name + "]");
        } catch(Exception e) {
            log.error(e.getMessage());
            log.error("Failed to create error message.");
            return null;
        }

		return lifecycle;
		
	}

    @Deprecated // should be using util.map.MapLifecycle
	public static HashMap<String, String> map(Lifecycle[] lifecycles)
	{
        log.info("Creating name:id map for lifecycles.");
		HashMap<String, String> map = new HashMap<String, String>();

		for(int i=0; i<lifecycles.length; i++)
		{
			map.put(lifecycles[i].getName(), lifecycles[i].getId());
		}

		return map;
	}

    // Populate creates a Lifecycle object out of the 
    // specific fields.
	public static Lifecycle populate(String name, String description, boolean markers, int uploads, ArrayList<LifecycleRule> rules) {
		log.info("Creating lifecycle object [" + name + "]");

        Lifecycle rule = new Lifecycle();

		rule.setName(name);
		rule.setDescription(description);
		rule.setMarkers(markers);
		rule.setUploads(uploads);
		rule.setRules(rules);

		return rule;
	}

}
