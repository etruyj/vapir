//===================================================================
// Endpoint.java
// 	Description:
// 		This holds the endpoint information that is returned
// 		from the sl/api/endpoints command as well as credential
// 		information to reduce the number of times a password
// 		has to be entered when configuring storage.
//===================================================================

package com.socialvagrancy.vail.structures;

public class Endpoint
{
	String id;
	String type;
	String name;
	String location;
	String currentVersion;
	String preferredVersion;
	String endpoint;
	String managementEndpoint;
	String status;

	// Credential for creating storage
	String login;
	String key;
	String arn;
	String pod;

	public String managementAddress() { return managementEndpoint; }
	public String id() { return id; }
	public String key() { return key; }
	public String login() { return login; }
	public String name() { return name; }

	public void setAccessKey(String a) { login = a; }
	public void setSecretKey(String s) { key = s; }
}
