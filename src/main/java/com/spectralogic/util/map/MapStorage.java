//===================================================================
// MapStorage.java
// 	Description:
// 		Creates a HashMap<String, String> of the storage
// 	
// 	Functions:
// 		- createIDNameMap
// 		- createNameIDMap
//===================================================================

package com.spectralogic.vail.vapir.util.map;

import com.spectralogic.vail.vapir.model.Storage;
import java.util.ArrayList;
import java.util.HashMap;

public class MapStorage
{
    @Deprecated // renamed function
	public static HashMap<String, String> createIDNameMap(Storage[] locations)
	{
	    ArrayList<Storage> storage_list = new ArrayList<Storage>();
        
        for(int i=0; i<locations.length; i++) {
            storage_list.add(locations[i]);
        }

        return createIdNameMap(storage_list);
    }

	public static HashMap<String, String> createIdNameMap(ArrayList<Storage> locations)
	{
		HashMap<String, String> id_name_map = new HashMap<String, String>();

		for(Storage location : locations) {
			id_name_map.put(location.getId(), location.getName());
		}
		
		return id_name_map;
	}

	public static HashMap<String, String> createNameIdMap(Storage[] locations)
	{
		HashMap<String, String> name_id_map = new HashMap<String, String>();

		for(int i=0; i < locations.length; i++)
		{
			name_id_map.put(locations[i].getName(), locations[i].getId());
		}

		return name_id_map;
	}

	@Deprecated // renamed function
    public static HashMap<String, String> createNameIDMap(Storage[] locations)
	{
	    return createNameIdMap(locations);
    }

    
}
