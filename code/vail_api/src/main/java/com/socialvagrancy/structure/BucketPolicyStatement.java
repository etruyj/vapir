//===================================================================
// BucketPolicyStatement.java
//      Description:
//          A model of the policy statement.
//===================================================================

package com.socialvagrancy.vail.structures;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.socialvagrancy.vail.utils.http.ArrayOrStringAdapter;
import java.lang.IllegalArgumentException;
import java.util.ArrayList;

public class BucketPolicyStatement {
    @SerializedName("Action")
    @JsonAdapter(ArrayOrStringAdapter.class)
    public ArrayList<String> action;
    @SerializedName("Effect")
    public String effect;
    @SerializedName("Principal")
    public BucketPolicyPrincipal principal;
    @SerializedName("Resource")
    public ArrayList<String> resource;
    @SerializedName("Sid")
    public String sid;

    public BucketPolicyStatement() {
        action = new ArrayList<String>();
        resource = new ArrayList<String>();
    }

    public BucketPolicyStatement(BucketPolicyStatement other) {
        action = other.getAction();
        effect = other.getEffect();
        principal = new BucketPolicyPrincipal(other.getPrincipal());
        resource = other.getResource();
        sid = other.getSid();
    }

    //==========================================
    // Getters
    //==========================================
    public ArrayList<String> getAction() { return action; }
    public String getEffect() { return effect; }
    public BucketPolicyPrincipal getPrincipal() { return principal; }
    public ArrayList<String> getResource() { return resource; }
    public String getSid() { return sid; }

    //==========================================
    // Setters
    //==========================================
    public void addAction(String action) { this.action.add(action); }
    public void addResource(String resource) { this.resource.add(resource); }
    public void setAction(ArrayList<String> action) { this.action = action; }
    public void setEffect(String effect) { this.effect = effect; }
    public void setPrincipal(BucketPolicyPrincipal principal) { this.principal = principal; }
    public void setResource(ArrayList<String> resource) { this.resource = resource; }
    public void setSid(String sid) { this.sid = sid; }
}
