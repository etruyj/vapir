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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchConfigVariables {
    private static final Logger log = LoggerFactory.getLogger(SearchConfigVariables.class);

    public static SphereConfig populateFields(SphereConfig config, String ip_address, VailConnector sphere) throws Exception {
        log.info("Setting variables present in the configuration.");

        Endpoint this_endpoint = FindEndpoint.byIp(ip_address, sphere);

        if(this_endpoint == null) throw new Exception("Failed to identify endpoint associated with " + ip_address);

        if(config.getStorage() != null) config.setStorage(updateStorage(config.getStorage(), this_endpoint));
        if(config.getLifecycles() != null) config.setLifecycles(updateLifecycles(config.getLifecycles(), this_endpoint)); 
        if(config.getBuckets() != null) config.setBuckets(updateBuckets(config.getBuckets(), this_endpoint));

        /*for(Storage storage : config.getStorage()) {
            System.out.println("Endpoint: " + storage.getEndpoint() + ":: " + storage.getName());
        }*/

        return config;
    }

    //===========================================
    // Private Functions
    //===========================================
    public static ArrayList<Bucket> updateBuckets(ArrayList<Bucket> bucket_list, Endpoint this_endpoint) {
        log.debug("Updating bucket names.");
        String field;

        for(Bucket bucket : bucket_list) {
            field = bucket.getLifecycle();
            // Check to make sure a lifecycle exists
            if(field != null) {
                field = field.replace("{{this-hostname}}", this_endpoint.getName())
                            .replace("{{this-endpoint-id}}", this_endpoint.getId());
                bucket.setLifecycle(field);
            }
        }
        
        return bucket_list;
    }

    public static ArrayList<Lifecycle> updateLifecycles(ArrayList<Lifecycle> lifecycle_list, Endpoint this_endpoint) {
        log.debug("Updating lifecycle names.");
        String field;

        if(lifecycle_list != null && !lifecycle_list.isEmpty()) {
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
        }

        return lifecycle_list;
    }

    private static ArrayList<Storage> updateStorage(ArrayList<Storage> storage_list, Endpoint this_endpoint) {
        // Update the {{this-hostname}} and {{this-endpoint-id}} variables in the storage list.
        log.debug("Updating storage names.");
        String field;
        
        if(storage_list != null && !storage_list.isEmpty()) {
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
        }

        return storage_list;
    }
}

