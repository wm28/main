package seedu.address.logic.parser;

import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

import java.util.Arrays;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

/**
 * Parses a person in CSV format and creates a new Person object
 */
public class CsvParser {
    String csvInput;
    private static final Pattern GUEST_DATA_FORMAT = Pattern.compile("(?<name>\"[^\"]*\"|[^\",]*),"
            + "(?<phone>\"[^\"]*\"|[^\",]*),"
            + "(?<email>\"[^\"]*\"|[^\",]*),"
            + "(?<address>\"[^\"]*\"|[^\",]*),"
            + "(?<tags>.*)");

    public CsvParser(String csvInput) {
        this.csvInput = csvInput;
    }

    public Person parse() throws ParseException {
        Matcher matcher = GUEST_DATA_FORMAT.matcher(csvInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }
        Name name = ParserUtil.parseName(matcher.group("name"));
        Phone phone = ParserUtil.parsePhone(matcher.group("phone"));
        Email email = ParserUtil.parseEmail(matcher.group("email"));
        Address address = ParserUtil.parseAddress(matcher.group("address"));
        Set<Tag> tagList = splitTags(matcher.group("tags"));

        return new Person(name, phone, email, address, tagList);
    }

    private Set<Tag> splitTags(String tags) throws ParseException {
        return ParserUtil.parseTags(Arrays.asList(tags.split(",")));
    }

}
