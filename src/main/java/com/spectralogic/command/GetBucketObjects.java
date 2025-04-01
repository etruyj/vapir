//===================================================================
// GetBucketObjects.java
//      Description:
//          This class pulls a list of objects in the associated Vail
//          bucket.
//
// Created by Sean Snyder
//===================================================================

package com.spectralogic.vail.vapir.command;

import com.spectralogic.vail.vapir.api.VailConnector;
import com.spectralogic.vail.vapir.model.BucketObjects;
import com.spectralogic.vail.vapir.model.Object;
import com.spectralogic.vail.vapir.model.report.BucketDetails;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetBucketObjects {
    private static final Logger log = LoggerFactory.getLogger(GetBucketObjects.class);

    public static ArrayList<Object> all(String bucket, String max_keys, VailConnector sphere) {
        log.info("Listing all objects in bucket [" + bucket + "]");

        ArrayList<Object> object_list = new ArrayList<Object>();
        BucketObjects results = new BucketObjects();

        //==== QUICK FILTER ARGPARSER DEFAULTS ====
        if(max_keys.equals("none")) {
            max_keys = "0";
        }

        try {
            do {
                log.debug("Querying results starting with key: " + results.getNextMarker());
                results = sphere.listObjectsInBucket(bucket, results.getNextMarker(), Integer.valueOf(max_keys));
                
                log.debug("Retrieved (" + results.getContents().size() + ") objects from Vail. Total objects retrieved: " + (object_list.size() + results.getContents().size()));

                object_list.addAll(results.getContents());
            } while(results.getNextMarker() != null);
        } catch(Exception e) {
            log.error(e.getMessage());
            log.error("Failed to list objects in the bucket.");
        }

        log.info("Found (" + object_list.size() + ") objects in bucket [" + bucket + "]");

        return object_list;
    } 

    public static ArrayList<Object> limited(String bucket, String max_keys, String marker, String limit, VailConnector sphere) {
        log.info("Listing (" + limit + ") objects in bucket [" + bucket + "] starting with " + marker);

        long max_results = new Long(limit);

        ArrayList<Object> object_list = new ArrayList<Object>();
        BucketObjects results = new BucketObjects();
        results.setNextMarker(marker);

        //==== QUICK FILTER ARGPARSER DEFAULTS ====
        if(max_keys.equals("none")) {
            max_keys = "0";
        }

        try {
            do {
                log.debug("Querying results starting with key: " + results.getNextMarker());
                results = sphere.listObjectsInBucket(bucket, results.getNextMarker(), Integer.valueOf(max_keys));

                if(results.getContents() != null) {                
                    log.debug("Retrieved (" + results.getContents().size() + ") objects from Vail. Total objects retrieved: " + (object_list.size() + results.getContents().size()));

                    object_list.addAll(results.getContents());
                }
            } while(results.getNextMarker() != null && object_list.size() < max_results);
        } catch(Exception e) {
            log.error(e.getMessage());
            log.error("Failed to list objects in the bucket.");
        }

        log.info("Found (" + object_list.size() + ") objects in bucket [" + bucket + "]");

        return object_list;
    } 


}
