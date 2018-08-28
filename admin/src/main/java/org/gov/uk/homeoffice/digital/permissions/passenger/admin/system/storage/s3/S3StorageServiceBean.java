package org.gov.uk.homeoffice.digital.permissions.passenger.admin.system.storage.s3;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.apache.commons.io.FileUtils;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.system.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static java.nio.file.Files.createTempDirectory;

@Service
public class S3StorageServiceBean implements StorageService {

    private final S3KmsClient client;

    @Autowired
    public S3StorageServiceBean(final S3KmsClient client) {
        this.client = client;
    }

    @Override
    public void upload(final File file) throws IOException {
        if (file.isFile() && file.canRead()) {
            final String fileName = file.getName();
            final InputStream targetStream = FileUtils.openInputStream(file);
            upload(fileName, targetStream);
        }
    }

    @Override
    public void upload(final String fileName, final InputStream contents) {
        client.uploadFile(fileName, contents);
    }

    @Override
    public InputStream download(final String fileName) {
        return client.getFile(fileName);
    }

    @Override
    public Path downloadFile(final String fileName) throws IOException {
        final InputStream file = client.getFile(fileName);
        if (file != null){
            return downloadFileToTemp(fileName, file);
        }
        return null;
    }

    @Override
    public void delete(final String... fileNames) {
        Arrays.stream(fileNames).filter(this::exists).forEach(client::deleteFile);
    }

    @Override
    public boolean exists(final String fileName) {
        return client.fileExists(fileName);
    }

    @Override
    public Collection<String> list() {
        return client.listFiles().getObjectSummaries().stream().map(S3ObjectSummary::getKey).collect(Collectors.toList());
    }

    private Path downloadFileToTemp(final String fileName, final InputStream contents) throws IOException {
        Path localOutputDir = createTempDirectory("tmp_");
        Path localOutputFile = Paths.get(localOutputDir.toString(), fileName);
        try (FileOutputStream fos = FileUtils.openOutputStream(localOutputFile.toFile());
             ReadableByteChannel rbc = Channels.newChannel(contents)) {
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        }
        return localOutputFile;
    }

}
