package seedu.address.logic.commands;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

//@@author kronicler
class MarkCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new MarkCommand(null);
    }

    @Test
    void retrieveIndex_noPersonInGuestList_throwCommandException() {

    }

    @Test
    void retrieveIndex_filteredGuestListWithoutThePhoneNumber_throwCommandException() {
    }

    @Test
    void retrieveIndex_filteredGuestListWithThePhoneNumber_success() {
    }

    @Test
    void retrieveIndex_unfilteredGuestListWithThePhoneNumber_success() {
    }

    @Test
    void execute_phoneNumberExistsInGuestList_success() {
    }

    @Test
    void execute_phoneNumberDoesNotExistInGuestList_throwCommandException() {
    }

}
