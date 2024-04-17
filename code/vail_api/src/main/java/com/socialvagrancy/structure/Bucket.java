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
    

    public Bucket() {
        policy = new BucketPolicy();
    }

    // Copy Constructor required for the configure Veeam workflow
    public Bucket(Bucket other) {
        this.versioning = other.getVersioning();
        this.encrypt = other.isEncrypt();
        this.restore = other.isRestore();
        this.compress = other.isCompress();
        this.lifecycle = other.getLifecycle();
        this.acls = other.getAcls();
        this.policy = new BucketPolicy(other.getPolicy());
        this.owner = other.getOwner();
        this.control = other.getControl();
        this.blockPublicAcls = other.isBlockPublicAcls();
        this.blockPublicPolicy = other.isBlockPublicPolicy();
        this.ignorePublicAcls = other.isIgnorePublicAcls();
        this.restrictPublicBuckets = other.isRestrictPublicBuckets();
        this.locking = other.isLocking();
        this.defaultRetention = other.getDefaultRetention();
        this.name = other.getName();
        this.created = other.getCreated();
        this.permissionType = other.getPermissionType();
        this.mode = other.getMode();
        this.timeUnits = other.getTimeUnits();
        this.time = other.getTime();
    }
    //===========================================
    // Getters
    //===========================================

    public String getVersioning() { return versioning; }
    public boolean isEncrypt() { return encrypt; }
    public boolean isRestore() { return restore; }
    public boolean isCompress() { return compress; }
    public String getLifecycle() { return lifecycle; }
    public ACL[] getAcls() { return acls; }
    public BucketPolicy getPolicy() { return policy; }
    public String getOwner() { return owner; }
    public String getControl() { return control; }
    public boolean isBlockPublicAcls() { return blockPublicAcls; }
    public boolean isBlockPublicPolicy() { return blockPublicPolicy; }
    public boolean isIgnorePublicAcls() { return ignorePublicAcls; }
    public boolean isRestrictPublicBuckets() { return restrictPublicBuckets; }
    public boolean isLocking() { return locking; }
    public RetentionPolicy getDefaultRetention() { return defaultRetention; }
    public String getName() { return name; }
    public String getCreated() { return created; }
    public String getPermissionType() { return permissionType; }
    public String getMode() { return mode; }
    public String getTimeUnits() { return timeUnits; }
    public String getTime() { return time; }    

    //===========================================
    // Setters
    //===========================================
    public void addPolicyStatement(BucketPolicyStatement statement) { this.policy.addPolicyStatement(statement); }

    public void setVersioning(String versioning) { this.versioning = versioning; }
    public void setEncrypt(boolean encrypt) { this.encrypt = encrypt; }
    public void setRestore(boolean restore) { this.restore = restore; }
    public void setCompress(boolean compress) { this.compress = compress; }
    public void setLifecycle(String lifecycle) { this.lifecycle = lifecycle; }
    public void setAcls(ACL[] acls) { this.acls = acls; }
    public void setPolicy(BucketPolicy policy) { this.policy = policy; }
    public void setOwner(String owner) { this.owner = owner; }
    public void setControl(String control) { this.control = control; }
    public void setBlockPublicAcls(boolean blockPublicAcls) { this.blockPublicAcls = blockPublicAcls; }
    public void setBlockPublicPolicy(boolean blockPublicPolicy) { this.blockPublicPolicy = blockPublicPolicy; }
    public void setIgnorePublicAcls(boolean ignorePublicAcls) { this.ignorePublicAcls = ignorePublicAcls; }
    public void setRestrictPublicBuckets(boolean restrictPublicBuckets) { this.restrictPublicBuckets = restrictPublicBuckets; }
    public void setLocking(boolean locking) { this.locking = locking; }
    public void setDefaultRetention(RetentionPolicy defaultRetention) { this.defaultRetention = defaultRetention; }
    public void setName(String name) { this.name = name; }
    public void setCreated(String created) { this.created = created; }
    public void setPermissionType(String permissionType) { this.permissionType = permissionType; }
    public void setMode(String mode) { this.mode = mode; }
    public void setTimeUnits(String timeUnits) { this.timeUnits = timeUnits; }
    public void setTime(String time) { this.time = time; }

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
