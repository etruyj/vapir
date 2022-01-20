package com.socialvagrancy.vail.ui;

import com.socialvagrancy.vail.structures.Account;
import com.socialvagrancy.vail.structures.Bucket;
import com.socialvagrancy.vail.structures.OutputFormat;
import com.socialvagrancy.vail.structures.Summary;
import com.socialvagrancy.vail.structures.User;
import com.socialvagrancy.vail.utils.VailController;
import com.socialvagrancy.vail.ui.display.Display;

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
		ArrayList<Summary> response;

		switch(command)
		{
			case "clear-cache":
				controller.clearCache(ip);
				break;
			case "configure":
			case "configure-sphere":
				Display.print(controller.configureSphere(ip, option1));
				break;
			case "create-bucket":
				Display.output(controller.createBucket(ip, option2, option1), outputFormat);
				break;
			case "help":
				Display.printHelp("../lib/help/basic.txt");
				Display.printHelp("../lib/help/advanced.txt");
				break;
			case "list-accounts":
				Display.output(controller.listAccounts(ip), outputFormat);
				break;
			case "list-buckets":
				if(option1.equals("none"))
				{
					Display.output(controller.listBuckets(ip), outputFormat);
				}
				else
				{
					response = controller.listBucketSummary(ip, option1);
					Display.output(response, outputFormat);
				}
				break;
			case "list-users":
				if(option1.equals("none") && !boolean_flag)
				{
					Display.output(controller.listUsers(ip), outputFormat);;
				}
				else
				{
					response = controller.listUserSummary(ip, option1, boolean_flag);
					Display.output(response, outputFormat);
				}
				break;
			case "min-iam-policy":
				controller.findMinIAMPermissions(ip);
				break;
			case "default":
				Display.print("Invalid command selected. Please used -c help for a list of valid commands.");
				break;
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

