//===================================================================
// Message.java
// Description:
//     Represents a message with a code and a message string.
//
// Created by Sean Snyder.
//===================================================================

package com.spectralogic.vail.vapir.model;

public class Message {
    private String code;
    private String message;

    public Message() {}

    public Message(String code, String message) {
        this.code = code;
        this.message = message;
    }

    //==========================================
    // Getters
    //==========================================
    public String getCode() { return code; }
    public String getMessage() { return message; }
    //==========================================

    //==========================================
    // Setters
    //==========================================
    public void setCode(String code) { this.code = code; }
    public void setMessage(String message) { this.message = message; }
    //==========================================
}
