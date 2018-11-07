package seedu.address.testutil.stubs;

import seedu.address.logic.converters.fileformats.SupportedFileFormat;

/**
 * A CsvFile stub, an implementation of SupportedFile in the CSV format
 */
public class CsvFileStub extends SupportedFileStub {
    @Override
    public SupportedFileFormat getSupportedFileFormat() {
        return SupportedFileFormat.CSV;
    }
}
