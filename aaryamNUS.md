# aaryamNUS
###### \java\seedu\address\logic\commands\AddTagCommand.java
``` java
/**
 * Adds a set of tags from all the people in the current GuestBook
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

    private static final String MESSAGE_NO_PERSON_IN_LIST = "No persons in the list!";
    private static Logger logger = Logger.getLogger("execute");
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
###### \java\seedu\address\logic\commands\Email.java
``` java
/**
 * This abstract class is inherited by Mail, EMailAll, and ForceEmail commands,
 * in order to reduce code duplicity.
 */
public abstract class Email extends Command {

    /**
     * Checks whether username, password, email subject and email message are
     * provided by the user. If any of the parameters are either null or an
     * empty string, the respective command exception is throw.
     * @throws CommandException whenever a field in the email of credentials is missing
     */
    public void checkFields(String username, String password,
                            String emailSubject, String emailMessage) throws CommandException {
        if (username == null || username.replaceAll("\\s+", "").equals("")) {
            throw new CommandException(Messages.MESSAGE_USERNAME_NOT_PROVIDED);
        } else if (password == null || password.replaceAll("\\s+", "").equals("")) {
            throw new CommandException(Messages.MESSAGE_PASSWORD_NOT_PROVIDED);
        } else if (emailSubject == null || emailSubject.replaceAll("\\s+", "").equals("")) {
            throw new CommandException(Messages.MESSAGE_EMAIL_SUBJECT_NOT_PROVIDED);
        } else if (emailMessage == null || emailMessage.replaceAll("\\s+", "").equals("")) {
            throw new CommandException(Messages.MESSAGE_EMAIL_MESSAGE_NOT_PROVIDED);
        }
    }

    /**
     * Reads and parses the files Credentials.txt and Message.txt to retrieve
     * username, password, email message and email subject. Error handling is also performed
     * through the try-catch block, which details with FileNotFoundExceptions as well as
     * General Exceptions. Once parsed, the private global variables in the MailCommand username,
     * password, emailSubject, and emailMessage are set with the strings parsed from the .txt files
     */
    public String[] retrieveInformation() throws FileNotFoundException {
        String[] information = new String[4];

        try {
            File credentials = new File("src/main/resources/EmailData/Credentials.txt")
                    .getAbsoluteFile();
            Scanner credentialsScanner = new Scanner(credentials);

            // Retrieve the two strings in Credentials.txt
            String unmodifiedUsername = credentialsScanner.nextLine();
            String unmodifiedPassword = credentialsScanner.nextLine();

            // Parse the strings to retrieve the username and password within quotation marks
            information[0] = unmodifiedUsername.split("\"")[1];
            information[1] = unmodifiedPassword.split("\"")[1];

        } catch (FileNotFoundException fe) {
            throw new FileNotFoundException("Error: The file Credentials.txt was not found!");
        } catch (ArrayIndexOutOfBoundsException ae) {
            throw new ArrayIndexOutOfBoundsException(Messages.MESSAGE_PARSE_ERROR_MESSAGE);
        } catch (NoSuchElementException ne) {
            throw new NoSuchElementException("Error: Please specify your credentials, email message, "
                    + "and email subject in Credentials.txt and Message.txt");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            File message = new File("src/main/resources/EmailData/Message.txt")
                    .getAbsoluteFile();
            Scanner messageScanner = new Scanner(message);

            String unmodifiedSubject = messageScanner.nextLine();
            StringBuilder unmodifiedMessage = new StringBuilder();

            while (messageScanner.hasNextLine()) {
                unmodifiedMessage.append(messageScanner.nextLine());
                unmodifiedMessage.append("\n");
            }

            information[2] = unmodifiedSubject.split("\"")[1];
            information[3] = unmodifiedMessage.toString().split("\"")[1];

        } catch (FileNotFoundException fe) {
            throw new FileNotFoundException("Error: The file Message.txt was not found!");
        } catch (NoSuchElementException ne) {
            throw new NoSuchElementException("Error: Please specify your credentials, email message, "
                    + "and email subject in Credentials.txt and Message.txt");
        } catch (ArrayIndexOutOfBoundsException ae) {
            throw new ArrayIndexOutOfBoundsException(Messages.MESSAGE_PARSE_ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return information;
    }

    /**
     * Creates a connection to the host gmail account via gmail's smtp port
     * @return the properties of the host domain server, in this case gmail
     */
    public Properties createPropertiesConfiguration() {
        // Connects to Gmail using it's smtp port and previous authorization
        return new Properties() {
            {
                put("mail.smtp.auth", "true");
                put("mail.smtp.starttls.enable", "true");
                put("mail.smtp.host", "smtp.gmail.com");
                put("mail.smtp.port", "587");
            }
        };
    }

    /**
     * Creates the message of the email using the emailMessage and emailSubject parameters
     * provided, and sends the email using Transport.send(). Moreover, the 'to' and 'from'
     * fields are provided by the child classes
     */
    public void createAndSendEmail(String username, String emailSubject, String emailMessage,
                              String recipient, Session session) throws CommandException {
        try {
            // Creates a default MimeMessage object
            Message message = new MimeMessage(session);

            // Set the email of the host
            message.setFrom(new InternetAddress(username));

            // Set the email of the guest
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));

            // Set email subject and message
            message.setSubject(emailSubject);
            message.setText(emailMessage);

            Transport.send(message);
        } catch (MessagingException mex) {
            throw new CommandException("Error: could not send email, please ensure you have strong "
                    + "internet connectivity.");
        }
    }

    /**
     * This method checks whether a given email address has the valid format, through the use
     * of a Java Regular expression, which is a special sequence of characters that allows you
     * to match and find other strings or sets of strings
     *
     * A basic outline of the 'expression' string is given below:
     *
     * Subexpression                                Meaning
     *      ^                           Matches the beginning of the line
     *      $                           Matches the end of the line
     *    [...]                         Matches with any character in the brackets
     *     \w                           Matches any word characters
     *    {2,4}                         Matches between 2 and 4 occurrences of preceding expressions
     *
     * @param guestAddress is the address of the guest you wish to send an email to
     * @return a boolean that determines whether the given email address is of the correct format
     * The following regular expression was adapted from zParacha.com,
     * Source: http://zparacha.com/ultimate-java-regular-expression-to-validate-email-address
     */
    public boolean isValidEmail (String guestAddress) {
        String expression = "^[\\w\\-]([\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

        // Create a pattern object using the expression provided
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);

        // Create the corresponding matcher object
        Matcher matcher = pattern.matcher(guestAddress);
        return matcher.matches();
    }
}
```
###### \java\seedu\address\logic\commands\EmailAllCommand.java
``` java
/**
 * Sends an email to the specified person in the guest list.
 */
public class EmailAllCommand extends Email {

    public static final String COMMAND_WORD = "emailAll";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sends an email to all guests in the "
            + "current filtered list\n"
            + "Example: " + COMMAND_WORD;

    private static final String MESSAGE_MAIL_ALL_PERSON_SUCCESS = "Successfully sent an email to %1$d persons, "
            + "could not send an email to %2$d guests will addresses: %3$s!";

    private static Logger logger = Logger.getLogger("execute");
    private static EmailAllCommand emailCommandSimpleton = null;
    private static String username;
    private static String password;

    private EmailAllCommand() {}

    /**
     * Applying the Simpleton design pattern to EmailAllCommand
     */
    public static EmailAllCommand getInstance() {
        if (emailCommandSimpleton == null) {
            emailCommandSimpleton = new EmailAllCommand();
        }
        return emailCommandSimpleton;
    }

    /**
     * Sends an email to all the persons in the current filtered list
     * @param model is instantiated to get the latest filtered person list
     */
    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();
        String emailSubject;
        String emailMessage;
        StringBuilder invalidEmails = new StringBuilder();
        int successfulEmails = 0;
        int failedEmails = 0;

        for (Person personToMail : lastShownList) {
            assert personToMail != null;

            if (!isValidEmail(personToMail.getEmail().toString())) {
                failedEmails++;
                String invalidEmail = " || " + personToMail.getEmail().toString() + " || ";
                invalidEmails.append(invalidEmail);
                continue;
            }

            // Retrieve all email fields and user credentials and validate that they are not null
            try {
                // Array of strings to store all the necessary information
                String[] information;

                // Retrieve the information through a method in the super class Email
                information = retrieveInformation();
                emailSubject = information[2];
                emailMessage = information[3];
                username = information[0];
                password = information[1];

                // Verify the information exists through the method in the super class Email
                checkFields(username, password, emailSubject, emailMessage);
            } catch (FileNotFoundException | NoSuchElementException | ArrayIndexOutOfBoundsException e) {
                failedEmails = lastShownList.size();
                successfulEmails = 0;

                for (Person person : lastShownList) {
                    String personEmail = person.getEmail().toString() + " ";
                    invalidEmails.append(personEmail);
                }

                break;
            }

            // Creates a new session with the user gmail account as the host
            Properties props = createPropertiesConfiguration();

            // Authenticate the user credentials
            EmailPasswordAuthenticator authenticate = new EmailPasswordAuthenticator();

            // Create a new session using the authenticated credentials and the properties of
            // the Gmail host
            Session session = Session.getDefaultInstance(props, authenticate);

            createAndSendEmail(username, emailSubject, emailMessage,
                    personToMail.getEmail().toString(), session);

            successfulEmails++;
        }

        logger.log(Level.INFO, "All emails sent successfully!");
        return new CommandResult(String.format(MESSAGE_MAIL_ALL_PERSON_SUCCESS, successfulEmails,
                failedEmails, invalidEmails));
    }

    @Override
    public Properties createPropertiesConfiguration() {
        return super.createPropertiesConfiguration();
    }

    @Override
    public String[] retrieveInformation() throws FileNotFoundException {
        return super.retrieveInformation();
    }

    @Override
    public void checkFields(String username, String password, String emailSubject,
                            String emailMessage) throws CommandException {
        super.checkFields(username, password, emailSubject, emailMessage);
        logger.log(Level.INFO, "All fields from Credentials.txt and Message.txt"
                + "received successfully");
    }

    /**
     * Authenticates the user account based on the credentials provided
     */
    private static class EmailPasswordAuthenticator extends Authenticator {
        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    }

    @Override
    public void createAndSendEmail(String username, String emailSubject, String emailMessage,
                                   String recipient, Session session) throws CommandException {
        super.createAndSendEmail(username, emailSubject, emailMessage, recipient, session);
    }

    @Override
    public boolean isValidEmail(String guestAddress) {
        return super.isValidEmail(guestAddress);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EmailAllCommand)) {
            return false;
        }

        // state check
        EmailAllCommand e = (EmailAllCommand) other;
        return e.equals(other);
    }
}
```
###### \java\seedu\address\logic\commands\MailCommand.java
``` java
/**
 * Sends an email to the specified person in the guest list.
 */
public class MailCommand extends Email {

    public static final String COMMAND_WORD = "email";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sends an email to the specified person "
            + "provided by INDEX.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "Example: " + COMMAND_WORD + " 1 ";

    private static final String MESSAGE_MAIL_PERSON_SUCCESS = "Successfully sent email!";

    private static Logger logger = Logger.getLogger("execute");
    private static String username;
    private static String password;
    private Index index;

    /**
     * @param index of the person in the filtered person list to edit
     */
    public MailCommand(Index index) {
        requireNonNull(index);

        this.index = index;
    }

    /**
     * Sends an email to the person at the specified INDEX
     * @param model
     * Note: the following code was adapted from the SendEmail.java class code provided by @Rish on stackoverflow
     */
    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();
        String emailSubject;
        String emailMessage;

        assert index != null;
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToMail = lastShownList.get(index.getZeroBased());
        assert personToMail != null;
        if (!isValidEmail(personToMail.getEmail().toString())) {
            throw new CommandException("Error: The email of the recipient is invalid!");
        }

        // Retrieve all email fields and user credentials and validate that they are not null
        try {
            // Array of strings to store all the necessary information
            String[] information;

            // Retrieve the information through a method in the super class Email
            information = retrieveInformation();
            username = information[0];
            password = information[1];
            emailSubject = information[2];
            emailMessage = information[3];

            // Verify the information exists through the method in the super class Email
            checkFields(username, password, emailSubject, emailMessage);
        } catch (FileNotFoundException fe) {
            throw new CommandException("Error: The file Credentials.txt or Message.txt was not found!");
        } catch (NoSuchElementException ne) {
            throw new CommandException("Error: Please specify your credentials, email message, "
                    + "and email subject in Credentials.txt and Message.txt");
        } catch (ArrayIndexOutOfBoundsException ae) {
            throw new CommandException(Messages.MESSAGE_PARSE_ERROR_MESSAGE);
        }

        // Creates a new session with the user gmail account as the host
        Properties props = createPropertiesConfiguration();

        // Authenticate the user credentials
        EmailPasswordAuthenticator authenticate = new EmailPasswordAuthenticator();

        // Create a new session using the authenticated credentials and the properties of
        // the gmail host
        Session session = Session.getDefaultInstance(props, authenticate);

        createAndSendEmail(username, emailSubject, emailMessage,
                personToMail.getEmail().toString(), session);

        logger.log(Level.INFO, "Email sent successfully");
        return new CommandResult(MESSAGE_MAIL_PERSON_SUCCESS);
    }

    @Override
    public Properties createPropertiesConfiguration() {
        return super.createPropertiesConfiguration();
    }

    @Override
    public String[] retrieveInformation() throws FileNotFoundException {
        return super.retrieveInformation();
    }

    @Override
    public void checkFields(String username, String password, String emailSubject,
                            String emailMessage) throws CommandException {
        super.checkFields(username, password, emailSubject, emailMessage);
        logger.log(Level.INFO, "All fields from Credentials.txt and Message.txt"
                + "received successfully");
    }

    /**
     * Authenticates the user account based on the credentials provided
     */
    private static class EmailPasswordAuthenticator extends Authenticator {
        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    }

    @Override
    public void createAndSendEmail(String username, String emailSubject, String emailMessage,
                                   String recipient, Session session) throws CommandException {
        super.createAndSendEmail(username, emailSubject, emailMessage, recipient, session);
    }

    @Override
    public boolean isValidEmail(String guestAddress) {
        return super.isValidEmail(guestAddress);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof MailCommand)) {
            return false;
        }

        // state check
        MailCommand e = (MailCommand) other;
        return index.equals(e.index);
    }
}
```
###### \java\seedu\address\logic\commands\RemoveTagCommand.java
``` java
/**
 * Removes a set of tags from all the people in the current GuestBook
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
###### \java\seedu\address\logic\parser\MailCommandParser.java
``` java
public class MailCommandParser implements Parser<MailCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the MailCommand
     * and returns a MailCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public MailCommand parse(String args) throws ParseException {
        //ensure the arguments are not empty
        assert args != null;

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args);
        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    MailCommand.MESSAGE_USAGE), pe);
        }

        return new MailCommand(index);
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
###### \java\seedu\address\ui\BrowserPanel.java
``` java
    /**
     * Method createTags initialises the tag labels for {@code person}
     * Note: This code was adapted from the example implementation provide by @yamgent from SE-EDU
     */
    private void createTags(seedu.address.model.event.Event event) {
        event.getEventTags().forEach(tag -> {
            Label tagLabel = new Label(tag.tagName);
            tagLabel.getStyleClass().add(getTagColor(tag.tagName));
            tags.getChildren().add(tagLabel);
        });
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
