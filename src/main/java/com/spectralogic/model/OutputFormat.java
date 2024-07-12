//===================================================================
// OutputFormat.java
// Description:
//     Represents a format for output with a header, value, and indentation level.
//
// Created by Sean Snyder.
//===================================================================

package com.spectralogic.vail.vapir.model;

public class OutputFormat {
    private String header;
    private String value;
    private int indents;

    //=======================================
    // Getters
    //=======================================

    public String getHeader() { return header; }
    public String getValue() { return value; }
    public int getIndents() { return indents; }

    //=======================================
    // Setters
    //=======================================

    public void setHeader(String header) { this.header = header; }
    public void setValue(String value) { this.value = value; }
    public void setIndents(int indents) { this.indents = indents; }
}
