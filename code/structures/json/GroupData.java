//===================================================================
// GroupData.java
// 	Description:
// 		Holds the data returned from the /sl/api/iam/group/
// 		api call. The structure of the returned json was changed
// 		between v1.0.0 and v2.0.0 of the vail code.
//===================================================================

package com.socialvagrancy.vail.structures.json;

public class GroupData
{
	Group[] data;

	//=======================================
	// Getters
	//=======================================
	
	public int count() { return data.length; }
	public String name(int user) { return data[user].name; }
	public String accountID(int user) { return data[user].accountid; }
}
