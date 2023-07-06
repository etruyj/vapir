//===================================================================
// Group.java
// 	Description:
// 		Holds basic group information returned from api calls
// 		Also referenced as the sub-class for the data field 
// 		in GroupData.java, which couches this information in
// 		a data variable.
//===================================================================

package com.socialvagrancy.vail.structures.json;

public class Group
{
	String name;
	String accountid;

	public String name() { return name; }
	public String accountId() { return accountid; }
}
