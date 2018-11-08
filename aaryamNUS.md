# aaryamNUS
###### \java\seedu\address\commons\core\Messages.java
``` java
    public static final String MESSAGE_USERNAME_NOT_PROVIDED = "Error: you have not provided your username "
                                                                + "for authentication!";
    public static final String MESSAGE_PASSWORD_NOT_PROVIDED = "Error: you have not provided your password "
                                                                + "for authentication!";
    public static final String MESSAGE_EMAIL_SUBJECT_NOT_PROVIDED = "Error: you have not included the subject "
                                                                    + "of your email!";
    public static final String MESSAGE_EMAIL_MESSAGE_NOT_PROVIDED = "Error: you have not included the message "
                                                                    + "of your email!";
    public static final String MESSAGE_NO_EMAIL_SENT_MESSAGE = "No emails sent to any guests!";
    public static final String MESSAGE_INVALID_EMAIL = "Error: The email address you have provided is invalid!";
    public static final String MESSAGE_NO_INTERNET_CONNECTION_OR_INVALID_CREDENTIALS = "Error: could not send "
                                                             + "email, either your "
                                                             + "internet connection is not strong or the "
                                                             + "credentials provided are invalid!";
}
```
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
    static final String MESSAGE_ADDED_TAG_SUCCESS = "Successfully added all tags to %1$d persons";

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
        // Uses edited AddressBook API to make an editable AddressBook for addTag() to work
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
        if (!(other instanceof AddTagCommand)) {
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
 * This abstract class is inherited by Mail, EmailAll, and ForceEmail commands,
 * in order to reduce code duplicity.
 */
public abstract class Email extends Command {

    /**
     * Creates a new EmailWindow controller which subsequently launches a GUI Window to retrieve
     * username, password, email message and email subject. Error handling is also performed
     * through the try-catch block, which details with CommandException as well as
     * General Exceptions. Once parsed, the private global variables in the MailCommand username,
     * password, emailSubject, and emailMessage are set with the strings received from the EmailWindow
     */
    public String[] retrieveInformation() throws CommandException {
        String[] information;
        EmailWindow newEmailWindow = new EmailWindow();

        newEmailWindow.showAndWait();

        if (newEmailWindow.isSendButton()) {
            information = newEmailWindow.getInformation();
            if (!isValidEmail(information[0])) {
                throw new CommandException(Messages.MESSAGE_INVALID_EMAIL);
            }
        } else if (newEmailWindow.isQuitButton()) {
            throw new CommandException(Messages.MESSAGE_NO_EMAIL_SENT_MESSAGE);
        } else {
            throw new CommandException(Messages.MESSAGE_NO_EMAIL_SENT_MESSAGE);
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
        } catch (MessagingException e) {
            throw new CommandException(Messages.MESSAGE_NO_INTERNET_CONNECTION_OR_INVALID_CREDENTIALS);
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
 * Sends an email to all of the guests in the specified list.
 */
public class EmailAllCommand extends Email {

    public static final String COMMAND_WORD = "emailAll";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sends an email to all guests in the "
            + "current filtered list\n"
            + "Parameters: none\n"
            + "Please ensure you don't enter any characters after the command word!\n"
            + "Example: " + COMMAND_WORD;

    private static final String MESSAGE_MAIL_ALL_PERSON_SUCCESS = "Successfully sent an email to %1$d persons, "
            + "could not send an email to %2$d guests will addresses: %3$s!";

    private static Logger logger = Logger.getLogger("execute");
    private static String username;
    private static String password;

    public EmailAllCommand() {}

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

        // Array of strings to store all the necessary information
        String[] information;
        // Retrieve the information through a method in the super class Email
        information = retrieveInformation();

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
                emailSubject = information[2];
                emailMessage = information[3];
                username = information[0];
                password = information[1];

            } catch (NoSuchElementException | ArrayIndexOutOfBoundsException e) {
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
    public String[] retrieveInformation() throws CommandException {
        return super.retrieveInformation();
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

        // Array of strings to store all the necessary information
        String[] information;
        // Retrieve the information through a method in the super class Email
        information = retrieveInformation();

        try {
            username = information[0];
            password = information[1];
            emailSubject = information[2];
            emailMessage = information[3];

            // Creates a new session with the user gmail account as the host
            Properties props = createPropertiesConfiguration();

            // Authenticate the user credentials
            EmailPasswordAuthenticator authenticate = new EmailPasswordAuthenticator();

            // Create a new session using the authenticated credentials and the properties of
            // the gmail host
            Session session = Session.getDefaultInstance(props, authenticate);

            createAndSendEmail(username, emailSubject, emailMessage,
                    personToMail.getEmail().toString(), session);
        } catch (NullPointerException ne) {
            logger.log(Level.SEVERE, "Error: retrieving information was unsuccessful!");
        }

        logger.log(Level.INFO, "Email sent successfully");
        return new CommandResult(MESSAGE_MAIL_PERSON_SUCCESS);
    }

    @Override
    public Properties createPropertiesConfiguration() {
        return super.createPropertiesConfiguration();
    }

    @Override
    public String[] retrieveInformation() throws CommandException {
        return super.retrieveInformation();
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
###### \java\seedu\address\logic\parser\AddressBookParser.java
``` java
        case MailCommand.COMMAND_WORD:
            return new MailCommandParser().parse(arguments);

        case EmailAllCommand.COMMAND_WORD:
            return new EmailAllCommandParser().parse(arguments);

        case UndoCommand.COMMAND_WORD:
            return new UndoCommandParser().parse(arguments);

        case RedoCommand.COMMAND_WORD:
            return new RedoCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommandParser().parse(arguments);

        case HistoryCommand.COMMAND_WORD:
            return new HistoryCommandParser().parse(arguments);

        case ExitCommand.COMMAND_WORD:
            return new ExitCommandParser().parse(arguments);

        case HelpCommand.COMMAND_WORD:
            return new HelpCommandParser().parse(arguments);

```
###### \java\seedu\address\logic\parser\AddressBookParser.java
``` java
        case ClearCommand.COMMAND_WORD:
            return new ClearCommandParser().parse(arguments);
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
###### \java\seedu\address\logic\parser\ClearCommandParser.java
``` java
/**
 * Parses input arguments and creates a new ClearCommand object
 */
public class ClearCommandParser implements Parser<ClearCommand> {
    private static Logger logger = Logger.getLogger("parse");

    /**
     * Parses the given {@code String} of arguments in the context of the ClearCommand
     * and returns a ClearCommand object for execution. For this command, parser needs to
     * ensure that the arguments are null, i.e. no extra characters are inputted after the
     * command word clear
     * @throws ParseException if the user input does not conform the expected format
     */
    public ClearCommand parse(String args) throws ParseException {
        //need to ensure that the arguments are indeed null

        if (args == null || args.replaceAll("\\s+", "").equals("")) {
            logger.log(Level.INFO, "ClearCommand arguments are null");
        } else {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    ClearCommand.MESSAGE_USAGE));
        }

        return new ClearCommand();
    }
}
```
###### \java\seedu\address\logic\parser\DeleteEventCommandParser.java
``` java
/**
 * Parses input arguments and creates a new DeleteEventCommand object
 */
public class DeleteEventCommandParser implements Parser<DeleteEventCommand> {
    private static Logger logger = Logger.getLogger("parse");

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteEventCommand
     * and returns a DeleteEventCommand object for execution. For this command, parser needs to
     * ensure that the arguments are null, i.e. no extra characters are inputted after the
     * command word delete_event
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteEventCommand parse(String args) throws ParseException {
        //need to ensure that the arguments are indeed null

        if (args == null || args.replaceAll("\\s+", "").equals("")) {
            logger.log(Level.INFO, "DeleteEventCommand arguments are null");
        } else {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    DeleteEventCommand.MESSAGE_USAGE));
        }

        return new DeleteEventCommand();
    }
}
```
###### \java\seedu\address\logic\parser\EmailAllCommandParser.java
``` java
/**
 * Parses input arguments and creates a new EmailAllCommand object
 */
public class EmailAllCommandParser implements Parser<EmailAllCommand> {
    private static Logger logger = Logger.getLogger("parse");

    /**
     * Parses the given {@code String} of arguments in the context of the EmailAllCommand
     * and returns a EmailAllCommand object for execution. For this command, parser needs to
     * ensure that the arguments are null, i.e. no extra characters are inputted after the
     * command word EmailAll
     * @throws ParseException if the user input does not conform the expected format
     */
    public EmailAllCommand parse(String args) throws ParseException {
        //need to ensure that the arguments are indeed null

        if (args == null || args.replaceAll("\\s+", "").equals("")) {
            logger.log(Level.INFO, "EmailAllCommand arguments are null");
        } else {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    EmailAllCommand.MESSAGE_USAGE));
        }

        return new EmailAllCommand();
    }
}
```
###### \java\seedu\address\logic\parser\ExitCommandParser.java
``` java
/**
 * Parses input arguments and creates a new ExitCommand object
 */
public class ExitCommandParser implements Parser<ExitCommand> {
    private static Logger logger = Logger.getLogger("parse");

    /**
     * Parses the given {@code String} of arguments in the context of the ExitCommand
     * and returns a ExitCommand object for execution. For this command, parser needs to
     * ensure that the arguments are null, i.e. no extra characters are inputted after the
     * command word exit
     * @throws ParseException if the user input does not conform the expected format
     */
    public ExitCommand parse(String args) throws ParseException {
        //need to ensure that the arguments are indeed null

        if (args == null || args.replaceAll("\\s+", "").equals("")) {
            logger.log(Level.INFO, "ExitCommand arguments are null");
        } else {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    ExitCommand.MESSAGE_USAGE));
        }

        return new ExitCommand();
    }
}
```
###### \java\seedu\address\logic\parser\HelpCommandParser.java
``` java
/**
 * Parses input arguments and creates a new HelpCommand object
 */
public class HelpCommandParser implements Parser<HelpCommand> {
    private static Logger logger = Logger.getLogger("parse");

    /**
     * Parses the given {@code String} of arguments in the context of the HelpCommand
     * and returns a HelpCommand object for execution. For this command, parser needs to
     * ensure that the arguments are null, i.e. no extra characters are inputted after the
     * command word help
     * @throws ParseException if the user input does not conform the expected format
     */
    public HelpCommand parse(String args) throws ParseException {
        //need to ensure that the arguments are indeed null

        if (args == null || args.replaceAll("\\s+", "").equals("")) {
            logger.log(Level.INFO, "HelpCommand arguments are null");
        } else {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    HelpCommand.MESSAGE_USAGE));
        }

        return new HelpCommand();
    }
}
```
###### \java\seedu\address\logic\parser\HistoryCommandParser.java
``` java
/**
 * Parses input arguments and creates a new HistoryCommand object
 */
public class HistoryCommandParser implements Parser<HistoryCommand> {
    private static Logger logger = Logger.getLogger("parse");

    /**
     * Parses the given {@code String} of arguments in the context of the HistoryCommand
     * and returns a HistoryCommand object for execution. For this command, parser needs to
     * ensure that the arguments are null, i.e. no extra characters are inputted after the
     * command word history
     * @throws ParseException if the user input does not conform the expected format
     */
    public HistoryCommand parse(String args) throws ParseException {
        //need to ensure that the arguments are indeed null

        if (args == null || args.replaceAll("\\s+", "").equals("")) {
            logger.log(Level.INFO, "HistoryCommand arguments are null");
        } else {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    HistoryCommand.MESSAGE_USAGE));
        }

        return new HistoryCommand();
    }
}
```
###### \java\seedu\address\logic\parser\ListCommandParser.java
``` java
/**
 * Parses input arguments and creates a new ListCommand object
 */
public class ListCommandParser implements Parser<ListCommand> {
    private static Logger logger = Logger.getLogger("parse");

    /**
     * Parses the given {@code String} of arguments in the context of the ListCommand
     * and returns a ListCommand object for execution. For this command, parser needs to
     * ensure that the arguments are null, i.e. no extra characters are inputted after the
     * command word list
     * @throws ParseException if the user input does not conform the expected format
     */
    public ListCommand parse(String args) throws ParseException {
        //need to ensure that the arguments are indeed null

        if (args == null || args.replaceAll("\\s+", "").equals("")) {
            logger.log(Level.INFO, "ListCommand arguments are null");
        } else {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    ListCommand.MESSAGE_USAGE));
        }

        return new ListCommand();
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
###### \java\seedu\address\logic\parser\RedoCommandParser.java
``` java
/**
 * Parses input arguments and creates a new RedoCommand object
 */
public class RedoCommandParser implements Parser<RedoCommand> {
    private static Logger logger = Logger.getLogger("parse");

    /**
     * Parses the given {@code String} of arguments in the context of the RedoCommand
     * and returns a RedoCommand object for execution. For this command, parser needs to
     * ensure that the arguments are null, i.e. no extra characters are inputted after the
     * command word redo
     * @throws ParseException if the user input does not conform the expected format
     */
    public RedoCommand parse(String args) throws ParseException {
        //need to ensure that the arguments are indeed null

        if (args == null || args.replaceAll("\\s+", "").equals("")) {
            logger.log(Level.INFO, "RedoCommand arguments are null");
        } else {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    RedoCommand.MESSAGE_USAGE));
        }

        return new RedoCommand();
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

        if (!RemoveTagCommandParser.arePrefixesPresent(argMultimap, PREFIX_TAG)
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
###### \java\seedu\address\logic\parser\UndoCommandParser.java
``` java
/**
 * Parses input arguments and creates a new UndoCommand object
 */
public class UndoCommandParser implements Parser<UndoCommand> {
    private static Logger logger = Logger.getLogger("parse");

    /**
     * Parses the given {@code String} of arguments in the context of the UndoCommand
     * and returns an UndoCommand object for execution. For this command, parser needs to
     * ensure that the arguments are null, i.e. no extra characters are inputted after the
     * command word undo
     * @throws ParseException if the user input does not conform the expected format
     */
    public UndoCommand parse(String args) throws ParseException {
        //need to ensure that the arguments are indeed null

        if (args == null || args.replaceAll("\\s+", "").equals("")) {
            logger.log(Level.INFO, "UndoCommand arguments are null");
        } else {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    UndoCommand.MESSAGE_USAGE));
        }

        return new UndoCommand();
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
###### \java\seedu\address\ui\EmailWindow.java
``` java
/**
 * Controller for an email window.
 */
public class EmailWindow extends UiPart<Stage> {

    private static final Logger logger = LogsCenter.getLogger(EmailWindow.class);
    private static final String FXML = "EmailWindow.fxml";
    private String[] information = new String[4];
    private boolean isStillOpen = false;
    private boolean isSendButtonPressed = false;
    private boolean isQuitButtonPressed = false;
    private Email email = new Email() {
        @Override
        public CommandResult execute(Model model, CommandHistory history) throws CommandException {
            return null;
        }
    };

    @FXML
    private VBox emailContainer;

    @FXML
    private TextField emailField;

    @FXML
    private TextArea messageField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button quitButton;

    @FXML
    private Button sendButton;

    @FXML
    private TextField subjectField;

    public EmailWindow() {
        super(FXML, new Stage());
    }

    /**
     * This is the event handler when the Send Email Button is pressed.
     * Checks whether username, password, email subject and email message are
     * provided by the user. If any of the parameters are either null or an
     * empty string, the respective alert is thrown. Moreover, if all the fields are
     * correct, the information String array is returned back to the Email abstract class
     * to construct the email message and connect to the Gmail smtp server
     */
    @FXML
    public void handleSendEmailButtonAction() {
        information[0] = emailField.getText();
        information[1] = passwordField.getText();
        information[2] = subjectField.getText();
        information[3] = messageField.getText();

        if (emailField.getText() == null
                || emailField.getText().replaceAll("\\s+", "").equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, Messages.MESSAGE_USERNAME_NOT_PROVIDED, ButtonType.OK);
            alert.showAndWait();
            isStillOpen = true;
        } else if (passwordField.getText() == null
                || passwordField.getText().replaceAll("\\s+", "").equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, Messages.MESSAGE_PASSWORD_NOT_PROVIDED, ButtonType.OK);
            alert.showAndWait();

            isStillOpen = true;
        } else if (subjectField.getText() == null
                || subjectField.getText().replaceAll("\\s+", "").equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, Messages.MESSAGE_EMAIL_SUBJECT_NOT_PROVIDED, ButtonType.OK);
            alert.showAndWait();

            isStillOpen = true;
        } else if (messageField.getText() == null
                || messageField.getText().replaceAll("\\s+", "").equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, Messages.MESSAGE_EMAIL_MESSAGE_NOT_PROVIDED, ButtonType.OK);
            alert.showAndWait();

            isStillOpen = true;
        } else {
            logger.log(Level.INFO, "All fields from EmailWindow received successfully!");
            isStillOpen = false;
            isSendButtonPressed = true;
        }

        if (!isStillOpen) {
            getRoot().close();
        }
    }

    /**
     * This is the event handler when the Quit Button is pressed. When done so,
     * the EmailWindow event closes and the command box provides a simple message to the user
     * indicating that they have not sent an email to any guests
     */
    @FXML
    public void handleQuitButtonAction() {
        isQuitButtonPressed = true;
        getRoot().close();
    }

    /**
     * Shows the email window
     */
    public void showAndWait() {
        logger.fine("Showing email window");
        isStillOpen = true;
        try {
            getRoot().showAndWait();
        } catch (AssertionError ae) {
            System.out.println("Unhandled NSEvent - usually caused by Mac OS");
        } catch (Exception e) {
            System.out.println("Unhandled NSException");
        }
    }

    public String[] getInformation() {
        return information;
    }

    public boolean isSendButton() {
        return isSendButtonPressed;
    }

    public boolean isQuitButton() {
        return isQuitButtonPressed;
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
     */
    public String getTagColor(String tagName) {
        /**
         * Using a switch statement with the tag name ensures the color of the tag remains consistent
         * during different iterations of the code
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
###### \resources\view\EmailWindow.fxml
``` fxml
<fx:root type="javafx.stage.Stage" xmlns="http://javafx.com/javafx/8" xmlns:fx="http://javafx.com/fxml/1"
         minWidth="450" minHeight="600" title="Email A Guest" maximized="true">
    <icons>
        <Image url="@/docs/images/InvitésLogo.jpg"/>
    </icons>

    <scene>
        <Scene>
            <stylesheets>
                <URL value="@DarkTheme.css"/>
                <URL value="@Extensions.css"/>
            </stylesheets>

            <VBox fx:id="emailContainer" spacing="10">
                <Label fx:id="emailLabel" text="Your Email Address:"/>
                <TextField fx:id="emailField" promptText="Your Email Address"/>

                <Label fx:id="passwordLabel" text="Your Password:"/>
                <PasswordField fx:id="passwordField" promptText="Your Password"/>

                <Label fx:id="subjectLabel" text="Email Subject:"/>
                <TextField fx:id="subjectField" promptText="Email Subject"/>

                <Label fx:id="messageLabel" text="Email Message:"/>
                <TextArea fx:id="messageField" promptText="Email Message"/>

                <Button fx:id="sendButton" text="Send" onAction="#handleSendEmailButtonAction"/>
                <Button fx:id="quitButton" text="Quit" onAction="#handleQuitButtonAction"/>
            </VBox>
        </Scene>
    </scene>
</fx:root>
```
