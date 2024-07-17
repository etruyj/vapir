//===================================================================
// VailController.java
//      Description:
//          This is the main access point for the script's commands. 
//          The VailConnector class is the main connection between the 
//          UI elements and the rest of the script.
//===================================================================

package com.spectralogic.vail.vapir.command;

import com.spectralogic.vail.vapir.api.VailConnector;
import com.spectralogic.vail.vapir.model.Account;
import com.spectralogic.vail.vapir.model.Bucket;
import com.spectralogic.vail.vapir.model.BucketSummary;
import com.spectralogic.vail.vapir.model.CapacitySummary;
import com.spectralogic.vail.vapir.model.Endpoint;
import com.spectralogic.vail.vapir.model.SphereConfig;
import com.spectralogic.vail.vapir.model.Storage;
import com.spectralogic.vail.vapir.model.Summary;
import com.spectralogic.vail.vapir.model.User;
import com.spectralogic.vail.vapir.model.UserSummary;

import java.util.ArrayList;

public class VailController {
    private VailConnector sphere;
    boolean ignore_ssl; // Stored for using the S3Connector in EnableVeeam

    public VailController(String ip_address, boolean ignore_ssl) {
        sphere = new VailConnector(ip_address, ignore_ssl);
        this.ignore_ssl = ignore_ssl;
    }

    //===============================================================
    // Commands
    //===============================================================

    public String addAccount(String ip_address, String role_arn, String external_id, String username, String description) {
        try {
            AddAccount.fromFields(ip_address, role_arn, external_id, username, description, sphere);
        
            return "Successfully attached AWS account to Vail sphere.";
        } catch(Exception e) {
            return e.getMessage();
        }
    }

    public String clearCache(String ip_address) {
        return ClearCache.clearIamPermissions(ip_address, sphere);
    }

    public ArrayList<String> configureSphere(String ip_address, String file_name) {
        ArrayList<String> report = ConfigureSphere.start(sphere, ip_address, file_name);
        return report;
    }

    public ArrayList<String> configureSphereFromObject(SphereConfig config, String ip_address) {
        return ConfigureSphere.buildSphere(sphere, ip_address, config);
    }

    public String createBucket(String ip_address, String name, String account) {
        try {
            Bucket bucket = CreateBucket.createSimpleBucket(ip_address, name, account, sphere);
            
            if(bucket != null) {
                return "Successfully created bucket [" + name + "]";
            }
            else {
                return "Failed to create bucket [" + name + "]";
            }
        } catch(Exception e) {
            return e.getMessage();
        }
    }

    public String createGroup(String ip_address, String name, String account) {
        try {
            CreateGroup.create(ip_address, name, account, sphere);
        
            return "Successfully created group [" + name + "]";
        } catch(Exception e) {
            return e.getMessage();
        }
    }

    public String createUser(String ip_address, String account, String username) {
        try {
            User user = CreateUser.create(ip_address, username, account, sphere);

            if(user != null) {
                return "Successfully created user [" + username + "]";
            } else {
                return "Failed to create user [" + username + "]";
            }
        } catch(Exception e) {
            return e.getMessage();
        }
    }

    public String enableVeeam(String bucket) {
        return EnableVeeam.configureSosapi(sphere, bucket);
    }

    public ArrayList<CapacitySummary> getCapacitySummary(String ip_address) {
        return GetCapacitySummary.fromSphere(ip_address, sphere);
    }

    public Account[] listAccounts(String ip_address) {
        return ListAccounts.all(ip_address, sphere);
    }

    public ArrayList<Account> listAccounts() {
        return ListAccounts.all(sphere);
    }

    public ArrayList<BucketSummary> listBuckets(String ip_address, String filter) {
        return ListBuckets.summary(ip_address, filter, sphere);
    }

    public ArrayList<Bucket> listBucketsAll() {
        return ListBuckets.all(sphere);
    }

    public ArrayList<Endpoint> listEndpointsAll() {
        return ListEndpoints.all(sphere);
    }

    public ArrayList<Endpoint> listEndpointsBlackPearl() {
        return ListEndpoints.blackpearl(sphere);
    }

    public ArrayList<Summary> listGroups(String ip_address, String account) {
        return ListGroups.summary(ip_address, account, sphere);
    }

    public Storage[] listStorage(String ip_address) {
        return ListStorage.all(ip_address, sphere);
    }

    public ArrayList<UserSummary> listUsers(String ip_address, String account, boolean active_only) {
        try {
            return ListUsers.summary(ip_address, account, active_only, sphere);
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public boolean login(String ip_address, String user, String password) {
        return Login.toSphere(ip_address, user, password, sphere);
    }
}
