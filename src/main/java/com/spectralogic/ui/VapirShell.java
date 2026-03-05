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
import com.spectralogic.vail.vapir.model.VapirConfigModel;
import com.spectralogic.vail.vapir.ui.display.Display;

import com.socialvagrancy.utils.io.Configuration;
import com.socialvagrancy.utils.ui.ArgParser;

import java.util.List;

public class VapirShell
{
	VailController controller;

	public VapirShell(String ip_address, boolean ignore_ssl, String configPath)
	{
        try {
            Configuration.load(configPath, VapirConfigModel.class);
            VapirConfigModel config = Configuration.get();

		    controller = new VailController(ip_address, ignore_ssl, config);
	    } catch(Exception e) {
            System.err.println(e.getMessage());
            System.err.println("Failed to initialize script.");
        }
    }

	public void execute(ArgParser aparser) throws Exception
	{
		List response;
        // Parse the inputs for an output-format. if not present, go with the default (table)
        String outputFormat = aparser.get("output-format") != null ? aparser.get("output-format") : "table";

        try {
		    switch(aparser.getRequired("command"))
		    {
                case "activate":
//                    Display.print(controller.activateNode(option1, option2));
                    break;
                case "capacity-summary":
                    System.err.println("Doesn't work.");
//                  controller.getCapacitySummary(ip);
                    break;
			    case "clear-cache":
				    controller.clearCache(aparser.getRequired("endpoint"));
				    break;
			    case "configure":
			    case "configure-sphere":
				    Display.print(controller.configureSphere(aparser.getRequired("endpoint"), aparser.getRequired("file")));
				    break;
			    case "create-bucket":
				    Display.output(controller.createBucket(aparser.getRequired("endpoint"),
                                aparser.getRequired("bucket"), 
                                aparser.getRequired("account")), 
                            outputFormat);
				    break;
			    case "create-group":
				    Display.print(controller.createGroup(aparser.getRequired("endpoint"), 
                                aparser.getRequired("group"), 
                                aparser.getRequired("account")));
				    break;
			    case "create-user":
				    Display.print(controller.createUser(aparser.getRequired("endpoint"), 
                                aparser.getRequired("account"), 
                                aparser.getRequired("user")));
				    break;
                case "enable-veeam":
                    Display.print(controller.enableVeeam(aparser.getRequired("bucket")));
                    break;
                case "fetch-config":
                    System.out.println("Code coming soon.");
//				    Display.output(controller.fetchConfiguration(ip), outputFormat, option4);
				    break;
/*                case "get-bucket":
                    Display.output(controller.getBucket(option2, option3, option4), outputFormat);
                    break;
*/                case "help":
				    Display.printHelp("../lib/help/options.txt");
				    break;
			    case "list-accounts":
				    Display.output(controller.listAccounts(aparser.getRequired("endpoint")), 
                            outputFormat);
				    break;
                case "list-buckets":
                    Display.output(controller.listBuckets(aparser.getRequired("endpoint"), 
                                aparser.getRequired("account")),
                                aparser.get("output-format"));
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
				    Display.output(controller.listGroups(aparser.getRequired("account")), outputFormat);
				    break;
                case "list-endpoints":
                    Display.output(controller.listEndpointsAll(), outputFormat);
                    break;
                case "list-objects":
                    Display.output(controller.listObjectsInBucket(aparser.getRequired("endpoint"), aparser.get("max-keys")), outputFormat);
                    break; 
                case "list-storage":
				    Display.output(controller.listStorage(aparser.getRequired("endpoint")), 
                            outputFormat);
				    break;
			    case "list-users":
				    response = controller.listUsers(aparser.getRequired("endpoint"), aparser.getRequired("account"), aparser.getBoolean("active-only"));
				    Display.output(response, outputFormat);
				    break;
			    case "update-owner":
                    System.out.println("Code coming soon.");
//				    controller.updateOwner(ip, option2, option1);
				    break;
                case "search-users":
                    Display.output(controller.searchUsers(aparser.getRequired("endpoint"), 
                                aparser.getRequired("account"), 
                                aparser.getRequired("activation-key"), 
                                aparser.getBoolean("active-only")), 
                            outputFormat);
                    break;
                case "default":
				    Display.print("Invalid command [" + aparser.getRequired("command") + "] selected. Please used -c help for a list of valid commands.");
				    break;
		    }
        } catch(Exception e) {
            System.err.println(e.getMessage());
        }
	}

	public boolean login(String ip, String username, String password)
	{
		return controller.login(ip, username, password);
	}

	public static void main(String[] args)
	{
		ArgParser aparser = new ArgParser();
        aparser.parse(args);

		try {
            String configPath = "../vapir.yml";
			VapirShell ui = new VapirShell(aparser.getRequired("endpoint"), aparser.getBoolean("ignore-ssl"), configPath);
		
			if(aparser.helpRequested())
			{
				Display.printHelp("../lib/help/options.txt");
			}
            else if(aparser.getBoolean("version")) {
                Display.printHelp("../lib/help/version.txt");
            }
			else if(aparser.getRequired("command").substring(0, 4).equals("help"))
			{
				ui.execute(aparser);
			}
            else if(aparser.getRequired("command").equals("activate")) {
                // Special exception here as there are no required credentials
                // to activate a Vail sphere (at this moment
/*                ui.execute("", aparser.getCommand(),
                        aparser.getOption1(),
                        aparser.getOption2(),
                        aparser.getOption3(),
                        aparser.getOption4(),
                        aparser.getBooleanFlag(),
                        aparser.getOutputFormat());
*/
            }
			else if(ui.login(aparser.getRequired("endpoint"), aparser.getRequired("username"), aparser.getRequired("password")))
			{
				ui.execute(aparser);
			}
			else
			{
				Display.print("Unable to login with specified credentials.");
			}
		} catch(Exception e) {
		    System.err.println(e.getMessage());
        }
	}
}
