package seedu.address.logic.converters.fileformats;

//@@author wm28
import java.util.Optional;

import seedu.address.commons.util.FileUtil;

/**
 * Represents a supported file format for the import & export commands
 */
public enum SupportedFileFormat {
    CSV;

    /**
     * Searches SupportedFileFormat for the correct file format
     */
    public static Optional<SupportedFileFormat> findSupportedFileFormat(String fileName) {
        Optional<SupportedFileFormat> fileFormat = Optional.empty();
        for (SupportedFileFormat supportedFileFormat : SupportedFileFormat.values()) {
            if (FileUtil.isValidFileExtension(fileName, supportedFileFormat.name())) {
                fileFormat = Optional.of(supportedFileFormat);
            }
        }
        return fileFormat;
    }
}
//@@author
