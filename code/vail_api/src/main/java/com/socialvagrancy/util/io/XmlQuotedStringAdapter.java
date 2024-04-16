//===================================================================
// XmlQuotedStringAdapter.java
//      Description:
//          This class works in tandem with the XmlParser to put 
//          double-quotes (") around the required strings. This works
//          by placing an annotation (@) to this adapter in front of
//          the get parameters, e.g. 
//          @XmlJavaTypeAdapater(XmlQuotedStringAdapter.class)
//
//          This is required formatting for Veeam's SOSAPI
//===================================================================

package com.socialvagrancy.vail.utils.io;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

public class XmlQuotedStringAdapter extends XmlAdapter<String, String> {
    @Override
    public String marshal(String value) {
        // Add quotes to the string.
        return "\"" + value + "\"";
    }

    @Override
    public String unmarshal(String value) {
        // No need to do anything.
        return value;
    }
}
