//===================================================================
// SphereConfig.java
// 	Description:
// 		Container class for all the values input from the
// 		SphereConfig YAML.
//===================================================================

package com.socialvagrancy.vail.structures;

import java.util.ArrayList;

public class SphereConfig
{
	public Account[] accounts;
	public Storage[] storage;
	public Lifecycle[] lifecycles;
	public Bucket[] buckets;
	public ArrayList<Summary> groups;

	//=======================================
	// Functions
	//=======================================

	public int accountCount() { return accounts.length; }
	public int bucketCount() { return buckets.length; }
	public String bucketLifecycle(int bucket) { return buckets[bucket].lifecycle; }
	public String bucketOwner(int bucket) { return buckets[bucket].owner; }
	public int groupCount() { return groups.size(); }
	public int lifecycleCount() { return lifecycles.length; }
	public int lifecycleRuleCount(int l) { return lifecycles[l].ruleCount(); }
	public int lifecycleRuleStorageCount(int l, int i) { return lifecycles[l].storageCount(i); }
	public String lifecycleRuleStorageId(int l, int i, int s) { return lifecycles[l].storageID(i, s); }
	public int storageCount() { return storage.length; }

	public void clearAccountID(int a) { accounts[a].id = null; }
       	public void clearAccountCanonicalID(int a) { accounts[a].canonicalId = null; }	
	public void clearBucketAcls(int bucket) { buckets[bucket].acls = null; }
	public void clearBucketCreated(int bucket) { buckets[bucket].created = null; }
	public void clearGroupAccountID(int group) { groups.get(group).account_id = null; }
	public void clearGroupType(int group) { groups.get(group).type = null; }
	public void clearLifecycleId(int l) { lifecycles[l].id = null; }
	public void clearLifecycleModified(int l) { lifecycles[l].modified = null; }
	public void clearLifecycleRuleDestinationCount(int l, int r) { lifecycles[l].clearDestinationCount(r); }
	public void clearStorageID(int s) { storage[s].id = null; }
	public void clearStorageStatus(int s) { storage[s].status = null; }

	public void setBucketLifecycle(int bucket, String l_name) { buckets[bucket].lifecycle = l_name; }
	public void setBucketOwner(int bucket, String account) { buckets[bucket].owner = account; }
	public void setLifecycleRuleStorageId(int l, int i, int s, String name) { lifecycles[l].setStorage(i, s, name); }


}
