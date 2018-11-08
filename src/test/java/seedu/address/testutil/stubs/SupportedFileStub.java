package seedu.address.testutil.stubs;

import java.io.IOException;
import java.util.List;

import seedu.address.logic.converters.fileformats.AdaptedPerson;
import seedu.address.logic.converters.fileformats.SupportedFile;
import seedu.address.logic.converters.fileformats.SupportedFileFormat;

/**
 * A default SupportedFile stub that have all of the methods failing.
 */
public class SupportedFileStub implements SupportedFile {
    @Override
    public List<AdaptedPerson> readAdaptedPersons() throws IOException {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void writeAdaptedPersons(List<AdaptedPerson> adaptedPersons) throws IOException {
        throw new AssertionError("This method should not be called.");

    }

    @Override
    public SupportedFileFormat getSupportedFileFormat() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public String getFileName() {
        throw new AssertionError("This method should not be called.");
    }
}
