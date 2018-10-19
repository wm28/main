# aaryamNUS
###### \main\java\seedu\address\logic\commands\AddTagCommand.java
``` java
/**
 * Adds a set of tags from all the people in the current GuestList
 */
public class AddTagCommand extends Command {
    public static final String COMMAND_WORD = "addTag";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds the specified tags from all "
            + "persons in the list.\n"
            + "Parameters: "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_TAG + "VIP " + PREFIX_TAG + "Paid";

    public static final String MESSAGE_ADDED_TAG_SUCCESS = "Successfully added all tags to %1$d persons";
    private static Logger logger = Logger.getLogger("execute");
    private static final String MESSAGE_NO_PERSON_IN_LIST = "No persons in the list!";
    private final Set<Tag> tagsToAdd;

    /**
     * @param tagsToAdd of the person in the filtered person list to edit
     */
    public AddTagCommand(Set<Tag> tagsToAdd) {
        requireNonNull(tagsToAdd);
        this.tagsToAdd = tagsToAdd;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        ReadOnlyAddressBook currentAddressBookReadOnly = model.getAddressBook();
        // Uses edited AddressBook API to make an editable AddressBook for removeTag() to work
        AddressBook currentAddressBook = new AddressBook(currentAddressBookReadOnly);
        List<Person> currentList = model.getFilteredPersonList();

        if (currentList.isEmpty()) {
            throw new CommandException(MESSAGE_NO_PERSON_IN_LIST);
        } else {
            for (Tag tagToBeAdded: tagsToAdd) {
                currentAddressBook.addTag(tagToBeAdded);
            }
            logger.log(Level.INFO, "All tags added successfully");
            model.resetData(currentAddressBook);
            model.commitAddressBook();

            return new CommandResult(String.format(MESSAGE_ADDED_TAG_SUCCESS, currentList.size()));
        }
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }
        // instanceof handles nulls
        if (!(other instanceof MarkCommand)) {
            return false;
        }
        // state check
        AddTagCommand e = (AddTagCommand) other;
        return tagsToAdd.equals(e.tagsToAdd);
    }
}
```
###### \main\java\seedu\address\logic\commands\RemoveTagCommand.java
``` java
/**
 * Removes a set of tags from all the people in the current GuestList
 */
public class RemoveTagCommand extends Command {
    public static final String COMMAND_WORD = "removeTag";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Removes the specified tag "
            + "from all persons in the list.\n"
            + "Parameters: "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_TAG + "VIP " + PREFIX_TAG + "Paid";

    public static final String MESSAGE_REMOVED_TAG_SUCCESS = "Successfully removed all tags from %1$d persons";
    public static final String MESSAGE_NO_PERSON_WITH_TAG = "No persons in the list have the specified tags";

    private static Logger logger = Logger.getLogger("calculateNumberOfPeopleToChange");
    private int numberOfPeopleToChange = 0;
    private final Set<Tag> tagsToRemove;

    /**
     * @param tagsToRemove of the person in the filtered person list to edit
     */
    public RemoveTagCommand(Set<Tag> tagsToRemove) {
        requireNonNull(tagsToRemove);
        this.tagsToRemove = tagsToRemove;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        List<Person> currentList = model.getFilteredPersonList();
        ReadOnlyAddressBook currentAddressBookReadOnly = model.getAddressBook();
        // Uses edited AddressBook API to make an editable AddressBook for removeTag() to work
        AddressBook currentAddressBook = new AddressBook(currentAddressBookReadOnly);

        calculateNumberOfPeopleToChange(currentList);

        if (numberOfPeopleToChange == 0) {
            throw new CommandException(MESSAGE_NO_PERSON_WITH_TAG);
        } else {
            for (Tag tagToBeRemoved: tagsToRemove) {
                currentAddressBook.removeTag(tagToBeRemoved);
            }
            logger.log(Level.INFO, "All tags removed successfully");

            model.resetData(currentAddressBook);
            model.commitAddressBook();

            return new CommandResult(String.format(MESSAGE_REMOVED_TAG_SUCCESS, numberOfPeopleToChange));
        }
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }
        // instanceof handles nulls
        if (!(other instanceof MarkCommand)) {
            return false;
        }
        // state check
        RemoveTagCommand e = (RemoveTagCommand) other;
        return tagsToRemove.equals(e.tagsToRemove);
    }

    /**
     * Calculates how many people in the list have at least one tag matching with the set of
     * tags to be removed.
     * @param currentList the current list of guests
     */
    private void calculateNumberOfPeopleToChange(List<Person> currentList) {
        assert numberOfPeopleToChange == 0 : "numberOfPeopleToChange should start at 0";

        Set<Tag> currentTags;

        for (Person personToBeEdited : currentList) {
            currentTags = personToBeEdited.getTags();

            for (Tag tagToBeRemoved: tagsToRemove) {
                try {
                    if (currentTags.contains(tagToBeRemoved)) {
                        numberOfPeopleToChange++;
                        break;
                    }
                } catch (IllegalArgumentException ex) {
                    logger.log(Level.WARNING, "Incorrect format for tags", ex);
                }
            }
        }
    }
}
```
###### \main\java\seedu\address\logic\parser\AddTagCommandParser.java
``` java
/**
 * Parses input arguments and creates a new AddTagCommand object
 */
public class AddTagCommandParser implements Parser<AddTagCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddTagCommand
     * and returns an AddTagCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddTagCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TAG);

        if (!AddTagCommandParser.arePrefixesPresent(argMultimap, PREFIX_TAG)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddTagCommand.MESSAGE_USAGE));
        }

        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
        return new AddTagCommand(tagList);
    }

    /**
     * Returns true if the tag prefix does not return empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
```
###### \main\java\seedu\address\logic\parser\RemoveTagCommandParser.java
``` java
/**
 * Parses input arguments and creates a new RemoveTagCommand object
 */
public class RemoveTagCommandParser implements Parser<RemoveTagCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the RemoveTagCommand
     * and returns an RemoveTagCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public RemoveTagCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TAG);

        if (!arePrefixesPresent(argMultimap, PREFIX_TAG)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemoveTagCommand.MESSAGE_USAGE));
        }

        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
        return new RemoveTagCommand(tagList);
    }

    /**
     * Returns true if the tag prefix does not return empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
```
###### \main\java\seedu\address\model\AddressBook.java
``` java
    /**
     * Removes {@code tag} from {@code person} in this {@code AddressBook}.
     * Note: This code snippet was inspired from the PR "Model: Add deleteTag(Tag)" by @yamgent
     */
    private void removeTagFromPerson(Tag tag, Person person) {
        Set<Tag> newTags = new HashSet<>(person.getTags());

        if (!newTags.remove(tag)) {
            return;
        }

        Person newPerson =
                new Person (person.getName(), person.getPhone(), person.getEmail(), person.getPayment(),
                            person.getAttendance(), newTags);

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
     * Note: This code snippet was inspired from the PR "Model: Add deleteTag(Tag)" by @yamgent
     */
    private void addTagFromPerson(Tag tag, Person person) {
        Set<Tag> newTags = new HashSet<>(person.getTags());

        if (!newTags.add(tag)) {
            return;
        }

        Person newPerson =
                new Person (person.getName(), person.getPhone(), person.getEmail(), person.getPayment(),
                            person.getAttendance(), newTags);

        updatePerson(person, newPerson);
    }

    /**
     * Adds {@code tag} to all persons in this {@code AddressBook}
     */
    public void addTag(Tag tag) {
        persons.forEach(person -> addTagFromPerson(tag, person));
    }
```
###### \main\java\seedu\address\model\AddressBook.java
``` java

    @Override
    public String toString() {
        return persons.asUnmodifiableObservableList().size() + " persons";
        // TODO: refine later
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

```
###### \main\java\seedu\address\ui\PersonCard.java
``` java
    /**
     * Method createTags initialises the tag labels for {@code person}
     * Note: This code was adapted from the example implementation provide by @yamgent from SE-EDU
     */
    private void createTags(Person person) {
        person.getTags().forEach(tag -> {
            Label tagLabel = new Label(tag.tagName);
            tagLabel.getStyleClass().add(getTagColor(tag.tagName));
            tags.getChildren().add(tagLabel);
        });
    }
```
###### \main\java\seedu\address\ui\PersonDisplay.java
``` java
    /**
     * Method createTags initialises the tag labels for {@code person}
     * Note: This code was adapted from the example implementation provide by @yamgent from SE-EDU
     */
    private void createTags(Person person) {
        person.getTags().forEach(tag -> {
            Label tagLabel = new Label(tag.tagName);
            tagLabel.getStyleClass().add(getTagColor(tag.tagName));
            tags.getChildren().add(tagLabel);
        });
    }

```
###### \main\java\seedu\address\ui\UiPart.java
``` java
    /**
     Method getTagColor returns the specific color style for {@code tagName}'s label.
     Note: This code was adapted from the example implementation provide by @yamgent from SE-EDU
     */
    public String getTagColor(String tagName) {
        /**
         * Using the hashcode of the tag name ensures the color of the tag remains consistent
         * during different iterations of the code by generating a random color
         */
        switch (tagName.replaceAll("\\s+", "").toLowerCase()) {
        case "absent":
            return TAG_COLORS[0];
        case "present":
            return TAG_COLORS[1];
        case "vip":
            return TAG_COLORS[2];
        case "guestspeaker":
            return TAG_COLORS[3];
        case "bronze":
            return TAG_COLORS[4];
        case "silver":
            return TAG_COLORS[5];
        case "gold":
            return TAG_COLORS[6];
        case "platinum":
            return TAG_COLORS[7];
        default:
            return TAG_COLORS[8];
        }
    }
}
```
###### \main\resources\view\DarkTheme.css
``` css
* Adding the definitions for the tag labels
*/
#tags .label {
    -fx-padding: 1 3 1 3;
    -fx-border-radius: 2;
    -fx-background-radius: 2;
    -fx-font-size: 11;
}

#tags .orange {
    -fx-text-fill: black;
    -fx-background-color: #ff9900;
}

#tags .yellow {
    -fx-text-fill: white;
    -fx-background-color: #cb9d1d;
}

#tags .lightblue {
    -fx-text-fill: black;
    -fx-background-color: #96e1f0;
}

#tags .white {
    -fx-text-fill: black;
    -fx-background-color: #ffffff;
}

#tags .bronze {
    -fx-text-fill: black;
    -fx-background-color: #cd7f32;
}

#tags .silver {
    -fx-text-fill: black;
    -fx-background-color: #C0C0C0;
}

#tags .gold {
    -fx-text-fill: black;
    -fx-background-color: #DAA520;
}

#tags .platinum {
    -fx-text-fill: black;
    -fx-background-color: #E5E4E2;
}

#tags .default {
    -fx-text-fill: red;
    -fx-background-color: #ffffff;
}

#tags .transparent {
    -fx-text-fill: red;
    -fx-background-color: null !important;
}
```
###### \test\java\seedu\address\logic\commands\AddTagCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * {@code RemoveTagCommand}.
 */
public class AddTagCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();
    private Set<Tag> tagsToAdd = new HashSet<>(Arrays.asList(new Tag("NORMAL"), new Tag("VIP")));
    private Set<Tag> allNewTags = new HashSet<>(Arrays.asList(new Tag("HUSBAND"), new Tag("TEST")));

    @Test
    public void execute_validSetOfTags_success() {
        AddTagCommand addTagCommand = new AddTagCommand(tagsToAdd);
        String expectedMessage = String.format(AddTagCommand.MESSAGE_ADDED_TAG_SUCCESS, 7);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        for (Tag eachTagsToAdd: tagsToAdd) {
            expectedModel.addTag(eachTagsToAdd);
        }
        expectedModel.commitAddressBook();

        assertCommandSuccess(addTagCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    /**
     * 1. Adds a set of tags to all persons in the filtered list.
     * 2. Undo the addition.
     * 3. The unfiltered list should be shown now. Verify that the set of tags of the all previous persons in the
     * unfiltered list is different from the set of tags at the filtered list.
     * 4. Redo the addition. This ensures {@code RedoCommand} adds the set of tags object.
     */
    @Test
    public void executeUndoRedo_validSetOfTags_success() throws Exception {
        AddTagCommand addTagCommand = new AddTagCommand(allNewTags);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        for (Tag eachTagsToAdd: allNewTags) {
            expectedModel.addTag(eachTagsToAdd);
        }
        expectedModel.commitAddressBook();

        // removeTag -> set of tags deleted
        addTagCommand.execute(model, commandHistory);

        // undo -> reverts addressbook back to previous state and filtered person list to show all persons
        expectedModel.undoAddressBook();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same tags added again
        expectedModel.redoAddressBook();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() {
        AddTagCommand addTagFirstCommand = new AddTagCommand(tagsToAdd);
        AddTagCommand addTagSecondCommand = new AddTagCommand(allNewTags);

        // same object -> returns true
        assertTrue(addTagFirstCommand.equals(addTagFirstCommand));

        // not same values -> returns false
        AddTagCommand addTagFirstCommandCopy = new AddTagCommand(tagsToAdd);
        assertFalse(addTagFirstCommand.equals(addTagFirstCommandCopy));

        // different types -> returns false
        assertFalse(addTagFirstCommand.equals(new Tag("Veg")));

        // null -> returns false
        assertFalse(addTagFirstCommand.equals(new Tag("Test")));

        // different objects -> returns false
        assertFalse(addTagFirstCommand.equals(addTagSecondCommand));
    }
}
```
###### \test\java\seedu\address\logic\commands\RemoveTagCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * {@code RemoveTagCommand}.
 */
public class RemoveTagCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();
    private Set<Tag> tagsToRemove = new HashSet<>(Arrays.asList(new Tag("NORMAL"), new Tag("VIP")));
    private Set<Tag> noCommonTags = new HashSet<>(Arrays.asList(new Tag("Unused"), new Tag("Invalid")));

    @Test
    public void execute_validSetOfTags_success() {
        RemoveTagCommand removeTagCommand = new RemoveTagCommand(tagsToRemove);
        String expectedMessage = String.format(RemoveTagCommand.MESSAGE_REMOVED_TAG_SUCCESS, 2);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        for (Tag eachTagsToDelete: tagsToRemove) {
            expectedModel.deleteTag(eachTagsToDelete);
        }
        expectedModel.commitAddressBook();

        assertCommandSuccess(removeTagCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noPersonWithTags_throwsCommandException() {
        RemoveTagCommand removeTagCommand = new RemoveTagCommand(noCommonTags);
        String expectedMessage = RemoveTagCommand.MESSAGE_NO_PERSON_WITH_TAG;

        assertCommandFailure(removeTagCommand, model, commandHistory, expectedMessage);
    }

    /**
     * 1. Deletes a set of tags from all persons in the filtered list.
     * 2. Undo the deletion.
     * 3. The unfiltered list should be shown now. Verify that the set of tags of the all previous peerons in the
     * unfiltered list is different from the set of tags at the filtered list.
     * 4. Redo the deletion. This ensures {@code RedoCommand} deletes the set of tags object.
     */
    @Test
    public void executeUndoRedo_validSetOfTags_success() throws Exception {
        RemoveTagCommand removeTagCommand = new RemoveTagCommand(tagsToRemove);
        String expectedMessage = String.format(RemoveTagCommand.MESSAGE_REMOVED_TAG_SUCCESS, 2);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        for (Tag eachTagsToDelete: tagsToRemove) {
            expectedModel.deleteTag(eachTagsToDelete);
        }
        expectedModel.commitAddressBook();

        // removeTag -> set of tags deleted
        removeTagCommand.execute(model, commandHistory);

        // undo -> reverts addressbook back to previous state and filtered person list to show all persons
        expectedModel.undoAddressBook();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same tags removed again
        expectedModel.redoAddressBook();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_noPersonsWithTags_failure() {
        RemoveTagCommand removeTagCommand = new RemoveTagCommand(noCommonTags);
        String expectedMessage = RemoveTagCommand.MESSAGE_NO_PERSON_WITH_TAG;

        // execution failed -> address book state not added into model
        assertCommandFailure(removeTagCommand, model, commandHistory, expectedMessage);

        // single address book state in model -> undoCommand and redoCommand fail
        assertCommandFailure(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_FAILURE);
    }

    @Test
    public void equals() {
        RemoveTagCommand removeTagFirstCommand = new RemoveTagCommand(noCommonTags);
        RemoveTagCommand removeTagSecondCommand = new RemoveTagCommand(tagsToRemove);

        // same object -> returns true
        assertTrue(removeTagFirstCommand.equals(removeTagFirstCommand));

        // not same values -> returns false
        RemoveTagCommand removeTagFirstCommandCopy = new RemoveTagCommand(noCommonTags);
        assertFalse(removeTagFirstCommand.equals(removeTagFirstCommandCopy));

        // different types -> returns false
        assertFalse(removeTagFirstCommand.equals(new Tag("Veg")));

        // null -> returns false
        assertFalse(removeTagFirstCommand.equals(new Tag("Test")));

        // different objects -> returns false
        assertFalse(removeTagFirstCommand.equals(removeTagSecondCommand));
    }
}
```
###### \test\java\seedu\address\logic\parser\AddTagCommandParserTest.java
``` java
/**
 * AddTagCommandParserTest checks for various user input situations, such as tags without
 * any prefix, tags with only the prefix, and if no input is presented
 */
public class AddTagCommandParserTest {

    private AddTagCommandParser parser = new AddTagCommandParser();

    @Test
    public void parse_noPrefixSpecifiedForAllTags_throwsParseException() {
        assertParseFailure(parser, "Veg Gold VIP", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AddTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_onlyPrefixSpecified_throwsParseException() {
        assertParseFailure(parser, "t", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AddTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "    ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AddTagCommand.MESSAGE_USAGE));
    }
}
```
###### \test\java\seedu\address\logic\parser\RemoveTagCommandParserTest.java
``` java
/**
 * RemoveTagCommandParserTest checks for various user input situations, such as tags without
 * any prefix, tags with only the prefix, and if no input is presented
 */
public class RemoveTagCommandParserTest {

    private RemoveTagCommandParser parser = new RemoveTagCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "    ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                RemoveTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_noPrefixSpecifiedForAllTags_throwsParseException() {
        assertParseFailure(parser, "Silver GuestSpeaker", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                RemoveTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_onlyPrefixSpecified_throwsParseException() {
        assertParseFailure(parser, "t", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                RemoveTagCommand.MESSAGE_USAGE));
    }
}
```
###### \test\java\seedu\address\model\AddressBookTest.java
``` java
    /**
     * Note: This code snippet was inspired from the PR "Model: Add deleteTag(Tag)" by @yamgent
     */
    @Test
    public void removeTag_nonExistentTag_addressBookUnchanged() throws Exception {
        addressBookWithBobAndAmy.removeTag(new Tag(VALID_TAG_UNUSED));

        AddressBook expectedAddressBook = new AddressBookBuilder().withPerson(BOB).withPerson(AMY).build();

        assertEquals(expectedAddressBook, addressBookWithBobAndAmy);
    }

    @Test
    public void removeTag_sharedTagOfDifferentPersons_tagRemoved() {
        addressBookWithBobAndAmy.removeTag(new Tag(VALID_TAG_FRIEND));
        addressBookWithBobAndAmy.removeTag(new Tag(VALID_TAG_FRIEND));

        Person amyWithoutFriendTag = new PersonBuilder(AMY).withTags(VALID_TAG_DIET_AMY).build();
        Person bobWithoutFriendTag = new PersonBuilder(BOB).withTags(VALID_TAG_DIET_BOB).build();
        AddressBook expectedAddressBook = new AddressBookBuilder().withPerson(bobWithoutFriendTag)
                .withPerson(amyWithoutFriendTag).build();

        assertEquals(expectedAddressBook, addressBookWithBobAndAmy);
    }

    @Test
    public void addTag_noNewTagsToAdd_addressBookUnchanged() {
        addressBookWithDanny.addTag(new Tag(VALID_TAG_FRIEND));

        AddressBook expectedAddressBook = new AddressBookBuilder().withPerson(DANNY).build();

        assertEquals(expectedAddressBook, addressBookWithDanny);
    }

    @Test
    public void addTag_tagAddedToMultiplePersons_tagAdded() {
        addressBookWithBobAndAmy.addTag(new Tag(VALID_TAG_HUSBAND));

        Person amyWithHusbandTag = new PersonBuilder(AMY).withTags(VALID_TAG_DIET_AMY, VALID_TAG_HUSBAND).build();
        Person bobWithHusbandTag = new PersonBuilder(BOB).withTags(VALID_TAG_DIET_BOB, VALID_TAG_HUSBAND).build();
        AddressBook expectedAddressBook = new AddressBookBuilder().withPerson(bobWithHusbandTag)
                .withPerson(amyWithHusbandTag).build();

        assertEquals(expectedAddressBook, addressBookWithBobAndAmy);
    }
```
###### \test\java\seedu\address\model\AddressBookTest.java
``` java

    /**
     * A stub ReadOnlyAddressBook whose persons list can violate interface constraints.
     */
    private static class AddressBookStub implements ReadOnlyAddressBook {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();
        private final Event eventDetails = new Event();
        AddressBookStub(Collection<Person> persons, Event event) {
            this.persons.setAll(persons);
            this.eventDetails.setEvent(event);
        }

        AddressBookStub(Collection<Person> persons) {
            this.persons.setAll(persons);
        }

        @Override
        public ObservableList<Person> getPersonList() {
            return persons;
        }

        @Override
        public Event getEventDetails() {
            return eventDetails;
        }
    }

}
```
###### \test\java\seedu\address\model\ModelManagerTest.java
``` java
    @Test
    public void deleteTag_nonExistentTag_modelUnchanged() throws Exception {
        AddressBook addressBook = new AddressBookBuilder().withPerson(AMY).withPerson(BOB).build();
        UserPrefs userPrefs = new UserPrefs();
        ModelManager modelManager = new ModelManager(addressBook, userPrefs);

        modelManager.deleteTag(new Tag(VALID_TAG_UNUSED));
        assertEquals(new ModelManager(addressBook, userPrefs), modelManager);
    }

    @Test
    public void deleteTag_tagUsedByMultiplePersons_tagRemovedFromAll() {
        AddressBook addressBook = new AddressBookBuilder().withPerson(AMY).withPerson(BOB).build();
        UserPrefs userPrefs = new UserPrefs();
        ModelManager modelManager = new ModelManager(addressBook, userPrefs);

        modelManager.deleteTag(new Tag(VALID_TAG_FRIEND));
        ModelManager expectedModelManager = new ModelManager(addressBook, userPrefs);

        Person amyWithoutFriendTag = new PersonBuilder(AMY).withTags(VALID_TAG_DIET_AMY).build();
        Person bobWithoutFriendTag = new PersonBuilder(BOB).withTags(VALID_TAG_DIET_BOB).build();
        expectedModelManager.updatePerson(AMY, amyWithoutFriendTag);
        expectedModelManager.updatePerson(BOB, bobWithoutFriendTag);

        assertEquals(expectedModelManager, modelManager);
    }

    @Test
    public void addTag_addingExistentTag_modelUnchanged() {
        AddressBook addressBook = new AddressBookBuilder().withPerson(AMY).build();
        UserPrefs userPrefs = new UserPrefs();

        ModelManager modelManager = new ModelManager(addressBook, userPrefs);
        modelManager.addTag(new Tag(VALID_TAG_DIET_AMY));

        assertEquals(new ModelManager(addressBook, userPrefs), modelManager);
    }

    @Test
    public void addTag_tagAddedToMultiplePersons_tagAddedToAll() {
        AddressBook addressBook = new AddressBookBuilder().withPerson(AMY).withPerson(BOB).build();
        UserPrefs userPrefs = new UserPrefs();
        ModelManager modelManager = new ModelManager(addressBook, userPrefs);

        modelManager.addTag(new Tag(VALID_TAG_HUSBAND));
        ModelManager expectedModelManager = new ModelManager(addressBook, userPrefs);
        Person amyWithHusbandTag = new PersonBuilder(AMY).withTags(VALID_TAG_DIET_AMY, VALID_TAG_HUSBAND).build();
        Person bobWithHusbandTag = new PersonBuilder(BOB).withTags(VALID_TAG_DIET_BOB, VALID_TAG_HUSBAND).build();

        expectedModelManager.updatePerson(AMY, amyWithHusbandTag);
        expectedModelManager.updatePerson(BOB, bobWithHusbandTag);
        assertEquals(expectedModelManager, modelManager);
    }
```
###### \test\java\seedu\address\model\ModelManagerTest.java
``` java

    @Test
    public void equals() {
        AddressBook addressBook = new AddressBookBuilder().withPerson(ALICE).withPerson(BENSON).build();
        AddressBook differentAddressBook = new AddressBook();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(addressBook, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(addressBook, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different addressBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentAddressBook, userPrefs)));

        // different filteredList -> returns false
        String[] keywords = ALICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(addressBook, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        // different userPrefs -> returns true
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setAddressBookFilePath(Paths.get("differentFilePath"));
        assertTrue(modelManager.equals(new ModelManager(addressBook, differentUserPrefs)));
    }
}
```
###### \test\java\seedu\address\ui\testutil\GuiTestAssert.java
``` java
    /**
     * Returns the color style for {@code tagName}'s label. The tag's color is determined by looking up the color
     * in {@code PersonCard#TAG_COLORS}, using an index generated by the hash code of the tag's content.
     *
     * @see PersonCard#getTagColor(String)
     *
     * Note: This code was adapted from the example implementation provide by @yamgent from SE-EDU
     */

    private static String getTagColor(String tagName) {
        switch (tagName) {
        case "vegetarian":
            return "green";
        case "halal":
            return "orange";
        case "vegan":
            return "yellow";
        case "vip":
            return "lightblue";
        case "bride":
        case "groom":
            return "red";
        case "Guest Speaker":
        case "guest":
            return "white";
        case "NoNuts":
        case "NoBeef":
        case "NoSeafood":
        case "NoGluten":
        case "NoShrimp":
            return "purple";
        default:
            return "grey";
            //throw new AssertionError(tagName + "does not have a color assigned.");
        }
    }

    /**
     * Asserts that the tags in {@code actualCard} matches all the tags in {@code expectedPerson} with the
     * correct color.
     *
     * Note: This code was adapted from the example implementation provide by @yamgent from SE-EDU
     */

    private static void assertTagsEqual(Person expectedPerson, PersonCardHandle actualCard) {
        List<String> expectedTags = expectedPerson.getTags().stream().map(tag ->
                tag.tagName).collect(Collectors.toList());
        assertEquals(expectedTags, actualCard.getTags());
        expectedTags.forEach(tag -> assertEquals(Arrays.asList(LABEL_DEFAULT_STYLE,
                getTagColor(tag)), actualCard.getTagStyleClasses(tag)));
    }
```
