//===================================================================
// BucketPolicy.java
//      Description:
//          This is the model of an AWS bucket policy to be used with
//          Vail buckets.
//===================================================================

package com.socialvagrancy.vail.structures;

import java.util.ArrayList;

public class BucketPolicy {
    private String version;
    private String id;
    private ArrayList<BucketPolicyStatement> statement;

    public BucketPolicy() {
        version = "2012-10-17";
        statement = new ArrayList<BucketPolicyStatement>();
    }

    //==========================================
    // Getters
    //==========================================
    public String getVersion() { return version; }
    public String getId() { return id; }
    public ArrayList<BucketPolicyStatement> getStatement() { return statement; }

    //==========================================
    // Setters
    //==========================================
    public void addPolicyStatement(BucketPolicyStatement statement) {
        this.statement.add(statement);
    }
    public void setVersion(String version) { this.version = version; }
    public void setId(String id) { this.id = id; }
    public void setStatement(ArrayList<BucketPolicyStatement> statement) { this.statement = statement; }
}

