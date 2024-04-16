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
//===================================================================

package com.socialvagrancy.vail.commands.sub;

import com.socialvagrancy.vail.commands.BasicCommands;
import com.socialvagrancy.vail.structures.Account;
import com.socialvagrancy.vail.structures.Bucket;
import com.socialvagrancy.vail.structures.BucketPolicy;
import com.socialvagrancy.vail.structures.BucketPolicyPrincipal;
import com.socialvagrancy.vail.structures.BucketPolicyStatement;
import com.socialvagrancy.vail.structures.BucketSummary;
import com.socialvagrancy.vail.structures.Lifecycle;
import com.socialvagrancy.vail.structures.Storage;
import com.socialvagrancy.vail.structures.User;
import com.socialvagrancy.vail.structures.UserKey;
import com.socialvagrancy.vail.structures.veeam.Capacity;
import com.socialvagrancy.vail.structures.veeam.SystemInfo;
import com.socialvagrancy.vail.utils.aws.S3Connector;
import com.socialvagrancy.vail.utils.io.XmlParser;
import com.socialvagrancy.vail.utils.map.MapAccounts;
import com.socialvagrancy.utils.io.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EnableVeeam {
    public static String configureSosapi(BasicCommands sphere, String ip_address, String bucket_name, Logger log) {
        String message = "Unable to configure SOSAPI.";
        String temp_user = "spectra-veeam-configure-0";
        String file_path = "../output/";
        String veeam_prefix = ".system-d26a9498-cb7c-4a87-a44a-8ae204f5ba6c";
        boolean ignore_ssl = true;

        Bucket bucket = sphere.getBucket(ip_address, bucket_name);

        if(bucket != null) {
            // Bucket Exists - Proceed.
            String storage_id = getStorageId(sphere, ip_address, log);

            if(storage_id.length() == 0) {
                message = message + " Unable to find a storage tier for capacity.xml.";
            }
            else {
                // Continue account creation.
                try {
                    String account_id = getAccountId(sphere, ip_address, bucket.getOwner(), log);
                    UserKey access_keys = createUser(sphere, ip_address, account_id, temp_user, log);                
                    updateBucketPolicyAddUserToPolicy(sphere, ip_address, bucket, account_id, temp_user, log);
                    createXmlDocs(file_path, storage_id, log);
                    putXmlDocsToBucket(ip_address, bucket.getName(), veeam_prefix, file_path, "system.xml", access_keys, ignore_ssl, log);
                    putXmlDocsToBucket(ip_address, bucket.getName(), veeam_prefix, file_path, "capacity.xml", access_keys, ignore_ssl, log);
                    
                    // Clean-up Tasks
                    updateBucketResetToOriginal(sphere, ip_address, bucket, log);
                    sphere.deleteUserKey(ip_address, account_id, temp_user, access_keys.getId());
                    sphere.deleteUser(ip_address, account_id, temp_user);
                } catch(Exception e) {
                    log.error(e.getMessage());
                }
                
            }
        
        }
        else {
            message = message + " Bucket [" + bucket_name + "] does not exist.";
        }

        log.info(message);
        return message;
    }

    //===========================================
    // Private Functions
    //===========================================

    private static UserKey createUser(BasicCommands sphere, String ip_address, String account_id, String username, Logger log) {
        log.info("Creating temp user [" + username + "] for bucket.");

        UserKey key_pair = null;
        User user = sphere.createUser(ip_address, account_id, username);

        if(user != null) {
            key_pair = sphere.createUserKey(ip_address, account_id, username);            
        }

        return key_pair;
    }

    private static void createXmlDocs(String file_path, String storage_id, Logger log) {
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

    private static String getAccountId(BasicCommands sphere, String ip_address, String canonical_id, Logger log) {
        Account[] accounts = sphere.listAccounts(ip_address);

        Map<String, String> canon_id_map = MapAccounts.createCanonicalIDMap(accounts);

        log.info("Bucket belongs to account id " + canon_id_map.get(canonical_id));

        return canon_id_map.get(canonical_id);
    }

    private static ArrayList<Storage> getStandardStorage(BasicCommands sphere, String ip_address, Logger log) {
        ArrayList<Storage> storage_list = new ArrayList<Storage>();

        Storage[] storage = sphere.listStorage(ip_address);

        for(Storage location : storage) {
            if(location.getStorageClass().equals("STANDARD")) {
                storage_list.add(location);
            }
        }

        return storage_list;
    }

    private static String getStorageId(BasicCommands sphere, String ip_address, Logger log) {
        log.info("Searching for standard-tier storage id.");
        
        String storage_id = "";

        ArrayList<Storage> storage_list = getStandardStorage(sphere, ip_address, log);

        if(storage_list.size() == 1) {
            storage_id = storage_list.get(0).getId();
            log.info("Using storage id [" + storage_id + "] for capacity.xml");
        }
        else if(storage_list.size() > 1) {
            // We need to figure out what our Storage location should be.
        }
        else {
            log.error("No standard storage configured in this Vail Sphere.");
        }

        return storage_id;
    }

    public static void putXmlDocsToBucket(String ip_address, String bucket, String prefix, String file_path, String file, UserKey access_keys, boolean ignore_ssl, Logger log) throws Exception {
        log.info("Putting objects to S3 bucket " + bucket);
        Map<String, String> metadata = new HashMap<String, String>();

        metadata.put("Spectra-Active", "refresh");

        String path = file_path + file;
        String object_key = prefix + "/" + file;

        String etag = S3Connector.putObjectFileWithMetadata(ip_address, "us-east-1", bucket, object_key, path, metadata, access_keys.getId(), access_keys.getSecretAccessKey(), ignore_ssl);
        
        log.info("Created " + object_key + " with eTag " + etag);
    }

    public static void updateBucketPolicyAddUserToPolicy(BasicCommands sphere, String ip_address, Bucket bucket, String account_id, String user, Logger log) {
        log.info("Updating bucket policy for " + bucket.getName() + " to allow PUT access for " + user);

        Bucket new_config = bucket;

        if(new_config.getPolicy() == null) {
            BucketPolicy policy = new BucketPolicy();
            policy.setId("AllowTempUserAccess");
            
            new_config.setPolicy(policy);
        }

        BucketPolicyStatement statement = new BucketPolicyStatement();
        BucketPolicyPrincipal principal = new BucketPolicyPrincipal();

        principal.addAWS("arn:aws:iam::" + account_id + ":user/" + user);
        String resource = "arn:aws:s3:::" + bucket.getName();

        log.debug("AWS Principal: " + principal.getAWS());

        statement.setSid("AllowTempUserPut");
        statement.addAction("s3:Put*");
        statement.addAction("s3:BypassGovernanceRetention");
        statement.setEffect("Allow");
        statement.setPrincipal(principal);
        statement.addResource(resource);
        statement.addResource(resource + "/*");

        new_config.addPolicyStatement(statement);

        sphere.updateBucket(ip_address, new_config.getName(), new_config);

    }

    public static void updateBucketResetToOriginal(BasicCommands sphere, String ip_address, Bucket bucket, Logger log) {
        log.info("Resetting bucket to original configuration.");

        if(bucket.getPolicy() != null) { System.err.println("Bucket Policy Exists."); }
        sphere.updateBucket(ip_address, bucket.getName(), bucket);
    }
}
