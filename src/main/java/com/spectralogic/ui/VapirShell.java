//===================================================================
// VapirShell.java
//      Description:
//          This is the main class for the function. This offers the
//          shell commands for the vapir script.
//
//  Created by Sean Snyder
//===================================================================

package com.spectralogic.vail.vapir.ui;

import com.spectralogic.vail.vapir.command.VailController;
import com.spectralogic.vail.vapir.model.Account;
import com.spectralogic.vail.vapir.model.Bucket;
import com.spectralogic.vail.vapir.model.OutputFormat;
import com.spectralogic.vail.vapir.model.Summary;
import com.spectralogic.vail.vapir.model.User;
import com.spectralogic.vail.vapir.ui.display.Display;

import java.util.ArrayList;

public class VapirShell
{
	VailController controller;

	public VapirShell(String ip_address, boolean ignore_ssl)
	{
		controller = new VailController(ip_address, ignore_ssl);
	}

	public void execute(String ip, String command, String option1, String option2, String option3, String option4, boolean boolean_flag, String outputFormat)
	{
		ArrayList response;

		switch(command)
		{
            case "activate":
                Display.print(controller.activateNode(option1, option2));
                break;
            case "capacity-summary":
                System.err.println("Doesn't work.");
//                controller.getCapacitySummary(ip);
                break;
			case "clear-cache":
				controller.clearCache(ip);
				break;
			case "configure":
			case "configure-sphere":
				Display.print(controller.configureSphere(ip, option4));
				break;
			case "create-bucket":
				Display.output(controller.createBucket(ip, option2, option1), outputFormat);
				break;
			case "create-group":
				Display.print(controller.createGroup(ip, option2, option1));
				break;
			case "create-user":
				Display.print(controller.createUser(ip, option1, option3));
				break;
            case "enable-veeam":
                Display.print(controller.enableVeeam(option2));
                break;
            case "fetch-config":
                System.out.println("Code coming soon.");
//				Display.output(controller.fetchConfiguration(ip), outputFormat, option4);
				break;
            case "get-bucket":
                Display.output(controller.getBucket(option2, option3, option4), outputFormat);
                break;
            case "help":
				Display.printHelp("../lib/help/options.txt");
				break;
			case "list-accounts":
				Display.output(controller.listAccounts(ip), outputFormat);
				break;
			case "list-buckets":
                Display.output(controller.listBuckets(ip, option1), outputFormat);
                /* Cleaning up this code. 
                   Not sure what was supposed to happen here. Will remove and
                   revisit later.
				if(outputFormat.equals("raw"))
				{
					Display.output(controller.listBuckets(ip, option4), outputFormat);
				}
				else
				{
					Display.output(controller.listBucketSummary(ip, option1), outputFormat);
				}
                */
				break;
			case "list-groups":
				Display.output(controller.listGroups(option1), outputFormat);
				break;
            case "list-endpoints":
                Display.output(controller.listEndpointsAll(), outputFormat);
                break;
            case "list-objects":
                Display.output(controller.listObjectsInBucket(option2, option3), outputFormat);
                break; 
            case "list-storage":
				Display.output(controller.listStorage(ip), outputFormat);
				break;
			case "list-users":
				response = controller.listUsers(ip, option1, boolean_flag);
				Display.output(response, outputFormat);
				break;
			case "update-owner":
                System.out.println("Code coming soon.");
//				controller.updateOwner(ip, option2, option1);
				break;
            case "search-users":
                Display.output(controller.searchUsers(ip, option1, option4, boolean_flag), outputFormat);
                break;
            case "default":
				Display.print("Invalid command [" + command + "] selected. Please used -c help for a list of valid commands.");
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
			VapirShell ui = new VapirShell(aparser.getIP(), aparser.isIgnoreSsl());
		
			if(aparser.helpRequested())
			{
				Display.printHelp("../lib/help/options.txt");
			}
            else if(aparser.isVersionRequested()) {
                Display.printHelp("../lib/help/version.txt");
            }
			else if(aparser.getCommand().substring(0, 4).equals("help"))
			{
				ui.execute("",  aparser.getCommand(), "", "", "", "", false, "");
			}
            else if(aparser.getCommand().equals("activate")) {
                // Special exception here as there are no required credentials
                // to activate a Vail sphere (at this moment
                ui.execute("", aparser.getCommand(),
                        aparser.getOption1(),
                        aparser.getOption2(),
                        aparser.getOption3(),
                        aparser.getOption4(),
                        aparser.getBooleanFlag(),
                        aparser.getOutputFormat());
            }
			else if(ui.login(aparser.getIP(), aparser.getUsername(), aparser.getPassword()))
			{
				ui.execute(aparser.getIP(), aparser.getCommand(), aparser.getOption1(), aparser.getOption2(), aparser.getOption3(), aparser.getOption4(), aparser.getBooleanFlag(), aparser.getOutputFormat());
			}
			else
			{
				Display.print("Unable to login with specified credentials.");
			}
		}
		else
		{
			Display.print("Invalid input entered. Please use --help to see a list of valid commands.");
		}
	}
}
