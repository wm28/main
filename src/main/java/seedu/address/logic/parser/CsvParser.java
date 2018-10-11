package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Attendance;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Payment;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;




/**
 * Parses a person in CSV format and creates a new Person object
 */
public class CsvParser {
    private static final Pattern GUEST_DATA_FORMAT = Pattern.compile("(?<name>\"[^\"]*\"|[^\",]*),"
            + "(?<phone>\"[^\"]*\"|[^\",]*),"
            + "(?<email>\"[^\"]*\"|[^\",]*),"
            + "(?<payment>\"[^\"]*\"|[^\",]*),"
            + "(?<attendance>\"[^\"]*\"|[^\",]*),"
            + "(?<tags>.*)");
    /**
     * Parses csv-formatted input into a Person object.
     *
     * @param csvInput Csv-formatted guest input string
     * @return Person based on the csv-formatted input string of the guest
     * @throws ParseException if the csv input does not conform to the expected format
     */
    public Person parsePerson(String csvInput) throws ParseException {
        Matcher matcher = GUEST_DATA_FORMAT.matcher(csvInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }
        Name name = ParserUtil.parseName(matcher.group("name"));
        Phone phone = ParserUtil.parsePhone(matcher.group("phone"));
        Email email = ParserUtil.parseEmail(matcher.group("email"));
        //@@author Sarah
        Payment payment = ParserUtil.parsePayment(matcher.group("payment"));
        Attendance attendance = ParserUtil.parseAttendance(matcher.group("attendance"));
        //@@author SE-EDU
        Set<Tag> tagList = splitTags(matcher.group("tags"));

        return new Person(name, phone, email, payment, attendance, tagList);
    }

    private Set<Tag> splitTags(String tags) throws ParseException {
        return ParserUtil.parseTags(Arrays.asList(tags.split(",")));
    }

}
