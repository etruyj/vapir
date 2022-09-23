//===================================================================
// ConfigureSphere.java
// 	Description: Automates the configuraition of a Vail Sphere
// 		with information provided in a YAML config file.
//
// 	Processes:
// 		1.) Attaches accounts.
// 		2.) Attaches storage.
// 		3.) Configures lifecycles.
// 		4.) Creates buckets.
//===================================================================

package com.socialvagrancy.vail.commands.sub;

import com.socialvagrancy.vail.commands.BasicCommands;
import com.socialvagrancy.vail.commands.sub.CreateGroup;
import com.socialvagrancy.vail.structures.Account;
import com.socialvagrancy.vail.structures.Bucket;
import com.socialvagrancy.vail.structures.Lifecycle;
import com.socialvagrancy.vail.structures.SphereConfig;
import com.socialvagrancy.vail.structures.Storage;
import com.socialvagrancy.vail.structures.Summary;
import com.socialvagrancy.vail.utils.map.MapAccounts;
import com.socialvagrancy.utils.Logger;

import com.google.gson.Gson;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.HashMap;

public class ConfigureSphere
{
	public static int addAccounts(BasicCommands sphere, String ip_address, Account[] accounts, Logger logbook)
	{
		// Returns the number of successfully added accounts
		// Can use this to measure error rate.

		String json_body;
		Account account_verify;
		int success_count = 0;

		Gson gson = new Gson();

		logbook.logWithSizedLogRotation("Adding (" + accounts.length + ") accounts to Sphere...", 1);

		for(int i=0; i<accounts.length; i++)
		{
			json_body = gson.toJson(accounts[i], Account.class);

			account_verify = sphere.addAccount(ip_address, accounts[i].username, accounts[i].roleArn, json_body);
			if(account_verify != null)
			{
				success_count++;
			}
		}

		logbook.logWithSizedLogRotation("Successfully added (" + success_count + ") accounts.", 2);
	       	logbook.logWithSizedLogRotation("Failed to add (" + String.valueOf(accounts.length - success_count) + ") accounts.", 2);

		return success_count;	
	}

	public static int addBuckets(BasicCommands sphere, String ip_address, Bucket[] buckets, HashMap<String, String> account_map, HashMap<String, String> lifecycle_map, Logger logbook)
	{
		// Returns the number of successfully added accounts
		// Can use this to measure error rate.

		Bucket bucket_verify;
		int success_count = 0;
		boolean account_found = true;
		boolean lifecycle_found = true;

		logbook.logWithSizedLogRotation("Adding (" + buckets.length + ") buckets to Sphere...", 1);

		for(int i=0; i<buckets.length; i++)
		{
			if(buckets[i].locking)
			{
				logbook.WARN("Bucket [" + buckets[i].name + "] has object locking enabled.");
				logbook.WARN("Creating bucket without object locking. Locking can be configured by editing the bucket on the UI.");
				System.err.println("WARNING: Bucket [" + buckets[i].name + "] has object locking enabled.");
				System.err.println("WARNING: Creating bucket without object locking. Locking can be configured by editing the bucket on the UI.");
				
				buckets[i].locking = false;
				buckets[i].defaultRetention = null;
			}

			// Find account Canonical ID
			if(account_map.get(buckets[i].owner) != null)
			{
				buckets[i].owner = account_map.get(buckets[i].owner);
			}
			else
			{
				logbook.ERR("Unabled to find account [" + buckets[i].owner + "].");
				account_found = false;
			}

			// Convert lifecycle names to lifecycle identifiers
			if(lifecycle_map.get(buckets[i].lifecycle) != null)
			{
				buckets[i].lifecycle = lifecycle_map.get(buckets[i].lifecycle);
			}
			else if(buckets[i].lifecycle != null && !buckets[i].lifecycle.equals("none")) // no need to do anything for none.
			{
				logbook.logWithSizedLogRotation("ERROR: Unable to find lifecycle [" + buckets[i].lifecycle + "]", 3);

				lifecycle_found = false;

			}

			// Proceed only if translation was successful.
			if(account_found && lifecycle_found)
			{
				bucket_verify = CreateBucket.createWithJson(sphere, ip_address, buckets[i], logbook);
				if(bucket_verify != null)
				{
					success_count++;
				}
			}
		}

		logbook.logWithSizedLogRotation("Successfully added (" + success_count + ") buckets.", 2);
	       	logbook.logWithSizedLogRotation("Failed to add (" + String.valueOf(buckets.length - success_count) + ") buckets.", 2);

		return success_count;	
	}

	public static int addGroups(BasicCommands sphere, String ip_address, ArrayList<Summary> groups, HashMap<String, String> name_id_map, Logger logbook)
	{
		int success_count = 0;
		String response;
		String[] account_pair = new String[2];
		account_pair[0] = "";

		logbook.INFO("Adding (" + groups.size() + ") groups to Sphere...");

		for(int i=0; i<groups.size(); i++)
		{
			// Find account id

			if(!groups.get(i).account_name.equals(account_pair[0]))
			{
				account_pair[0] = groups.get(i).account_name;

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
				response = CreateGroup.withAccountID(sphere, ip_address, groups.get(i).name, account_pair[1], logbook);
				if(!response.substring(0, 6).equals("Unable"))
				{
					success_count++;
				}
			
			}
		}

		logbook.INFO("Successfully created (" + success_count + ") groups");
		logbook.WARN("Failed to create (" + String.valueOf(groups.size() - success_count) + ") groups.");

		return success_count;
	}

	public static int addLifecycles(BasicCommands sphere, String ip_address, Lifecycle[] lifecycles, HashMap<String, String> storage_map, Logger logbook)
	{
		// Returns the number of successfully added accounts
		// Can use this to measure error rate.

		String json_body;
		Lifecycle lifecycle_verify;
		int success_count = 0;
		boolean storage_found_successfully = true;

		Gson gson = new Gson();

		logbook.logWithSizedLogRotation("Adding (" + lifecycles.length + ") lifecycles to Sphere...", 1);

		for(int i=0; i<lifecycles.length; i++)
		{
			// Convert storage location names to storage identifiers
			for(int r=0; r<lifecycles[i].rules.length; r++)
			{
				for(int s=0; s<lifecycles[i].rules[r].destinations.storage.length; s++)
				{
					if(storage_map.get(lifecycles[i].rules[r].destinations.storage[s]) != null)
					{
						lifecycles[i].rules[r].destinations.storage[s] = 
storage_map.get(lifecycles[i].rules[r].destinations.storage[s]);
					}
					else
					{
						logbook.logWithSizedLogRotation("ERROR: Unable to find storage location (" + lifecycles[i].rules[r].destinations.storage[s] + ").", 3);

						storage_found_successfully = false;
					}
				}
			}	

			// Proceed only if translation was successful.
			if(storage_found_successfully)
			{
				json_body = gson.toJson(lifecycles[i], Lifecycle.class);

				lifecycle_verify = sphere.createLifecycle(ip_address, lifecycles[i].name, json_body);
				if(lifecycle_verify != null)
				{
					success_count++;
				}
			}
		}

		logbook.logWithSizedLogRotation("Successfully added (" + success_count + ") lifecycless.", 2);
	       	logbook.logWithSizedLogRotation("Failed to add (" + String.valueOf(lifecycles.length - success_count) + ") lifecycles.", 2);

		return success_count;	
	}

	public static int addStorage(BasicCommands sphere, String ip_address, Storage[] locations, Logger logbook)
	{
		// Returns the number of successfully added accounts
		// Can use this to measure error rate.

		String json_body;
		Storage storage_verify;
		int success_count = 0;

		Gson gson = new Gson();

		logbook.logWithSizedLogRotation("Adding (" + locations.length + ") storage locations to Sphere...", 1);

		for(int i=0; i<locations.length; i++)
		{
			json_body = gson.toJson(locations[i], Storage.class);

			storage_verify = sphere.addStorage(ip_address, locations[i].name, json_body);
			if(storage_verify != null)
			{
				success_count++;
			}
		}

		logbook.logWithSizedLogRotation("Successfully added (" + success_count + ") storage locations.", 2);
	       	logbook.logWithSizedLogRotation("Failed to add (" + String.valueOf(locations.length - success_count) + ") storage locations.", 2);

		return success_count;	
	}

	public static ArrayList<String> buildSphere(BasicCommands sphere, String ip_address, SphereConfig config, Logger logbook)
	{
		int[] success = new int[5];
		ArrayList<String> report = new ArrayList<String>();

		// Add accounts
		success[0] = addAccounts(sphere, ip_address, config.accounts, logbook);
		
		// Create a list of all accounts.
		// Using the api to get the list instead of config allows
		// for using the configure sphere to add to the config instead of 
		Account[] accounts = sphere.listAccounts(ip_address);
		HashMap<String, String> account_name_id_map = MapAccounts.createNameIDMap(accounts);
		HashMap<String, String> account_name_canon_map = MapAccounts.createNameCanonicalIDMap(accounts);

		// Add groups
		success[1] = addGroups(sphere, ip_address, config.groups, account_name_id_map, logbook);

		// Add storage
		success[2] = addStorage(sphere, ip_address, config.storage, logbook);
		Storage[] locations = sphere.listStorage(ip_address);
		HashMap<String, String> map = StorageLocations.map(locations);

		// Add lifecycles
		success[3] = addLifecycles(sphere, ip_address, config.lifecycles, map, logbook);
		Lifecycle[] lifecycles = sphere.listLifecycles(ip_address);
		HashMap<String, String> lifecycle_map = Lifecycles.map(lifecycles);

		// Add buckets
		success[4] = addBuckets(sphere, ip_address, config.buckets, account_name_canon_map, lifecycle_map, logbook);
	
		report.add("Configuration complete.");
		report.add("Added " + success[0] + "/" + config.accounts.length + " accounts.");
		report.add("Created " + success[1] + "/" + config.groups.size() + " groups.");
		report.add("Added " + success[2] + "/" + config.storage.length + " storage locations.");
		report.add("Created " + success[3] + "/" + config.lifecycles.length + " lifecycle rules.");
		report.add("Created " + success[4] + "/" + config.buckets.length + " buckets.");

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
	public static ArrayList<String> start(BasicCommands sphere, String ip_address, String filename, Logger logbook)
	{
		ArrayList<String> report = new ArrayList<String>();

		logbook.INFO("Starting configuration of Vail Sphere (" + ip_address + ")...");
		logbook.INFO("Loading configuration file " + filename);

		SphereConfig config = importJSONConfigFile(filename);

		if(config != null)
		{
			logbook.INFO("Loading complete.");
			logbook.INFO("Found (" + config.accounts.length + ") accounts.");
			logbook.INFO("Found (" + config.groups.size() + ") groups");
			logbook.INFO("Found (" + config.storage.length + ") storage locations.");
			logbook.INFO("Found (" + config.lifecycles.length + ") lifecycle rules.");
			logbook.INFO("Found (" + config.buckets.length + ") buckets.");

			report = buildSphere(sphere, ip_address, config, logbook);

			return report;
		}
		else
		{
			logbook.ERR("ERROR: Unable to load config file.");
	
			report.add("ERROR: Unable to load config file.");

			return report;
		}
	}
}
