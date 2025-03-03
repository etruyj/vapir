//===================================================================
// FindEndpoint.java
//      Description:
//          This function searches the list of endpoint and returns
//          the desired endpoint.
//
// Created by Sean Snyder
//===================================================================

package com.spectralogic.vail.vapir.util.search;

import com.spectralogic.vail.vapir.api.VailConnector;
import com.spectralogic.vail.vapir.command.ListEndpoints;
import com.spectralogic.vail.vapir.model.Endpoint;
import com.spectralogic.vail.vapir.util.http.AddressResolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class FindEndpoint {
    private static final Logger log = LoggerFactory.getLogger(FindEndpoint.class);

    public static Endpoint byIp(String ip, VailConnector sphere) {
        log.info("Searching for endpoint associated with endpoint " + ip);

        Endpoint this_endpoint = null;
        String ip_address = null;

        List<Endpoint> endpoint_list = ListEndpoints.all(sphere);

        log.info("Parsing endpoint list for the desired endpoint.");

        for(Endpoint node : endpoint_list) {
            try {
                ip_address = AddressResolver.resolveDomainNameToIP(node.getUrl());
                log.debug("Endpoint " + node.getName() + " has IP " + ip_address);

                if(ip_address.equals(ip)) {
                    log.info("IP address " + ip + " belongs to endpoint " + node.getName());
                    this_endpoint = node;
                }
            } catch(Exception e) {
                log.error(e.getMessage());
            }
        }

        return this_endpoint;
    }
}
