package seedu.address.logic.converters.fileformats.csv;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileSystemUtils;
import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.converters.fileformats.AdaptedPerson;
import seedu.address.logic.converters.fileformats.SupportedFileFormat;

public class CsvFileTest {

    public static final String VALID_CSV_FILE = "src\\test\\data\\data\\CsvTest\\typicalPersonsGuestList.csv";
    public static final String VALID_PERSON_ONE_CSV_FILE =
            "Alma Andrade,80000867,80000867@gmail.com,PAID,ABSENT,halal";
    public static final String VALID_PERSON_TWO_CSV_FILE =
            "Eamon Webster,80000030,80000030@gmail.com,PAID,ABSENT,vegetarian";
    public static final String VALID_PERSON_THREE_CSV_FILE =
            "Edan Morse,80000200,80000200@gmail.com,PAID,ABSENT,vegan";
    public static final String VALID_PERSON_FOUR_CSV_FILE =
            "ElsieMae Vasquez,80000044,80000044@gmail.com,PAID,ABSENT";

    public static final String NON_EXISTENT_CSV_FILE = "src\\test\\data\\data\\CsvTest\\nonExistentGuestList.csv";
    public static final String EXPORTED_CSV_FILE =
            "src\\test\\data\\data\\CsvTest\\exportedTypicalPersonsGuestList.csv";

    public static final List<AdaptedPerson> ADAPTED_PEOPLE;

    static {
        ADAPTED_PEOPLE = new ArrayList<>();
        ADAPTED_PEOPLE.add(new CsvAdaptedPerson(VALID_PERSON_ONE_CSV_FILE));
        ADAPTED_PEOPLE.add(new CsvAdaptedPerson(VALID_PERSON_TWO_CSV_FILE));
        ADAPTED_PEOPLE.add(new CsvAdaptedPerson(VALID_PERSON_THREE_CSV_FILE));
        ADAPTED_PEOPLE.add(new CsvAdaptedPerson(VALID_PERSON_FOUR_CSV_FILE));

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
        CsvFile csvFile = new CsvFile(VALID_CSV_FILE);
        assertTrue(csvFile.getSupportedFileFormat().equals(SupportedFileFormat.CSV));
    }

    @Test
    public void readAdaptedPersons_validCsvFile_readSuccessful() throws Exception {
        CsvFile csvFile = new CsvFile(VALID_CSV_FILE);
        List<AdaptedPerson> adaptedPeople = csvFile.readAdaptedPersons();

        assertTrue(adaptedPeople.equals(ADAPTED_PEOPLE));
    }

    @Test
    public void readAdaptedPersons_nonExistentCsvFile_readSuccessful() throws Exception {
        CsvFile csvFile = new CsvFile(NON_EXISTENT_CSV_FILE);
        thrown.expect(IOException.class);
        csvFile.readAdaptedPersons();
    }

    @Test
    public void writeAdaptedPersons_validCsvFile_writeSuccessful() throws Exception {
        try {
            CsvFile csvFile = new CsvFile(EXPORTED_CSV_FILE);
            csvFile.writeAdaptedPersons(ADAPTED_PEOPLE);

            File actualOutputFile = new File(EXPORTED_CSV_FILE);
            File expectedOutputFile = new File(VALID_CSV_FILE);

            assertTrue(FileUtils.contentEqualsIgnoreEOL(actualOutputFile, expectedOutputFile, "UTF-8"));
        } finally {
            File file = new File(EXPORTED_CSV_FILE);
            file.delete();
        }
    }
}
