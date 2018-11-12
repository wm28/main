package seedu.address.logic.converters;

import static junit.framework.TestCase.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.converters.exceptions.PersonDecodingException;
import seedu.address.logic.converters.exceptions.PersonEncodingException;
import seedu.address.logic.converters.fileformats.AdaptedPerson;
import seedu.address.logic.converters.fileformats.SupportedFileFormat;
import seedu.address.logic.converters.fileformats.csv.CsvAdaptedPerson;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

//@@author wm28
/**
 * Contains unit tests for CsvPersonConverter.
 */
public class CsvPersonConverterTest {

    public static final String VALID_NAME = "Amy Bee";
    public static final String VALID_PHONE = "11111111";
    public static final String VALID_EMAIL = "amy@gmail.com";
    public static final String VALID_PAYMENT = "PAID";
    public static final String VALID_ATTENDANCE = "PRESENT";
    public static final String VALID_UID = "10000";
    public static final String VALID_TAG_FRIEND = "friend";
    public static final String VALID_TAG_DIET = "NORMAL";


    public static final String INVALID_NAME = "James&"; // '&' not allowed in names
    public static final String INVALID_PHONE = "911a"; // 'a' not allowed in phone numbers
    public static final String INVALID_EMAIL = "bob!yahoo"; // missing '@' symbol
    public static final String INVALID_UID = "!0001"; // '!' is not allowed in uid
    public static final String INVALID_TAG_QUOTATIONS = "\"vegetarian"; // '"' not allowed
    public static final String INVALID_TAG_SPACES = "\"No shrimp"; // spaces are not allowed

    public static final String VALID_CSV = VALID_NAME + ","
            + VALID_PHONE + ","
            + VALID_EMAIL + ","
            + VALID_PAYMENT + ","
            + VALID_ATTENDANCE + ","
            + VALID_UID + ","
            + VALID_TAG_FRIEND + ","
            + VALID_TAG_DIET;

    public static final String INVALID_NAME_CSV = INVALID_NAME + ","
            + VALID_PHONE + ","
            + VALID_EMAIL + ","
            + VALID_PAYMENT + ","
            + VALID_ATTENDANCE + ","
            + VALID_UID + ","
            + VALID_TAG_FRIEND + ","
            + VALID_TAG_DIET;

    public static final String INVALID_PHONE_CSV = VALID_NAME + ","
            + INVALID_PHONE + ","
            + VALID_EMAIL + ","
            + VALID_PAYMENT + ","
            + VALID_ATTENDANCE + ","
            + VALID_UID + ","
            + VALID_TAG_FRIEND + ","
            + VALID_TAG_DIET;

    public static final String INVALID_EMAIL_CSV = VALID_NAME + ","
            + VALID_PHONE + ","
            + INVALID_EMAIL + ","
            + VALID_PAYMENT + ","
            + VALID_ATTENDANCE + ","
            + VALID_UID + ","
            + VALID_TAG_FRIEND + ","
            + VALID_TAG_DIET;

    public static final String INVALID_TAGS_CSV = VALID_NAME + ","
            + VALID_PHONE + ","
            + VALID_EMAIL + ","
            + VALID_PAYMENT + ","
            + VALID_ATTENDANCE + ","
            + VALID_UID + ","
            + VALID_TAG_FRIEND + ","
            + INVALID_TAG_SPACES + ","
            + INVALID_TAG_QUOTATIONS;

    public static final String INSUFFICIENT_FIELDS_CSV = VALID_NAME + ","
            + VALID_PHONE + ","
            + INVALID_PHONE;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void decodePerson_validAdaptedPerson_decodeSuccessful() throws Exception {
        PersonBuilder personBuilder = new PersonBuilder();
        personBuilder.withName(VALID_NAME)
                .withPhone(VALID_PHONE)
                .withEmail(VALID_EMAIL)
                .withAttendance(VALID_ATTENDANCE)
                .withPayment(VALID_PAYMENT)
                .withTags(VALID_TAG_FRIEND, VALID_TAG_DIET);
        Person validPerson = personBuilder.build();

        CsvPersonConverter csvPersonConverter = new CsvPersonConverter();
        AdaptedPerson validAdaptedPerson = new CsvAdaptedPerson(VALID_CSV);
        Person decodedPerson = csvPersonConverter.decodePerson(validAdaptedPerson);

        assertTrue(decodedPerson.equals(validPerson));
    }

    @Test
    public void decodePerson_invalidPersonName_throwsPersonDecodingException() throws Exception {
        CsvPersonConverter csvPersonConverter = new CsvPersonConverter();
        AdaptedPerson invalidNameAdaptedPerson = new CsvAdaptedPerson(INVALID_NAME_CSV);
        thrown.expect(PersonDecodingException.class);
        thrown.expectMessage(Name.MESSAGE_NAME_CONSTRAINTS);
        csvPersonConverter.decodePerson(invalidNameAdaptedPerson);
    }

    @Test
    public void decodePerson_invalidPhoneNumber_throwsPersonDecodingException() throws Exception {
        CsvPersonConverter csvPersonConverter = new CsvPersonConverter();
        AdaptedPerson invalidPhoneAdaptedPerson = new CsvAdaptedPerson(INVALID_PHONE_CSV);
        thrown.expect(PersonDecodingException.class);
        thrown.expectMessage(Phone.MESSAGE_PHONE_CONSTRAINTS);
        csvPersonConverter.decodePerson(invalidPhoneAdaptedPerson);
    }

    @Test
    public void decodePerson_invalidEmail_throwsPersonDecodingException() throws Exception {
        CsvPersonConverter csvPersonConverter = new CsvPersonConverter();
        AdaptedPerson invalidEmailAdaptedPerson = new CsvAdaptedPerson(INVALID_EMAIL_CSV);
        thrown.expect(PersonDecodingException.class);
        thrown.expectMessage(Email.MESSAGE_EMAIL_CONSTRAINTS);
        csvPersonConverter.decodePerson(invalidEmailAdaptedPerson);
    }

    @Test
    public void decodePerson_invalidTags_throwsPersonDecodingException() throws Exception {
        CsvPersonConverter csvPersonConverter = new CsvPersonConverter();
        AdaptedPerson invalidTagsAdaptedPerson = new CsvAdaptedPerson(INVALID_TAGS_CSV);
        thrown.expect(PersonDecodingException.class);
        thrown.expectMessage(Tag.MESSAGE_TAG_CONSTRAINTS);
        csvPersonConverter.decodePerson(invalidTagsAdaptedPerson);
    }

    @Test
    public void decodePerson_insufficientCsvData_throwsPersonDecodingException() throws Exception {
        CsvPersonConverter csvPersonConverter = new CsvPersonConverter();
        AdaptedPerson insufficientFieldsAdaptedPerson = new CsvAdaptedPerson(INSUFFICIENT_FIELDS_CSV);
        thrown.expect(PersonDecodingException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        csvPersonConverter.decodePerson(insufficientFieldsAdaptedPerson);
    }


    @Test
    public void encodePerson_validPerson_encodeSuccessful() throws Exception {
        PersonBuilder personBuilder = new PersonBuilder();
        personBuilder.withName(VALID_NAME)
                .withPhone(VALID_PHONE)
                .withEmail(VALID_EMAIL)
                .withAttendance(VALID_ATTENDANCE)
                .withPayment(VALID_PAYMENT)
                .withUid(VALID_UID)
                .withTags(VALID_TAG_FRIEND, VALID_TAG_DIET);
        Person validPerson = personBuilder.build();
        CsvPersonConverter csvPersonConverter = new CsvPersonConverter();
        AdaptedPerson validAdaptedPerson = csvPersonConverter.encodePerson(validPerson);

        assertTrue(VALID_CSV.equals(validAdaptedPerson.getFormattedString()));
    }

    @Test
    public void encodePerson_nullPerson_throwsPersonEncodingException() throws Exception {
        CsvPersonConverter csvPersonConverter = new CsvPersonConverter();
        thrown.expect(PersonEncodingException.class);
        csvPersonConverter.encodePerson(null);
    }

    @Test
    public void getSupportedFileFormat_correctFileFormat_returnsFileFormat() throws Exception {
        CsvPersonConverter csvPersonConverter = new CsvPersonConverter();
        assertTrue(csvPersonConverter.getSupportedFileFormat().equals(SupportedFileFormat.CSV));
    }
}
//@@author
