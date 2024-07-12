//===================================================================
// GroupData.java
// Description:
//     Holds the data returned from the /sl/api/iam/group/
//     api call. The structure of the returned json was changed
//     between v1.0.0 and v2.0.0 of the vail code.
//
// Variables:
// - data: The array of Group objects.
//
// Created by Sean Snyder 
//===================================================================

package com.spectralogic.vail.vapir.model;

public class GroupData {
    private Group[] data;

    //=============================
    // Getters
    //=============================
    public int count() { return data.length; }
    public Group[] getData() { return data; }
    public String name(int user) { return data[user].getName(); }
    public String accountID(int user) { return data[user].getAccountId(); }

    //=============================
    // Setters
    //=============================
    public void setData(Group[] data) { this.data = data; }
}
