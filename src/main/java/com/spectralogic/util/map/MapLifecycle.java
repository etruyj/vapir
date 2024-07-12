//===================================================================
// MapLifeCycle.java
// 	Description:
// 		Creates a map of Lifecycle to its id to be used with
// 		converting id to a human-readable value and to 
// 		convert a human-readable value to a usuable name.
// 	
// 	Functions:
// 		- createNameIDMap
// 		- createIDNameMap
//===================================================================

package com.spectralogic.vail.vapir.util.map;

import com.spectralogic.vail.vapir.model.Lifecycle;
import java.util.HashMap;

public class MapLifecycle
{
	public static HashMap<String, String> createNameIdMap(Lifecycle[] lifecycles)
	{
		HashMap<String, String> name_id_map = new HashMap<String, String>();

		for(int i=0; i < lifecycles.length; i++)
		{
			name_id_map.put(lifecycles[i].getName(), lifecycles[i].getId());
		}

		return name_id_map;
	}

	public static HashMap<String, String> createIdNameMap(Lifecycle[] lifecycles)
	{
		HashMap<String, String> id_lifecycle_map = new HashMap<String, String>();

		for(int i=0; i < lifecycles.length; i++)
		{
			id_lifecycle_map.put(lifecycles[i].getId(), lifecycles[i].getName());
		}

		return id_lifecycle_map;
	}
	
    @Deprecated // changed function name.
    public static HashMap<String, String> createNameIDMap(Lifecycle[] lifecycles)
	{
		return createNameIdMap(lifecycles);
	}

    @Deprecated // checked function name
	public static HashMap<String, String> createIDNameMap(Lifecycle[] lifecycles)
	{
	    return createIdNameMap(lifecycles);
    }

	
}
