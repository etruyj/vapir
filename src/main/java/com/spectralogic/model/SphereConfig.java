//===================================================================
// SphereConfig.java
// 	Description:
// 		Container class for all the values input from the
// 		SphereConfig YAML.
//===================================================================

package com.spectralogic.vail.vapir.model;

import java.util.ArrayList;

public class SphereConfig
{
	private ArrayList<Account> accounts;
	private ArrayList<Storage> storage;
	private ArrayList<Lifecycle> lifecycles;
	private ArrayList<Bucket> buckets;
	private ArrayList<Summary> groups;
	
	//=======================================
	// Getters
	//=======================================

	public ArrayList<Account> getAccounts() { return accounts; }
	public ArrayList<Storage> getStorage() { return storage; }
	public ArrayList<Lifecycle> getLifecycles() { return lifecycles; }
	public ArrayList<Bucket> getBuckets() { return buckets; }
	public ArrayList<Summary> getGroups() { return groups; }

	//=======================================
	// Setters
	//=======================================

	public void setAccounts(ArrayList<Account> accounts) { this.accounts = accounts; }
	public void setStorage(ArrayList<Storage> storage) { this.storage = storage; }
	public void setLifecycles(ArrayList<Lifecycle> lifecycles) { this.lifecycles = lifecycles; }
	public void setBuckets(ArrayList<Bucket> buckets) { this.buckets = buckets; }
	public void setGroups(ArrayList<Summary> groups) { this.groups = groups; }

	//=======================================
	// Functions
	//=======================================

	public int accountCount() { return accounts.size(); }
	public int bucketCount() { return buckets.size(); }
	public String bucketLifecycle(int bucket) { return buckets.get(bucket).getLifecycle(); }
	public String bucketOwner(int bucket) { return buckets.get(bucket).getOwner(); }
	public int groupCount() { return groups.size(); }
	public int lifecycleCount() { return lifecycles.size(); }
	public int lifecycleRuleCount(int l) { return lifecycles.get(l).ruleCount(); }
	public int lifecycleRuleStorageCount(int l, int i) { return lifecycles.get(l).storageCount(i); }
	public String lifecycleRuleStorageId(int l, int i, int s) { return lifecycles.get(l).storageID(i, s); }
	public int storageCount() { return storage.size(); }

	public void clearAccountID(int a) { accounts.get(a).setId(null); }
    public void clearAccountCanonicalID(int a) { accounts.get(a).setCanonicalId(null); }	
	public void clearBucketAcls(int bucket) { buckets.get(bucket).setAcls(null); }
	public void clearBucketCreated(int bucket) { buckets.get(bucket).setCreated(null); }
	public void clearGroupAccountID(int group) { groups.get(group).setAccountId(null); }
	public void clearGroupType(int group) { groups.get(group).setType(null); }
	public void clearLifecycleId(int l) { lifecycles.get(l).setId(null); }
	public void clearLifecycleModified(int l) { lifecycles.get(l).setModified(null); }
	public void clearLifecycleRuleDestinationCount(int l, int r) { lifecycles.get(l).clearDestinationCount(r); }
	public void clearStorageID(int s) { storage.get(s).setId(null); }
	public void clearStorageStatus(int s) { storage.get(s).setStatus(null); }

	public void setBucketLifecycle(int bucket, String l_name) { buckets.get(bucket).setLifecycle(l_name); }
	public void setBucketOwner(int bucket, String account) { buckets.get(bucket).setOwner(account); }
	public void setLifecycleRuleStorageId(int l, int r, int s, String name) { lifecycles.get(l).getRule(r).setStorage( s, name); }


}
