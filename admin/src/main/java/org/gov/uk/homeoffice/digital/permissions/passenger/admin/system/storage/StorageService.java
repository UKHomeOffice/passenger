package org.gov.uk.homeoffice.digital.permissions.passenger.admin.system.storage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Collection;

public interface StorageService {

    void upload(File file) throws IOException;

    void upload(String fileName, InputStream contents);

    InputStream download(String fileName);

    Path downloadFile(String fileName) throws IOException;

    void delete(String... filenames);

    boolean exists(String fileName);

    Collection<String> list();

}
