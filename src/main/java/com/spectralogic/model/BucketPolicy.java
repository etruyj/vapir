//===================================================================
// BucketPolicy.java
// Description:
//     This is the model of an AWS bucket policy to be used with
//     Vail buckets.
//
// Created by Sean Snyder.
//===================================================================

package com.spectralogic.vail.vapir.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class BucketPolicy {
    @SerializedName("Version")
    private String version;
    @SerializedName("Id")
    private String id;
    @SerializedName("Statement")
    private ArrayList<BucketPolicyStatement> statement;

    public BucketPolicy() {
        version = "2012-10-17";
        statement = new ArrayList<BucketPolicyStatement>();
    }

    public BucketPolicy(BucketPolicy other) {
        version = other.getVersion();
        id = other.getId();
        statement = new ArrayList<BucketPolicyStatement>();

        for(BucketPolicyStatement otherStatement : other.getStatement()) {
            statement.add(new BucketPolicyStatement(otherStatement));
        }
    }

    //==========================================
    // Getters
    //==========================================
    public String getVersion() { return version; }
    public String getId() { return id; }
    public ArrayList<BucketPolicyStatement> getStatement() { return statement; }
    //==========================================

    //==========================================
    // Setters
    //==========================================
    public void addPolicyStatement(BucketPolicyStatement statement) {
        this.statement.add(statement);
    }
    public void setVersion(String version) { this.version = version; }
    public void setId(String id) { this.id = id; }
    public void setStatement(ArrayList<BucketPolicyStatement> statement) { this.statement = statement; }
    public void setStatement(BucketPolicyStatement statement) { this.statement.add(statement); }
    //==========================================
}
