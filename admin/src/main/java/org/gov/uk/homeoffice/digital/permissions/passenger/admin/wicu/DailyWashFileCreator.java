package org.gov.uk.homeoffice.digital.permissions.passenger.admin.wicu;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRecord;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Optional;

@Service
public class DailyWashFileCreator {
    private static final Logger logger = LoggerFactory.getLogger(DailyWashFileCreator.class);

    private final CsvRowCreator csvRowCreator;

    public DailyWashFileCreator(CsvRowCreator csvRowCreator) {
        this.csvRowCreator = csvRowCreator;
    }

    public Tuple3<Optional<File>, Optional<File>, Integer> createFiles(Collection<VisaRecord> visaRecords, Instant now) {

        if (visaRecords.isEmpty()) {
            logger.info("no visas found at time: {}, no files created", now);
            return Tuple3.tpl(Optional.empty(), Optional.empty(), 0);
        }

        String nameCheckFileName = filename("Name", now);
        String docCheckFileName = filename("Doc", now);

        Path tmpDir = createTempDir(now);
        File nameCheckFile = createFile(tmpDir, nameCheckFileName);
        File docCheckFile = createFile(tmpDir, docCheckFileName);

        logger.info("nameCheckFile: {}, docCheckFile: {}", nameCheckFile.getAbsolutePath(), docCheckFile.getAbsolutePath());

        int rows = 0;
        try (BufferedWriter nameCheckWriter = new BufferedWriter(new FileWriter(nameCheckFile));
             BufferedWriter docCheckWriter = new BufferedWriter(new FileWriter(docCheckFile))) {

            for (VisaRecord visaRecord : visaRecords) {
                nameCheckWriter.append(csvRowCreator.nameCheckRow(visaRecord)).append("\n");
                docCheckWriter.append(csvRowCreator.docCheckRow(visaRecord)).append("\n");
                rows++;
            }
        } catch (IOException e) {
            logger.warn("error creating temporary files nameCheckFile: {}, docCheckFile: {}",
                    nameCheckFile.getAbsolutePath(), docCheckFile.getAbsolutePath(), e);
            throw new WicuException(e.getMessage());
        }

        return Tuple3.tpl(Optional.of(docCheckFile), Optional.of(nameCheckFile), rows);
    }

    private File createFile(Path dir, String fileName) {
        File nameCheckFile = dir.resolve(fileName).toFile();
        nameCheckFile.deleteOnExit();
        return nameCheckFile;
    }

    private Path createTempDir(Instant now) {
        Path tmpDir;
        try {
            tmpDir = Files.createTempDirectory(now.toEpochMilli() + "");
            logger.info("tmpDir: {}", tmpDir);
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
            throw new WicuException(e.getMessage());
        }
        return tmpDir;
    }

    private String filename(String prefix, Instant instant) {
        return String.format("%sEVISA%sWash.csv",
                DateTimeFormatter.ofPattern("yyMMdd").withZone(ZoneId.systemDefault()).format(instant),
                prefix);
    }
}
