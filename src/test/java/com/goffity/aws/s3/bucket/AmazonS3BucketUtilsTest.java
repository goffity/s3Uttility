package com.goffity.aws.s3.bucket;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.Region;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AmazonS3BucketUtilsTest {
    private final Log logging = LogFactory.getLog(AmazonS3BucketUtilsTest.class);

    @Mock
    private AmazonS3 amazonS3;

    private Region region;

    private AmazonS3BucketUtils amazonS3BucketUtils;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);

    @Before
    public void setUp() {
        amazonS3BucketUtils = new AmazonS3BucketUtils(amazonS3, region);
    }

    @After
    public void tearDown() {

    }

    @Test
    public void createBucket() {
        System.out.println("createBucket()");

        String bucketName = "bucket-name-test-" + simpleDateFormat.format(new Date());

        Bucket bucket = new Bucket(bucketName);
        when(amazonS3.createBucket(anyString())).thenReturn(bucket);

        Bucket actual = amazonS3BucketUtils.createBucket(bucketName);

        assertNotNull(actual);
        assertEquals(bucketName, actual.getName());
    }

    @Test
    public void listBucket() {
        System.out.println("listBucket()");
        List<Bucket> buckets = new ArrayList<Bucket>();
        Bucket bucket = new Bucket();
        buckets.add(bucket);

        when(amazonS3.listBuckets()).thenReturn(buckets);
        List<Bucket> bucketList = amazonS3BucketUtils.listBucket();
        assertNotNull(bucketList);

        verify(amazonS3, times(1)).listBuckets();
    }

    @Test
    public void truncateBucket() {
    }

    @Test
    public void deleteObjectsInBucket() {

    }

    @Test
    public void removeVersionFromBucket() {

    }

    @Test
    public void deleteBucket() {

    }

    @Test
    public void getBucketAccessControlList() {

    }

}