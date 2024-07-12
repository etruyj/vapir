//===================================================================
// ConfigureSphere.java
//      Description:
//          This command automates the configuration of a Vail Sphere
//          with information provided in a configuration file.
//
//      Processes:
//          1.) Attach accounts
//          2.) Attach storage
//          3.) Configure lifecycles
//          4.) Create buckets
//
//  Created by Sean Snyder
//===================================================================

package com.spectralogic.vail.vapir.command;

import com.spectralogic.vail.vapir.api.VailConnector;
import com.spectralogic.vail.vapir.model.Account;
import com.spectralogic.vail.vapir.model.Bucket;
import com.spectralogic.vail.vapir.model.Endpoint;
import com.spectralogic.vail.vapir.model.Group;
import com.spectralogic.vail.vapir.model.Lifecycle;
import com.spectralogic.vail.vapir.model.SphereConfig;
import com.spectralogic.vail.vapir.model.Storage;
import com.spectralogic.vail.vapir.model.Summary;
import com.spectralogic.vail.vapir.model.blackpearl.BpDataPolicy;
import com.spectralogic.vail.vapir.model.requests.EndpointStorageRequest;
import com.spectralogic.vail.vapir.util.map.MapAccounts;
import com.spectralogic.vail.vapir.util.map.MapDataPolicies;
import com.spectralogic.vail.vapir.util.map.MapEndpoints;
import com.spectralogic.vail.vapir.util.map.MapLifecycle;
import com.spectralogic.vail.vapir.util.map.MapStorage;

import com.google.gson.Gson;
// Removing YAML support for now
//import org.yaml.snakeyaml.Yaml;
//import org.yaml.snakeyaml.constructor.Constructor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigureSphere
{
    private static final Logger log = LoggerFactory.getLogger(ConfigureSphere.class);

	public static int addAccounts(VailConnector sphere, String ip_address, ArrayList<Account> accounts)
	{
		// Returns the number of successfully added accounts
		// Can use this to measure error rate.

		String json_body;
		Account account_verify;
		int success_count = 0;

		Gson gson = new Gson();

		log.info("Adding (" + accounts.size() + ") accounts to Sphere...");

		for(int i=0; i<accounts.size(); i++)
		{
			json_body = gson.toJson(accounts.get(i), Account.class);

            try {
			    account_verify = AddAccount.fromFields(ip_address, accounts.get(i).getRoleArn(), accounts.get(i).getExternalId(), accounts.get(i).getUsername(), accounts.get(i).getDescription(), sphere);
			    if(account_verify != null)
			    {
				    success_count++;
			    }
            } catch(Exception e) {
                log.warn(e.getMessage());
            }
		}

		log.warn("Successfully added (" + success_count + ") accounts.");
	    log.warn("Failed to add (" + String.valueOf(accounts.size() - success_count) + ") accounts.");

		return success_count;	
	}

	public static int addBuckets(VailConnector sphere, String ip_address, ArrayList<Bucket> buckets, HashMap<String, String> account_map, HashMap<String, String> lifecycle_map)
	{
		// Returns the number of successfully added accounts
		// Can use this to measure error rate.

		Bucket bucket_verify;
		int success_count = 0;
		boolean account_found = true;
		boolean lifecycle_found = true;

		log.info("Adding (" + buckets.size() + ") buckets to Sphere...");

		for(int i=0; i<buckets.size(); i++)
		{
			// Default settings.
			if(buckets.get(i).isLocking())
			{
				log.warn("Bucket [" + buckets.get(i).getName() + "] has object locking enabled.");
				log.warn("Creating bucket without object locking. Locking can be configured by editing the bucket on the UI.");
				System.err.println("WARNING: Bucket [" + buckets.get(i).getName() + "] has object locking enabled.");
				System.err.println("WARNING: Creating bucket without object locking. Locking can be configured by editing the bucket on the UI.");
				
				buckets.get(i).setLocking(false);
				buckets.get(i).setDefaultRetention(null);
			}

			if(buckets.get(i).getControl() == null)
			{
				log.warn("Bucket control not specified. Setting to the recommended BucketOwnerEnforce.");
				System.err.println("WARNING: Bucket control not specified. Setting to the recommended BucketOwnerEnforced.");
				buckets.get(i).setControl("BucketOwnerEnforced");
			}

			// Find account Canonical ID
			if(account_map.get(buckets.get(i).getOwner()) != null)
			{
				buckets.get(i).setOwner(account_map.get(buckets.get(i).getOwner()));
			    account_found = true;
            }
			else
			{
				log.error("Unabled to find account [" + buckets.get(i).getOwner() + "] for bucket " + buckets.get(i).getName());
				account_found = false;
			}

			// Convert lifecycle names to lifecycle identifiers
			if(lifecycle_map.get(buckets.get(i).getLifecycle()) != null)
			{
				buckets.get(i).setLifecycle(lifecycle_map.get(buckets.get(i).getLifecycle()));
			    lifecycle_found = true;
            }
			else if(buckets.get(i).getLifecycle() != null && !buckets.get(i).getLifecycle().equals("none")) // no need to do anything for none.
			{
				log.error("ERROR: Unable to find lifecycle [" + buckets.get(i).getLifecycle() + "] for bucket " + buckets.get(i).getName());

				lifecycle_found = false;

			}

			// Proceed only if translation was successful.
			if(account_found && lifecycle_found)
			{
                log.info("Attempting to create bucket " + buckets.get(i).getName());
				bucket_verify = CreateBucket.create(ip_address, buckets.get(i), sphere);
				if(bucket_verify != null)
				{
					success_count++;
				}
			}
		}

		log.warn("Successfully added (" + success_count + ") buckets.");
	    log.warn("Failed to add (" + String.valueOf(buckets.size() - success_count) + ") buckets.");

		return success_count;	
	}

	public static int addGroups(VailConnector sphere, String ip_address, ArrayList<Summary> groups, HashMap<String, String> name_id_map)
	{
		int success_count = 0;
		String response;
		String[] account_pair = new String[2];
		account_pair[0] = "";

		log.info("Adding (" + groups.size() + ") groups to Sphere...");

		for(int i=0; i<groups.size(); i++)
		{
			// Find account id

			if(!groups.get(i).getAccountName().equals(account_pair[0]))
			{
				account_pair[0] = groups.get(i).getAccountName();

				if(name_id_map.get(account_pair[0]) == null)
				{
					account_pair[1] = "none";
				}
				else
				{
					account_pair[1] = name_id_map.get(account_pair[0]);
				}
			}
			
			// Create account

			if(!account_pair[1].equals("none"))
			{
                Group group = CreateGroup.create(ip_address, groups.get(i).getName(), account_pair[1], sphere);
				if(group != null)
				{
					success_count++;
				}
			
			}
		}

		log.info("Successfully created (" + success_count + ") groups");
		log.warn("Failed to create (" + String.valueOf(groups.size() - success_count) + ") groups.");

		return success_count;
	}

	public static int addLifecycles(VailConnector sphere, String ip_address, ArrayList<Lifecycle> lifecycles, HashMap<String, String> storage_map)
	{
		// Returns the number of successfully added accounts
		// Can use this to measure error rate.

		String json_body;
		Lifecycle lifecycle_verify;
		int success_count = 0;
		boolean storage_found_successfully = true;

		Gson gson = new Gson();

		log.info("Adding (" + lifecycles.size() + ") lifecycles to Sphere...");

		for(int i=0; i<lifecycles.size(); i++)
		{
			// Convert storage location names to storage identifiers
			for(int r=0; r<lifecycles.get(i).getRules().size(); r++)
			{
				for(int s=0; s<lifecycles.get(i).getRule(r).getDestinations().getStorage().size(); s++)
				{
					if(storage_map.get(lifecycles.get(i).getRule(r).getDestinations().getStorage().get(s)) != null)
					{
						lifecycles.get(i).getRule(r).getDestinations().getStorage().set(s, 
storage_map.get(lifecycles.get(i).getRule(r).getDestinations().getStorage().get(s)));
					}
					else
					{
						log.error("ERROR: Unable to find storage location (" + lifecycles.get(i).getRule(r).getDestinations().getStorage().get(s) + ").");

						storage_found_successfully = false;
					}
				}
			}	

			// Proceed only if translation was successful.
			if(storage_found_successfully)
			{
				lifecycles.get(i).setTypeMarkers();
                lifecycle_verify = CreateLifecycle.create(sphere, ip_address, lifecycles.get(i).getName(), lifecycles.get(i).getDescription(), lifecycles.get(i).getMarkers(), lifecycles.get(i).getUploads(), lifecycles.get(i).getRules());
                if(lifecycle_verify != null)
				{
					success_count++;
				}
			}
		}

		log.warn("Successfully added (" + success_count + ") lifecycless.");
	    log.warn("Failed to add (" + String.valueOf(lifecycles.size() - success_count) + ") lifecycles.");

		return success_count;	
	}

	public static int addStorage(VailConnector sphere, String ip_address, ArrayList<Storage> locations)
	{
		// Returns the number of successfully added accounts
		// Can use this to measure error rate.

		String json_body;
		Storage storage_verify;
		int success_count = 0;

		// Get a list of endpoints associated with the system
		// And their associated data policies.
        ArrayList<Endpoint> endpoints = new ArrayList<Endpoint>(); 
        try {
            endpoints = ListEndpoints.blackpearl(sphere);
        } catch(Exception e) {
            log.error(e.getMessage());
            log.error("Failed to list endpoints.");
        }
		
        HashMap<String, Endpoint> endpoint_map = MapEndpoints.createNameObjectMap(endpoints);
        // Build a map of available data policies.
        // endpoint_dp_map will be a hashmap of endpoint_id to the dp_map <data policy name, data policy id>
        HashMap<String, HashMap<String, String>> endpoint_dp_map = new HashMap<String, HashMap<String, String>>();
        ArrayList<BpDataPolicy> dp_list = null;

        try {
            log.info("Pulling a list of Vail-compliant data policies associated with the (" + endpoints.size() + ") endpoints.");
            
            for(Endpoint endpoint : endpoints) {
                dp_list = sphere.listEndpointDataPolicies(endpoint.getId());
                log.info("Found (" + dp_list.size() + ") data policies on the endpoint " + endpoint.getName());

                endpoint_dp_map.put(endpoint.getId(), MapDataPolicies.createNameIdMap(dp_list));
            }
        } catch(Exception e) {
            log.error(e.getMessage());
        }
        
        String data_policy_id = "";
        String endpoint_id = "";

		log.info("Adding (" + locations.size() + ") storage locations to Sphere...");

		for(int i=0; i<locations.size(); i++)
		{
            try {
                // Update user-friendly fields with ids.
                endpoint_id = endpoint_map.get(locations.get(i).getEndpoint()).getId();
                data_policy_id = endpoint_dp_map.get(endpoint_id).get(locations.get(i).getItem());
                
                locations.get(i).setEndpoint(endpoint_id);
                locations.get(i).setItem(data_policy_id);
                
                if(locations.get(i).getTarget() != null) {
                    if(locations.get(i).getTarget().equals("bppolicy")) {
                        storage_verify = AddStorage.createWithDataPolicy(locations.get(i), sphere);
                    } else {
                        throw new Exception("Unrecognized target type " + locations.get(i).getTarget());
                    }
                } else {
                    throw new Exception("Legacy storage type not supported.");
                }
			    
                if(storage_verify != null)
			    {
				    success_count++;
			    }
            } catch(Exception e) {
                log.error(e.getMessage());
                log.error("Failed to create endpoint [" + locations.get(i).getName() + "]");
            }

		}

		log.warn("Successfully added (" + success_count + ") storage locations.");
	    log.warn("Failed to add (" + String.valueOf(locations.size() - success_count) + ") storage locations.");

		return success_count;	
	}

	public static ArrayList<String> buildSphere(VailConnector sphere, String ip_address, SphereConfig config)
	{
		int[] success = new int[5];
		ArrayList<String> report = new ArrayList<String>();

		// Error Handling on import
		if(config.getAccounts() == null) { config.setAccounts(new ArrayList<Account>()); }
		if(config.getGroups() == null) { config.setGroups(new ArrayList<Summary>()); }
		if(config.getStorage() == null) { config.setStorage(new ArrayList<Storage>()); }
		if(config.getLifecycles() == null) { config.setLifecycles(new ArrayList<Lifecycle>()); }
		if(config.getBuckets() == null) { config.setBuckets(new ArrayList<Bucket>()); }

		if(config != null)
		{
			log.info("Loading complete.");
			log.info("Found (" + config.getAccounts().size() + ") accounts.");
			log.info("Found (" + config.getGroups().size() + ") groups");
			log.info("Found (" + config.getStorage().size() + ") storage locations.");
			log.info("Found (" + config.getLifecycles().size() + ") lifecycle rules.");
			log.info("Found (" + config.getBuckets().size() + ") buckets.");

		}
		
        // Add accounts
		success[0] = addAccounts(sphere, ip_address, config.getAccounts());
		
		// Create a list of all accounts.
		// Using the api to get the list instead of config allows
		// for using the configure sphere to add to the config instead of 
		Account[] accounts = ListAccounts.all(ip_address, sphere);
		HashMap<String, String> account_name_id_map = MapAccounts.createNameIDMap(accounts);
		HashMap<String, String> account_name_canon_map = MapAccounts.createNameCanonicalIDMap(accounts);


		// Add groups
		success[1] = addGroups(sphere, ip_address, config.getGroups(), account_name_id_map);

		// Add storage
		success[2] = addStorage(sphere, ip_address, config.getStorage());
		Storage[] locations = new Storage[0];
        try {
            locations = sphere.listStorage(ip_address);
        } catch(Exception e) {
            log.error(e.getMessage());
            log.error("Failed to list storage locations.");
        }
		
        HashMap<String, String> map = MapStorage.createNameIdMap(locations);

		// Add lifecycles
		success[3] = addLifecycles(sphere, ip_address, config.getLifecycles(), map);
		Lifecycle[] lifecycles = new Lifecycle[0];
        try {
        lifecycles = sphere.listLifecycles(ip_address);
        } catch(Exception e) {
            log.error(e.getMessage());
            log.error("Failed to list lifecycles.");
        }
        HashMap<String, String> lifecycle_map = MapLifecycle.createNameIdMap(lifecycles);

		// Add buckets
		success[4] = addBuckets(sphere, ip_address, config.getBuckets(), account_name_canon_map, lifecycle_map);
	
		report.add("Configuration complete.");
		report.add("Added " + success[0] + "/" + config.getAccounts().size() + " accounts.");
		report.add("Created " + success[1] + "/" + config.getGroups().size() + " groups.");
		report.add("Added " + success[2] + "/" + config.getStorage().size() + " storage locations.");
		report.add("Created " + success[3] + "/" + config.getLifecycles().size() + " lifecycle rules.");
		report.add("Created " + success[4] + "/" + config.getBuckets().size() + " buckets.");

		return report;
	}

	public static SphereConfig importJSONConfigFile(String filename)
	{
		Gson gson = new Gson();
		
		StringBuilder json = new StringBuilder();

		try
		{
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));

			String line = null;

			while((line = br.readLine()) != null)
			{
				json.append(line);
				json.append("\n");
			}

			SphereConfig config = gson.fromJson(json.toString(), SphereConfig.class);

			return config;
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
		
			return null;
		}
	}
/*	
	public static SphereConfig importConfigFile(String filename)
	{
		Yaml yaml = new Yaml(new Constructor(SphereConfig.class));
		try
		{
			InputStream iStream = new FileInputStream(new File(filename));

			SphereConfig config = yaml.load(iStream);

			System.out.println("Accounts: " + config.accounts.length);
			System.out.println("Lifecycles: " + config.lifecycles.length);
			System.out.println("Buckets: " + config.buckets.length);
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
		}

		return null;
	}
*/
	public static ArrayList<String> start(VailConnector sphere, String ip_address, String filename)
	{
		ArrayList<String> report = new ArrayList<String>();

		log.info("Starting configuration of Vail Sphere (" + ip_address + ")...");
		log.info("Loading configuration file " + filename);

		SphereConfig config = importJSONConfigFile(filename);
        if(config != null) {
			report = buildSphere(sphere, ip_address, config);

			return report;
        }
		else
		{
			log.error("ERROR: Unable to load config file.");
	
			report.add("ERROR: Unable to load config file.");

			return report;
		}
	}
}

