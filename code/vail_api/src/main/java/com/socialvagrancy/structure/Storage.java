package com.socialvagrancy.vail.structures;

public class Storage
{
	public String id;
	public String name;
	public String type;
	public boolean forceDelete;
	public String cloudProvider;
	public String region;
	public String endpoint;
	public String bucket;
	public String link; 		// linked Vail bucket
	public Integer cautionThreshold = null; // set to null as these default to 0 on initialization
	public Integer warningThreshold = null;
	public String status;
	public String storageClass;

    //===========================================    
    // Required Variables that aren't included in
    // the class. These variables were flagged in
    // the 2.0.0 release when migrated to Maven.
    // I'm not sure what they're used for, maybe
    // cloud locations, but they're put here
    // to allow the code to compile.
    //===========================================

    // Review and delete if not necessary
    public String accessKey;
    public String arn;
    public String podId;
    public String secretKey;

    //===========================================
    // Getters
    //===========================================
    
    public String getId() { return id; }
    public String getName() { return name; }
    public String getStorageClass() { return storageClass; }

    //===========================================
    // Setters
    //===========================================

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setStorageClass(String storage_class) { this.storageClass = storage_class; }
}
