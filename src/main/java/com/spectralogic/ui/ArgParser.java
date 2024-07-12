package com.spectralogic.vail.vapir.ui;

public class ArgParser
{
	private String ip_address;
	private String port;
	private String username;
	private String password;
	private String command;
	private String option1;
	private String option2;
	private String option3;
	private String option4;
	private String output_format;
	private boolean boolean_flag;
	private boolean help_requested;
    private boolean version_requested;
    private boolean ignore_ssl;
	private boolean option1_set;
	private boolean option2_set;
	private boolean option3_set;
	private boolean option4_set;
	private boolean isValid;

	public ArgParser()
	{
		ip_address = "127.0.0.1";
		port = "none";
		username = "none";
		password = "none";
		command = "none";
		option1 = "none";
		option2 = "none";
		option3 = "none";
		option4 = "none";
		output_format = "table";
		boolean_flag = false;
		help_requested = false;
		option1_set = false;
		option2_set = false;
		option3_set = false;
		option4_set = false;
		isValid = true;
        ignore_ssl = false;
	}

	//==============================================
	//	Getters
	//==============================================
	
	public boolean getBooleanFlag() { return boolean_flag; }
	public String getCommand() { return command; }
	
	public String getIP() 
	{ 
		if(port.equals("none"))
		{
			return ip_address;
		}
		else
		{
			return ip_address + ":" + port;
		}
	}
	
	public String getOption1() { return option1; }
	public String getOption2() { return option2; }
	public String getOption3() { return option3; }
	public String getOption4() { return option4; }
	public String getOutputFormat() { return output_format; }
	public String getPassword() { return password; }
	public String getPort() { return port; }
	public String getUsername() { return username; }
	public boolean helpRequested() { return help_requested; } 
    public boolean isVersionRequested() { return version_requested; }
    public boolean isIgnoreSsl() { return ignore_ssl; }

	//==============================================
	//	Parser
	//==============================================

	public void setIPAddress(String ip)
	{
		String[] address = ip.split("/");

		if(address[0].equalsIgnoreCase("HTTP:") || address[0].equalsIgnoreCase("HTTPS:"))
		{
			ip_address = address[2];
		}
		else
		{
			ip_address = address[0];
		}
	}

	public void setOption1(String option)
	{
		if(!option1_set)
		{
			option1 = option;
			option1_set = true;
		}
		else
		{
			isValid = false;
		}
	}
	
	public void setOption2(String option)
	{
		if(!option2_set)
		{
			option2 = option;
			option2_set = true;
		}
		else
		{
			isValid = false;
		}
	}
	
	public void setOption3(String option)
	{
		if(!option3_set)
		{
			option3 = option;
			option3_set = true;
		}
		else
		{
			isValid = false;
		}
	}
	
	public void setOption4(String option)
	{
		if(!option4_set)
		{
			option4 = option;
			option4_set = true;
		}
		else
		{
			isValid = false;
		}
	}

	public void setBooleanFlag(boolean option)
	{
		if(boolean_flag)
		{
			// Already set.
			isValid = false;
		}
		else
		{
			boolean_flag = option;
		}
	}


	public boolean checkValidInputs()
	{
		// Just a quick check to make sure 
		// the bare minimum values are set.
		if(ip_address.equalsIgnoreCase("none"))
		{
			isValid = false;	
		}
		if(username.equalsIgnoreCase("none"))
		{
			isValid = false;	
		}
		if(password.equalsIgnoreCase("none"))
		{
			isValid = false;	
		}
		if(command.equalsIgnoreCase("none"))
		{
			isValid = false;	
		}

		// Allow the user to request help without entering credentials.
		if(help_requested || command.substring(0, 4).equals("help") || version_requested)
		{
			isValid = true;
		}

		return isValid;
	}
		

	public boolean parseArgs(String[] args)
	{
		for(int i=0; i<args.length; i++)
		{
			switch(args[i])
			{
				case "--boolean-flag":
				case "--active-only":
					setBooleanFlag(true);
					break;
				case "-c":
				case "--command":
					if((i+1)<args.length)
					{
						command = args[i+1];
						i++;
					}
					break;
				case "-e":
				case "--endpoint":
					if((i+1)<args.length)
					{
						ip_address = args[i+1];
						setIPAddress(args[i+1]);
						i++;
					}
					break;
				case "--help":
				case "-h":
					help_requested = true;
					break;
                case "--ignore-ssl":
                    ignore_ssl = true;
                    break;
                case "--option1":
				case "-a":
				case "--account":
					if((i+1)<args.length)
					{
						setOption1(args[i+1]);
						i++;
					}
					break;
				case "--option2":
				case "-b":
				case "--bucket":
				case "-g":
				case "--group":
					if((i+1)<args.length)
					{
						setOption2(args[i+1]);
						i++;
					}
					break;	
				case "--option3":
				case "--user": // For specifying Vail user, not to be confused with -u/--username which is for logging in.
					if((i+1)<args.length)
					{
						setOption3(args[i+1]);
						i++;
					}
					break;	
				case "--option4":
				case "--file":
                case "--filter":
                case "--input-file":
					if((i+1)<args.length)
					{
						setOption4(args[i+1]);
						i++;
					}
					break;	
				case "--output-format":
				case "--output":
					if((i+1)<args.length)
					{
						output_format = args[i+1];
						i++;
					}
					break;	
				case "-p":
				case "--password":
					if((i+1)<args.length)
					{
						password = args[i+1];
						i++;
					}
					break;
				case "--port":
					if((i+1)<args.length)
					{
						port = args[i+1];
						i++;
					}
					break;
				case "-u":
				case "--username":
					if((i+1)<args.length)
					{
						username = args[i+1];
						i++;
					}
					break;
                case "--version":
                case "-v":
                    version_requested = true;
                    break;
			}

		}
		return checkValidInputs();
	}
}
