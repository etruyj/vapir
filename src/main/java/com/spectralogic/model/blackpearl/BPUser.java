//===================================================================
// BPUser.java
// Description:
//     Contains user information for the BlackPearl
//===================================================================

package com.spectralogic.vail.vapir.model.blackpearl;

public class BPUser {
    User[] data;

    public int getUserCount() { return data.length; }
    public int getUserId(int user) { return data[user].getId(); }
    public String getUserUsername(int user) { return data[user].getUsername(); }

    public class User {
        private int id;
        private String name;
        private String username;
        private int session_timeout;
        private String created_at;
        private String updated_at;
        private String ds3_user_id;
        private boolean mfa_configured;
        private boolean remote_support_enabled;
        private String default_data_policy_id;
        private boolean global_data_policy_acl_enabled;
        private int max_buckets;

        //=============================
        // Getters
        //=============================
        public int getId() { return id; }
        public String getName() { return name; }
        public String getUsername() { return username; }
        public int getSessionTimeout() { return session_timeout; }
        public String getCreatedAt() { return created_at; }
        public String getUpdatedAt() { return updated_at; }
        public String getDs3UserId() { return ds3_user_id; }
        public boolean isMfaConfigured() { return mfa_configured; }
        public boolean isRemoteSupportEnabled() { return remote_support_enabled; }
        public String getDefaultDataPolicyId() { return default_data_policy_id; }
        public boolean isGlobalDataPolicyAclEnabled() { return global_data_policy_acl_enabled; }
        public int getMaxBuckets() { return max_buckets; }
        //=============================

        //=============================
        // Setters
        //=============================
        public void setId(int id) { this.id = id; }
        public void setName(String name) { this.name = name; }
        public void setUsername(String username) { this.username = username; }
        public void setSessionTimeout(int sessionTimeout) { this.session_timeout = sessionTimeout; }
        public void setCreatedAt(String createdAt) { this.created_at = createdAt; }
        public void setUpdatedAt(String updatedAt) { this.updated_at = updatedAt; }
        public void setDs3UserId(String ds3UserId) { this.ds3_user_id = ds3UserId; }
        public void setMfaConfigured(boolean mfaConfigured) { this.mfa_configured = mfaConfigured; }
        public void setRemoteSupportEnabled(boolean remoteSupportEnabled) { this.remote_support_enabled = remoteSupportEnabled; }
        public void setDefaultDataPolicyId(String defaultDataPolicyId) { this.default_data_policy_id = defaultDataPolicyId; }
        public void setGlobalDataPolicyAclEnabled(boolean globalDataPolicyAclEnabled) { this.global_data_policy_acl_enabled = globalDataPolicyAclEnabled; }
        public void setMaxBuckets(int maxBuckets) { this.max_buckets = maxBuckets; }
        //=============================
    }
}
