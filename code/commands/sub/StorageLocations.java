//===================================================================
// StorageLocations
// 	Description: Houses all the code associate with storage in
// 	Vail.
//===================================================================

package com.socialvagrancy.vail.commands.sub;

import com.socialvagrancy.vail.commands.BasicCommands;
import com.socialvagrancy.vail.structures.Storage;
import com.socialvagrancy.utils.Logger;

import com.google.gson.Gson;

import java.util.HashMap;

public class StorageLocations
{
	public static Storage addAWSWithARN(BasicCommands sphere, String ip_address, String arn, String bucket, String cloud_provider, String linked_bucket, String storage_name, String podID, String region, String storageClass, String type)
	{
		Gson gson = new Gson();

		Storage location = new Storage();

		location.arn = arn;
		location.bucket = bucket;
		location.cloudProvider = cloud_provider;
		location.link = linked_bucket;
		location.name = storage_name;
		location.podId = podID;
		location.region = region;
		location.storageClass = storageClass;
		location.type = type;

		String json_body = gson.toJson(location, Storage.class);

		location = sphere.addStorage(ip_address, storage_name, json_body);
	
		return location;
	}
	
	public static Storage addAWSWithAccessKey(BasicCommands sphere, String ip_address, String access_key, String secret_key, String bucket, String cloud_provider, String linked_bucket, String storage_name, String podID, String region, String storageClass, String type)
	{
		Gson gson = new Gson();

		Storage location = new Storage();

		location.accessKey = access_key;
		location.secretKey = secret_key;
		location.bucket = bucket;
		location.cloudProvider = cloud_provider;
		location.link = linked_bucket;
		location.name = storage_name;
		location.podId = podID;
		location.region = region;
		location.storageClass = storageClass;
		location.type = type;

		String json_body = gson.toJson(location, Storage.class);

		location = sphere.addStorage(ip_address, storage_name, json_body);
	
		return location;
	}

	public static Storage addCloudOther(BasicCommands sphere, String ip_address, String endpoint, String access_key, String secret_key, String bucket, String cloud_provider, String linked_bucket, String storage_name, String podID, String region, String storageClass, String type)
	{
		Gson gson = new Gson();

		Storage location = new Storage();

		location.endpoint = endpoint;
		location.accessKey = access_key;
		location.secretKey = secret_key;
		location.bucket = bucket;
		location.cloudProvider = cloud_provider;
		location.link = linked_bucket;
		location.name = storage_name;
		location.podId = podID;
		location.region = region;
		location.storageClass = storageClass;
		location.type = type;

		String json_body = gson.toJson(location, Storage.class);

		location = sphere.addStorage(ip_address, storage_name, json_body);

		return location;
	}

	public static HashMap<String, String> map(Storage[] locations)
	{
		HashMap<String, String> storage_map = new HashMap<String, String>();

		for(int i=0; i<locations.length; i++)
		{
			storage_map.put(locations[i].name, locations[i].id);
		}

		return storage_map;
	}

}
