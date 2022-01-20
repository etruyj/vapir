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
	

	//==============================================
	// INTERNAL CLASSES
	//==============================================
	
	public class Destination
	{
		public int count;
		public String[] storage;
	}

	public class Schedule
	{
		public int days;
	}
}
