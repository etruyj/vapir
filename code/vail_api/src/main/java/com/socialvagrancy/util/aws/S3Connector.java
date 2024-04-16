//===================================================================
// S3Connector.java
//      Description:
//          The function calls to interact with an S3 endpoint.
//===================================================================

package com.socialvagrancy.vail.utils.aws;

import software.amazon.awssdk.utils.AttributeMap;
import software.amazon.awssdk.core.internal.http.loader.DefaultSdkHttpClientBuilder;
import software.amazon.awssdk.http.SdkHttpConfigurationOption;
import software.amazon.awssdk.http.SdkHttpClient;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import javax.net.ssl.SSLContext;
import java.net.URI;
import java.security.NoSuchAlgorithmException;

import java.util.Map;

public class S3Connector {
    public static void listBuckets(String endpoint, String region, String access_key, String secret_key, boolean ignore_ssl) {
        S3Client s3 = buildClient(endpoint, region, access_key, secret_key, ignore_ssl);

        ListBuckets.all(s3);
    } 

    public static String putObjectFileWithMetadata(String endpoint, String region, String bucket, String object_key, String file_path, Map<String, String> metadata, String access_key, String secret_key, boolean ignore_ssl) throws Exception {
        S3Client s3 = buildClient(endpoint, region, access_key, secret_key, ignore_ssl);
        
        return PutObject.fileWithMetadata(bucket, object_key, file_path, metadata, s3);
    }

    public static String putObjectFromFile(String endpoint, String region, String bucket, String object_key, String file_path, String access_key, String secret_key, boolean ignore_ssl) throws Exception {
        S3Client s3 = buildClient(endpoint, region, access_key, secret_key, ignore_ssl);
        
        return PutObject.file(bucket, object_key, file_path, s3);
    }


    //===========================================
    // Private Functions
    //===========================================

    private static S3Client buildClient(String endpoint, String region, String access_key, String secret_key, boolean ignore_ssl) {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(access_key, secret_key);
        SdkHttpClient httpClient;

        // Add https:// and / to the endpoint as the arg parser
        // converts the endpoint a format missing this.
        endpoint = "https://" + endpoint + "/"; 

        if(ignore_ssl) {
//            try {
                /*
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, null, null);
                httpClient = UrlConnectionHttpClient.builder().sslContext(sslContext).build();
                */
                final AttributeMap attributeMap = AttributeMap.builder()
                        .put(SdkHttpConfigurationOption.TRUST_ALL_CERTIFICATES, true)
                        .build();
                httpClient = new DefaultSdkHttpClientBuilder().buildWithDefaults(attributeMap);
//            } catch(NoSuchAlgorithmException | java.security.KeyManagementException e) {
//                System.err.println("Failed to create SSL context. " + e.getMessage());
//            }
        }
        else {
            httpClient = ApacheHttpClient.builder().build();
        }
try {
        return S3Client.builder()
                    .region(Region.US_EAST_1)
                    .endpointOverride(new URI(endpoint))
                    .credentialsProvider(StaticCredentialsProvider.create(credentials))
                    .httpClient(httpClient)
                    .build();
    } catch (Exception e) {
        System.err.println("endpoint: " + endpoint);
        System.err.println(e.getMessage());
    }
        return null;
    }


}
