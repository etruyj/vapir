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

package com.socialvagrancy.vail.utils.map;

import com.socialvagrancy.vail.structures.Lifecycle;
import java.util.HashMap;

public class MapLifecycle
{
	public static HashMap<String, String> createNameIDMap(Lifecycle[] lifecycles)
	{
		HashMap<String, String> name_id_map = new HashMap<String, String>();

		for(int i=0; i < lifecycles.length; i++)
		{
			name_id_map.put(lifecycles[i].name, lifecycles[i].id);
		}

		return name_id_map;
	}

	public static HashMap<String, String> createIDNameMap(Lifecycle[] lifecycles)
	{
		HashMap<String, String> id_lifecycle_map = new HashMap<String, String>();

		for(int i=0; i < lifecycles.length; i++)
		{
			id_lifecycle_map.put(lifecycles[i].id, lifecycles[i].name);
		}

		return id_lifecycle_map;
	}

	
}
