//===================================================================
// Lifecycles.java
// 	Description: This class holds all the functions related to
// 	lifecycles.
//===================================================================

package com.socialvagrancy.vail.commands.sub;

import com.socialvagrancy.vail.commands.BasicCommands;
import com.socialvagrancy.vail.structures.Lifecycle;
import com.socialvagrancy.vail.structures.LifecycleRule;
import com.socialvagrancy.utils.io.Logger;

import com.google.gson.Gson;

import java.util.HashMap;

public class Lifecycles
{
	public static LifecycleRule[] buildRule(String name, String type, String apply, int destination_count, String[] storage_locations, int days, LifecycleRule[] existing)
	{
		LifecycleRule[] new_rule = new LifecycleRule[existing.length+1];
	       	
		for(int i=0; i<existing.length; i++)
		{
			new_rule[i].name = existing[i].name;
			new_rule[i].apply = existing[i].apply;
			new_rule[i].type = existing[i].type;
			new_rule[i].destinations = existing[i].destinations;
			new_rule[i].schedule = existing[i].schedule;
		}

		int last = existing.length; // no need to modify as the actual last index is existing.length - 1
		new_rule[last] = new LifecycleRule(storage_locations.length);	
		new_rule[last].name = name;
		new_rule[last].apply = apply;
		new_rule[last].type = type;
		new_rule[last].destinations.count = destination_count;
		
		for(int j=0; j<storage_locations.length; j++)
		{
			new_rule[last].destinations.storage[j] = storage_locations[j];
		}

		new_rule[last].schedule.days = days;

		return new_rule;	
	}
	
	public static Lifecycle create(BasicCommands sphere, String ip_address, String name, String description, boolean markers, int uploads, LifecycleRule[] rules, Logger logbook)
	{
		logbook.logWithSizedLogRotation("Creating lifecycle rule [" + name + "] from inputs...", 1);
		logbook.logWithSizedLogRotation("Formatting json body...", 1);
		Lifecycle lifecycle = populate(name, description, markers, uploads, rules);
		String body = formatJson(lifecycle);

		// Using same var to catch response
		lifecycle = sphere.createLifecycle(ip_address, name, body);

		return lifecycle;
		
	}

	public static String formatJson(Lifecycle rule)
	{
		Gson gson = new Gson();

		return gson.toJson(rule, Lifecycle.class);
	}

	public static HashMap<String, String> map(Lifecycle[] lifecycles)
	{
		HashMap<String, String> map = new HashMap<String, String>();

		for(int i=0; i<lifecycles.length; i++)
		{
			map.put(lifecycles[i].name, lifecycles[i].id);
		}

		return map;
	}

	public static Lifecycle populate(String name, String description, boolean markers, int uploads, LifecycleRule[] rules)
	{
		Lifecycle rule = new Lifecycle();

		rule.name = name;
		rule.description = description;
		rule.markers = markers;
		rule.uploads = uploads;
		rule.rules = rules;

		return rule;
	}

}
