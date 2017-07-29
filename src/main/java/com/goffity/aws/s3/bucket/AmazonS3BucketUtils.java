package com.goffity.aws.s3.bucket;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.Grant;
import com.amazonaws.services.s3.model.ListVersionsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.Region;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.S3VersionSummary;
import com.amazonaws.services.s3.model.VersionListing;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

public class AmazonS3BucketUtils {
    private final Log logging = LogFactory.getLog(AmazonS3BucketUtils.class);

    private AmazonS3 amazonS3;
    private Region region;

    public AmazonS3BucketUtils(AmazonS3 amazonS3, Region region) {
        this.amazonS3 = amazonS3;
        this.region = region;
    }

    public AmazonS3BucketUtils(Region region) {
        this.region = region;

        this.amazonS3 = AmazonS3Client.builder().withRegion(region.getFirstRegionId()).build();
    }

    public AmazonS3BucketUtils() {
        this.region = Region.AP_Singapore;
        this.amazonS3 = AmazonS3Client.builder().withRegion(Regions.AP_SOUTHEAST_1).build();
    }

    public Bucket createBucket(String bucketName) {
        logging.info("createBucket()");

        return amazonS3.createBucket(bucketName);
    }

    public List<Bucket> listBucket() {
        logging.info("listBucket()");
        return amazonS3.listBuckets();
    }

    public void truncateBucket(Bucket bucket) {
        logging.info("truncateBucket()");

        deleteObjectsInBucket(bucket);
        removeVersionFromBucket(bucket);
    }

    public void deleteObjectsInBucket(Bucket bucket) {
        logging.info("deleteObjectsInBucket()");
        ObjectListing object_listing = amazonS3.listObjects(bucket.getName());
        while (true) {
            for (S3ObjectSummary summary : object_listing.getObjectSummaries()) {
                logging.debug("Delete object " + summary.getKey() + " in bucket " + bucket.getName());
                amazonS3.deleteObject(bucket.getName(), summary.getKey());
            }

            // more object_listing to retrieve?
            if (object_listing.isTruncated()) {
                object_listing = amazonS3.listNextBatchOfObjects(object_listing);
            } else {
                break;
            }
        }
    }

    public void removeVersionFromBucket(Bucket bucket) {
        logging.info("removeVersionFromBucaket()");
        logging.debug(" - removing versions from bucket");
        VersionListing version_listing = amazonS3.listVersions(new ListVersionsRequest().withBucketName(bucket.getName()));
        while (true) {
            for (S3VersionSummary vs : version_listing.getVersionSummaries()) {
                amazonS3.deleteVersion(bucket.getName(), vs.getKey(), vs.getVersionId());
            }

            if (version_listing.isTruncated()) {
                version_listing = amazonS3.listNextBatchOfVersions(version_listing);
            } else {
                break;
            }
        }
    }

    public void deleteBucket(Bucket bucket) {
        logging.info("deleteBucket()");
        amazonS3.deleteBucket(bucket.getName());
    }

    public List<Grant> getBucketAccessControlList(String bucketName) {
        logging.info("getBucketAccessControlList()");

        AccessControlList accessControlList = amazonS3.getBucketAcl(bucketName);
        return accessControlList.getGrantsAsList();
    }

}
