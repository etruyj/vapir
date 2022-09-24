package com.socialvagrancy.vail.structures;

public class LifecycleRule
{
	public String name;
	public String apply;
	public String type;
	public Destination destinations;
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

	//=============================================
	// Setter Functions
	//=============================================

	public void clearDestinationCount() { destinations.count = null; }
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
