//===================================================================
//	VailController.java
//		Description:
//			This is the controller for the Vail Sphere
//			commands defined in the commands/ directory.
//			This class abstracts the individual command
//			layer and performs necessary translations
//			between the interface layer and the command
//			layer.
//===================================================================

package com.socialvagrancy.vail.utils;

import com.socialvagrancy.vail.commands.AdvancedCommands;
import com.socialvagrancy.vail.commands.BasicCommands;
import com.socialvagrancy.vail.structures.Account;
import com.socialvagrancy.vail.structures.Bucket;
import com.socialvagrancy.vail.structures.BucketSummary;
import com.socialvagrancy.vail.structures.OutputFormat;
import com.socialvagrancy.vail.structures.User;
import com.socialvagrancy.vail.structures.UserKey;
import com.socialvagrancy.vail.structures.UserSummary;
import com.socialvagrancy.utils.Logger;

import java.util.ArrayList;

public class VailController
{
	private BasicCommands sphere;
	private AdvancedCommands advanced;
	private String token;
	private Logger logbook;

	public VailController()
	{
		logbook = new Logger("../logs/vail_api.log", 102400, 1, 1);
		sphere = new BasicCommands(logbook);
		advanced = new AdvancedCommands(sphere, logbook);
	}

	public String clearCache(String ip_address)
	{
		return sphere.clearCache(ip_address);
	}

	public void findMinIAMPermissions(String ip_address)
	{
		advanced.minimumIAMPermissions(ip_address);
	}

	public ArrayList<OutputFormat> listAccounts(String ip_address)
	{
		Account[] accounts = sphere.listAccounts(ip_address);
		ArrayList<OutputFormat> output = new ArrayList<OutputFormat>();
		OutputFormat line;

		for(int i=0; i<accounts.length; i++)
		{
			line = new OutputFormat();
			line.header = "account";
			line.value = null;
			output.add(line);
			
			line = new OutputFormat();
			line.header = "account>id";
			line.value = accounts[i].id;
			output.add(line);
			
			line = new OutputFormat();
			line.header = "account>canonicalId";
			line.value = accounts[i].canonicalId;
			output.add(line);
			
			line = new OutputFormat();
			line.header = "account>externalId";
			line.value = accounts[i].externalId;
			output.add(line);
			
			line = new OutputFormat();
			line.header = "account>roleArn";
			line.value = accounts[i].roleArn;
			output.add(line);
			
			line = new OutputFormat();
			line.header = "account>username";
			line.value = accounts[i].username;
			output.add(line);
			
			line = new OutputFormat();
			line.header = "account>email";
			line.value = accounts[i].email;
			output.add(line);
			
			line = new OutputFormat();
			line.header = "account";
			line.value = null;
			output.add(line);
		}
		
		return output;
	}

	public ArrayList<OutputFormat> listBuckets(String ip_address, String account)
	{
		ArrayList<OutputFormat> output = new ArrayList<OutputFormat>();
		OutputFormat line;

		if(account.equals("none"))
		{
			Bucket[] buckets = sphere.listBuckets(ip_address);

			for(int i=0; i< buckets.length; i++)
			{
				System.out.println("BUCKET");
				System.out.println(buckets[i].lifecycle);
				System.out.println(buckets[i].restore);

				for(int j=0; j<buckets[i].acls.length; j++)
				{
					System.out.println("\t" + buckets[i].acls[j].type);
					System.out.println("\t" + buckets[i].acls[j].id);
					System.out.println("\tPermissions");
					
					for(int k=0; k<buckets[i].acls[j].permissions.length; k++)
					{
						System.out.println("\t\t" + buckets[i].acls[j].permissions[k]);
					}
				}

				System.out.println(buckets[i].owner);
				System.out.println(buckets[i].name);
				System.out.println(buckets[i].created);	
			}
		}
		else
		{
			ArrayList<BucketSummary> bucket_list = advanced.filteredBucketList(ip_address, account);

			for(int i=0; i<bucket_list.size(); i++)
			{
				line = new OutputFormat();
				line.header = "bucket";
				line.value = null;
				output.add(line);
	
				line = new OutputFormat();
				line.header = "bucket>name";
				line.value = bucket_list.get(i).name;
				output.add(line);

				line = new OutputFormat();
				line.header = "bucket>accountName";
				line.value = bucket_list.get(i).account_name;
				output.add(line);

				line = new OutputFormat();
				line.header = "bucket>accountId";
				line.value = bucket_list.get(i).account_id;
				output.add(line);

				line = new OutputFormat();
				line.header = "bucket";
				line.value = null;
				output.add(line);
			}
		}

		return output;
	}

	public ArrayList<OutputFormat> listUsers(String ip_address, String account, boolean active_only)
	{
		ArrayList<OutputFormat> output = new ArrayList<OutputFormat>();
		OutputFormat line; 

		if(!active_only && account.equals("none"))
		{
			 User[] users = sphere.listUsers(ip_address);
		
			for(int i=0; i<users.length; i++)
			{
				line = new OutputFormat();
				line.header = "user";
				line.value = null;
				output.add(line);
				
				line = new OutputFormat();
				line.header = "user>username";
				line.value = users[i].username;
				output.add(line);
				
				line = new OutputFormat();
				line.header = "user>accountId";
				line.value = users[i].accountid;
				output.add(line);
				
				line = new OutputFormat();
				line.header = "user";
				line.value = null;
				output.add(line);
			}
		}
		else
		{
			// More complex code.
			ArrayList<UserSummary> summary = advanced.filteredUserList(ip_address, account, active_only);

			for(int i=0; i<summary.size(); i++)
			{
				line = new OutputFormat();
				line.header = "user";
				line.value = null;
				output.add(line);

				line = new OutputFormat();
				line.header = "user>username";
				line.value = summary.get(i).username;
				output.add(line);

				line = new OutputFormat();
				line.header = "user>accountName";
				line.value = summary.get(i).account_name;
				output.add(line);

				line = new OutputFormat();
				line.header = "user>accountId";
				line.value = summary.get(i).account_id;
				output.add(line);

				line = new OutputFormat();
				line.header = "user>status";
				line.value = summary.get(i).status;
				output.add(line);

				line = new OutputFormat();
				line.header = "user";
				line.value = null;
				output.add(line);
			}
		}

		return output;
	}

	public OutputFormat[] listKeys(String ip_address, String account, String user)
	{
		sphere.listUserKeys(ip_address, account, user);
		return null;
	}

	public boolean login(String ip_address, String username, String password)
	{
		return sphere.login(ip_address, username, password);
	}

}
