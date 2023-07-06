package com.socialvagrancy.vail.structures;

public class LifecycleRule
{
	// deletion and expiration are set to false to prevent
	// unexpected behavior (aka surprise deletions).
	public String name;
	public String apply;
	public String type;
	public boolean deletion = false;
	public Destination destinations;
	public boolean expiration = false;
	public Schedule schedule;

	public LifecycleRule(int location_count)
	{
		destinations = new Destination();
		destinations.storage = new String[location_count];
		schedule = new Schedule();
	}

	//==============================================
	// GETTER FUNCTIONS
	//==============================================

	public int storageCount() { return destinations.storage.length; }	
	public String storageID(int s) { return destinations.storage[s]; }
	public String type() { return type; }

	//=============================================
	// Setter Functions
	//=============================================

	public void clearDestinationCount() { destinations.count = null; }
	public void setDeletion(boolean d) { deletion = d; }
	public void setExpiration(boolean e) { expiration = e; }
	public void setStorage(int s, String sid) { destinations.storage[s] = sid; }


	//==============================================
	// INTERNAL CLASSES
	//==============================================
	
	public class Destination
	{
		public Integer count;
		public String[] storage;
	}

	public class Schedule
	{
		public int days;
	}
}
