//===================================================================
// BPUser.java
// 	Description:
// 		Contains user information for the BlackPearl
//===================================================================

package com.socialvagrancy.vail.structures.blackpearl;

public class BPUser
{
	User[] data;

	public int count() { return data.length; }
	public int id(int user) { return data[user].id; }
	public String username(int user) { return data[user].username; }

	public class User
	{
		int id;
		String name;
		String username;
		int seesion_timeout;
		String created_at;
		String updated_at;
		String ds3_user_id;
		boolean mfa_configured;
		boolean remote_support_enabled;
		String default_data_policy_id;
		boolean global_data_policy_acl_enabled;
		int max_buckets;

		public int id() { return id; }
		public String username() { return username; }
	}
}
