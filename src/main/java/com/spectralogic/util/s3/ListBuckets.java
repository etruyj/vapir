//===================================================================
// ListBuckets.java
//      Description:
//          Function calls to list buckets in AWS.
//          THIS IS A TEST FUNCTION AND NOT INTENDED FOR PRODUCTION CODE
//===================================================================

package com.spectralogic.vail.vapir.util.s3;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.util.List;

public class ListBuckets {
    public static void all(S3Client s3) {
        try {
            ListBucketsResponse response = s3.listBuckets();

            List<String> bucket_names = response.buckets().stream()
                                            .map(b -> b.name())
                                            .toList();

            for(String name : bucket_names) {
                System.out.println(name);
            }
        } catch(S3Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
