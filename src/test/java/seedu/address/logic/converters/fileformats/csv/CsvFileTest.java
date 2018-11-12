package seedu.address.logic.converters.fileformats.csv;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.converters.fileformats.AdaptedPerson;
import seedu.address.logic.converters.fileformats.SupportedFileFormat;
import seedu.address.testutil.TypicalPersons;

/**
 * Contains unit tests for CsvFile.
 */
public class CsvFileTest {
    public static final String NON_EXISTENT_CSV_FILE = "src/test/data/data/CsvTest/nonExistentGuestList.csv";
    public static final String EXPORTED_CSV_FILE =
            "src/test/data/data/CsvTest/exportedTypicalPersonsGuestList.csv";
    public static final String INVALID_CSV_FILE_PATH = "invalidPath/NoSuchPath/test.csv";

    public static final List<AdaptedPerson> ADAPTED_TYPICAL_PERSONS;

    static {
        ADAPTED_TYPICAL_PERSONS = new ArrayList<>();
        ADAPTED_TYPICAL_PERSONS.add(new CsvAdaptedPerson(TypicalPersons.CSV_ALICE));
        ADAPTED_TYPICAL_PERSONS.add(new CsvAdaptedPerson(TypicalPersons.CSV_BENSON));
        ADAPTED_TYPICAL_PERSONS.add(new CsvAdaptedPerson(TypicalPersons.CSV_CARL));
        ADAPTED_TYPICAL_PERSONS.add(new CsvAdaptedPerson(TypicalPersons.CSV_DANIEL));
        ADAPTED_TYPICAL_PERSONS.add(new CsvAdaptedPerson(TypicalPersons.CSV_ELLE));
        ADAPTED_TYPICAL_PERSONS.add(new CsvAdaptedPerson(TypicalPersons.CSV_FIONA));
        ADAPTED_TYPICAL_PERSONS.add(new CsvAdaptedPerson(TypicalPersons.CSV_GEORGE));
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_nullFilename_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        new CsvFile(null);
    }

    @Test
    public void getSupportedFileFormat_correctFileFormat_returnsFileFormat() {
        CsvFile csvFile = new CsvFile(TypicalPersons.TYPICAL_PERSONS_CSV);
        assertTrue(csvFile.getSupportedFileFormat().equals(SupportedFileFormat.CSV));
    }

    @Test
    public void readAdaptedPersons_validCsvFile_readSuccessful() throws Exception {
        CsvFile csvFile = new CsvFile(TypicalPersons.TYPICAL_PERSONS_CSV);
        List<AdaptedPerson> adaptedPeople = csvFile.readAdaptedPersons();

        assertTrue(adaptedPeople.equals(ADAPTED_TYPICAL_PERSONS));
    }

    @Test
    public void readAdaptedPersons_nonExistentCsvFile_readSuccessful() throws Exception {
        CsvFile csvFile = new CsvFile(NON_EXISTENT_CSV_FILE);
        thrown.expect(IOException.class);
        csvFile.readAdaptedPersons();
    }

    @Test
    public void writeAdaptedPersons_invalidFilePath_throwsNullPointerException() throws Exception {
        thrown.expect(IOException.class);
        CsvFile csvFile = new CsvFile(INVALID_CSV_FILE_PATH);
        csvFile.writeAdaptedPersons(ADAPTED_TYPICAL_PERSONS);
    }

    @Test
    public void writeAdaptedPersons_validCsvFile_writeSuccessful() throws Exception {
        try {
            CsvFile csvFile = new CsvFile(EXPORTED_CSV_FILE);
            csvFile.writeAdaptedPersons(ADAPTED_TYPICAL_PERSONS);

            File actualOutputFile = new File(EXPORTED_CSV_FILE);
            File expectedOutputFile = new File(TypicalPersons.TYPICAL_PERSONS_CSV);

            assertTrue(FileUtils.contentEqualsIgnoreEOL(actualOutputFile, expectedOutputFile, "UTF-8"));
        } finally {
            File file = new File(EXPORTED_CSV_FILE);
            file.delete();
        }
    }
}
