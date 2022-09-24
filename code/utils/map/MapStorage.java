//===================================================================
// MapStorage.java
// 	Description:
// 		Creates a HashMap<String, String> of the storage
// 	
// 	Functions:
// 		- createIDNameMap
// 		- createNameIDMap
//===================================================================

package com.socialvagrancy.vail.utils.map;

import com.socialvagrancy.vail.structures.Storage;
import java.util.HashMap;

public class MapStorage
{
	public static HashMap<String, String> createIDNameMap(Storage[] locations)
	{
		HashMap<String, String> id_name_map = new HashMap<String, String>();

		for(int i=0; i < locations.length; i++)
		{
			id_name_map.put(locations[i].id, locations[i].name);
		}
		
		return id_name_map;
	}

	public static HashMap<String, String> createNameIDMap(Storage[] locations)
	{
		HashMap<String, String> name_id_map = new HashMap<String, String>();

		for(int i=0; i < locations.length; i++)
		{
			name_id_map.put(locations[i].name, locations[i].id);
		}

		return name_id_map;
	}
}
