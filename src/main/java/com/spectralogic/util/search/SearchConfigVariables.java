//===================================================================
// SearchConfigVariables.java
//      Description:
//          This class searches a Sphere configuration object for 
//          potential variables in the config and replaces them with
//          the desired value.
//
//      Variables:
//          this-hostname: hostname of the Vail node with this ip
//          this-endpoint-id: endpoint id of this Vail node
//
// Created by Sean Snyder
//===================================================================

package com.spectralogic.vail.vapir.util.search;

import com.spectralogic.vail.vapir.api.VailConnector;
import com.spectralogic.vail.vapir.model.Bucket;
import com.spectralogic.vail.vapir.model.Endpoint;
import com.spectralogic.vail.vapir.model.Lifecycle;
import com.spectralogic.vail.vapir.model.LifecycleRule;
import com.spectralogic.vail.vapir.model.SphereConfig;
import com.spectralogic.vail.vapir.model.Storage;

import java.util.ArrayList;

public class SearchConfigVariables {
    public static SphereConfig populateFields(SphereConfig config, String ip_address, VailConnector sphere) {
        Endpoint this_endpoint = FindEndpoint.byIp(ip_address, sphere);

        config.setStorage(updateStorage(config.getStorage(), this_endpoint));
        config.setLifecycles(updateLifecycles(config.getLifecycles(), this_endpoint)); 
        config.setBuckets(updateBuckets(config.getBuckets(), this_endpoint));

        for(Storage storage : config.getStorage()) {
            System.out.println("Endpoint: " + storage.getEndpoint() + ":: " + storage.getName());
        }

        return config;
    }

    //===========================================
    // Private Functions
    //===========================================
    public static ArrayList<Bucket> updateBuckets(ArrayList<Bucket> bucket_list, Endpoint this_endpoint) {
        String field;

        for(Bucket bucket : bucket_list) {
            field = bucket.getLifecycle();
            field = field.replace("{{this-hostname}}", this_endpoint.getName())
                        .replace("{{this-endpoint-id}}", this_endpoint.getId());
            bucket.setLifecycle(field);
        }
        
        return bucket_list;
    }

    public static ArrayList<Lifecycle> updateLifecycles(ArrayList<Lifecycle> lifecycle_list, Endpoint this_endpoint) {
        String field;

        for(Lifecycle lifecycle : lifecycle_list) {
            // Update the lifecycle name
            field = lifecycle.getName();
            field = field.replace("{{this-hostname}}", this_endpoint.getName())
                        .replace("{{this-endpoint-id}}", this_endpoint.getId());
            lifecycle.setName(field);

            // Update the storage names in the underlying rules.
            for(LifecycleRule rule : lifecycle.getRules()) {
                for(String storage : rule.getDestination().getStorage()) {
                    field = storage.replace("{{this-hostname}}", this_endpoint.getName())
                                .replace("{{this-endpoint-id}}", this_endpoint.getId());
                    storage = field;
                }
            }
        }

        return lifecycle_list;
    }

    private static ArrayList<Storage> updateStorage(ArrayList<Storage> storage_list, Endpoint this_endpoint) {
        // Update the {{this-hostname}} and {{this-endpoint-id}} variables in the storage list.
        String field;

        for(Storage storage : storage_list) {
            // Update storage name
            field = storage.getName();
            field = field.replace("{{this-hostname}}", this_endpoint.getName())
                        .replace("{{this-endpoint-id}}", this_endpoint.getId());
            storage.setName(field);

            // Update endpoint
            field = storage.getEndpoint();
            field = field.replace("{{this-hostname}}", this_endpoint.getName());
            storage.setEndpoint(field);
        }
        
        return storage_list;
    }
}

