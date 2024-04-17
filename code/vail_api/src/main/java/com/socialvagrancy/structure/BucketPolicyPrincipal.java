//===================================================================
// BucketPolicyPrincipal.java
//      Description:
//          The model of the principal field of the bucket policy
//===================================================================

package com.socialvagrancy.vail.structures;

import com.socialvagrancy.vail.utils.http.ArrayOrStringAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class BucketPolicyPrincipal {
    @SerializedName("AWS")
    @JsonAdapter(ArrayOrStringAdapter.class)
    public ArrayList<String> aws;

    public BucketPolicyPrincipal() {
        aws = new ArrayList<String>();
    }

    public BucketPolicyPrincipal(BucketPolicyPrincipal other) {
        if(other != null) { aws = other.getAWS(); }
    }

    //==========================================
    // Getters
    //==========================================
    public ArrayList<String> getAWS() { return aws; }

    //==========================================
    // Setters
    //==========================================
    public void addAWS(String aws) { this.aws.add(aws); }
    public void setAWS(ArrayList<String> AWS) { this.aws = AWS; }
}
