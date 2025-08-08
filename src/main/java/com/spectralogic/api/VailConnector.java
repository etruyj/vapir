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
import com.spectralogic.vail.vapir.model.BucketObjects;
import com.spectralogic.vail.vapir.model.Clone;
import com.spectralogic.vail.vapir.model.Endpoint;
import com.spectralogic.vail.vapir.model.Lifecycle;
import com.spectralogic.vail.vapir.model.Message;
import com.spectralogic.vail.vapir.model.Storage;
import com.spectralogic.vail.vapir.model.Token;
import com.spectralogic.vail.vapir.model.CapacitySummary;
import com.spectralogic.vail.vapir.model.Group;
import com.spectralogic.vail.vapir.model.GroupData;
import com.spectralogic.vail.vapir.model.NodeActivationPacket;
import com.spectralogic.vail.vapir.model.UserData;
import com.spectralogic.vail.vapir.model.User;
import com.spectralogic.vail.vapir.model.UserKey;
import com.spectralogic.vail.vapir.model.VapirConfigModel;
import com.spectralogic.vail.vapir.model.blackpearl.BpDataPolicy;
import com.spectralogic.vail.vapir.model.blackpearl.BPUser;
import com.spectralogic.vail.vapir.model.blackpearl.Ds3KeyPair;
import com.spectralogic.vail.vapir.util.http.RestClient;

import com.socialvagrancy.utils.http.OpenApiPathLoader;

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

    public VailConnector(String ip_address, boolean ignore_ssl, VapirConfigModel config) {
        if(ignore_ssl) {
            log.info("Initializing script with ingore_ssl = true");
        } else {
            log.info("Initializing script with ingore_ssl = false");
        }

        this.ip_address = ip_address;
		rest_client = new RestClient(ignore_ssl);
        
        if(config.getApiPath() != null) {
            log.info("Loading api paths from " + config.getApiPath());
            OpenApiPathLoader.loadJson(config.getApiPath());
        } else {
            log.error("No API paths specified. Please specified API path location in the config file under the apiPath key.");
        }

        if(config.getApiMap() != null) {
            URLs.load(config.getApiMap());
        } else {
            log.error("No API commands specified. Please map API commands in the config file under apiMap key.");
        }
    }

    //===========================================
    // Getters
    //===========================================
    public boolean getConnectionStatus() { 
        if(token != null && token.length() > 0) {
            return true;
        } else {
            return false;
        }
    }
    public String getIpAddress() { return ip_address; }

    //===========================================
    // Setters
    //===========================================
    public void setIpAddress(String ip) { this.ip_address = ip; }


	//===========================================================
	// Commands
	//===========================================================
	
    public boolean activateNode(NodeActivationPacket packet) throws IOException, JsonParseException {
        return ActivateNode.activate(packet, ip_address, token, rest_client);
    }

    @Deprecated // removing ip address requirement
    public Account addAccount(String ipaddress, String json_body) throws IOException, JsonParseException {
	    return addAccount(json_body);
    }

    public Account addAccount(String json_body) throws IOException, JsonParseException {
	    return Accounts.addAccount(ip_address, token, json_body, rest_client);
    }
   
    @Deprecated // removing ip address requirement
	public Account addAccount(String ipaddress, Account account) throws IOException, JsonParseException {
        return addAccount(account);
    }

	public Account addAccount(Account account) throws IOException, JsonParseException {
	    return Accounts.addAccount(ip_address, token, account, rest_client);
    }

    @Deprecated // removing ip address requirement
	public Account addAccount(String ipaddress, String account_name, String email, String external_id, String role_arn) throws IOException, JsonParseException {
        return addAccount(account_name, email, external_id, role_arn);
    }

	public Account addAccount(String account_name, String email, String external_id, String role_arn) throws IOException, JsonParseException {
	    return Accounts.addAccount(ip_address, account_name, email, external_id, role_arn, token, rest_client);
    }

    @Deprecated // removing ip address requirement
	public Storage addStorage(String ipaddress, String name, String body) throws IOException, JsonParseException {
        return addStorage(name, body);
    }

	public Storage addStorage(String name, String body) throws IOException, JsonParseException {
	    return StorageLocations.addStorage(ip_address, name, body, token, rest_client);
    }

    @Deprecated // removing ip address requirement
	public Storage addStorage(String ipaddress, Storage location) throws IOException, JsonParseException {
        return addStorage(location);
    }

	public Storage addStorage(Storage location) throws IOException, JsonParseException {
	    return StorageLocations.addStorage(ip_address, location, token, rest_client);
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

    @Deprecated // removing ip address requirement
	public void clearCache(String ipaddress) throws IOException {
        clearCache();
    }

	public void clearCache() throws IOException {
	    Cache.clearCache(ip_address, token, rest_client);
    }

    @Deprecated // removing ip address requirement
	public Bucket createBucket(String ipaddress, String name, String json_body) throws IOException, JsonParseException {
        return createBucket(name, json_body);
    }

	public Bucket createBucket(String name, String json_body) throws IOException, JsonParseException {
	    return Buckets.createBucket(ip_address, name, json_body, token, rest_client);
    }

    @Deprecated // removing ip address requirement
    public Bucket createBucket(String ipaddress, Bucket bucket) throws IOException, JsonParseException {
        return createBucket(bucket);
    }

    public Bucket createBucket(Bucket bucket) throws IOException, JsonParseException {
        return Buckets.createBucket(ip_address, bucket, token, rest_client);
    }

    @Deprecated // removing ip address requirement
	public Group createGroup(String ipaddress, String name, String account_id) throws IOException, JsonParseException {
        return createGroup(name, account_id);
    }

	public Group createGroup(String name, String account_id) throws IOException, JsonParseException {
	    return Groups.createGroup(ip_address, account_id, name, token, rest_client);
    }

    public Group createGroup(Group group) throws IOException, JsonParseException {
        return Groups.create(group, ip_address, token, rest_client);
    }

    @Deprecated // removing ip address requirement
	public Lifecycle createLifecycle(String ipaddress, String name, String json_body) throws IOException, JsonParseException {
        return createLifecycle(name, json_body);
    }

	public Lifecycle createLifecycle(String name, String json_body) throws IOException, JsonParseException {
	    return Lifecycles.createLifecycle(ip_address, name, json_body, token, rest_client);
    }

    @Deprecated // removing ip address requirement
	public Lifecycle createLifecycle(String ipaddress, Lifecycle lifecycle) throws IOException, JsonParseException {
        return createLifecycle(lifecycle);
    }

	public Lifecycle createLifecycle(Lifecycle lifecycle) throws IOException, JsonParseException {
	    return Lifecycles.createLifecycle(ip_address, lifecycle, token, rest_client);
    }

    @Deprecated // removed the need to have the ip in the call
    public Storage createStorage(Storage storage, String ip_address) throws IOException, JsonParseException {
        return createStorage(storage);
    }

    public Storage createStorage(Storage storage) throws IOException, JsonParseException {
        return StorageLocations.createStorage(storage, ip_address, token, rest_client);
    }

    @Deprecated // removing ip address requirement
	public User createUser(String ipaddress, String account_id, String username) throws IOException, JsonParseException {
        return createUser(account_id, username);
    }

	public User createUser(String account_id, String username) throws IOException, JsonParseException {
	    return Users.createUser(ip_address, account_id, username, token, rest_client);
    }

    @Deprecated // removing ip address requirement
	public UserKey createUserKey(String ipaddress, String account, String user) throws IOException, JsonParseException {
        return createUserKey(account, user);
    }

	public UserKey createUserKey(String account, String user) throws IOException, JsonParseException {
	    return UserKeys.createUserKey(ip_address, account, user, token, rest_client);
    }

    @Deprecated // removing ip address requirement
	public void deleteUser(String ipaddress, String account_id, String username) throws IOException {
        deleteUser(account_id, username);
    }

	public void deleteUser(String account_id, String username) throws IOException {
	    Users.deleteUser(ip_address, account_id, username, token, rest_client);
    }

    @Deprecated // removing ip address requirement
	public void deleteUserKey(String ipaddress, String account, String user, String access_key) throws IOException {
        deleteUserKey(account, user, access_key);
    }

	public void deleteUserKey(String account, String user, String access_key) throws IOException {
	    UserKeys.deleteUserKey(ip_address, account, user, access_key, token, rest_client);
    }

    @Deprecated // removing ip address requirement
    public Bucket getBucket(String ipaddress, String bucket_name) throws IOException, JsonParseException {
        return getBucket(bucket_name);
    }

    public Bucket getBucket(String bucket_name) throws IOException, JsonParseException {
        return Buckets.getBucket(ip_address, bucket_name, token, rest_client);
    }

    public Clone getClones(String bucket_name, String key, String version_id) throws IOException, JsonParseException {
        return Clones.get(ip_address, bucket_name, key, version_id, token, rest_client);
    }

    @Deprecated // removing ip address requirement
    public Lifecycle getLifecycle(String ipaddress, String lifecycle_id) throws IOException, JsonParseException {
        return getLifecycle(lifecycle_id);
    }

    public Lifecycle getLifecycle(String lifecycle_id) throws IOException, JsonParseException {
        return Lifecycles.getLifecycle(ip_address, lifecycle_id, token, rest_client);
    }

    @Deprecated // removing ip address requirement
    public ArrayList<CapacitySummary> getCapacitySummary(String ipaddress) throws IOException, JsonParseException {
        return getCapacitySummary();
    }

    public ArrayList<CapacitySummary> getCapacitySummary() throws IOException, JsonParseException {
        return Capacity.getCapacitySummary(ip_address, token, rest_client);
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

    @Deprecated // removing ip address requirement
	public GroupData listGroups(String ipaddress, String account) throws IOException, JsonParseException {
        return listGroups(account);
    }

	public GroupData listGroups(String account) throws IOException, JsonParseException {
	    return Groups.list(ip_address, account, token, rest_client);
    }

    @Deprecated // removing ip address requirement
	public Lifecycle[] listLifecycles(String ipaddress) throws IOException, JsonParseException {
	    return listLifecycles();
    }

	public Lifecycle[] listLifecycles() throws IOException, JsonParseException {
	    return Lifecycles.list(ip_address, token, rest_client);
    }

    public BucketObjects listObjectsInBucket(String bucket_id, String marker, Integer max_keys) throws IOException, JsonParseException {
        return Objects.list(ip_address, bucket_id, marker, max_keys, token, rest_client);
    }

    @Deprecated // removing ip address requirement
	public Storage[] listStorage(String ipaddress) throws IOException, JsonParseException {
	    return listStorage();
    }

	public Storage[] listStorage() throws IOException, JsonParseException {
	    return StorageLocations.list(ip_address, token, rest_client);
    }

    @Deprecated // removing ip address requirement
	public Group[] listUserGroups(String ipaddress, String account_id, String username) throws IOException, JsonParseException {
	    return listUserGroups(account_id, username);
    }

	public Group[] listUserGroups(String account_id, String username) throws IOException, JsonParseException {
	    return UserGroups.list(ip_address, account_id, username, token, rest_client);
    }

    @Deprecated // removing ip address requirement
	public UserData listUsers(String ipaddress, String account_id) throws IOException, JsonParseException {
	    return listUsers(account_id);
    }

	public UserData listUsers(String account_id) throws IOException, JsonParseException {
	    return Users.list(ip_address, account_id, token, rest_client);
    }

    @Deprecated // removing ip address requirement
	public UserKey[] listUserKeys(String ipaddress, String account, String user) throws IOException, JsonParseException {
	    return listUserKeys(account, user);
    }

	public UserKey[] listUserKeys(String account, String user) throws IOException, JsonParseException {
	    return UserKeys.list(ip_address, account, user, token, rest_client);
    }

    @Deprecated // removing ip address requirement
	public boolean login(String ipaddress, String username, String password) throws IOException, JsonParseException {
	    return login(username, password);
    }

	public boolean login(String username, String password) throws IOException, JsonParseException {
	    this.token = "Bearer " + Tokens.login(ip_address, username, password, rest_client).getToken();
        return true;
    }

    @Deprecated // removing ip address requirement
    public Bucket updateBucket(String ipaddress, Bucket body) throws IOException, JsonParseException {
	    return updateBucket(body);
    }
    
    public Bucket updateBucket(Bucket body) throws IOException, JsonParseException {
	    return Buckets.updateBucket(ip_address, body, token, rest_client);
    }
}

