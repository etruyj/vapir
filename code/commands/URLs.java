package com.socialvagrancy.vail.commands;

public class URLs
{
	public static String accountsURL(String ipaddress)
	{
		return "https://" + ipaddress + "/sl/api/accounts";
	}

	public static String clearCacheURL(String ipaddress)
	{
		return "https://" + ipaddress + "/sl/api/iam/users/clear_cache";
	}

	public static String keysURL(String ipaddress, String account, String user)
	{
		return "https://" + ipaddress + "/sl/api/iam/users/" + account +"/" + user + "/keys";
	}
	
	public static String loginURL(String ipaddress)
	{
		return "https://" + ipaddress + "/sl/api/tokens";
	}

	public static String usersURL(String ipaddress)
	{
		return "https://" + ipaddress + "/sl/api/iam/users";
	}

}
