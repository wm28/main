package seedu.address.logic.converters.fileformats.csv;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_FILE_ALREADY_EXIST;
import static seedu.address.commons.core.Messages.MESSAGE_FILE_NOT_FOUND;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import seedu.address.commons.util.FileUtil;
import seedu.address.logic.converters.fileformats.AdaptedPerson;
import seedu.address.logic.converters.fileformats.SupportedFile;
import seedu.address.logic.converters.fileformats.SupportedFileFormat;

//@@author wm28
/**
 * Represents a csv file that can read and write AdaptedPersons
 */
public class CsvFile implements SupportedFile {
    private final Path fileName;
    private final SupportedFileFormat supportedFileFormat = SupportedFileFormat.CSV;

    public CsvFile(String fileName) {
        requireNonNull(fileName);
        this.fileName = Paths.get(fileName);
    }

    /**
     * Reads data in file and returns csv-formatted person, {@code CsvAdaptedPerson}.
     *
     * @return A List of AdaptedPerson which has objects of the CsvAdaptedPerson class.
     * @throws IOException if file fails to read
     */
    public List<AdaptedPerson> readAdaptedPersons() throws IOException {
        if (!FileUtil.isFileExists(fileName)) {
            throw new FileNotFoundException(String.format(MESSAGE_FILE_NOT_FOUND, fileName.toAbsolutePath()));
        }
        String fileContent = FileUtil.readFromFile(fileName);
        List<String> dataLines = Arrays.asList(fileContent.split("\\r?\\n"));
        List<AdaptedPerson> result = dataLines.stream()
                .filter((dataLine) -> !dataLine.trim().isEmpty())
                .map(line -> new CsvAdaptedPerson(line))
                .collect(Collectors.toList());
        return result;
    }

    /**
     * Write data csv-formatted person, {@code CsvAdaptedPerson}, into the file.
     *
     * @param adaptedPersons A list of AdaptedPerson, which contains CsvAdaptedPerson.
     * @throws IOException if file fails to write.
     */
    public void writeAdaptedPersons(List<AdaptedPerson> adaptedPersons) throws IOException {
        if (FileUtil.isFileExists(fileName)) {
            throw new FileAlreadyExistsException(String.format(MESSAGE_FILE_ALREADY_EXIST, fileName.toAbsolutePath()));
        }
        FileUtil.writeToFile(fileName, adaptedPersons.stream()
                .map((adaptedPerson) -> adaptedPerson.getFormattedString())
                .collect(Collectors.joining("\n")));
    }

    @Override
    public SupportedFileFormat getSupportedFileFormat() {
        return supportedFileFormat;
    }

    @Override
    public String getFileName() {
        return fileName.getFileName().toString();
    }
}
//@@author
