//===================================================================
// Bucket.java
// 	Description:
// 		Contains the values for creating the bucket json and
// 		for deciphering the response from the GET /sl/api/buckets
// 		call.
//===================================================================

package com.socialvagrancy.vail.structures;

public class Bucket
{
	public String versioning;
	public boolean restore;
	public boolean compress;
	public String lifecycle;
	public ACL[] acls;
	public String owner;
	public String control;
	public boolean blockPublicAcls;
	public boolean blockPublicPolicy;
	public boolean ignorePublicAcls;
	public boolean restricPublicBuckets;
	public String name;
	public String created;
	public String permissionType;
}
