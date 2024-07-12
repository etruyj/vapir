//===================================================================
// AddStorage.java
// 	    Description: 
//          Commands associated with adding Storage to a Vail sphere.
//
// Created by Sean Snyder
//===================================================================

package com.spectralogic.vail.vapir.command;

import com.spectralogic.vail.vapir.api.VailConnector;
import com.spectralogic.vail.vapir.model.Storage;

import java.util.HashMap;

public class AddStorage {
	public static Storage addAWSWithARN(VailConnector sphere, String ip_address, String arn, String bucket, String cloud_provider, String linked_bucket, String storage_name, String podID, String region, String storageClass, String type) throws Exception
	{
		Storage location = new Storage();

		location.setArn(arn);
		location.setBucket(bucket);
		location.setCloudProvider(cloud_provider);
		location.setLink(linked_bucket);
		location.setName(storage_name);
		location.setPodId(podID);
		location.setRegion(region);
		location.setStorageClass(storageClass);
		location.setType(type);

		location = sphere.addStorage(ip_address, location);
	
		return location;
	}
	
	public static Storage addAWSWithAccessKey(VailConnector sphere, String ip_address, String access_key, String secret_key, String bucket, String cloud_provider, String linked_bucket, String storage_name, String podID, String region, String storageClass, String type) throws Exception
	{
		Storage location = new Storage();

		location.setAccessKey(access_key);
		location.setSecretKey(secret_key);
		location.setBucket(bucket);
		location.setCloudProvider(cloud_provider);
		location.setLink(linked_bucket);
		location.setName(storage_name);
		location.setPodId(podID);
		location.setRegion(region);
		location.setStorageClass(storageClass);
		location.setType(type);

		location = sphere.addStorage(ip_address, location);
	
		return location;
	}

	public static Storage addCloudOther(VailConnector sphere, String ip_address, String endpoint, String access_key, String secret_key, String bucket, String cloud_provider, String linked_bucket, String storage_name, String podID, String region, String storageClass, String type) throws Exception
	{
		Storage location = new Storage();

		location.setEndpoint(endpoint);
		location.setAccessKey(access_key);
		location.setSecretKey(secret_key);
		location.setBucket(bucket);
		location.setCloudProvider(cloud_provider);
		location.setLink(linked_bucket);
		location.setName(storage_name);
		location.setPodId(podID);
		location.setRegion(region);
		location.setStorageClass(storageClass);
		location.setType(type);

		location = sphere.addStorage(ip_address, location);

		return location;
	}

    public static Storage createWithDataPolicy(Storage location, VailConnector sphere) throws Exception {
        location = sphere.createStorage(location);

        return location;
    }

	public static HashMap<String, String> map(Storage[] locations)
	{
		HashMap<String, String> storage_map = new HashMap<String, String>();

		for(int i=0; i<locations.length; i++)
		{
			storage_map.put(locations[i].getName(), locations[i].getId());
		}

		return storage_map;
	}

}

