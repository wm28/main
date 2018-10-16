package seedu.address.logic.converters;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.converters.exceptions.PersonDecodingException;
import seedu.address.logic.converters.exceptions.PersonEncodingException;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Attendance;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Payment;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

//@@author wm28
/**
 * Converts a person between the CSV format and the Person Class object
 */
public class CsvConverter implements PersonConverter<String> {
    private static final Pattern PERSON_CSV_INPUT_FORMAT = Pattern.compile("[\"|']?(?<name>[^\"',]*)[\"|']?,"
            + "[\"|']?(?<phone>[^\"',]*)[\"|']?,"
            + "[\"|']?(?<email>[^\"',]*)[\"|']?,"
            + "[\"|']?(?<payment>[^\"',]*)[\"|']?,"
            + "[\"|']?(?<attendance>[^\"',]*)[\"|']?,?"
            + "(?<tags>.*)");

    /**
     * Encodes a Person object to a csv-formatted string.
     *
     * @param person to be encoded
     * @return String based on the Person object
     * @throws PersonEncodingException if the person fails to encode
     */
    @Override
    public String encodePerson(Person person) throws PersonEncodingException {
        if (person == null) {
            throw new PersonEncodingException("Person is null");
        }
        StringBuilder result = new StringBuilder();
        result.append(person.getName() + ",");
        result.append(person.getPhone() + ",");
        result.append(person.getEmail() + ",");
        result.append(person.getPayment() + ",");
        result.append(person.getAttendance() + ",");
        result.append(person.getTags().stream()
                .map(tag -> tag.tagName)
                .collect(Collectors.joining(",")));
        return result.toString();
    }

    /**
     * Decodes csv-formatted input into a Person object.
     *
     * @param personInput Csv-formatted person input string
     * @return Person based on the csv-formatted input string of the guest
     * @throws PersonDecodingException if the csv input does not conform to the expected format
     */
    @Override
    public Person decodePerson(String personInput) throws PersonDecodingException {
        Matcher matcher = PERSON_CSV_INPUT_FORMAT.matcher(personInput.trim());
        Person person;
        if (!matcher.matches()) {
            throw new PersonDecodingException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }
        try {
            Name name = ParserUtil.parseName(matcher.group("name"));
            Phone phone = ParserUtil.parsePhone(matcher.group("phone"));
            Email email = ParserUtil.parseEmail(matcher.group("email"));
            Payment payment = ParserUtil.parsePayment(matcher.group("payment"));
            Attendance attendance = ParserUtil.parseAttendance(matcher.group("attendance"));
            Set<Tag> tagList = splitTags(matcher.group("tags"));
            person = new Person(name, phone, email, payment, attendance, tagList);
        } catch (ParseException pe) {
            throw new PersonDecodingException(pe.getMessage(), pe);
        }
        return person;
    }

    /**
     * Splits and parses the tags into a set of Tags
     *
     * @param tags String input tags
     * @return set of parsed Tags
     * @throws ParseException if the csv input does not conform to the expected format
     */
    private Set<Tag> splitTags(String tags) throws ParseException {
        if (tags.trim().isEmpty()) {
            return new HashSet<>();
        }
        return ParserUtil.parseTags(Arrays.asList(tags.split(",")));
    }
}
//@@author
