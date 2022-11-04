//===================================================================
// MapEndpointss.java
// 	Description:
// 		These functions create HashMaps of endpoint name to 
// 		endpoint referencing between classes.
//
// 	Functions:
// 		- createNameObjectMap
//===================================================================

package com.socialvagrancy.vail.utils.map;

import com.socialvagrancy.vail.structures.Endpoint;
import java.util.HashMap;

public class MapEndpoints
{
	public static HashMap<String, Endpoint> createNameObjectMap(Endpoint[] endpoints)
	{
		HashMap<String, Endpoint> endpoint_map = new HashMap<String, Endpoint>();

		for(int i=0; i < endpoints.length; i++)
		{
			endpoint_map.put(endpoints[i].name(), endpoints[i]);
		}

		return endpoint_map;
	}
}
