//===================================================================
// UserData.java
// 	Description:
// 		Holds the data returned from the /sl/api/iam/user/
// 		api call. The structure of the returned json was changed
// 		between v1.0.0 and v2.0.0 of the vail code.
//===================================================================

package com.spectralogic.vail.vapir.model;

public class UserData
{
	User[] data;

	//=======================================
	// Getters
	//=======================================
	
	public int count() { return data.length; }
	public String username(int user) { return data[user].getUsername(); }
	public String accountID(int user) { return data[user].getAccountId(); }
    public User[] getData() { return data; }

}
