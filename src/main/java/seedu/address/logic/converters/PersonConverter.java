package seedu.address.logic.converters;

import seedu.address.logic.converters.exceptions.PersonDecodingException;
import seedu.address.logic.converters.exceptions.PersonEncodingException;
import seedu.address.logic.converters.fileformats.AdaptedPerson;
import seedu.address.logic.converters.fileformats.SupportedFileFormat;
import seedu.address.model.person.Person;

//@@author wm28
/**
 * Represents a Converter that is able to convert between a {@code Person} and an {@code AdaptedPerson}.
 */
public interface PersonConverter {

    /**
     * Encodes {@code Person} into an {@code AdaptedPerson}.
     * @throws PersonEncodingException if {@code person} does not conform the expected format
     */
    AdaptedPerson encodePerson(Person person) throws PersonEncodingException;

    /**
     * Decodes {@code AdaptedPerson} into a {@code Person}.
     * @throws PersonDecodingException if {@code person} does not conform the expected format
     */
    Person decodePerson(AdaptedPerson person) throws PersonDecodingException;

    /**
     * Returns the file format the particular PersonConverter supports
     */
    SupportedFileFormat getSupportedFileFormat();
}
//@@author

