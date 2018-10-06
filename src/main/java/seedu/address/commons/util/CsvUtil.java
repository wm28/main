package seedu.address.commons.util;

import static seedu.address.commons.core.Messages.MESSAGE_FILE_ALREADY_EXIST;
import static seedu.address.commons.core.Messages.MESSAGE_FILE_NOT_FOUND;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Helps with reading from and writing to CSV files.
 */
public class CsvUtil {

    /**
     * reads data lines from the csv file
     */
    public static List<String> getDataLinesFromFile(Path path) throws IOException {
        if (!FileUtil.isFileExists(path)) {
            throw new FileNotFoundException(String.format(MESSAGE_FILE_NOT_FOUND, path.toAbsolutePath()));
        }
        String fileContent = FileUtil.readFromFile(path);
        return Arrays.asList(fileContent.split("\\r?\\n"));
    }

    /**
     * writes data lines to csv file
     */
    public static void saveDataLinesToFile(Path path, List<String> dataLines) throws IOException {
        if (FileUtil.isFileExists(path)) {
            throw new FileAlreadyExistsException(String.format(MESSAGE_FILE_ALREADY_EXIST, path.toAbsolutePath()));
        }
        FileUtil.writeToFile(path, dataLines.stream().collect(Collectors.joining("\n")));
    }
}
