//===================================================================
// BucketPolicyPrincipal.java
//      Description:
//          The model of the principal field of the bucket policy
//===================================================================

package com.socialvagrancy.vail.structures;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class BucketPolicyPrincipal {
    @SerializedName("AWS")
    private ArrayList<String> aws;

    public BucketPolicyPrincipal() {
        aws = new ArrayList<String>();
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
