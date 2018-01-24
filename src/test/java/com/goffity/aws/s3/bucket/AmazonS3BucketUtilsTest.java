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
import org.mockito.Mockito;
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
    private AmazonS3 amazonS3;

    //    @Mock
    private Region region;

    private AmazonS3BucketUtils amazonS3BucketUtils;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);

    @Before
    public void setUp() throws Exception {
        amazonS3BucketUtils = new AmazonS3BucketUtils(amazonS3, region);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void createBucket() throws Exception {
        System.out.println("createBucket()");

        String bucketName = "bucket-name-test-" + simpleDateFormat.format(new Date());

        Bucket bucket = new Bucket(bucketName);
        when(amazonS3.createBucket(anyString())).thenReturn(bucket);

        Bucket actual = amazonS3BucketUtils.createBucket(bucketName);

        assertNotNull(actual);
        assertEquals(bucketName, actual.getName());
    }

    @Test
    public void listBucket() throws Exception {
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