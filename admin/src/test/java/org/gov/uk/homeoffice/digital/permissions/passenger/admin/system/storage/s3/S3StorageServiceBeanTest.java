package org.gov.uk.homeoffice.digital.permissions.passenger.admin.system.storage.s3;

import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.StringInputStream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class S3StorageServiceBeanTest {

    private static final String FILE_NAME = "test-file.txt";

    @InjectMocks
    private S3StorageServiceBean underTest;

    @Mock
    private S3KmsClient mockS3KmsClient;

    @Test
    public void shouldUpload() {
        final InputStream mockContent = mock(InputStream.class);
        underTest.upload(FILE_NAME, mockContent);
        verify(mockS3KmsClient).uploadFile(FILE_NAME, mockContent);
    }

    @Test
    public void shouldDownload() {
        underTest.download(FILE_NAME);
        verify(mockS3KmsClient).getFile(FILE_NAME);
    }

    @Test
    public void shouldDownloadFile() throws Exception {
        when(mockS3KmsClient.getFile(FILE_NAME)).thenReturn(new StringInputStream("test contents"));
        final Path file = underTest.downloadFile(FILE_NAME);
        assertThat(file, notNullValue());
    }

    @Test
    public void shouldDeleteFiles() {
        final String MISSING_FILE_NAME = "missing.txt";

        when(mockS3KmsClient.fileExists(FILE_NAME)).thenReturn(true);
        when(mockS3KmsClient.fileExists(MISSING_FILE_NAME)).thenReturn(false);

        underTest.delete(FILE_NAME, MISSING_FILE_NAME);

        verify(mockS3KmsClient).deleteFile(FILE_NAME);
    }

    @Test
    public void shouldExist() {
        when(mockS3KmsClient.fileExists(FILE_NAME)).thenReturn(true);
        assertTrue(underTest.exists(FILE_NAME));
    }

    @Test
    public void shouldNotExist() {
        when(mockS3KmsClient.fileExists(FILE_NAME)).thenReturn(false);
        assertFalse(underTest.exists(FILE_NAME));
    }

    @Test
    public void shouldListFiles() {
        final ObjectListing objectListing = new ObjectListing();
        objectListing.getObjectSummaries().add(objectSummary("file1.txt"));
        objectListing.getObjectSummaries().add(objectSummary("file2.txt"));

        when(mockS3KmsClient.listFiles()).thenReturn(objectListing);

        final Collection<String> listing = underTest.list();
        assertThat(listing, containsInAnyOrder("file1.txt", "file2.txt"));
    }

    private S3ObjectSummary objectSummary(final String key) {
        S3ObjectSummary s3ObjectSummary = new S3ObjectSummary();
        s3ObjectSummary.setKey(key);
        return s3ObjectSummary;
    }

}