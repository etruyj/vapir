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
import com.socialvagrancy.vail.structures.Account;
import com.socialvagrancy.vail.structures.Bucket;
import com.socialvagrancy.vail.structures.Lifecycle;
import com.socialvagrancy.vail.structures.SphereConfig;
import com.socialvagrancy.vail.structures.Storage;
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

	public static int addBuckets(BasicCommands sphere, String ip_address, Bucket[] buckets, HashMap<String, String> lifecycle_map, Logger logbook)
	{
		// Returns the number of successfully added accounts
		// Can use this to measure error rate.

		String json_body;
		Bucket temp_bucket;
		Bucket bucket_verify;
		int success_count = 0;
		boolean lifecycle_found_successfully = true;

		logbook.logWithSizedLogRotation("Adding (" + buckets.length + ") buckets to Sphere...", 1);

		for(int i=0; i<buckets.length; i++)
		{
			// Convert lifecycle names to lifecycle identifiers
			if(lifecycle_map.get(buckets[i].lifecycle) != null)
			{
				buckets[i].lifecycle = lifecycle_map.get(buckets[i].lifecycle);
			}
			else if(!buckets[i].lifecycle.equals("none")) // no need to do anything for none.
			{
				logbook.logWithSizedLogRotation("ERROR: Unable to find lifecycle [" + buckets[i].lifecycle + "]", 3);

				lifecycle_found_successfully = false;

			}

			// Proceed only if translation was successful.
			if(lifecycle_found_successfully)
			{
				bucket_verify = Buckets.createForAccount(sphere, ip_address, buckets[i], logbook);
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
		int[] success = new int[4];
		ArrayList<String> report = new ArrayList<String>();

		// Add accounts
		success[0] = addAccounts(sphere, ip_address, config.accounts, logbook);

		// Add storage
		success[1] = addStorage(sphere, ip_address, config.storage, logbook);

		// Add lifecycles
		Storage[] locations = sphere.listStorage(ip_address);
		HashMap<String, String> map = StorageLocations.map(locations);
		success[2] = addLifecycles(sphere, ip_address, config.lifecycles, map, logbook);

		// Add buckets
		Lifecycle[] lifecycles = sphere.listLifecycles(ip_address);
		map = Lifecycles.map(lifecycles);
		success[3] = addBuckets(sphere, ip_address, config.buckets, map, logbook);
	
		report.add("Configuration complete.");
		report.add("Added " + success[0] + "/" + config.accounts.length + " accounts.");
		report.add("Added " + success[1] + "/" + config.storage.length + " storage locations.");
		report.add("Created " + success[2] + "/" + config.lifecycles.length + " lifecycle rules.");
		report.add("Created " + success[3] + "/" + config.buckets.length + " buckets.");

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

		logbook.logWithSizedLogRotation("Starting configuration of Vail Sphere (" + ip_address + ")...", 1);
	
		logbook.logWithSizedLogRotation("Loading configuration file " + filename, 1);

		SphereConfig config = importJSONConfigFile(filename);

		if(config != null)
		{
			logbook.logWithSizedLogRotation("Loading complete.", 1);
			logbook.logWithSizedLogRotation("Found (" + config.accounts.length + ") accounts.", 1);
			logbook.logWithSizedLogRotation("Found (" + config.storage.length + ") storage locations.", 1);
			logbook.logWithSizedLogRotation("Found (" + config.lifecycles.length + ") lifecycle rules.", 1);
			logbook.logWithSizedLogRotation("Found (" + config.buckets.length + ") buckets.", 1);

			report = buildSphere(sphere, ip_address, config, logbook);

			return report;
		}
		else
		{
			logbook.logWithSizedLogRotation("ERROR: Unable to load config file.", 3);
	
			report.add("ERROR: Unable to load config file.");

			return report;
		}
	}
}
