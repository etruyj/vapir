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
	public BucketPolicy policy;
    public String owner;
	public String control;
	// blockPublicAcls seems to be a non-configurable setting
	// in the vail ui. Setting this to true and allowing overwrite to false
	// if necessary.
	public boolean blockPublicAcls = true;
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

    //===========================================
    // Getters
    //===========================================

    public BucketPolicy getPolicy() { return policy; }
    public String getOwner() { return owner; }
    public String getName() { return name; }

    //===========================================
    // Setters
    //===========================================
    public void addPolicyStatement(BucketPolicyStatement statement) { this.policy.addPolicyStatement(statement); }

    public void setPolicy(BucketPolicy policy) { this.policy = policy; }
    public void setOwner(String owner) { this.owner = owner; }
    public void setName(String name) { this.name = name; }

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
