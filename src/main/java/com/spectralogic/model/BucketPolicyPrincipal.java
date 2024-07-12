package com.spectralogic.vail.vapir.model;

import com.spectralogic.vail.vapir.util.json.ArrayOrStringAdapter;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


//===================================================================
// BucketPolicyPrincipal.java
// Description:
//     The model of the principal field of the bucket policy
//
// Created by Sean Snyder.
//===================================================================

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

    //==========================================
    // Setters
    //==========================================
    public void addAWS(String aws) { this.aws.add(aws); }
    public void setAWS(ArrayList<String> AWS) { this.aws = AWS; }
    //==========================================
}
