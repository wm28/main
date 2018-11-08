package seedu.address.testutil.stubs;

import seedu.address.logic.converters.PersonConverter;
import seedu.address.logic.converters.exceptions.PersonDecodingException;
import seedu.address.logic.converters.exceptions.PersonEncodingException;
import seedu.address.logic.converters.fileformats.AdaptedPerson;
import seedu.address.logic.converters.fileformats.SupportedFileFormat;
import seedu.address.model.person.Person;

/**
 * A default PersonConverter stub that have all of the methods failing.
 */
public class PersonConverterStub implements PersonConverter {
    @Override
    public AdaptedPerson encodePerson(Person person) throws PersonEncodingException {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public Person decodePerson(AdaptedPerson person) throws PersonDecodingException {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public SupportedFileFormat getSupportedFileFormat() {
        throw new AssertionError("This method should not be called.");
    }
}
