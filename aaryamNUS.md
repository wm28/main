# aaryamNUS
###### \java\seedu\address\logic\commands\AddTagCommandTest.java
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
###### \java\seedu\address\logic\commands\RemoveTagCommandTest.java
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
###### \java\seedu\address\logic\parser\AddTagCommandParserTest.java
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
###### \java\seedu\address\logic\parser\RemoveTagCommandParserTest.java
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
###### \java\seedu\address\model\AddressBookTest.java
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
###### \java\seedu\address\model\AddressBookTest.java
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
###### \java\seedu\address\model\ModelManagerTest.java
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
###### \java\seedu\address\model\ModelManagerTest.java
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
###### \java\seedu\address\ui\testutil\GuiTestAssert.java
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
