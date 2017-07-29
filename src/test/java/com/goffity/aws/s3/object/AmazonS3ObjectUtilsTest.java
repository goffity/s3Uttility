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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AmazonS3ObjectUtilsTest {

    private final Log logging = LogFactory.getLog(AmazonS3ObjectUtilsTest.class);

    @Mock
    private
    AmazonS3 amazonS3;

    @Mock
    private
    Bucket bucket;

    private AmazonS3ObjectUtils amazonS3ObjectUtils;

    @Before
    public void setUp() throws Exception {
        amazonS3ObjectUtils = new AmazonS3ObjectUtils(amazonS3, bucket);

        when(bucket.getName()).thenReturn("bucket-test-" + new SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(new Date()));
    }

    @After
    public void tearDown() throws Exception {
        amazonS3ObjectUtils = null;
        reset(amazonS3);
        reset(bucket);
    }

    @Test
    public void getObjectList() throws Exception {
        logging.info("getObjectList()");
        ObjectListing objectListing = new ObjectListing();
        objectListing.setBucketName(bucket.getName());
        when(amazonS3.listObjects(anyString())).thenReturn(objectListing);

        ObjectListing result = amazonS3ObjectUtils.getObjectList();

        verify(amazonS3).listObjects(anyString());

        assertNotNull(result);
        assertEquals(bucket.getName(), result.getBucketName());
    }

    @Test
    public void getObjectSummaryList() throws Exception {
        ObjectListing objectListing = mock(ObjectListing.class);
        when(amazonS3.listObjects(anyString())).thenReturn(objectListing);

        S3ObjectSummary s3ObjectSummary = new S3ObjectSummary();
        s3ObjectSummary.setBucketName(bucket.getName());
        List<S3ObjectSummary> s3ObjectSummaries = new ArrayList<S3ObjectSummary>();
        s3ObjectSummaries.add(s3ObjectSummary);
        when(objectListing.getObjectSummaries()).thenReturn(s3ObjectSummaries);

        List<S3ObjectSummary> result = amazonS3ObjectUtils.getObjectSummaryList();

        verify(amazonS3).listObjects(anyString());
        verify(objectListing).getObjectSummaries();

        assertNotNull(result);
        assertNotNull(result.get(0));
        assertEquals(bucket.getName(), result.get(0).getBucketName());
    }

    @Test
    public void getS3Object() throws Exception {
        S3Object s3Object = new S3Object();
        s3Object.setBucketName(bucket.getName());
        String key = bucket.getName() + "-key";
        s3Object.setKey(key);

        when(amazonS3.getObject(anyString(), anyString())).thenReturn(s3Object);

        S3Object result = amazonS3ObjectUtils.getS3Object(bucket, key);

        verify(amazonS3).getObject(anyString(), anyString());

        assertNotNull(result);
        assertEquals(bucket.getName(), result.getBucketName());
        assertEquals(key, result.getKey());
    }

    @Test
    public void deleteAmazonS3Object() throws Exception {
        amazonS3ObjectUtils.deleteAmazonS3Object(bucket, "");
        verify(amazonS3).deleteObject(anyString(), anyString());
    }

    @Test
    public void createAmazonS3Object() throws Exception {
        S3Object s3Object = new S3Object();
        s3Object.setBucketName(bucket.getName());
        String key = bucket.getName() + "-key";
        s3Object.setKey(key);

        when(amazonS3.getObject(anyString(), anyString())).thenReturn(s3Object);

        S3Object result = amazonS3ObjectUtils.createAmazonS3Object(bucket, key, new File(""), CannedAccessControlList.PublicRead);

        verify(amazonS3).putObject(anyString(), anyString(), any(File.class));
        verify(amazonS3).setObjectAcl(anyString(), anyString(), any(CannedAccessControlList.class));

        assertNotNull(result);
    }

    @Test
    public void getObjectUrl() throws Exception {
        String expected = "";

        AmazonS3Client amazonS3Client = mock(AmazonS3Client.class);
        amazonS3ObjectUtils = new AmazonS3ObjectUtils(amazonS3Client, bucket);

        when(amazonS3Client.getResourceUrl(anyString(), anyString())).thenReturn(expected);

        String result = amazonS3ObjectUtils.getObjectUrl(bucket, "");

        assertNotNull(result);
        assertEquals(expected, result);

    }

    @Test
    public void getS3ObjectAccessControlList() throws Exception {
        AccessControlList  accessControlList = mock(AccessControlList.class);
        when(amazonS3.getObjectAcl(anyString(),anyString())).thenReturn(accessControlList);
        when(accessControlList.getGrantsAsList()).thenReturn(new ArrayList<Grant>());

        amazonS3ObjectUtils.getS3ObjectAccessControlList("","");

        verify(amazonS3).getObjectAcl(anyString(),anyString());
    }
}