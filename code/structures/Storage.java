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
	
	// For creating storage
	public String arn;
	public String podId;
	public String accessKey;
	public String secretKey;

}
