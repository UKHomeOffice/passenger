package org.gov.uk.homeoffice.digital.permissions.passenger.admin.system.storage.s3;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Encryption;
import com.amazonaws.services.s3.AmazonS3EncryptionClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

import static com.amazonaws.regions.Region.getRegion;

@Component
public class S3KmsClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(S3KmsClient.class);

    private String bucketName;
    private String kmsId;
    private AmazonS3Encryption s3client;

    S3KmsClient() {
        super();
    }

    @Autowired
    S3KmsClient(
            @Value("${s3.kms.accessKey}") String accessKey,
            @Value("${s3.kms.secretKey}") String secretKey,
            @Value("${s3.kms.bucket}") String bucketName,
            @Value("${s3.kms.region}") String region,
            @Value("${s3.kms.keyid}") String kmsId) {

        final KMSEncryptionMaterialsProvider kmsEncryptionMaterialsProvider = new KMSEncryptionMaterialsProvider(kmsId);
        final Regions regions = Regions.fromName(region);

        this.bucketName = bucketName;
        this.kmsId = kmsId;
        this.s3client = AmazonS3EncryptionClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .withCryptoConfiguration(new CryptoConfiguration().withAwsKmsRegion(getRegion(regions)))
                .withEncryptionMaterials(kmsEncryptionMaterialsProvider)
                .withRegion(regions)
                .build();
    }

    void uploadFile(final String fileNameAsKey, final InputStream fileData){
        s3client.putObject(new PutObjectRequest(bucketName, fileNameAsKey,
                fileData, new ObjectMetadata()).withSSEAwsKeyManagementParams(new SSEAwsKeyManagementParams(kmsId)));
    }

    ObjectListing listFiles(){
        return s3client.listObjects(bucketName);
    }

    void deleteFile(String fileNameAsKey){
        s3client.deleteObject(new DeleteObjectRequest(bucketName, fileNameAsKey));
    }

    boolean fileExists(final String fileNameAsKey){
        S3Object s3Object = null;
        try {
            s3Object = s3client.getObject(new GetObjectRequest(bucketName, fileNameAsKey));
            if (s3Object.getObjectMetadata() != null) {
                return true;
            }
        }catch (Exception e){
            LOGGER.error("Exception {} thown while checking file {}", e.getMessage(), fileNameAsKey);
        } finally {
            closeS3Object(s3Object);
        }
        return false;
    }

    InputStream getFile(final String fileNameAsKey) {
        S3Object s3Object = null;
        try {
            s3Object = s3client.getObject(new GetObjectRequest(bucketName, fileNameAsKey));
            return IOUtils.toBufferedInputStream(s3Object.getObjectContent());
        } catch (Exception e) {
            LOGGER.error("Exception {} thrown while retrieving file {}", e.getMessage(), fileNameAsKey);
            return null;
        } finally {
            closeS3Object(s3Object);
        }
    }

    private void closeS3Object(final S3Object s3Object) {
        try {
            if (s3Object != null) s3Object.close();
        } catch (IOException e) {
            LOGGER.warn("Unable to close S3 object.", e);
        }
    }

}
