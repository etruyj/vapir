//===================================================================
// Ds3KeyPair.java
// 	Description:
// 		Holds the DS3 key info for the java.
//===================================================================

package com.socialvagrancy.vail.structures.blackpearl;

public class Ds3KeyPair
{
	Credentials[] data;

	public int count() { return data.length; }
	public String accessKey(int key) { return data[key].accessKey(); }
	public String secretKey(int key) { return data[key].secretKey(); }

	public class Credentials
	{
		String auth_id;
		String secret_key;

		public String accessKey() { return auth_id; }
		public String secretKey() { return secret_key; }
	}
}
