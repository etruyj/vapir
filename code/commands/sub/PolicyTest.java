//===================================================================
// PolicyTest.java
// 	Tests to determine the minimum required policies.
//===================================================================

package com.socialvagrancy.vail.commands.sub;

import com.socialvagrancy.vail.commands.BasicCommands;
import com.socialvagrancy.vail.commands.sub.AWSPolicy;
import com.socialvagrancy.vail.structures.ActionTest;
import com.socialvagrancy.utils.FileManager;
import com.socialvagrancy.utils.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.IOException;

import java.lang.ProcessBuilder;
import java.lang.Process;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class PolicyTest
{
	public static void findMinIAMPermissions(BasicCommands vail, String ip_address, String permissions_list, String output_file, Logger logbook)
	{
		logbook.logWithSizedLogRotation("Finding minimum IAM permissions for Vail Role...", 1);

		ArrayList<ActionTest> actions = readPermissions("IAM", permissions_list, logbook);

		for(int i=0; i<actions.size(); i++)
//		for(int i=0; i<1; i++)
		{
			logbook.logWithSizedLogRotation("[" + i+1 + "/" + actions.size() + "] Testing if policy line [" + actions.get(i).action + "] is necessary for an external-vail role...", 1);
			// Remove the action from the policy to see if it is necessary.
			actions.get(i).necessary = false;
			
			if(testActionLine(vail, ip_address, actions, output_file, logbook))
			{
				logbook.logWithSizedLogRotation("Action is [NOT REQUIRED]", 1);
			}
			else
			{
				logbook.logWithSizedLogRotation("Action is [REQUIRED]", 1);
				// Add the policy back to IAM policy.
				actions.get(i).necessary = true;
			}
		}

		logbook.logWithSizedLogRotation("Permissions tests have completed. Check the ../output directory for a json policy.", 2);
	}

	public static String executeAWSCLICommand(String command)
	{
		StringBuilder response = new StringBuilder();
		ProcessBuilder pb = new ProcessBuilder();
		
		pb.command("/bin/bash", "-c", command);

		try
		{
			Process process = pb.start();
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			

			String line = null;

			while((line = br.readLine()) != null)
			{
				response.append(line + "\n");
			}

			System.out.println(command);
			System.out.println(response.toString());
		
		}
		catch(IOException e)
		{
			System.out.println(e.getMessage());
		}
	
		return response.toString();
	}

	public static String getPolicyJson(ArrayList<ActionTest> actions, Logger logbook)
	{
		ArrayList<String> test_actions = new ArrayList<String>();

		for(int i=0; i<actions.size(); i++)
		{
			if(actions.get(i).necessary)
			{
				test_actions.add(actions.get(i).action);
			}
		}

		return AWSPolicy.buildSimple("2012-10-17", "Allow", "*", test_actions);
	}


	public static ArrayList<ActionTest> readPermissions(String resource, String permissions_list, Logger logbook)
	{
		ArrayList<ActionTest> actions = new ArrayList<ActionTest>();
		ActionTest action;

		try
		{
			logbook.logWithSizedLogRotation("Reading " + resource + " permissions from " + permissions_list, 1);

			File ifile = new File(permissions_list);

			BufferedReader br = new BufferedReader(new FileReader(ifile));

			String line = null;

			while((line = br.readLine()) != null)
			{
				action = new ActionTest();

				action.action = resource + ":" + line;
				action.necessary = true;

				actions.add(action);
			}

			logbook.logWithSizedLogRotation("Imported (" + actions.size() + ") permissions.", 1);
		}
		catch(IOException e)
		{
			logbook.logWithSizedLogRotation(e.getMessage(),2);
			System.out.println(e.getMessage());
		}

		return actions;
	}

	public static boolean testActionLine(BasicCommands vail, String ip_address, ArrayList<ActionTest> actions, String output_file, Logger logbook)
	{
		String policy = getPolicyJson(actions, logbook);

		FileManager fm = new FileManager();

		fm.createFileDeleteOld(output_file, false);

		fm.appendToFile(output_file, policy);
		
		uploadPolicy("etruyj", output_file, logbook);

		try { TimeUnit.SECONDS.sleep(5); }
		catch(Exception e) { System.out.println(e.getMessage()); }

		vail.clearCache(ip_address);

		try { TimeUnit.SECONDS.sleep(5); }
		catch(Exception e) { System.out.println(e.getMessage()); }
		
		vail.clearCache(ip_address);

		try { TimeUnit.SECONDS.sleep(5); }
		catch(Exception e) { System.out.println(e.getMessage()); }
		
		vail.clearCache(ip_address);

		try { TimeUnit.SECONDS.sleep(10); }
		catch(Exception e) { System.out.println(e.getMessage()); }

		return testListBuckets("etruyj-vail", logbook);
	}

	public static boolean testListBuckets(String profile, Logger logbook)
	{
		String command = "aws s3api list-buckets --profile " + profile 
			+ " --no-verify-ssl --endpoint https://192.168.72.24/"
			+ " --no-paginate";

		logbook.logWithSizedLogRotation("Listing Vail Buckets: " + command, 1);

		String response = executeAWSCLICommand(command);

		command = "echo $?"; // get execution code.

		String code = executeAWSCLICommand(command);

		code = code.trim();

		logbook.logWithSizedLogRotation("Response code: " + code, 2);

		if(response.length() > 0)
		{
			logbook.logWithSizedLogRotation("List buckets [SUCCESSFUL]", 2);
			return true;
		}
		else
		{
			logbook.logWithSizedLogRotation("List buckets [FAILED]", 2);
			return false;
		}
	}

	public static boolean uploadPolicy(String profile, String source, Logger logbook)
	{
		String ARN = "arn:aws:iam::307805513751:policy/iam-vail-limited";

		String command = "aws iam detach-role-policy --role-name external-vail --policy-arn " + ARN + " --profile " + profile;

		logbook.logWithSizedLogRotation("Detaching policy: " + command, 1);

		executeAWSCLICommand(command);
			
		command = "aws iam delete-policy --policy-arn " + ARN + " --profile " + profile;

		logbook.logWithSizedLogRotation("Deleting policy: " + command, 1);

		executeAWSCLICommand(command);

		command = "aws iam create-policy --profile " + profile + " --policy-name iam-vail-limited --policy-document file://" + source;
	
		logbook.logWithSizedLogRotation("Creating policy: " + command, 1);

		executeAWSCLICommand(command);
	
		command = "aws iam attach-role-policy --role-name external-vail --policy-arn " + ARN + " --profile " + profile;

		logbook.logWithSizedLogRotation("Attaching to Role: " + command, 1);

		executeAWSCLICommand(command);

		return true;
	}
}
