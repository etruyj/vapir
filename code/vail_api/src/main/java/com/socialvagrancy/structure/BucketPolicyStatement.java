//===================================================================
// BucketPolicyStatement.java
//      Description:
//          A model of the policy statement.
//===================================================================

package com.socialvagrancy.vail.structures;

import java.util.ArrayList;

public class BucketPolicyStatement {
    private ArrayList<String> action;
    private String effect;
    private BucketPolicyPrincipal principal;
    private ArrayList<String> resource;
    private String sid;

    public BucketPolicyStatement() {
        action = new ArrayList<String>();
        resource = new ArrayList<String>();
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
