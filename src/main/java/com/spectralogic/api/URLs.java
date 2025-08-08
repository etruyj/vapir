package com.spectralogic.vail.vapir.api;

import com.socialvagrancy.utils.http.OpenApiPathLoader;

import java.util.Map;

public class URLs {
    private static Map<String, String> apiMap; // map the desired calls to the current api paths.

    public static void load(Map<String, String> commands) {
        // Set the command map.
        apiMap = commands;
    }

    //===========================================
    // API Paths
    //===========================================
    public static String getPath(String command, String ipaddress) throws Exception {
        if(apiMap.get(command) == null) {
            throw new Exception("API Path [" + command + "] is not defined in the configuration file.");
        }

        String path = OpenApiPathLoader.get(ipaddress, apiMap.get(command));

        return "https://" + path;
    }

	public static String accountsURL(String ipaddress)
	{
		return "https://" + ipaddress + "/sl/api/accounts";
	}

    public static String activateURL(String ipaddress) {
        return "https://" + ipaddress + "/sl/api/sphere/activate";
    }

	public static String blackpearlLoginURL(String url)
	{
		// No https:// as vail stores the domain name of the 
		// endpoint with http(s)://.
		return url + "/api/tokens.json";
	}

	public static String blackpearlUserKeyURL(String url, String id)
	{
		// No https:// as vail stores the domain name of the 
		// endpoint with http(s)://.
		return url + "/api/ds3/keys?user_id=" + id;
	}

	public static String blackpearlUsersListURL(String url)
	{
		// No https:// as vail stores the domain name of the 
		// endpoint with http(s)://.
		return url + "/api/users";
	}

	public static String bucketsURL(String ipaddress)
	{
		return "https://" + ipaddress + "/sl/api/buckets";
	}

    public static String capacitySummarySphereURL(String ipaddress) 
    {
        return "https://" + ipaddress + "/sl/api/capacity/sphere/summary";
    }

	public static String clearCacheURL(String ipaddress)
	{
		return "https://" + ipaddress + "/sl/api/iam/users/clear_cache";
	}

    public static String clonesURL(String ipaddress, String bucket_name, String object_key) {
        return getBaseApiUrl(ipaddress) + "/buckets/" + bucket_name + "/clones?object=" + object_key;
    }

	public static String createGroupURL(String ipaddress, String account_id, String group)
	{
		return "https://" + ipaddress + "/sl/api/iam/groups/" + account_id + "/" + group;
	}

	public static String endpointsURL(String ipaddress)
	{
		return "https://" + ipaddress + "/sl/api/endpoints";
	}

    public static String endpointDataPoliciesURL(String ipaddress, String endpoint_id) {
        return "https://" + ipaddress + "/sl/api/targets/" + endpoint_id + "/bppolicy";
    }

    public static String getBaseApiUrl(String ipaddress) {
        return "https://" + ipaddress + "/sl/api";
    }

    public static String getBucketURL(String ipaddress, String bucket) {
        return  getBaseApiUrl(ipaddress) + "/buckets/" + bucket;
    }

    public static String getLifecycleURL(String ipaddress, String lifecycle) {
        return getBaseApiUrl(ipaddress) + "/lifecycles/" + lifecycle;
    }

	public static String groupsURL(String ipaddress, String account)
	{
		return "https://" + ipaddress + "/sl/api/iam/groups/" + account;
	}

	public static String keysURL(String ipaddress, String account, String user)
	{
		return "https://" + ipaddress + "/sl/api/iam/users/" + account + "/" + user + "/keys";
	}

	public static String keysDeleteURL(String ipaddress, String account, String user, String key)
	{
		return "https://" + ipaddress + "/sl/api/iam/users/" + account + "/" + user + "/keys/" + key;
	}

	public static String lifecycleURL(String ipaddress)
	{
		return "https://" + ipaddress + "/sl/api/lifecycles";
	}

	public static String loginURL(String ipaddress)
	{
		return "https://" + ipaddress + "/sl/api/tokens";
	}

    public static String objectsURL(String ipaddress, String bucket_id) {
        return getBaseApiUrl(ipaddress) + "/buckets/" + bucket_id + "/objects";
    }

	public static String storageURL(String ipaddress)
	{
		return "https://" + ipaddress + "/sl/api/storage";
	}

	public static String usersURL(String ipaddress, String account_id)
	{
		return "https://" + ipaddress + "/sl/api/iam/users/" + account_id;
	}

	public static String getUserURL(String ipaddress, String account_id, String username)
	{
		return "https://" + ipaddress + "/sl/api/iam/users/" + account_id + "/" + username;
	}

	public static String userGroupsURL(String ipaddress, String account_id, String username)
	{
		return "https://" + ipaddress + "/sl/api/iam/users/" + account_id + "/" + username + "/groups";
	}

}
