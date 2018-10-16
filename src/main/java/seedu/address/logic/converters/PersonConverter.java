package seedu.address.logic.converters;

import seedu.address.logic.converters.exceptions.PersonDecodingException;
import seedu.address.logic.converters.exceptions.PersonEncodingException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Person;

//@@author wm28
/**
 * Represents a Converter that is able to convert between a {@code Person} and a type {@code T}.
 * Type {@code T} can represent various formats.
 */
public interface PersonConverter <T> {

    /**
     * Parses {@code userInput} into a command and returns it.
     * @throws ParseException if {@code userInput} does not conform the expected format
     */
    T encodePerson(Person person) throws PersonEncodingException;

    /**
     * Parses {@code userInput} into a command and returns it.
     * @throws ParseException if {@code userInput} does not conform the expected format
     */
    Person decodePerson(T personInput) throws PersonDecodingException;
}
//@@author

