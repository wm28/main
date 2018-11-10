package seedu.address.testutil.stubs;

import seedu.address.logic.converters.fileformats.SupportedFileFormat;

/**
 * A CsvPersonConverter stub, an implementation of PersonConverter in the CSV format
 */
public class CsvPersonConverterStub extends PersonConverterStub {
    @Override
    public SupportedFileFormat getSupportedFileFormat() {
        return SupportedFileFormat.CSV;
    }
}
