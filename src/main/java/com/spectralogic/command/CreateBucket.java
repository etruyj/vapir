//===================================================================
// CreateBucket.java
//	Description: 
//		Commands associated with creating a bucket in the
//	 	Vail sphere.
//
// Created by Sean Snyder 
//==================================================================

package com.spectralogic.vail.vapir.command;

import com.spectralogic.vail.vapir.api.VailConnector;
import com.spectralogic.vail.vapir.model.Account;
import com.spectralogic.vail.vapir.model.Bucket;
import com.spectralogic.vail.vapir.util.search.SearchAccounts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateBucket
{
    private static final Logger log = LoggerFactory.getLogger(CreateBucket.class);

    public static Bucket create(String ip_address, Bucket bucket, VailConnector sphere)	{
        try {		
            log.info("Creating bucket [" + bucket.getName() + "] for account " + bucket.getOwner());
            return sphere.createBucket(ip_address, bucket);
        } catch(Exception e) {
            log.error(e.getMessage());
            return null;
        }
	}

    public static Bucket createSimpleBucket(String ip_address, String name, String account_id, VailConnector sphere) throws Exception {
        log.info("Creating a bucket [" + name + "] for account " + account_id + " with a simple prompt.");
        
        Bucket bucket = new Bucket()
                            .defaultConfiguration()
                            .build();
        bucket.setName(name);

        log.debug("Querying " + ip_address + " for attached accounts.");
        Account[] accounts = ListAccounts.all(ip_address, sphere);
        log.info("Found (" + accounts.length + ") AWS accounts attached to the Vail Sphere.");

        String owner_id = SearchAccounts.findCanonicalId(accounts, account_id);
        log.debug("Account [" + account_id + "] has id " + owner_id);

        if(owner_id == null || owner_id.equals("none")) {
            log.error("Unable to locate account.");
            throw new Exception("Unable to create bucket. Invalid account [" + account_id + "] specified.");
        }

        bucket.setOwner(owner_id);

        try {
            Bucket new_bucket = sphere.createBucket(ip_address, bucket);

            if(new_bucket == null) {
                log.error("Failed to create bucket [" + name + "]");
                throw new Exception("Failed to create bucket [" + name + "]");
            }
            
            return new_bucket;
        } catch(Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
