package seedu.address.testutil.stubs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import seedu.address.model.person.Person;
import seedu.address.testutil.TypicalPersons;

/**
 * A Model stub that contains TypicalPersons
 */
public class ModelStubContainingTypicalPersons extends ModelStub {
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return FXCollections.observableList(TypicalPersons.getTypicalPersons());
    }
}
