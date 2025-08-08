//===================================================================
// Buckets.java
//      Description:
//          Functions associated with API calls to the /buckets
//
// Created by Sean Snyder
//===================================================================

package com.spectralogic.vail.vapir.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import com.spectralogic.vail.vapir.model.Bucket;
import com.spectralogic.vail.vapir.util.http.RestClient;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Buckets {
    private static final Logger log = LoggerFactory.getLogger(Buckets.class);

    public static Bucket createBucket(String ip_address, String name, String body, String token, RestClient rest_client) throws IOException, JsonParseException, Exception {
        Gson gson = new Gson();

        String url = URLs.getPath("bucketCreate", ip_address);

        log.debug("POST " + url);
        log.debug("PAYLOAD: " + body);

        String response = rest_client.post(url, token, body);

        log.debug("RESPONSE: " + response);

        return gson.fromJson(response, Bucket.class);
    }

    public static Bucket createBucket(String ip_address, Bucket bucket, String token, RestClient rest_client) throws IOException, JsonParseException, Exception {
        Gson gson = new Gson();

        String url = URLs.getPath("bucketCreate", ip_address);
        String payload = gson.toJson(bucket);

        log.debug("API URL: POST " + url);
        log.debug("API Payload: " + payload);

        String response = rest_client.post(url, token, payload);

        log.debug("API Response: " + response);

        try {
            Bucket new_bucket =  gson.fromJson(response, Bucket.class);
            

            if(new_bucket.getName() == null) {
                // Assume something went wrong with the JSON parsing.
                throw new JsonParseException("Failed to parse response into bucket object.");
            }
            
            return new_bucket;
        } catch(JsonParseException e) {
            log.error(e.getMessage());
            String error = ServerMessage.process(response);
            log.error(error);

            return null;
        }
    }

    public static Bucket getBucket(String ip_address, String bucket_name, String token, RestClient rest_client) throws IOException, JsonParseException, Exception {
        Gson gson = new Gson();

        String url = URLs.getPath("bucketDetails", ip_address)
                        .replace("{{name}}", bucket_name);

        log.debug("API URL: GET " + url);

        String response = rest_client.get(url, token);

        log.debug("API Response: " + response);

        return gson.fromJson(response, Bucket.class);
        
    } 

    public static Bucket[] list(String ip_address, String token, RestClient rest_client) throws IOException, JsonParseException, Exception {
        Gson gson = new Gson();

        String url = URLs.getPath("bucketList", ip_address);

        log.debug("API URL: GET " + url);

        String response = rest_client.get(url, token);

        log.debug("API Response: " + response);

        return gson.fromJson(response, Bucket[].class);
    }

    public static Bucket updateBucket(String ip_address, Bucket bucket, String token, RestClient rest_client) throws IOException, JsonParseException, Exception {
        // SerializeNulls is required to encode a policy: null value in the Json.
        // This allows us to remove or reset a bucket policy by setting it to null
        // on the patch. If this is omitted, the policy field is not included in the
        // json and we never remove the policy.
        Gson gson = new GsonBuilder().serializeNulls().create();

        String url = URLs.getPath("bucketUpdate", ip_address)
                        .replace("{{name}}", bucket.getName());

        String payload = gson.toJson(bucket);

        log.debug("API URL: PATCH " + url);
        log.debug("API Payload: " + payload);

        String response = rest_client.patch(url, token, payload);

        log.debug("API Response: " + response);

        return gson.fromJson(response, Bucket.class);
    }

    
}
