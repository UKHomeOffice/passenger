package org.gov.uk.homeoffice.digital.permissions.passenger.admin.system.storage.s3;

import com.amazonaws.services.s3.AmazonS3Encryption;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.StringInputStream;
import org.apache.http.client.methods.HttpGet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.InputStream;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class S3KmsClientTest {

    private static final String FILE_NAME = "TestFile.txt";
    private static final String BUCKET_NAME = "test-bucket-aws";
    private static final String KMS_ID = UUID.randomUUID().toString();

    @InjectMocks
    private S3KmsClient underTest = new S3KmsClient();

    @Mock
    private AmazonS3Encryption mockAmazonS3Encryption;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(underTest, "bucketName", BUCKET_NAME);
        ReflectionTestUtils.setField(underTest, "kmsId", KMS_ID);
    }

    @Test
    public void shouldUpload() {
        final InputStream mockInputStream = mock(InputStream.class);
        final ArgumentCaptor<PutObjectRequest> requestArgumentCaptor = ArgumentCaptor.forClass(PutObjectRequest.class);

        underTest.uploadFile(FILE_NAME, mockInputStream);

        verify(mockAmazonS3Encryption).putObject(requestArgumentCaptor.capture());

        final PutObjectRequest request = requestArgumentCaptor.getValue();
        assertThat(request.getBucketName(), is(BUCKET_NAME));
        assertThat(request.getInputStream(), is(mockInputStream));
        assertThat(request.getKey(), is(FILE_NAME));
    }

    @Test
    public void shouldListFiles() {
        underTest.listFiles();
        verify(mockAmazonS3Encryption).listObjects(BUCKET_NAME);
    }

    @Test
    public void shouldDeleteFile() {
        final ArgumentCaptor<DeleteObjectRequest> requestArgumentCaptor = ArgumentCaptor.forClass(DeleteObjectRequest.class);

        underTest.deleteFile(FILE_NAME);

        verify(mockAmazonS3Encryption).deleteObject(requestArgumentCaptor.capture());

        final DeleteObjectRequest request = requestArgumentCaptor.getValue();
        assertThat(request.getBucketName(), is(BUCKET_NAME));
        assertThat(request.getKey(), is(FILE_NAME));
    }

    @Test
    public void shouldReturnTrueIfKeyExists() {
        final S3Object mockS3Object = mock(S3Object.class);
        when(mockAmazonS3Encryption.getObject(Mockito.any(GetObjectRequest.class))).thenReturn(mockS3Object);
        when(mockS3Object.getObjectMetadata()).thenReturn(new ObjectMetadata());
        assertTrue(underTest.fileExists(FILE_NAME));
    }

    @Test
    public void shouldReturnFalseIfKeyDoesNotExist() {
        final S3Object mockS3Object = mock(S3Object.class);
        when(mockAmazonS3Encryption.getObject(Mockito.any(GetObjectRequest.class))).thenReturn(mockS3Object);
        when(mockS3Object.getObjectMetadata()).thenReturn(null);
        assertFalse(underTest.fileExists(FILE_NAME));
    }

    @Test
    public void shouldGetFile() throws Exception {
        final S3Object mockS3Object = mock(S3Object.class);
        final InputStream inputStream = new StringInputStream("file content");
        when(mockAmazonS3Encryption.getObject(Mockito.any(GetObjectRequest.class))).thenReturn(mockS3Object);
        when(mockS3Object.getObjectContent()).thenReturn(new S3ObjectInputStream(inputStream, new HttpGet()));
        assertThat(underTest.getFile(FILE_NAME), instanceOf(InputStream.class));
    }

}