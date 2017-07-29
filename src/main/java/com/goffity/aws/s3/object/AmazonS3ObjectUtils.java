package com.goffity.aws.s3.object;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.Grant;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.List;

public class AmazonS3ObjectUtils {

    private final Log logging = LogFactory.getLog(AmazonS3ObjectUtils.class);

    private AmazonS3 amazonS3;
    private Bucket bucket;

    public AmazonS3ObjectUtils(AmazonS3 amazonS3, Bucket bucket) {
        this.amazonS3 = amazonS3;
        this.bucket = bucket;
    }

    public ObjectListing getObjectList() {
        logging.info("getObjectList()");
        return amazonS3.listObjects(bucket.getName());
    }

    public List<S3ObjectSummary> getObjectSummaryList() {
        logging.info("getObject()");
        return getObjectList().getObjectSummaries();
    }

    public S3Object getS3Object(Bucket bucket, String objectName) {
        logging.info("getS3Object()");
        return amazonS3.getObject(bucket.getName(), objectName);
    }

    public void deleteAmazonS3Object(Bucket bucket, String objectKey) {
        logging.info("deleteAmazonS3Object()");
        this.amazonS3.deleteObject(bucket.getName(), objectKey);
    }

    public S3Object createAmazonS3Object(Bucket bucket, String objectName, File file, CannedAccessControlList cannedAccessControlList) {
        logging.info("createAmazonS3Object()");
        amazonS3.putObject(bucket.getName(), objectName, file);
        amazonS3.setObjectAcl(bucket.getName(), objectName, cannedAccessControlList);

        return amazonS3.getObject(bucket.getName(), objectName);
    }

    public String getObjectUrl(Bucket bucket, String objectKey) {
        logging.info("getObjectUrl()");
        return ((AmazonS3Client) amazonS3).getResourceUrl(bucket.getName(), objectKey);
    }

    public List<Grant> getS3ObjectAccessControlList(String bucketName, String objectName) {
        logging.info("getS3ObjectAccessControlList()");
        AccessControlList accessControlList = amazonS3.getObjectAcl(bucketName, objectName);

        return accessControlList.getGrantsAsList();
    }
}
