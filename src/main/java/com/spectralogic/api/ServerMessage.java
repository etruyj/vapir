//===================================================================
// ServerMessage.java
//      Description:
//          This class manages error handling for the different API
//          calls.
//
// Created by Sean Snyder
//===================================================================

package com.spectralogic.vail.vapir.api;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import com.spectralogic.vail.vapir.model.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerMessage {
    private static final Logger log = LoggerFactory.getLogger(ServerMessage.class);

    public static String process(String response) throws JsonParseException {
        Gson gson = new Gson();

        Message message = gson.fromJson(response, Message.class);

        return "Failed to parse response. [" + message.getCode() + "] " + message.getMessage();
    }
}
