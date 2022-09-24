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
	public boolean encrypt;
	public boolean restore;
	public boolean compress;
	public String lifecycle;
	public ACL[] acls;
	public String owner;
	public String control;
	public boolean blockPublicAcls;
	public boolean blockPublicPolicy;
	public boolean ignorePublicAcls;
	public boolean restrictPublicBuckets;
	public boolean locking;
	public RetentionPolicy defaultRetention;
	public String name;
	public String created;
	public String permissionType;
	public String mode;
	public String timeUnits;
	public String time;

	//=======================================
	// Functions
	//=======================================

	public boolean retentionCompliance() { return defaultRetention.compliance; }
	public int retentionDays() { return defaultRetention.days; }


	//=======================================
	// Inner Classes
	//=======================================

	public class RetentionPolicy
	{
		boolean compliance;
		int days;
	}
}
