//===================================================================
// ClearCache.java
//      Description:
//          This command clears cached AWS IAM permissions held within
//          the Vail sphere, forcing an update.
//===================================================================

package com.spectralogic.vail.vapir.command;

import com.spectralogic.vail.vapir.api.VailConnector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClearCache {
    private static final Logger log = LoggerFactory.getLogger(ClearCache.class);

    public static String clearIamPermissions(String ip_address, VailConnector sphere) {
        String message = "";

        try {
            log.info("Clearing Vail's AWS IAM account cache and resynchronizing with AWS accounts.");
            sphere.clearCache(ip_address);

            message = "Successfully cleared cached";
            log.info(message);
        } catch(Exception e) {
            message = "Failed to clear cache. " + e.getMessage();
            log.error(message);
        }

        return message;
    }
}
