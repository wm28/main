package seedu.address.logic.converters.fileformats;

import java.io.IOException;
import java.util.List;

//@@author wm28
/**
 * Represents a supported file that can read and write AdaptedPersons
 */
public interface SupportedFile {

    /**
     * Read {@code AdaptedPerson} AdaptedPerson of all types from file.
     */
    List<AdaptedPerson> readAdaptedPersons() throws IOException;

    /**
     * Write {@code AdaptedPerson} of all types to file
     */
    void writeAdaptedPersons(List<AdaptedPerson> adaptedPersons) throws IOException;

    /**
     * Returns the file format the PersonConverter supports
     */
    SupportedFileFormat getSupportedFileFormat();

    /**
     * Returns the file name of the file
     */
    String getFileName();
}
//@@author
