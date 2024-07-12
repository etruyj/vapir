//===================================================================
// VailConnector.java
//      Description:
//          This is the access point for all the API calls to be made
//          to the Vail sphere. 
//
// Created by Sean Snyder
//===================================================================

package com.spectralogic.vail.vapir.api;

import com.spectralogic.vail.vapir.model.Account;
import com.spectralogic.vail.vapir.model.Bucket;
import com.spectralogic.vail.vapir.model.Endpoint;
import com.spectralogic.vail.vapir.model.Lifecycle;
import com.spectralogic.vail.vapir.model.Message;
import com.spectralogic.vail.vapir.model.Storage;
import com.spectralogic.vail.vapir.model.Token;
import com.spectralogic.vail.vapir.model.CapacitySummary;
import com.spectralogic.vail.vapir.model.Group;
import com.spectralogic.vail.vapir.model.GroupData;
import com.spectralogic.vail.vapir.model.UserData;
import com.spectralogic.vail.vapir.model.User;
import com.spectralogic.vail.vapir.model.UserKey;
import com.spectralogic.vail.vapir.model.blackpearl.BpDataPolicy;
import com.spectralogic.vail.vapir.model.blackpearl.BPUser;
import com.spectralogic.vail.vapir.model.blackpearl.Ds3KeyPair;
import com.spectralogic.vail.vapir.util.http.RestClient;


import com.google.gson.JsonParseException;

import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VailConnector
{
	private String token;
	private RestClient rest_client;
    private static final Logger log = LoggerFactory.getLogger(VailConnector.class);
    private String ip_address;

	public VailConnector(String ip_address, boolean ignore_ssl) {
        if(ignore_ssl) {
            log.info("Initializing script with ingore_ssl = true");
        } else {
            log.info("Initializing script with ingore_ssl = false");
        }

        this.ip_address = ip_address;
		rest_client = new RestClient(ignore_ssl);
	}

	//===========================================================
	// Commands
	//===========================================================
	
	public Account addAccount(String ipaddress, String json_body) throws IOException, JsonParseException {
	    return Accounts.addAccount(ipaddress, token, json_body, rest_client);
    }

	public Account addAccount(String ipaddress, Account account) throws IOException, JsonParseException {
	    return Accounts.addAccount(ipaddress, token, account, rest_client);
    }

	public Account addAccount(String ipaddress, String account_name, String email, String external_id, String role_arn) throws IOException, JsonParseException {
	    return Accounts.addAccount(ipaddress, account_name, email, external_id, role_arn, token, rest_client);
    }

	public Storage addStorage(String ipaddress, String name, String body) throws IOException, JsonParseException {
	    return StorageLocations.addStorage(ipaddress, name, body, token, rest_client);
    }

	public Storage addStorage(String ipaddress, Storage location) throws IOException, JsonParseException {
	    return StorageLocations.addStorage(ipaddress, location, token, rest_client);
    }

	public String blackpearlLogin(String ipaddress, String username, char[] password) throws IOException, JsonParseException {
	    return BlackPearl.login(ipaddress, username, password, rest_client).getToken();
    }

	public BPUser blackpearlUserList(String ipaddress, String token) throws IOException, JsonParseException {
	    return BlackPearl.listUsers(ipaddress, token, rest_client);
    }

	public Ds3KeyPair blackpearlUserKeys(String ipaddress, String id, String token) throws IOException, JsonParseException {
        return BlackPearl.getUserKeys(ipaddress, id, token, rest_client);
    }

	public void clearCache(String ipaddress) throws IOException {
	    Cache.clearCache(ipaddress, token, rest_client);
    }

	public Bucket createBucket(String ipaddress, String name, String json_body) throws IOException, JsonParseException {
	    return Buckets.createBucket(ipaddress, name, json_body, token, rest_client);
    }

    public Bucket createBucket(String ipaddress, Bucket bucket) throws IOException, JsonParseException {
        return Buckets.createBucket(ipaddress, bucket, token, rest_client);
    }

	public Group createGroup(String ipaddress, String name, String account_id) throws IOException, JsonParseException {
	    return Groups.createGroup(ipaddress, account_id, name, token, rest_client);
    }

	public Lifecycle createLifecycle(String ipaddress, String name, String json_body) throws IOException, JsonParseException {
	    return Lifecycles.createLifecycle(ipaddress, name, json_body, token, rest_client);
    }

	public Lifecycle createLifecycle(String ipaddress, Lifecycle lifecycle) throws IOException, JsonParseException {
	    return Lifecycles.createLifecycle(ipaddress, lifecycle, token, rest_client);
    }

    @Deprecated // removed the need to have the ip in the call
    public Storage createStorage(Storage storage, String ip_address) throws IOException, JsonParseException {
        return createStorage(storage);
    }

    public Storage createStorage(Storage storage) throws IOException, JsonParseException {
        return StorageLocations.createStorage(storage, ip_address, token, rest_client);
    }

	public User createUser(String ipaddress, String account_id, String username) throws IOException, JsonParseException {
	    return Users.createUser(ipaddress, account_id, username, token, rest_client);
    }

	public UserKey createUserKey(String ipaddress, String account, String user) throws IOException, JsonParseException {
	    return UserKeys.createUserKey(ipaddress, account, user, token, rest_client);
    }

	public void deleteUser(String ipaddress, String account_id, String username) throws IOException {
	    Users.deleteUser(ipaddress, account_id, username, token, rest_client);
    }

	public void deleteUserKey(String ipaddress, String account, String user, String access_key) throws IOException {
	    UserKeys.deleteUserKey(ipaddress, account, user, access_key, token, rest_client);
    }

    public Bucket getBucket(String ipaddress, String bucket_name) throws IOException, JsonParseException {
        return Buckets.getBucket(ipaddress, bucket_name, token, rest_client);
    }

    public Lifecycle getLifecycle(String ipaddress, String lifecycle_id) throws IOException, JsonParseException {
        return Lifecycles.getLifecycle(ipaddress, lifecycle_id, token, rest_client);
    }

    public ArrayList<CapacitySummary> getCapacitySummary(String ipaddress) throws IOException, JsonParseException {
        return Capacity.getCapacitySummary(ipaddress, token, rest_client);
    }

    @Deprecated // removing the ip_address from the function call
	public Account[] listAccounts(String ipaddress) throws IOException, JsonParseException {
	    return listAccounts();
    }

    public Account[] listAccounts() throws IOException, JsonParseException {
        return Accounts.list(ip_address, token, rest_client);
    }

    @Deprecated // removing the ip_address from the calls.
	public Bucket[] listBuckets(String ipaddress) throws IOException, JsonParseException {
	    return listBuckets();
    }

    public Bucket[] listBuckets() throws IOException, JsonParseException {
        return Buckets.list(ip_address, token, rest_client);
    }

    @Deprecated // removing ip_address from the calls.
	public Endpoint[] listEndpoints(String ipaddress) throws IOException, JsonParseException {
        return listEndpoints(); // Keeping this function for backwards compatibility.
    }

    public Endpoint[] listEndpoints() throws IOException, JsonParseException {
	    return Endpoints.list(ip_address, token, rest_client);
    }

    public ArrayList<BpDataPolicy> listEndpointDataPolicies(String endpoint_id) throws IOException, JsonParseException {
        return Endpoints.listDataPolicies(ip_address, endpoint_id, token, rest_client);
    }

	public GroupData listGroups(String ipaddress, String account) throws IOException, JsonParseException {
	    return Groups.list(ipaddress, account, token, rest_client);
    }

	public Lifecycle[] listLifecycles(String ipaddress) throws IOException, JsonParseException {
	    return Lifecycles.list(ipaddress, token, rest_client);
    }

	public Storage[] listStorage(String ipaddress) throws IOException, JsonParseException {
	    return StorageLocations.list(ipaddress, token, rest_client);
    }

	public Group[] listUserGroups(String ipaddress, String account_id, String username) throws IOException, JsonParseException {
	    return UserGroups.list(ipaddress, account_id, username, token, rest_client);
    }

	public UserData listUsers(String ipaddress, String account_id) throws IOException, JsonParseException {
	    return Users.list(ipaddress, account_id, token, rest_client);
    }

	public UserKey[] listUserKeys(String ipaddress, String account, String user) throws IOException, JsonParseException {
	    return UserKeys.list(ipaddress, account, user, token, rest_client);
    }

	public boolean login(String ipaddress, String username, String password) throws IOException, JsonParseException {
	    this.token = "Bearer " + Tokens.login(ipaddress, username, password, rest_client).getToken();

        return true;
    }

    public Bucket updateBucket(String ipaddress, Bucket body) throws IOException, JsonParseException {
	    return Buckets.updateBucket(ipaddress, body, token, rest_client);
    }
}

