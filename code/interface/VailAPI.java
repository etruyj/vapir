package com.socialvagrancy.vail.ui;

import com.socialvagrancy.vail.structures.OutputFormat;
import com.socialvagrancy.vail.utils.VailController;
import com.socialvagrancy.vail.ui.Output;

import java.util.ArrayList;

public class VailAPI
{
	VailController controller;

	public VailAPI()
	{
		controller = new VailController();
	}

	public void execute(String ip, String command, String option1, String option2, String option3, String option4, boolean boolean_flag, String outputFormat)
	{
		ArrayList<OutputFormat> output = null;

		switch(command)
		{
			case "clear-cache":
				controller.clearCache(ip);
				break;
			case "list-accounts":
				output = controller.listAccounts(ip);
				break;
			case "list-buckets":
				output = controller.listBuckets(ip, option1);
				break;
			case "list-users":
				output = controller.listUsers(ip, option1, boolean_flag);
				break;
			case "min-iam-policy":
				controller.findMinIAMPermissions(ip);
				break;
			case "default":
				Output.print("Invalid command selected. Please used -c help for a list of valid commands.");
				break;
		}

		if(output != null)
		{
			if(output.size()>0)
			{
				Output.print(output, outputFormat);
			}
			else
			{
				Output.print("No results");
			}
		}
	}

	public boolean login(String ip, String username, String password)
	{
		return controller.login(ip, username, password);
	}

	public static void main(String[] args)
	{
		ArgParser aparser = new ArgParser();

		if(aparser.parseArgs(args))
		{
			VailAPI ui = new VailAPI();
		
			if(aparser.helpRequested())
			{
				Output.printHelp("../lib/help/options.txt");
			}
			else if(aparser.getCommand().substring(0, 4).equals("help"))
			{
				ui.execute("",  aparser.getCommand(), "", "", "", "", false, "");
			}
			else if(ui.login(aparser.getIP(), aparser.getUsername(), aparser.getPassword()))
			{
				ui.execute(aparser.getIP(), aparser.getCommand(), aparser.getOption1(), aparser.getOption2(), aparser.getOption3(), aparser.getOption4(), aparser.getBooleanFlag(), aparser.getOutputFormat());
			}
			else
			{
				Output.print("Unable to login with specified credentials.");
			}
		}
		else
		{
			Output.print("Invalid input entered. Please use --help to see a list of valid commands.");
		}
	}
}

