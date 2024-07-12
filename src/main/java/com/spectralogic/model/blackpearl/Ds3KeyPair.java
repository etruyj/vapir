//===================================================================
// Ds3KeyPair.java
// Description:
//     Holds the DS3 key info for the java.
//===================================================================

package com.spectralogic.vail.vapir.model.blackpearl;

public class Ds3KeyPair {
    Credentials[] data;

    public int getCount() { return data.length; }
    public String getAccessKey(int key) { return data[key].getAccessKey(); }
    public String getSecretKey(int key) { return data[key].getSecretKey(); }

    public class Credentials {
        String auth_id;
        String secret_key;

        public String getAccessKey() { return auth_id; }
        public String getSecretKey() { return secret_key; }
    }
}
