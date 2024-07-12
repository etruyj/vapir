//===================================================================
// MapEndpointss.java
// 	Description:
// 		These functions create HashMaps of endpoint name to 
// 		endpoint referencing between classes.
//
// 	Functions:
// 		- createNameObjectMap
//===================================================================

package com.spectralogic.vail.vapir.util.map;

import com.spectralogic.vail.vapir.model.Endpoint;
import java.util.ArrayList;
import java.util.HashMap;

public class MapEndpoints
{
	public static HashMap<String, Endpoint> createNameObjectMap(Endpoint[] endpoints)
	{
		HashMap<String, Endpoint> endpoint_map = new HashMap<String, Endpoint>();

		for(int i=0; i < endpoints.length; i++)
		{
			endpoint_map.put(endpoints[i].getName(), endpoints[i]);
		}

		return endpoint_map;
	}

    public static HashMap<String, Endpoint> createIdObjectMap(ArrayList<Endpoint> endpoint_list) {
		HashMap<String, Endpoint> endpoint_map = new HashMap<String, Endpoint>();

		for(Endpoint endpoint : endpoint_list) {
			endpoint_map.put(endpoint.getId(), endpoint);
		}

		return endpoint_map;
    }

    public static HashMap<String, Endpoint> createNameObjectMap(ArrayList<Endpoint> endpoint_list) {
		HashMap<String, Endpoint> endpoint_map = new HashMap<String, Endpoint>();

		for(Endpoint endpoint : endpoint_list) {
			endpoint_map.put(endpoint.getName(), endpoint);
		}

		return endpoint_map;
    }
}
