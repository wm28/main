package seedu.address.testutil.stubs;

import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.event.Event;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

//@@author wm28-reused
// Abstracted ModelStub out from AddCommandTest which was from the original AB4
/**
 * A default model stub that have all of the methods failing.
 */
public class ModelStub implements Model {
    @Override
    public void addPerson(Person person) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void addEvent(Event event) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void resetData(ReadOnlyAddressBook newData) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public boolean hasPerson(Person person) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public boolean hasEvent() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void deletePerson(Person target) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void deleteEvent() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void updatePerson(Person target, Person editedPerson) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public boolean hasUid(Person person) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void updateEvent(Event editedEvent) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void deleteTag(Tag tag) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void addTag(Tag tag) {
        throw new AssertionError("This method should not be called");
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public Event getEventDetails() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public boolean canUndoAddressBook() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public boolean canRedoAddressBook() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void undoAddressBook() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void redoAddressBook() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void commitAddressBook() {
        throw new AssertionError("This method should not be called.");
    }
}
//@@author
