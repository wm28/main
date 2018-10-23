# aaryamNUS
###### \java\seedu\address\logic\commands\AddTagCommand.java
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
###### \java\seedu\address\logic\commands\RemoveTagCommand.java
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
###### \java\seedu\address\logic\parser\AddTagCommandParser.java
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
###### \java\seedu\address\logic\parser\RemoveTagCommandParser.java
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
###### \java\seedu\address\model\AddressBook.java
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
     * Note: This code snippet was inspired from the PR "Model: Add deleteTag(Tag)" by @yamgent from SE-EDU
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
###### \java\seedu\address\model\AddressBook.java
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
```
###### \java\seedu\address\ui\PersonCard.java
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
###### \java\seedu\address\ui\PersonDisplay.java
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
###### \java\seedu\address\ui\UiPart.java
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
        case "veg":
            return TAG_COLORS[8];
        case "halal":
            return TAG_COLORS[9];
        default:
            return TAG_COLORS[10];
        }
    }
}
```
###### \resources\view\DarkTheme.css
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

#tags .veg {
    -fx-text-fill: white;
    -fx-background-color: #228B22;
}

#tags .halal {
    -fx-text-fill: black;
    -fx-background-color: #ADFF2F;
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
