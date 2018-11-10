package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.collections.ObservableList;
import seedu.address.model.event.Event;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.tag.Tag;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniquePersonList persons;
    //@@author SandhyaGopakumar
    private final Event eventDetails;
    //@@author
    /*
     * The 'unusual' code block below is an non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniquePersonList();
        eventDetails = new Event();
    }

    public AddressBook() {}

    /**
     * Creates an AddressBook using the Persons in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        this.persons.setPersons(persons);
    }

    //@@author SandhyaGopakumar

    /**
     * Replaces the current details of the event with {@code event}.
     */
    public void setEvent(Event event) {
        this.eventDetails.setEvent(event);
    }
    //@@author

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);
        setPersons(newData.getPersonList());
        setEvent(newData.getEventDetails());
    }

    //@@author SandhyaGopakumar
    //event-level operations
    /** Adds the details given by the user to event. */
    public void addEvent(Event e) {
        eventDetails.addEvent(e);
    }

    /** Deletes the event details stored in the addressbook. */
    public void deleteEvent() {
        eventDetails.deleteEvent();
    }

    /** Returns true if the event details given by the user is being stored in the addressbook. */
    public boolean hasEvent() {
        return eventDetails.isUserInitialised();
    }
    //@@author

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Adds a person to the address book.
     * The person must not already exist in the address book.
     */
    public void addPerson(Person p) {
        persons.add(p);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    public void updatePerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);

        persons.setPerson(target, editedPerson);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removePerson(Person key) {
        persons.remove(key);
    }

    //// util methods

    //@@author aaryamNUS
    /**
     * Removes {@code tag} from {@code person} in this {@code AddressBook}.
     * Note: This code snippet was inspired from the PR "Model: Add deleteTag(Tag)" by @yamgent
     */
    private void removeTagFromPerson(Tag tag, Person person) {
        Set<Tag> newTags = new HashSet<>(person.getTags());

        if (!newTags.remove(tag)) {
            return;
        }

        Person newPerson = new Person (person.getName(), person.getPhone(), person.getEmail(),
                                       person.getPayment(), person.getAttendance(), person.getUid(), newTags);

        updatePerson(person, newPerson);
    }

    /**
     * Removes {@code tag} from all persons in this {@code AddressBook}
     */
    public void removeTag(Tag tag) {
        persons.forEach(person -> removeTagFromPerson(tag, person));
    }

    /**
     * Adds {@code tag} from {@code person} in this {@code AddressBook}.
     * Note: This code snippet was inspired from the PR "Model: Add deleteTag(Tag)" by @yamgent from SE-EDU
     */
    private void addTagFromPerson(Tag tag, Person person) {
        Set<Tag> newTags = new HashSet<>(person.getTags());

        if (!newTags.add(tag)) {
            return;
        }

        Person newPerson =
                new Person (person.getName(), person.getPhone(), person.getEmail(), person.getPayment(),
                            person.getAttendance(), person.getUid(), newTags);

        updatePerson(person, newPerson);
    }

    /**
     * Adds {@code tag} to all persons in this {@code AddressBook}
     */
    public void addTag(Tag tag) {
        persons.forEach(person -> addTagFromPerson(tag, person));
    }
    //@@author aaryamNUS

    @Override
    public String toString() {
        return persons.asUnmodifiableObservableList().size() + " persons";
        // TODO: refine later
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public Event getEventDetails() {
        return eventDetails;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddressBook // instanceof handles nulls
                && persons.equals(((AddressBook) other).persons));
    }

    @Override
    public int hashCode() {
        return persons.hashCode();
    }
}
