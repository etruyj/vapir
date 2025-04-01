//===================================================================
// GetBucketSize.java
//      Description:
//          This command uses the Vail management API to get all the
//          objects in a Vail bucket and determine the size of the 
//          objects in the bucket and the placement of the clones
//          across different storage locations.
//
// Created by Sean Snyder
//===================================================================

package com.spectralogic.vail.vapir.command;

import com.spectralogic.vail.vapir.api.VailConnector;
import com.spectralogic.vail.vapir.model.BucketObjects;
import com.spectralogic.vail.vapir.model.Clone;
import com.spectralogic.vail.vapir.model.Object;
import com.spectralogic.vail.vapir.model.report.BucketDetails;
import com.spectralogic.vail.vapir.model.report.CloneLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetBucketSize {
    private static final Logger log = LoggerFactory.getLogger(GetBucketSize.class);

    public static BucketDetails chunked(String bucket, String max_keys, String limit, VailConnector sphere) {
        log.info("Calculating total size of the objects and clone placement for bucket [" + bucket + "]");

        BucketDetails report = new BucketDetails();

        HashMap<String, CloneLocation> clone_map = new HashMap<String, CloneLocation>();
        CloneLocation location = null;
        long object_count = 0;
        long object_size = 0;
        long object_counter = 0;
        long clone_counter = 0;

        ArrayList<Object> object_list = null;
        String marker = null;

        // Set the limit to 1000 if not specified.
        if(limit == null || limit.equals("none")) {
            limit = "1000";
        }

        do {
            // Fetch a chunk of objects from the bucket.
            if(object_list != null) {
                marker = object_list.get(object_list.size()-1).getKey(); // The marker is the last key in the result.
            }

            try {
                log.info("Fetching object batch...");
                object_list = GetBucketObjects.limited(bucket, max_keys, marker, limit, sphere);
                
                object_counter += object_list.size();
                log.info("Retrieved (" + object_list.size() + ") objects. Total result set is " + object_counter + " objects.");

                for(Object object : object_list) {
                    log.debug("Searching for clones associated with object [" + clone_counter + "]: " + object.getKey());
                    // Tally overall bucket info.
                    object_count++;
                    object_size += object.getSize();

                    // Tally clone information
                    Clone clone = sphere.getClones(bucket, object.getKey(), null); // null is version id

                    if(clone.getStorage() != null) {
                        for(Clone.CloneInfo info : clone.getStorage()) {
                            if(clone_map.get(info.getId()) == null) {
                                location = new CloneLocation();
                                location.setClones(1);
                                location.setSize(object.getSize());
                            } else {
                                location = clone_map.get(info.getId());
                                location.setClones(location.getClones()+1);
                                location.setSize(location.getSize() + object.getSize());
                            }

                            clone_map.put(info.getId(), location);
                        }
                    }
            
                    clone_counter++;
                }

                log.info("Current bucket statistics: " + object_count + " objects and size: " + object_size + " bytes"); 
            } catch(Exception e) {
                log.warn(e.getMessage());
                log.warn("Failed to get object batch starting with: " + marker);
            }

        } while(object_list.size() > 0);
       
        report.setName(bucket);            
        report.setObjects(object_count);
        report.setSize(object_size);
        report.setClones(clone_map);
        
        return report; 
    }

    public static BucketDetails threaded(String bucket, String thread_count, String batch_size, String username, String password, VailConnector sphere) {
        log.info("Calculating total size of the objects and clone placement for bucket [" + bucket + "]");
        BucketDetails report = null;
        List<BucketDetails> thread_reports = Collections.synchronizedList(new ArrayList<BucketDetails>());
        AtomicReference<ArrayList<Object>> object_batch = new AtomicReference<>(null);
        AtomicReference<String> marker = new AtomicReference<>(null);
        AtomicLong processed_objects = new AtomicLong(0);
        AtomicLong object_counter = new AtomicLong(0);

        //=======================================
        // Build Concurrency Info
        //=======================================
        AtomicInteger thread_id = new AtomicInteger(0);
        if(thread_count == null || thread_count.equals("none")) {
            thread_count = "1";
        }

        int NUM_WORKERS = Integer.valueOf(thread_count);
        BlockingQueue<ArrayList<Object>> queue = new LinkedBlockingQueue<>();

        ExecutorService workerPool = Executors.newFixedThreadPool(NUM_WORKERS);
        AtomicBoolean keepRunning = new AtomicBoolean(true);

        //=======================================
        // Consumer Queues
        //=======================================
        for(int i=0; i < NUM_WORKERS; i++) {
            workerPool.submit(() -> {
                while(keepRunning.get() || !queue.isEmpty()) {
                    try {
                        ArrayList<Object> object_list = queue.poll(1, TimeUnit.SECONDS);

                        if(object_list != null) {
                            BucketDetails thread_data = fetchCloneData(bucket, object_list, thread_id.get(), sphere); // Process clone batch
                            processed_objects.set(processed_objects.get() + thread_data.getObjects()); // store info for logging
                            thread_reports.add(thread_data); // store info for reporting.
                        }
                    } catch(Exception e) {
                        log.error(e.getMessage());
                        log.info("Failed to process worker pool [" + thread_id.get() + "].");
                    }

                    thread_id.getAndIncrement();
                }
            });
        }

        //=======================================
        // Producer Queue
        //=======================================
        Thread producerThread = new Thread(() -> {
            while(keepRunning.get()) {
                try {
                    if(object_batch.get() != null && object_batch.get().size() > 0) {
                        marker.set(object_batch.get().get(object_batch.get().size()-1).getKey());
                    }

                    log.info("Fetching object batch...");
                    object_batch.set(GetBucketObjects.limited(bucket, batch_size, marker.get(), batch_size, sphere));
                
                    object_counter.set(object_counter.get() + object_batch.get().size());
                    log.info("Retrieved (" + object_batch.get().size() + ") objects. Total result set is " + object_counter.get() + " objects.");

                    // Check to see if results were returned.
                    if(object_batch.get().size() == 0) {
                        log.info("No more objects in bucket.");
                        keepRunning.set(false);
                    } else { // post results to the queue
                        //queue.put(object_batch.get());
                    }
                } catch(Exception e) {
                    log.warn(e.getMessage());
                    log.warn("Failed to retrieve batch stating with marker: " + marker);
                }

                try {
                    Thread.sleep(5000); // Wait 5 seconds before grabbing the next batch
                } catch(InterruptedException e) {
                    log.warn(e.getMessage());
                    log.warn("Thread interrupted.");
                    Thread.currentThread().interrupt();
                }
            }
        });

        //=======================================
        // Status Thread
        //=======================================
        // Running status logging separately from the
        // other threads to allow it to occur on a 
        // more regularly basis.
        Thread statusThread = new Thread(() -> {
            while(keepRunning.get()) {
                try {
                    Thread.sleep(3600000); // Sleep first as there's no need for an initial report. Report every 5 minutes.
                    log.info("Processed " + processed_objects.get() + " out of " + object_counter.get() + " objects.");
                } catch(InterruptedException e) {
                    log.warn(e.getMessage());
                    log.warn("Status update thread was interrupted.");
                }
            }
        });

        //======================================= 
        // Login Thread
        //======================================= 
        // Re-login is required for any regular 
        // or larger size bucket. The API tokens
        // expires after 12 hours, and I haven't
        // successfully completed this script in
        // that window.
        Thread loginThread = new Thread(() -> {
            while(keepRunning.get()) {
                try {
                    Thread.sleep(36000000); // Login ever 5 seconds for a test
                    // Refresh the token
                    Login.toSphere(sphere.getIpAddress(), username, password, sphere);
                } catch(InterruptedException e) {
                    log.warn(e.getMessage());
                    log.warn("Login thread was interrupted.");
                }
            }
        });

        try {
            producerThread.start();
            statusThread.start();
            loginThread.start();
            producerThread.join();

            if(keepRunning.get() == false) {
                workerPool.shutdown();
                workerPool.awaitTermination(60, TimeUnit.SECONDS);
                loginThread.interrupt();
                statusThread.interrupt();
            }
        } catch(Exception e) {
            log.error(e.getMessage());
            log.error("Failed to terminate threads.");
        }

        log.info("Building report...");

        report = consolidateReports(thread_reports);
        report.setName(bucket);
        
        return report;
    }

    //===========================================
    // Private Functions
    //===========================================

    private static BucketDetails consolidateReports(List<BucketDetails> thread_reports) {
        log.info("Consolidating (" + thread_reports.size() + ") reports into single report.");

        BucketDetails report = new BucketDetails();

        long object_count = 0;
        long object_size = 0;
        HashMap<String, CloneLocation> clone_map = new HashMap<String, CloneLocation>();
        CloneLocation location;

        for(BucketDetails data : thread_reports) {
            object_count += data.getObjects();
            object_size += data.getSize();

            for(String key : data.getClones().keySet()) {
                if(clone_map.get(key) == null) {
                    location = new CloneLocation();
                    location.setClones(data.getClones().get(key).getClones());
                    location.setSize(data.getClones().get(key).getSize());
                } else {
                    location = clone_map.get(key);
                    location.setClones(location.getClones()+data.getClones().get(key).getClones());
                    location.setSize(location.getSize() + data.getClones().get(key).getSize());
                }

                clone_map.put(key, location);
            }
        }

        report.setObjects(object_count);
        report.setSize(object_size);
        report.setClones(clone_map);

        log.info("Report data contains " + report.getObjects() + " objects totalling " + report.getSize() + " bytes of data and " + report.getClones().size() + " storage locations.");

        return report;
    }

    private static BucketDetails fetchCloneData(String bucket, ArrayList<Object> object_list, int thread_id, VailConnector sphere) throws Exception {
        log.info("Gathering clone info for batch size " + object_list.size() + " starting with marker: " + object_list.get(0).getKey());

        BucketDetails report = new BucketDetails();
        HashMap<String, CloneLocation> clone_map = new HashMap<String, CloneLocation>();
        CloneLocation location;

        long object_count = 0;
        long object_size = 0;
        Clone clone = null;
        
        for(Object object : object_list) {
            log.debug("Thread[" + thread_id + "]: Searching for clones associated with object [" + object_count + "]: " + object.getKey());

            // Tally overall bucket info
            object_count++;
            object_size += object.getSize();

            // Fetch Clone Info
            clone = sphere.getClones(bucket, object.getKey(), null); // null is version_id

            // Tally clone information
            if(clone.getStorage() != null) { // Check to see if results were returned. dir and <500 byte objects don't have clones
                for(Clone.CloneInfo info : clone.getStorage()) {
                    if(clone_map.get(info.getId()) == null) {
                        location = new CloneLocation();
                        location.setClones(1);
                        location.setSize(object.getSize());
                    } else {
                        location = clone_map.get(info.getId());
                        location.setClones(location.getClones()+1);
                        location.setSize(location.getSize() + object.getSize());
                    }

                    clone_map.put(info.getId(), location);
                }
            } 
        }

        report.setName(bucket);
        report.setObjects(object_count);
        report.setSize(object_size);
        report.setClones(clone_map);

        log.info("Thread[" + thread_id + "] for bucket [" + bucket + "] contains " + report.getObjects() + " objects, totalling " + report.getSize() + " bytes with clones in " + report.getClones().size() + " storage locations."); 

        object_list = null; // Mark the variable null and allow garbage collection.

        return report;
    }
}
