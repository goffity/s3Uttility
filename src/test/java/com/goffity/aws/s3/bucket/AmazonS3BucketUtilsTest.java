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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AmazonS3BucketUtilsTest {

    private final Log logging = LogFactory.getLog(AmazonS3BucketUtilsTest.class);

    @Mock
    private
    AmazonS3 amazonS3;

    private Region region;

    private AmazonS3BucketUtils amazonS3BucketUtils;

    private String bucketName;
    private Bucket bucket;

    @Before
    public void setUp() throws Exception {

        region = Region.AP_Singapore;

        amazonS3BucketUtils = new AmazonS3BucketUtils(amazonS3, region);
        bucketName = "bucket-test-" + new SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(new Date());

        bucket = new Bucket(bucketName);
    }

    @After
    public void tearDown() throws Exception {
        amazonS3BucketUtils = null;
        reset(amazonS3);
    }

    @Test
    public void createBucket() throws Exception {
        when(amazonS3.createBucket(anyString())).thenReturn(bucket);

        Bucket result = amazonS3BucketUtils.createBucket(bucketName);

        verify(amazonS3).createBucket(anyString());

        assertNotNull(result);
        assertEquals(bucketName, result.getName());
    }

    @Test
    public void listBucket() throws Exception {
        List<Bucket> buckets = new ArrayList<Bucket>();
        buckets.add(bucket);

        when(amazonS3.listBuckets()).thenReturn(buckets);
        List<Bucket> result = amazonS3BucketUtils.listBucket();

        verify(amazonS3).listBuckets();

        assertNotNull(result);
        assertNotNull(result.get(0));
        assertEquals(bucketName, result.get(0).getName());
    }

    @Test
    public void truncateBucket() throws Exception {
    }

    @Test
    public void deleteObjectsInBucket() throws Exception {

    }

    @Test
    public void removeVersionFromBucket() throws Exception {

    }

    @Test
    public void deleteBucket() throws Exception {

    }

    @Test
    public void getBucketAccessControlList() throws Exception {

    }

}