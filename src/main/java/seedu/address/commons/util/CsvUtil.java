package seedu.address.commons.util;

import static seedu.address.commons.core.Messages.MESSAGE_FILE_NOT_FOUND;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Helps with reading from and writing to XML files.
 */
//@@author wm28
public class CsvUtil {

    /**
     * Retrieves non-empty lines from the specified csv file.
     */
    public static List<String> getDataLinesFromFile(Path path) throws IOException {
        if (!FileUtil.isFileExists(path)) {
            throw new FileNotFoundException(String.format(MESSAGE_FILE_NOT_FOUND, path.toAbsolutePath()));
        }
        String fileContent = FileUtil.readFromFile(path);
        List<String> dataLines = Arrays.asList(fileContent.split("\\r?\\n"));
        List<String> result = dataLines.stream()
                .filter((dataLine)->!dataLine.trim().isEmpty())
                .collect(Collectors.toList());
        return result;
    }
}
//@@author
