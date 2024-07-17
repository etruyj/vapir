//===================================================================
// EnableVeeam.java
//      Description:
//          This script enables Veeam's SOSAPI by putting the required
//          XML files in the specified Veeam bucket.
//
//      Steps:
//          1.) Verify the bucket exists
//          2.) Check to see the number of Storage locations
//              a.) If there is one Standard Storage Location use that.
//              b.) If there are more than one Standard Storage location
//                  check the lifecycle for which Standard-tier location
//                  is used.
//                      i.) If a standard tier is specified in the lifecycle
//                          use this location.
//                      ii.) If no standard tier is specified, prompt the user
//                          for which tier to report.
//          3.) Create a user in the bucket's AWS account.
//          4.) Add bucket policy to allow user to put to bucket.
//          5.) Generate the SOSAPI XML docs
//          6.) Put the files in the bucket.
//          7.) Remove the user from Vail.
//          8.) Output success message.
//
// Created by Sean Snyder
//===================================================================

package com.spectralogic.vail.vapir.command;

import com.spectralogic.vail.vapir.api.VailConnector;
import com.spectralogic.vail.vapir.model.Account;
import com.spectralogic.vail.vapir.model.Bucket;
import com.spectralogic.vail.vapir.model.BucketPolicy;
import com.spectralogic.vail.vapir.model.BucketPolicyPrincipal;
import com.spectralogic.vail.vapir.model.BucketPolicyStatement;
import com.spectralogic.vail.vapir.model.BucketSummary;
import com.spectralogic.vail.vapir.model.Lifecycle;
import com.spectralogic.vail.vapir.model.LifecycleRule;
import com.spectralogic.vail.vapir.model.Storage;
import com.spectralogic.vail.vapir.model.User;
import com.spectralogic.vail.vapir.model.UserKey;
import com.spectralogic.vail.vapir.model.veeam.Capacity;
import com.spectralogic.vail.vapir.model.veeam.SystemInfo;
import com.spectralogic.vail.vapir.util.s3.S3Connector;
import com.spectralogic.vail.vapir.util.xml.XmlParser;
import com.spectralogic.vail.vapir.util.map.MapAccounts;
import com.spectralogic.vail.vapir.util.map.MapStorage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnableVeeam {
    private static final Logger log = LoggerFactory.getLogger(EnableVeeam.class);

    //============= MAIN FUNCTION ===============
    // configureSosapi
    //      This is the main function in this class
    //      and intended as the primary entry point
    //      for executing this command.
    public static String configureSosapi(VailConnector sphere, String bucket_name) {
        log.info("Enabling Veeam SOSAPI on bucket [" + bucket_name + "]");

        String message = "Unable to configure SOSAPI.";
        String temp_user = "spectra-veeam-configure-0";
        String file_path = "../output/";
        String veeam_prefix = ".system-d26a9498-cb7c-4a87-a44a-8ae204f5ba6c";
        boolean ignore_ssl = true;
        Bucket bucket = null;


        try {
            bucket = sphere.getBucket(bucket_name);
        } catch(Exception e) {
            log.error(e.getMessage());
        }

        if(bucket != null) {
            // Bucket Exists - Proceed.
            String storage_id = getStorageId(sphere, bucket);
            UserKey access_keys = null;
            String account_id = "";

            if(storage_id.length() == 0) {
                message = message + " Unable to find a storage tier for capacity.xml.";
            }
            else {
                // Continue account creation.
                try {
                    // All these functions can be grouped together in a 
                    // single try-block. If any of these fail, we should
                    // abort the execution.
                    account_id = getAccountId(sphere, bucket.getOwner());
                    access_keys = createUser(sphere, account_id, temp_user);                
                    updateBucketPolicyAddUserToPolicy(sphere, bucket, account_id, temp_user);
                    createXmlDocs(file_path, storage_id);
                    putXmlDocsToBucket(sphere.getIpAddress(), bucket.getName(), veeam_prefix, file_path, "system.xml", access_keys, ignore_ssl);
                    putXmlDocsToBucket(sphere.getIpAddress(), bucket.getName(), veeam_prefix, file_path, "capacity.xml", access_keys, ignore_ssl);
                   
                    message = "Success. Bucket [" + bucket.getName() + "] configured for Veeam Archive tier."; 
                } catch(Exception e) {
                    log.error(e.getMessage());
                }

                // Clean-up Tasks
                // These need to be performed individually. Aside from deleting
                // user keys which have to be done before the user is deleted,
                // all clean-up tasks should be performed even if a prior task
                // fails.
                log.info("Performing clean-up tasks...");
                
                updateBucketResetToOriginal(sphere, bucket);
                
                try {
                    if(access_keys != null) { 
                        sphere.deleteUserKey(account_id, temp_user, access_keys.getId());
                    }
                
                    sphere.deleteUser(account_id, temp_user);
                } catch(Exception e) {
                    log.error(e.getMessage());
                } 
        
            }
        } else {
                message = message + " Bucket [" + bucket_name + "] does not exist.";
        }
        
        log.info(message);
        return message;
    }

    //===========================================
    // Private Functions
    //===========================================

    private static UserKey createUser(VailConnector sphere, String account_id, String username) {
        log.info("Creating temp user [" + username + "] for bucket.");

        UserKey key_pair = null;
        
        try {
            User user = sphere.createUser(account_id, username);

            if(user != null) {
                key_pair = sphere.createUserKey(account_id, username);            
            }
        } catch(Exception e) {
            log.error(e.getMessage());
        }

        return key_pair;
    }

    private static void createXmlDocs(String file_path, String storage_id) {
        log.info("Generating XML docs and saving them to " + file_path);

        try {        
            String path = file_path + "system.xml"; 
            SystemInfo system = new SystemInfo();
            log.debug("Saving " + path);

            XmlParser.saveDoc(path, system);

            path = file_path + "capacity.xml";
            Capacity capacity = new Capacity();

            String total = "{{.StorageTotal \"" + storage_id + "\"}}";
            String available = "{{.StorageAvailable \"" + storage_id + "\"}}";
            String used = "{{.StorageUnavailable \"" + storage_id + "\"}}";

            capacity.setCapacity(total);
            capacity.setAvailable(available);
            capacity.setUsed(used);

            log.debug("Saving " + path);

            XmlParser.saveDoc(path, capacity);
        } catch(Exception e) {
            log.error(e.getMessage());
        }
    }

    private static String getAccountId(VailConnector sphere, String canonical_id) throws IOException {
        Account[] accounts = sphere.listAccounts();

        Map<String, String> canon_id_map = MapAccounts.createCanonicalIDMap(accounts);

        log.info("Bucket belongs to account id " + canon_id_map.get(canonical_id));

        return canon_id_map.get(canonical_id);
    }

    private static ArrayList<Storage> getStandardStorage(VailConnector sphere) {
        ArrayList<Storage> storage_list = new ArrayList<Storage>();

        try {
            Storage[] storage = sphere.listStorage();

            for(Storage location : storage) {
                /*
                * Returning all storage as Veeam Needs to see Glacier
                * as well.
                if(location.getStorageClass().equals("STANDARD")) {
                    storage_list.add(location);
                }
                */
                storage_list.add(location);
            }
        } catch(IOException e) {
            log.error(e.getMessage());
        }
        return storage_list;
    }

    private static String getStorageId(VailConnector sphere, Bucket bucket) {
        log.info("Searching for standard-tier storage id.");
        
        String storage_id = "";

        ArrayList<Storage> storage_list = getStandardStorage(sphere);

        if(storage_list.size() == 1) {
            storage_id = storage_list.get(0).getId();
            log.info("Using storage id [" + storage_id + "] for capacity.xml");
        }
        else if(storage_list.size() > 1) {
            // We need to figure out what our Storage location should be.
            // We do this by looking at the lifecycle.
            storage_id = selectStorageFromLifecycle(sphere, bucket, storage_list);

            if(storage_id.length() == 0) { 
                log.info("Requesting user input to select storage.");
                storage_id = inputStorageId(storage_list);
            }
        }
        else {
            log.error("No standard storage configured in this Vail Sphere.");
        }

        return storage_id;
    }

    private static String inputStorageId(ArrayList<Storage> storage_list) {
        String storage_id = "";
        boolean storage_selected = false;
        Map<String, String> storage_verification = MapStorage.createIdNameMap(storage_list);

        Scanner input = new Scanner(System.in);
        
        do {
            System.out.println("Unable to determine desired storage for the bucket.");
            System.out.println("Please end the id of the desired storage from the list.");
            System.out.println("Enter [q] to quit.");
            System.out.println("\nStorage:");
            
            for(Storage storage : storage_list) {
                System.out.println("[" + storage.getId() + "]: " + storage.getName() + " (" + storage.getStorageClass() + ")");
            } 

            System.out.print("\nEnter selection: ");
            storage_id = input.nextLine();
            System.out.print("\n"); // Add a new line

            if(storage_id.equals("q")) {
                storage_id = "";
                storage_selected = true;
            }
            else if(storage_verification.get(storage_id) != null) {
                storage_selected = true;
            }
            else {
                System.out.println("Invalid selection.");
            }
        } while(!storage_selected);

        return storage_id;
    }

    private static void putXmlDocsToBucket(String ip_address, String bucket, String prefix, String file_path, String file, UserKey access_keys, boolean ignore_ssl) throws Exception {
        log.info("Putting objects to S3 bucket " + bucket);
        Map<String, String> metadata = new HashMap<String, String>();

        metadata.put("Spectra-Active", "refresh");

        String path = file_path + file;
        String object_key = prefix + "/" + file;

        String etag = S3Connector.putObjectFileWithMetadata(ip_address, "us-east-1", bucket, object_key, path, metadata, access_keys.getId(), access_keys.getSecretAccessKey(), ignore_ssl);
        
        log.info("Created " + object_key + " with eTag " + etag);
    }

    private static String selectStorageFromLifecycle(VailConnector sphere, Bucket bucket, ArrayList<Storage> storage_list) {
        log.info("Searching bucket lifecycle for associated standard storage.");

        String storage_id = "";
        int matched_storage = 0;

        Map<String, String> storage_map = MapStorage.createIdNameMap(storage_list);

        if(bucket.getLifecycle() != null) {
            try {
                Lifecycle lifecycle = sphere.getLifecycle(bucket.getLifecycle());

                for(LifecycleRule rule : lifecycle.getRules()) {
                    for(String storage : rule.getDestinations().getStorage()) {
                        // Check if specified storage is in the list of standard storage
                        if(storage_map.get(storage) != null) {
                            storage_id = storage;
                            matched_storage++;
                        }
                    }
                }
            } catch(IOException e) {
                log.error(e.getMessage());
            }
        }

        if(matched_storage == 1) {
            return storage_id; 
        }
        else {
            log.info("Unable to determine storage from bucket lifecycle.");
            return ""; // No location found.
        }
    }

    private static void updateBucketPolicyAddUserToPolicy(VailConnector sphere, Bucket bucket, String account_id, String user) throws IOException {
        log.info("Updating bucket policy for " + bucket.getName() + " to allow PUT access for " + user);

        Bucket new_config = new Bucket(bucket);

        if(new_config.getPolicy() == null) {
            // Create a new bucket policy if it doesn't exist.
            log.info("Bucket [" + bucket.getName() + "] does not have an associated bucket policy.");
            BucketPolicy policy = new BucketPolicy();
            policy.setId("AllowTempUserAccess");

            new_config.setPolicy(policy);
        }
        else {
            // Copy the data over it it already exists.
            log.info("Bucket [" + bucket.getName() + "] has an existing bucket policy");
        }

        // Create the required permission statement.
        BucketPolicyStatement statement = new BucketPolicyStatement();
        BucketPolicyPrincipal principal = new BucketPolicyPrincipal();

        principal.addAWS("arn:aws:iam::" + account_id + ":user/" + user);
        String resource = "arn:aws:s3:::" + bucket.getName();

        log.debug("AWS Principal: " + principal.getAWS());

        statement.setSid("AllowSpectraTempUserPutForVeeam");
        statement.addAction("s3:Put*");
        statement.addAction("s3:BypassGovernanceRetention");
        statement.setEffect("Allow");
        statement.setPrincipal(principal);
        statement.addResource(resource);
        statement.addResource(resource + "/*");

        // Add it to the new policy and then add the policy to the new_config
        new_config.addPolicyStatement(statement);

        sphere.updateBucket(new_config);
    
    }

    public static void updateBucketResetToOriginal(VailConnector sphere, Bucket bucket) {
        log.info("Resetting bucket to original configuration.");

        if(bucket.getPolicy() != null) {
            // Check the statement for our bucket policy
            // And delete if it exists
            if(bucket.getPolicy().getStatement().size() == 0) {
                log.info("Setting bucket policy to null as no permissions are configured.");
                bucket.setPolicy(null);
            }
        }
        
        try {
            sphere.updateBucket(bucket);
        } catch(Exception e) {
            log.error(e.getMessage());
            log.error("Failed to reset bucket policy");
        }
    }
}
