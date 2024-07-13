//======================================================================
// AddressResolver.java
//      Description:
//          This class contains methods to determine if a given address is an 
//          IPv4 address or a domain name. If the address is a domain name, 
//          it converts it to an IP address using DNS lookup.
//
// Created by Sean Snyder
//======================================================================
package com.spectralogic.vail.vapir.util.http;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

public class AddressResolver {
    // Regex pattern for matching IPv4 addresses
    private static final Pattern IPV4_PATTERN = Pattern.compile(
        "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$"
    );

    
    // Checks if the given address is an IPv4 address.
    //  @param address the address to check
    //  @return true if the address is an IPv4 address, false otherwise
    public static boolean isIPv4Address(String address) {
        return IPV4_PATTERN.matcher(address).matches();
    }

    // Resolves the given domain name to an IP address.
    //  @param domainName the domain name to resolve
    //  @return the resolved IP address as a string, or null if resolution fails
    public static String resolveDomainNameToIP(String domainName) throws UnknownHostException {
        if(isIPv4Address(domainName)) {
            return domainName;
        } else {
            InetAddress inetAddress = InetAddress.getByName(domainName);
            return inetAddress.getHostAddress();
        }
    }
}

