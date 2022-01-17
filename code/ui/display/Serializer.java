//===================================================================
// Serializer.java
// 	Description: This class takes the various output formats
// 	presented by the different functions and coverts them to a
// 	generic output to allow for easier display and display options,
// 	an ArrayList<OuputFormat>.
//===================================================================

package com.socialvagrancy.vail.ui.display;

import com.socialvagrancy.vail.structures.Account;
import com.socialvagrancy.vail.structures.Bucket;
import com.socialvagrancy.vail.structures.OutputFormat;
import com.socialvagrancy.vail.structures.Summary;
import com.socialvagrancy.vail.structures.User;

import java.util.ArrayList;

public class Serializer
{
	public static ArrayList<OutputFormat> convert(Account[] accounts)
	{
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

	public static ArrayList<OutputFormat> convert(Bucket[] buckets)
	{
		ArrayList<OutputFormat> output = new ArrayList<OutputFormat>();
		OutputFormat line;

		line = new OutputFormat();
		line.header = "bucket";
		line.value = null;
		output.add(line);
		
		for(int i=0; i< buckets.length; i++)
		{
		
				
			line = new OutputFormat();
			line.header = "bucket>lifecycle";
			line.value = buckets[i].lifecycle;
			output.add(line);
		
			line = new OutputFormat();
			line.header = "bucket>restore";
			
			if(buckets[i].restore)
			{
				line.value = "TRUE";
			}
			else
			{
				line.value = "FALSE";
			}
			output.add(line);
		
			for(int j=0; j<buckets[i].acls.length; j++)
			{
				line = new OutputFormat();
				line.header = "bucket>acls>type";
				line.value = buckets[i].acls[j].type;
				output.add(line);
		
				line = new OutputFormat();
				line.header = "bucket>acls>id";
				line.value = buckets[i].acls[j].id;
				output.add(line);
		
				for(int k=0; k<buckets[i].acls[j].permissions.length; k++)
				{
					line = new OutputFormat();
					line.header = "bucket>acls>permissions>permission";
					line.value = buckets[i].acls[j].permissions[k];
					output.add(line);
				}
			}

			line = new OutputFormat();
			line.header = "bucket>owner";
			line.value = buckets[i].owner;
			output.add(line);
		
			line = new OutputFormat();
			line.header = "bucket>name";
			line.value = buckets[i].name;
			output.add(line);
		
			line = new OutputFormat();
			line.header = "bucket>created";
			line.value = buckets[i].created;
			output.add(line);
		}
			
		line = new OutputFormat();
		line.header = "bucket";
		line.value = null;
		output.add(line);

		return output;
	}

	public static ArrayList<OutputFormat> convert(String response)
	{
		ArrayList<OutputFormat> output = new ArrayList<OutputFormat>();

		OutputFormat line = new OutputFormat();
		line.header = "response";
		line.value = null;
		output.add(line);

		line = new OutputFormat();
		line.header = "message";
		line.value = response;
		output.add(line);

		line = new OutputFormat();
		line.header = "response";
		line.value = null;
		output.add(line);

		return output;
	}

	public static ArrayList<OutputFormat> convert(User[] users)
	{
		ArrayList<OutputFormat> output = new ArrayList<OutputFormat>();
		OutputFormat line; 

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
	
		return output;
	}

	public static ArrayList<OutputFormat> convert(ArrayList<Summary> list)
	{
		ArrayList<OutputFormat> output = new ArrayList<OutputFormat>();
		OutputFormat line; 

		for(int i=0; i<list.size(); i++)
		{
			line = new OutputFormat();
			line.header = list.get(i).type;
			line.value = null;
			output.add(line);

			line = new OutputFormat();
			line.header = list.get(i).type + ">username";
			line.value = list.get(i).name;
			output.add(line);

			line = new OutputFormat();
			line.header = list.get(i).type + ">accountName";
			line.value = list.get(i).account_name;
			output.add(line);

			line = new OutputFormat();
			line.header = list.get(i).type + ">accountId";
			line.value = list.get(i).account_id;
			output.add(line);

			if(list.get(i).status != null)
			{
				line = new OutputFormat();
				line.header = list.get(i).type + ">status";
				line.value = list.get(i).status;
				output.add(line);
			}

			line = new OutputFormat();
			line.header = list.get(i).type;
			line.value = null;
			output.add(line);
		
		}

		return output;
	}
}
