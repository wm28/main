# Sarah
###### \main\java\seedu\address\logic\commands\EditCommand.java
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
###### \main\java\seedu\address\logic\commands\FilterCommand.java
``` java
package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.Messages;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.person.ContainsKeywordsPredicate;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FilterCommand extends Command {
    public static final String COMMAND_WORD = "filter";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose tags contain all of "
            + "the specified keywords (case-sensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD MORE_KEYWORDS...\n"
            + "Example: " + COMMAND_WORD + " pa/PAID t/Vegetarian a/Absent";

    //public static final String MESSAGE_PERSONS_FILTERED_OVERVIEW = "Filtered by: %1$s\n";

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
###### \main\java\seedu\address\logic\commands\MarkCommand.java
``` java
        Payment updatedPayment = personToEdit.getPayment();
```
###### \main\java\seedu\address\logic\commands\MarkCommand.java
``` java
        public void setPayment(Payment payment) {
            this.payment = payment;
        }
```
###### \main\java\seedu\address\logic\parser\CliSyntax.java
``` java
    public static final Prefix PREFIX_PAYMENT = new Prefix("pa/");
    public static final Prefix PREFIX_ATTENDANCE = new Prefix("a/");
```
###### \main\java\seedu\address\logic\parser\EditCommandParser.java
``` java
        if (argMultimap.getValue(PREFIX_PAYMENT).isPresent()) {
            editPersonDescriptor.setPayment(ParserUtil.parsePayment(argMultimap.getValue(PREFIX_PAYMENT).get()));
        }
        if (argMultimap.getValue(PREFIX_ATTENDANCE).isPresent()) {
            editPersonDescriptor.setAttendance(ParserUtil.parseAttendance(
                    argMultimap.getValue(PREFIX_ATTENDANCE).get()));
        }
```
###### \main\java\seedu\address\logic\parser\FilterCommandParser.java
``` java
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.ContainsKeywordsPredicate;

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

        return new FilterCommand(new ContainsKeywordsPredicate(Arrays.asList(keywords)));
    }
}
```
###### \main\java\seedu\address\logic\parser\ParserUtil.java
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
###### \main\java\seedu\address\logic\parser\ParserUtil.java
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
###### \main\java\seedu\address\model\person\ContainsKeywordsPredicate.java
``` java
package seedu.address.model.person;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.model.tag.Tag;

/**
 * Tests that a {@code Person}'s {@code Tags} matches all of the keywords given.
 */
public class ContainsKeywordsPredicate implements Predicate<Person> {
    private final List<String> keywords;
    private final ArrayList<String> checkKeywords = new ArrayList<>();

    public ContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    /**
     *
     * @param person containing details such as phone number, email address,
     *               payment status, attendance status and tags
     * @return the details that match keywords in the person's details, as mentioned above
     */
    public boolean test(Person person) {
        HashSet<seedu.address.model.tag.Tag> set = new HashSet<>(person.getTags());
        String strTags = "";

        int j = 0;

        checkKeywords.clear();

        for (int i = 0; i < keywords.size(); i++) {
            String str = keywords.get(i);
            String[] arrStr = str.split("/");

            if (arrStr[j].equals("t")) {
                checkKeywords.add(i, arrStr[j + 1]);

                strTags = "";

                for (Tag tag : set) {
                    strTags += " ";
                    strTags += tag.tagName;
                }
            }
        }

        final String checkStr = strTags;

        System.out.print("checkStr: "); //person's tags
        System.out.println(checkStr);
        System.out.print("checkKeywords: "); //keywords user wants to filter with
        System.out.println(checkKeywords);

        return keywords.stream()
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
###### \main\java\seedu\address\model\person\Payment.java
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
            "Payment should only contain alphanumeric characters and spaces, and it should not be blank";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String PAYMENT_VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

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
###### \main\java\seedu\address\model\person\Person.java
``` java
    public Payment getPayment() {
        return payment;
    }

    public Attendance getAttendance() {
        return attendance;
    }
```
###### \main\java\seedu\address\storage\XmlAdaptedPerson.java
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
###### \test\java\seedu\address\logic\parser\EditCommandParserTest.java
``` java
        // payment
        userInput = targetIndex.getOneBased() + PAYMENT_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withPayment(VALID_PAYMENT_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // attendance
        userInput = targetIndex.getOneBased() + ATTENDANCE_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withAttendance(VALID_ATTENDANCE_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

```
###### \test\java\seedu\address\model\person\AttendanceTest.java
``` java
package seedu.address.model.person;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class AttendanceTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Attendance(null));
    }

    @Test
    public void constructor_invalidAttendance_throwsIllegalArgumentException() {
        String invalidAttendance = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Attendance(invalidAttendance));
    }

    @Test
    public void isValidAttendance() {
        // null address
        Assert.assertThrows(NullPointerException.class, () -> Attendance.isValidAttendance(null));

        // invalid Attendance
        assertFalse(Attendance.isValidAttendance("")); // empty string
        assertFalse(Attendance.isValidAttendance(" ")); // spaces only

        // valid Attendance
        assertTrue(Attendance.isValidAttendance("PRESENT"));
        assertTrue(Attendance.isValidAttendance("ABSENT"));
    }
}
```
###### \test\java\seedu\address\testutil\EditPersonDescriptorBuilder.java
``` java
    /**
     * Sets the {@code Payment} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withPayment(String payment) {
        descriptor.setPayment(new Payment(payment));
        return this;
    }

    /**
     * Sets the {@code Attendance} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withAttendance(String attendance) {
        descriptor.setAttendance(new Attendance(attendance));
        return this;
    }

```
