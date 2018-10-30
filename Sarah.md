# Sarah
###### \java\seedu\address\logic\commands\EditCommand.java
``` java
        public void setPayment(Payment payment) {
            this.payment = payment;
        }

        public Optional<Payment> getPayment() {
            return Optional.ofNullable(payment);
        }

        public void setAttendance(Attendance attendance) {
            this.attendance = attendance;
        }

        public Optional<Attendance> getAttendance() {
            return Optional.ofNullable(attendance);
        }

```
###### \java\seedu\address\logic\commands\FilterCommand.java
``` java
/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FilterCommand extends Command {
    public static final String COMMAND_WORD = "filter";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose tags contain all of "
            + "the specified keywords (case-sensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD MORE_KEYWORDS...\n"
            + "Example: " + COMMAND_WORD + " n/NAME p/PHONE NUMBER e/EMAIL "
            + "pa/PAYMENT_STATUS a/ATTENDANCE_STATUS "
            + "t/TAG...";

    private final ContainsKeywordsPredicate predicate;

    public FilterCommand(ContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        return new CommandResult(String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW,
                model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FilterCommand // instanceof handles nulls
                && predicate.equals(((FilterCommand) other).predicate)); // state check
    }
}
```
###### \java\seedu\address\logic\commands\MarkCommand.java
``` java
        Payment updatedPayment = personToEdit.getPayment();
```
###### \java\seedu\address\logic\commands\MarkCommand.java
``` java
        public void setPayment(Payment payment) {
            this.payment = payment;
        }

        public Optional<Payment> getPayment() {
            return Optional.ofNullable(payment);
        }
```
###### \java\seedu\address\logic\parser\AddressBookParser.java
``` java
        case FilterCommand.COMMAND_WORD:
            return new FilterCommandParser().parse(arguments);
```
###### \java\seedu\address\logic\parser\CliSyntax.java
``` java
    public static final Prefix PREFIX_PAYMENT = new Prefix("pa/");
    public static final Prefix PREFIX_ATTENDANCE = new Prefix("a/");
```
###### \java\seedu\address\logic\parser\EditCommandParser.java
``` java
        if (argMultimap.getValue(PREFIX_PAYMENT).isPresent()) {
            editPersonDescriptor.setPayment(ParserUtil.parsePayment(argMultimap.getValue(PREFIX_PAYMENT).get()));
        }
        if (argMultimap.getValue(PREFIX_ATTENDANCE).isPresent()) {
            editPersonDescriptor.setAttendance(ParserUtil.parseAttendance(
                    argMultimap.getValue(PREFIX_ATTENDANCE).get()));
        }
```
###### \java\seedu\address\logic\parser\FilterCommandParser.java
``` java
/**
 * Parses input arguments and creates a new FilterCommand object
 */
public class FilterCommandParser implements Parser<FilterCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the FilterCommand
     * and returns an FilterCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FilterCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }

        String[] keywords = trimmedArgs.split("\\s+");

        ArrayList<String> checking = new ArrayList<>(Arrays.asList(keywords));

        for (int i = 0; i < checking.size(); i++) {

            if ((checking.get(i).charAt(0) == 'n' || checking.get(i).charAt(0) == 'e'
                    || (checking.get(i).charAt(0) == 'p' && checking.get(i).charAt(1) == '/')
                    || (checking.get(i).charAt(0) == 'p' && checking.get(i).charAt(1) == 'a')
                    || checking.get(i).charAt(0) == 'a' || checking.get(i).charAt(0) == 't')
                    && (checking.get(i).charAt(1) == '/' || checking.get(i).charAt(2) == '/')) {

                return new FilterCommand(new ContainsKeywordsPredicate(Arrays.asList(keywords)));
            } else {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
            }
        }
        return new FilterCommand(new ContainsKeywordsPredicate(Arrays.asList(keywords)));
    }
}
```
###### \java\seedu\address\logic\parser\ParserUtil.java
``` java
    /**
     * Parses a {@code String attendance} into an {@code attendance}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Attendance parseAttendance(String attendance) throws ParseException {
        requireNonNull(attendance);
        String trimmedAttendance = attendance.trim();
        if (!Attendance.isValidAttendance(trimmedAttendance)) {
            throw new ParseException(Attendance.MESSAGE_ATTENDANCE_CONSTRAINTS);
        }
        return new Attendance(trimmedAttendance);
    }

```
###### \java\seedu\address\logic\parser\ParserUtil.java
``` java
    /**
     * Parses a {@code String Payment} into an {@code Payment}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code payment} is invalid.
     */
    public static Payment parsePayment(String payment) throws ParseException {
        requireNonNull(payment);
        String trimmedPayment = payment.trim();
        if (!Payment.isValidPayment(trimmedPayment)) {
            throw new ParseException(Payment.MESSAGE_PAYMENT_CONSTRAINTS);
        }
        return new Payment(trimmedPayment);
    }

```
###### \java\seedu\address\model\person\ContainsKeywordsPredicate.java
``` java
/**
 * Tests that a {@code Person}'s {@code payment, attendance and tags etc.} matches all of the keywords given.
 */
public class ContainsKeywordsPredicate implements Predicate<Person> {
    private final List<String> keywords;
    private final ArrayList<String> checkKeywords = new ArrayList<>();

    public ContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    /**
     *
     * @param person containing details such as
     *               payment status, attendance status and tags
     * @return the details that match keywords in the person's details, as mentioned above
     */
    @Override
    public boolean test(Person person) {
        HashSet<seedu.address.model.tag.Tag> set = new HashSet<>(person.getTags());
        String strTags = "";

        int j = 0;

        checkKeywords.clear();

        for (int i = 0; i < keywords.size(); i++) {
            String str = keywords.get(i);
            String[] arrStr = str.split("/");

            if (arrStr[j].equals("pa")) {
                checkKeywords.add(i, arrStr[j + 1]);

                strTags += " ";
                strTags += person.getPayment();
            } else if (arrStr[j].equals("a")) {
                checkKeywords.add(i, arrStr[j + 1]);

                strTags += " ";
                strTags += person.getAttendance();
            } else if (arrStr[j].equals("t")) {
                checkKeywords.add(i, arrStr[j + 1]);

                strTags = "";

                for (Tag tag : set) {
                    strTags += " ";
                    strTags += tag.tagName;
                }
            }

        }

        final String checkStr = strTags;

        return checkKeywords.stream()
                .allMatch(checkKeywords -> StringUtil.containsWordIgnoreCase(checkStr, checkKeywords));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ContainsKeywordsPredicate // instanceof handles nulls
                && keywords.equals(((ContainsKeywordsPredicate) other).keywords)); // state check
    }
}
```
###### \java\seedu\address\model\person\NameContainsKeywordsPredicate.java
``` java
/**
 * Tests that a {@code Person}'s {@code Name, Phone or Email} matches any of the keywords given.
 */
public class NameContainsKeywordsPredicate implements Predicate<Person> {
    private final List<String> keywords;
    private final ArrayList<String> checkKeywords = new ArrayList<>();

    public NameContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Person person) {
        String strToCheck = "";

        int j = 0;

        checkKeywords.clear();

        for (int i = 0; i < keywords.size(); i++) {
            String str = keywords.get(i);
            String[] arrStr = str.split("/");

            if (arrStr[j].equals("n")) {
                checkKeywords.add(i, arrStr[j + 1]);

                strToCheck += " ";
                strToCheck += person.getName();
            } else if (arrStr[j].equals("p")) {
                checkKeywords.add(i, arrStr[j + 1]);

                strToCheck += " ";
                strToCheck += person.getPhone();
            } else if (arrStr[j].equals("e")) {
                checkKeywords.add(i, arrStr[j + 1]);

                strToCheck += " ";
                strToCheck += person.getEmail();
            }
        }

        final String checkStr = strToCheck;

        return checkKeywords.stream()
                .anyMatch(checkKeywords -> StringUtil.containsWordIgnoreCase(checkStr, checkKeywords));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof NameContainsKeywordsPredicate // instanceof handles nulls
                && keywords.equals(((NameContainsKeywordsPredicate) other).keywords)); // state check
    }

}
```
###### \java\seedu\address\model\person\Payment.java
``` java
package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's payment in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidPayment(String)}
 */
public class Payment {
    public static final String MESSAGE_PAYMENT_CONSTRAINTS =
            "Payment should only contain alphanumeric characters, spaces and . such as N.A. , "
                    + "and it should not be blank";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String PAYMENT_VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum}\\s.-]*";

    public final String paymentValue;

    /**
     * Constructs a {@code Payment}.
     *
     * @param payment A valid payment.
     */
    public Payment(String payment) {
        requireNonNull(payment);
        checkArgument(isValidPayment(payment), MESSAGE_PAYMENT_CONSTRAINTS);
        paymentValue = payment;
    }

    /**
     * Returns true if a given string is a valid attendance.
     */
    public static boolean isValidPayment(String test) {
        return test.matches(PAYMENT_VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return paymentValue;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Payment // instanceof handles nulls
                && paymentValue.equals(((Payment) other).paymentValue)); // state check
    }

    @Override
    public int hashCode() {
        return paymentValue.hashCode();
    }

}
```
###### \java\seedu\address\model\person\Person.java
``` java
    public Payment getPayment() {
        return payment;
    }

    public Attendance getAttendance() {
        return attendance;
    }
```
###### \java\seedu\address\storage\XmlAdaptedPerson.java
``` java
        if (payment == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Payment.class.getSimpleName()));
        }
        if (!Payment.isValidPayment(payment)) {
            throw new IllegalValueException(Payment.MESSAGE_PAYMENT_CONSTRAINTS);
        }
        final Payment modelPayment = new Payment(payment);

        if (attendance == null) {
            throw new IllegalValueException(String
                    .format(MISSING_FIELD_MESSAGE_FORMAT, Attendance.class.getSimpleName()));
        }
        if (!Attendance.isValidAttendance(attendance)) {
            throw new IllegalValueException(Attendance.MESSAGE_ATTENDANCE_CONSTRAINTS);
        }
        final Attendance modelAttendance = new Attendance(attendance);

```
