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


public class CsvConverterTest {

    public static final String VALID_NAME = "Amy Bee";
    public static final String VALID_PHONE = "11111111";
    public static final String VALID_EMAIL = "amy@gmail.com";
    public static final String VALID_PAYMENT = "PAID";
    public static final String VALID_ATTENDANCE = "PRESENT";
    public static final String VALID_TAG_FRIEND = "friend";
    public static final String VALID_TAG_ADDED = "added"; //used to test whether a tag has been added to a person
    public static final String VALID_TAG_UNUSED = "unused"; //do not use this tag when creating a person
    public static final String VALID_TAG_DIET = "NORMAL";


    public static final String INVALID_NAME = "James&"; // '&' not allowed in names
    public static final String INVALID_PHONE = "911a"; // 'a' not allowed in phone numbers
    public static final String INVALID_EMAIL = "bob!yahoo"; // missing '@' symbol
    public static final String INVALID_TAG_QUOTATIONS = "\"vegetarian"; // '"' not allowed
    public static final String INVALID_TAG_SPACES = "\"No shrimp"; // spaces are not allowed

    public static final String VALID_CSV = VALID_NAME + ","
            + VALID_PHONE + ","
            + VALID_EMAIL + ","
            + VALID_PAYMENT + ","
            + VALID_ATTENDANCE + ","
            + VALID_TAG_FRIEND + ","
            + VALID_TAG_DIET;

    public static final String INVALID_NAME_CSV = INVALID_NAME + ","
            + VALID_PHONE + ","
            + VALID_EMAIL + ","
            + VALID_PAYMENT + ","
            + VALID_ATTENDANCE + ","
            + VALID_TAG_FRIEND + ","
            + VALID_TAG_DIET;

    public static final String INVALID_PHONE_CSV = VALID_NAME + ","
            + INVALID_PHONE + ","
            + VALID_EMAIL + ","
            + VALID_PAYMENT + ","
            + VALID_ATTENDANCE + ","
            + VALID_TAG_FRIEND + ","
            + VALID_TAG_DIET;

    public static final String INVALID_EMAIL_CSV = VALID_NAME + ","
            + VALID_PHONE + ","
            + INVALID_EMAIL + ","
            + VALID_PAYMENT + ","
            + VALID_ATTENDANCE + ","
            + VALID_TAG_FRIEND + ","
            + VALID_TAG_DIET;

    public static final String INVALID_TAGS_CSV = VALID_NAME + ","
            + VALID_PHONE + ","
            + VALID_EMAIL + ","
            + VALID_PAYMENT + ","
            + VALID_ATTENDANCE + ","
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

        CsvConverter csvConverter = new CsvConverter();
        AdaptedPerson validAdaptedPerson = new CsvAdaptedPerson(VALID_CSV);
        Person decodedPerson = csvConverter.decodePerson(validAdaptedPerson);

        assertTrue(decodedPerson.equals(validPerson));
    }

    @Test
    public void decodePerson_invalidPersonName_throwsPersonDecodingException() throws Exception {
        CsvConverter csvConverter = new CsvConverter();
        AdaptedPerson invalidNameAdaptedPerson = new CsvAdaptedPerson(INVALID_NAME_CSV);
        thrown.expect(PersonDecodingException.class);
        thrown.expectMessage(Name.MESSAGE_NAME_CONSTRAINTS);
        csvConverter.decodePerson(invalidNameAdaptedPerson);
    }

    @Test
    public void decodePerson_invalidPhoneNumber_throwsPersonDecodingException() throws Exception {
        CsvConverter csvConverter = new CsvConverter();
        AdaptedPerson invalidPhoneAdaptedPerson = new CsvAdaptedPerson(INVALID_PHONE_CSV);
        thrown.expect(PersonDecodingException.class);
        thrown.expectMessage(Phone.MESSAGE_PHONE_CONSTRAINTS);
        csvConverter.decodePerson(invalidPhoneAdaptedPerson);
    }

    @Test
    public void decodePerson_invalidEmail_throwsPersonDecodingException() throws Exception {
        CsvConverter csvConverter = new CsvConverter();
        AdaptedPerson invalidEmailAdaptedPerson = new CsvAdaptedPerson(INVALID_EMAIL_CSV);
        thrown.expect(PersonDecodingException.class);
        thrown.expectMessage(Email.MESSAGE_EMAIL_CONSTRAINTS);
        csvConverter.decodePerson(invalidEmailAdaptedPerson);
    }

    @Test
    public void decodePerson_invalidTags_throwsPersonDecodingException() throws Exception {
        CsvConverter csvConverter = new CsvConverter();
        AdaptedPerson invalidTagsAdaptedPerson = new CsvAdaptedPerson(INVALID_TAGS_CSV);
        thrown.expect(PersonDecodingException.class);
        thrown.expectMessage(Tag.MESSAGE_TAG_CONSTRAINTS);
        csvConverter.decodePerson(invalidTagsAdaptedPerson);
    }

    @Test
    public void decodePerson_insufficientCsvData_throwsPersonDecodingException() throws Exception {
        CsvConverter csvConverter = new CsvConverter();
        AdaptedPerson insufficientFieldsAdaptedPerson = new CsvAdaptedPerson(INSUFFICIENT_FIELDS_CSV);
        thrown.expect(PersonDecodingException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        csvConverter.decodePerson(insufficientFieldsAdaptedPerson);
    }


    @Test
    public void encodePerson_validPerson_encodeSuccessful() throws Exception {
        PersonBuilder personBuilder = new PersonBuilder();
        personBuilder.withName(VALID_NAME)
                .withPhone(VALID_PHONE)
                .withEmail(VALID_EMAIL)
                .withAttendance(VALID_ATTENDANCE)
                .withPayment(VALID_PAYMENT)
                .withTags(VALID_TAG_FRIEND, VALID_TAG_DIET);
        Person validPerson = personBuilder.build();
        CsvConverter csvConverter = new CsvConverter();
        AdaptedPerson validAdaptedPerson = csvConverter.encodePerson(validPerson);

        assertTrue(VALID_CSV.equals(validAdaptedPerson.getFormattedString()));
    }

    @Test
    public void encodePerson_nullPerson_throwsPersonEncodingException() throws Exception {
        CsvConverter csvConverter = new CsvConverter();
        thrown.expect(PersonEncodingException.class);
        csvConverter.encodePerson(null);
    }

    @Test
    public void getSupportedFileFormat_correctFileFormat_returnsFileFormat() throws Exception {
        CsvConverter csvConverter = new CsvConverter();
        assertTrue(csvConverter.getSupportedFileFormat().equals(SupportedFileFormat.CSV));
    }
}
