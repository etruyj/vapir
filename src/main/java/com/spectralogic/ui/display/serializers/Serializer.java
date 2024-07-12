//===================================================================
// Serializer.java
// 	Description: This class takes the various output formats
// 	presented by the different functions and coverts them to a
// 	generic output to allow for easier display and display options,
// 	an ArrayList<OuputFormat>.
//===================================================================

package com.spectralogic.vail.vapir.ui.display.serializers;

import com.spectralogic.vail.vapir.model.Account;
import com.spectralogic.vail.vapir.model.Bucket;
import com.spectralogic.vail.vapir.model.BucketSummary;
import com.spectralogic.vail.vapir.model.OutputFormat;
import com.spectralogic.vail.vapir.model.Storage;
import com.spectralogic.vail.vapir.model.Summary;
import com.spectralogic.vail.vapir.model.UserSummary;
import com.spectralogic.vail.vapir.model.User;

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
			line.setHeader("account");
			line.setValue(null);
			output.add(line);
				
			line = new OutputFormat();
			line.setHeader("account>name");
			line.setValue(accounts[i].getUsername());
			output.add(line);
		
			line = new OutputFormat();
			line.setHeader("account>id");
			line.setValue(accounts[i].getId());
			output.add(line);
			
			line = new OutputFormat();
			line.setHeader("account>canonicalId");
			line.setValue(accounts[i].getCanonicalId());
			output.add(line);
			
			line = new OutputFormat();
			line.setHeader("account>externalId");
			line.setValue(accounts[i].getExternalId());
			output.add(line);
			
			line = new OutputFormat();
			line.setHeader("account>roleArn");
			line.setValue(accounts[i].getRoleArn());
			output.add(line);
			
			line = new OutputFormat();
			line.setHeader("account>email");
			line.setValue(accounts[i].getEmail());
			output.add(line);
			
			line = new OutputFormat();
			line.setHeader("account");
			line.setValue(null);
			output.add(line);
		}
		
		return output;
	}

	public static ArrayList<OutputFormat> convert(Bucket[] buckets)
	{
		ArrayList<OutputFormat> output = new ArrayList<OutputFormat>();
		OutputFormat line;

		line = new OutputFormat();
		line.setHeader("bucket");
		line.setValue(null);
		output.add(line);
		
		for(int i=0; i< buckets.length; i++)
		{
		
				
			line = new OutputFormat();
			line.setHeader("bucket>lifecycle");
			line.setValue(buckets[i].getLifecycle());
			output.add(line);
		
			line = new OutputFormat();
			line.setHeader("bucket>restore");
			
			if(buckets[i].isRestore())
			{
				line.setValue("TRUE");
			}
			else
			{
				line.setValue("FALSE");
			}
			output.add(line);
		
			for(int j=0; j<buckets[i].getAcls().length; j++)
			{
				line = new OutputFormat();
				line.setHeader("bucket>acls>type");
				line.setValue(buckets[i].getAcl(j).getType());
				output.add(line);
		
				line = new OutputFormat();
				line.setHeader("bucket>acls>id");
				line.setValue(buckets[i].getAcl(j).getId());
				output.add(line);
		
				line = new OutputFormat();
				line.setHeader("bucket>acls>read");
				line.setValue(boolToString(buckets[i].getAcl(j).isRead()));
				output.add(line);
			
				line = new OutputFormat();
				line.setHeader("bucket>acls>readACP");
				line.setValue(boolToString(buckets[i].getAcl(j).isReadACP()));
				output.add(line);
			
				line = new OutputFormat();
				line.setHeader("bucket>acls>write");
				line.setValue(boolToString(buckets[i].getAcl(j).isWrite()));
				output.add(line);
			
				line = new OutputFormat();
				line.setHeader("bucket>acls>writeACP");
				line.setValue(boolToString(buckets[i].getAcl(j).isWriteACP()));
				output.add(line);
			
			}

			line = new OutputFormat();
			line.setHeader("bucket>owner");
			line.setValue(buckets[i].getOwner());
			output.add(line);
		
			line = new OutputFormat();
			line.setHeader("bucket>name");
			line.setValue(buckets[i].getName());
			output.add(line);
		
			line = new OutputFormat();
			line.setHeader("bucket>created");
			line.setValue(buckets[i].getCreated());
			output.add(line);
		}
			
		line = new OutputFormat();
		line.setHeader("bucket");
		line.setValue(null);
		output.add(line);

		return output;
	}

	public static ArrayList<OutputFormat> convert(String response)
	{
		ArrayList<OutputFormat> output = new ArrayList<OutputFormat>();

		OutputFormat line = new OutputFormat();
		line.setHeader("response");
		line.setValue(null);
		output.add(line);

		line = new OutputFormat();
		line.setHeader("message");
		line.setValue(response);
		output.add(line);

		line = new OutputFormat();
		line.setHeader("response");
		line.setValue(null);
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
			line.setHeader("storage");
			line.setValue(null);
			output.add(line);

			line = new OutputFormat();
			line.setHeader("storage>name");
			line.setValue(storage[i].getName());
			output.add(line);

			line = new OutputFormat();
			line.setHeader("storage>type");
			line.setValue(storage[i].getType());
			output.add(line);

			line = new OutputFormat();
			line.setHeader("storage>class");
			line.setValue(storage[i].getStorageClass());
			output.add(line);

			line = new OutputFormat();
			line.setHeader("storage>status");
			line.setValue(storage[i].getStatus());
			output.add(line);
			
			line = new OutputFormat();
			line.setHeader("storage");
			line.setValue(null);
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
			line.setHeader("user");
			line.setValue(null);
			output.add(line);
				
			line = new OutputFormat();
			line.setHeader("user>username");
			line.setValue(users[i].getUsername());
			output.add(line);
				
			line = new OutputFormat();
			line.setHeader("user>accountId");
			line.setValue(users[i].getAccountId());
			output.add(line);
				
			line = new OutputFormat();
			line.setHeader("user");
			line.setValue(null);
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
