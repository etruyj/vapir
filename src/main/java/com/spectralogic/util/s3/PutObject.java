//===================================================================
// PutObject.java
//      Description:
//          Functions related to putting objects to AWS using the 
//          PutObjectRequest
//===================================================================

package com.spectralogic.vail.vapir.util.s3;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.File;

import java.util.Map;

public class PutObject {
    public static String fileWithMetadata(String bucket, String object_key, String file_path, Map<String, String> metadata, S3Client client) throws Exception {
        File file = new File(file_path);
        PutObjectRequest request = null;

        request = PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(object_key)
                            .metadata(metadata)
                            .build();

        PutObjectResponse response = client.putObject(request, RequestBody.fromFile(file));

        return response.eTag();
    }
    

    public static String file(String bucket, String object_key, String file_path, S3Client client) throws Exception {
        File file = new File(file_path);

        PutObjectRequest request = PutObjectRequest.builder()
                                    .bucket(bucket)
                                    .key(object_key)
                                    .build();

        PutObjectResponse response = client.putObject(request, RequestBody.fromFile(file));

        return response.eTag();
    }
}
