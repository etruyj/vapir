//===================================================================
// Serializer.java
// 	Description: This class takes the various output formats
// 	presented by the different functions and coverts them to a
// 	generic output to allow for easier display and display options,
// 	an ArrayList<OuputFormat>.
//===================================================================

package com.socialvagrancy.vail.ui.display.serializers;

import com.socialvagrancy.vail.structures.Account;
import com.socialvagrancy.vail.structures.Bucket;
import com.socialvagrancy.vail.structures.BucketSummary;
import com.socialvagrancy.vail.structures.OutputFormat;
import com.socialvagrancy.vail.structures.Storage;
import com.socialvagrancy.vail.structures.Summary;
import com.socialvagrancy.vail.structures.UserSummary;
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
			line.header = "account>name";
			line.value = accounts[i].username;
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
		
				line = new OutputFormat();
				line.header = "bucket>acls>read";
				line.value = boolToString(buckets[i].acls[j].read);
				output.add(line);
			
				line = new OutputFormat();
				line.header = "bucket>acls>readACP";
				line.value = boolToString(buckets[i].acls[j].readACP);
				output.add(line);
			
				line = new OutputFormat();
				line.header = "bucket>acls>write";
				line.value = boolToString(buckets[i].acls[j].write);
				output.add(line);
			
				line = new OutputFormat();
				line.header = "bucket>acls>writeACP";
				line.value = boolToString(buckets[i].acls[j].writeACP);
				output.add(line);
			
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

	public static ArrayList<OutputFormat> convert(Storage[] storage)
	{
		// Displays key info in the storage var
		ArrayList<OutputFormat> output = new ArrayList<OutputFormat>();
		OutputFormat line;

		for(int i=0; i < storage.length; i++)
		{
			line = new OutputFormat();
			line.header = "storage";
			line.value = null;
			output.add(line);

			line = new OutputFormat();
			line.header = "storage>name";
			line.value = storage[i].name;
			output.add(line);

			line = new OutputFormat();
			line.header = "storage>type";
			line.value = storage[i].type;
			output.add(line);

			line = new OutputFormat();
			line.header = "storage>class";
			line.value = storage[i].storageClass;
			output.add(line);

			line = new OutputFormat();
			line.header = "storage>status";
			line.value = storage[i].status;
			output.add(line);
			
			line = new OutputFormat();
			line.header = "storage";
			line.value = null;
			output.add(line);
		}

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

	public static ArrayList<OutputFormat> convert(ArrayList list)
	{
		ArrayList<OutputFormat> output = new ArrayList<OutputFormat>();
		
		if(list.size() > 0)
		{
			if(list.get(0) instanceof BucketSummary)
			{
				output = SerializeBucketSummary.forOutput(list);
			}
			else if(list.get(0) instanceof UserSummary)
			{
				output = SerializeUserSummary.forOutput(list);
			}
			else if(list.get(0) instanceof Summary)
			{
				output = SerializeSummary.forOutput(list);
			}
		}

		return output;
	}

	//=======================================
	// Private Functions
	//=======================================

	private static String boolToString(boolean val)
	{
		if(val)
		{
			return "true";
		}
		else
		{
			return "false";
		}
	}
}
