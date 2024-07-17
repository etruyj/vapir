//===================================================================
// ConfigureBucketPolicy.java
//      Description:
//          The purpose of this class is to take the simplified
//          bucket policy configuration that is provided as part
//          of the configuration file and turning it into an actual
//          bucket policy.
//
// Created by Sean Snyder
// Copyright Spectra Logic 2024
//===================================================================

package com.spectralogic.vail.vapir.command;

import com.spectralogic.vail.vapir.model.BucketPolicy;
import com.spectralogic.vail.vapir.model.BucketPolicyPrincipal;
import com.spectralogic.vail.vapir.model.BucketPolicyStatement;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigureBucketPolicy {
    private static final Logger log = LoggerFactory.getLogger(ConfigureBucketPolicy.class);

    public static BucketPolicy translateFromSimplifiedPolicy(String bucket_name, BucketPolicy simple_policy, HashMap<String, String> group_arn_map, HashMap<String, String> user_arn_map) {
        BucketPolicy policy = new BucketPolicy();
        BucketPolicyPrincipal principal;
        BucketPolicyStatement statement;
        String permission;
        String arn;

        // Character lengths
        int user_length = 5;
        int group_length = 6;

        log.info("Translating simplified policy to a bucket policy for " + bucket_name);

        for(BucketPolicyStatement stmt : simple_policy.getStatement()) {
            statement = new BucketPolicyStatement();

            // Grab the policy effect from the old rule.
            statement.setEffect(stmt.getEffect());

            // Translate Permissions
            for(String perm : stmt.getAction()) {
                if(perm.contains("s3:")) { 
                    // if the permission contains the s3: prefix
                    // assume it is correctly defined.
                    statement.addAction(perm);
                } else {
                    // Assume a simplified permission is defined
                    // add the s3: prefix and wildcard suffix.
                    permission = "s3:" + perm + "*";
                    statement.addAction(permission);
                }
            }

            // Translate Principals to ARNs.
            principal = new BucketPolicyPrincipal();

            for(String princ : stmt.getPrincipal().getAWS()) {
                // Check for user prefix.
                if(princ.length() > user_length
                        && princ.substring(0, user_length).equals("user/")) {
                    // find user arn.
                    arn = user_arn_map.get(princ.substring(user_length, princ.length()));
                    log.debug("Principal [" + princ + "] has ARN " + arn);

                    principal.addAWS(arn);
                }
               
                // Check for group prefix.
                if(princ.length() > group_length 
                        && princ.substring(0, group_length).equals("group/")) {
                    // find group arn    
                    arn = group_arn_map.get(princ.substring(group_length, princ.length()));
                    log.debug("Principal [" + princ + "] has ARN " + arn);

                    principal.addAWS(arn);
                }
            }

            // Add resource to policy.
            arn = "arn:aws:s3:::" + bucket_name; // apply for bucket
            statement.addResource(arn);

            arn = arn + "/*"; // apply for all objects in bucket.
            statement.addResource(arn);

            statement.setPrincipal(principal);
            policy.addPolicyStatement(statement);
        }

        return policy;
    }
}
