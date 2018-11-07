# Sarah
###### \java\seedu\address\logic\commands\FilterCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FilterCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void equals() {
        ContainsKeywordsPredicate firstPredicate =
                new ContainsKeywordsPredicate(Collections.singletonList("first"));
        ContainsKeywordsPredicate secondPredicate =
                new ContainsKeywordsPredicate(Collections.singletonList("second"));

        FilterCommand filterFirstCommand = new FilterCommand(firstPredicate);
        FilterCommand filterSecondCommand = new FilterCommand(secondPredicate);

        // same object -> returns true
        assertTrue(filterFirstCommand.equals(filterFirstCommand));

        // same values -> returns true
        FilterCommand filterFirstCommandCopy = new FilterCommand(firstPredicate);
        assertTrue(filterFirstCommand.equals(filterFirstCommandCopy));

        // different types -> returns false
        assertFalse(filterFirstCommand.equals(1));

        // null -> returns false
        assertFalse(filterFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(filterFirstCommand.equals(filterSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_allPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 7);
        ContainsKeywordsPredicate predicate = preparePredicate("");
        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ALICE, BENSON, CARL, DANIEL,
                ELLE, FIONA, GEORGE), model.getFilteredPersonList());
    }

    @Test
    public void execute_multipleKeywords_oneOrMultiplePersonsFound() {
        String expectedMessage1 = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        String expectedMessage2 = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2);
        String expectedMessage3 = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        String expectedMessage4 = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 4);

        //using payment NOTPAID -> returns equal for 2 people
        ContainsKeywordsPredicate predicate1 = preparePredicate("pa/NOTPAID");
        FilterCommand command1 = new FilterCommand(predicate1);
        expectedModel.updateFilteredPersonList(predicate1);
        assertCommandSuccess(command1, model, commandHistory, expectedMessage2, expectedModel);
        assertEquals(Arrays.asList(CARL, ELLE), model.getFilteredPersonList());

        //using payment PAID -> returns equal for 3 people
        ContainsKeywordsPredicate predicate2 = preparePredicate("pa/PAID");
        FilterCommand command2 = new FilterCommand(predicate2);
        expectedModel.updateFilteredPersonList(predicate2);
        assertCommandSuccess(command2, model, commandHistory, expectedMessage3, expectedModel);
        assertEquals(Arrays.asList(BENSON, DANIEL, FIONA), model.getFilteredPersonList());

        //using attendance PRESENT -> returns equal for 4 people
        ContainsKeywordsPredicate predicate3 = preparePredicate("a/PRESENT");
        FilterCommand command3 = new FilterCommand(predicate3);
        expectedModel.updateFilteredPersonList(predicate3);
        assertCommandSuccess(command3, model, commandHistory, expectedMessage4, expectedModel);
        assertEquals(Arrays.asList(ALICE, CARL, ELLE, GEORGE), model.getFilteredPersonList());

        //using attendance ABSENT -> returns equal for 3 people
        ContainsKeywordsPredicate predicate4 = preparePredicate("a/ABSENT");
        FilterCommand command4 = new FilterCommand(predicate4);
        expectedModel.updateFilteredPersonList(predicate4);
        assertCommandSuccess(command4, model, commandHistory, expectedMessage3, expectedModel);
        assertEquals(Arrays.asList(BENSON, DANIEL, FIONA), model.getFilteredPersonList());

        //using payment NOTPAID and attendance PRESENT -> returns equal for 2 people
        ContainsKeywordsPredicate predicate5 = preparePredicate("pa/NOTPAID a/PRESENT");
        FilterCommand command5 = new FilterCommand(predicate5);
        expectedModel.updateFilteredPersonList(predicate5);
        assertCommandSuccess(command5, model, commandHistory, expectedMessage2, expectedModel);
        assertEquals(Arrays.asList(CARL, ELLE), model.getFilteredPersonList());

        //using payment PAID and attendance ABSENT -> returns equal for 3 peopel
        ContainsKeywordsPredicate predicate6 = preparePredicate("pa/PAID a/ABSENT");
        FilterCommand command6 = new FilterCommand(predicate6);
        expectedModel.updateFilteredPersonList(predicate6);
        assertCommandSuccess(command6, model, commandHistory, expectedMessage3, expectedModel);
        assertEquals(Arrays.asList(BENSON, DANIEL, FIONA), model.getFilteredPersonList());

        //using tags NORMAL -> returns equal for 2 people
        ContainsKeywordsPredicate predicate7 = preparePredicate("t/NORMAL");
        FilterCommand command7 = new FilterCommand(predicate7);
        expectedModel.updateFilteredPersonList(predicate7);
        assertCommandSuccess(command7, model, commandHistory, expectedMessage2, expectedModel);
        assertEquals(Arrays.asList(BENSON, DANIEL), model.getFilteredPersonList());

        //using tags GUEST -> returns equal for 1 person
        ContainsKeywordsPredicate predicate8 = preparePredicate("t/GUEST");
        FilterCommand command8 = new FilterCommand(predicate8);
        expectedModel.updateFilteredPersonList(predicate8);
        assertCommandSuccess(command8, model, commandHistory, expectedMessage1, expectedModel);
        assertEquals(Collections.singletonList(BENSON), model.getFilteredPersonList());
    }

    @Test
    public void execute_multipleKeywords_noPersonsFound() {

        //using payment PAID and payment NOTPAID-> returns not equal
        ContainsKeywordsPredicate predicate1 = preparePredicate("pa/PAID pa/NOTPAID");
        FilterCommand command1 = new FilterCommand(predicate1);
        expectedModel.updateFilteredPersonList(predicate1);
        assertNotEquals(Arrays.asList(CARL, ELLE, FIONA), model.getFilteredPersonList());

        //using attendance PRESENT and attendance ABSENT-> returns not equal
        ContainsKeywordsPredicate predicate2 = preparePredicate("a/PRESENT a/ABSENT");
        FilterCommand command2 = new FilterCommand(predicate2);
        expectedModel.updateFilteredPersonList(predicate2);
        assertNotEquals(Arrays.asList(CARL, ELLE, FIONA), model.getFilteredPersonList());

        //using tags GUEST and tags VIP-> returns not equal
        ContainsKeywordsPredicate predicate4 = preparePredicate("t/GUEST t/VIP");
        FilterCommand command4 = new FilterCommand(predicate4);
        expectedModel.updateFilteredPersonList(predicate4);
        assertNotEquals(Arrays.asList(BENSON, DANIEL), model.getFilteredPersonList());
    }

```
###### \java\seedu\address\logic\commands\FindCommandTest.java
``` java
    @Test
    public void execute_multipleKeywords_oneOrMultiplePersonsFound() {
        String expectedMessage1 = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        String expectedMessage2 = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2);
        String expectedMessage3 = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);

        //using names -> returns equal
        NameContainsKeywordsPredicate predicate1 = preparePredicate("n/Kurz n/Elle n/Kunz");
        FindCommand command1 = new FindCommand(predicate1);
        expectedModel.updateFilteredPersonList(predicate1);
        assertCommandSuccess(command1, model, commandHistory, expectedMessage3, expectedModel);
        assertEquals(Arrays.asList(CARL, ELLE, FIONA), model.getFilteredPersonList());

        //using phone numbers -> returns equal
        NameContainsKeywordsPredicate predicate2 = preparePredicate("p/95352563 "
                + "p/9482224 p/9482427");
        FindCommand command2 = new FindCommand(predicate2);
        expectedModel.updateFilteredPersonList(predicate2);
        assertCommandSuccess(command2, model, commandHistory, expectedMessage3, expectedModel);
        assertEquals(Arrays.asList(CARL, ELLE, FIONA), model.getFilteredPersonList());

        //using email addresses -> returns equal
        NameContainsKeywordsPredicate predicate3 = preparePredicate("e/heinz@gmail.com "
                + "e/werner@gmail.com e/lydia@gmail.com");
        FindCommand command3 = new FindCommand(predicate3);
        expectedModel.updateFilteredPersonList(predicate3);
        assertCommandSuccess(command3, model, commandHistory, expectedMessage3, expectedModel);
        assertEquals(Arrays.asList(CARL, ELLE, FIONA), model.getFilteredPersonList());

        //using name, phone number and email address -> returns equal
        NameContainsKeywordsPredicate predicate4 = preparePredicate("n/Kurz "
                + "p/9482224 e/lydia@gmail.com");
        FindCommand command4 = new FindCommand(predicate4);
        expectedModel.updateFilteredPersonList(predicate4);
        assertCommandSuccess(command4, model, commandHistory, expectedMessage3, expectedModel);
        assertEquals(Arrays.asList(CARL, ELLE, FIONA), model.getFilteredPersonList());

        //using name, phone number and attendance -> returns equal
        NameContainsKeywordsPredicate predicate5 = preparePredicate("n/Kurz "
                + "p/9482224 a/ABSENT");
        FindCommand command5 = new FindCommand(predicate5);
        expectedModel.updateFilteredPersonList(predicate5);
        assertCommandSuccess(command5, model, commandHistory, expectedMessage2, expectedModel);
        assertEquals(Arrays.asList(CARL, ELLE), model.getFilteredPersonList());

        //using name, phone number and payment -> returns equal
        NameContainsKeywordsPredicate predicate6 = preparePredicate("n/Kurz "
                + "p/9482224 pa/PAID");
        FindCommand command6 = new FindCommand(predicate6);
        expectedModel.updateFilteredPersonList(predicate6);
        assertCommandSuccess(command6, model, commandHistory, expectedMessage2, expectedModel);
        assertEquals(Arrays.asList(CARL, ELLE), model.getFilteredPersonList());

        //using name, payment and attendance -> returns equal
        NameContainsKeywordsPredicate predicate7 = preparePredicate("n/Kurz "
                + "pa/NOT PAID a/ABSENT");
        FindCommand command7 = new FindCommand(predicate7);
        expectedModel.updateFilteredPersonList(predicate7);
        assertCommandSuccess(command7, model, commandHistory, expectedMessage1, expectedModel);
        assertEquals(Collections.singletonList(CARL), model.getFilteredPersonList());
    }

    @Test
    public void execute_multipleKeywords_noPersonsFound() {

        //using payment -> returns not equal
        NameContainsKeywordsPredicate predicate1 = preparePredicate("pa/NOT PAID"
                + "pa/NOT PAID pa/PAID");
        FindCommand command1 = new FindCommand(predicate1);
        expectedModel.updateFilteredPersonList(predicate1);
        assertNotEquals(Arrays.asList(CARL, ELLE, FIONA), model.getFilteredPersonList());

        //using attendance -> returns not equal
        NameContainsKeywordsPredicate predicate2 = preparePredicate("a/PRESENT "
                + "a/PRESENT a/ABSENT");
        FindCommand command2 = new FindCommand(predicate2);
        expectedModel.updateFilteredPersonList(predicate2);
        assertNotEquals(Arrays.asList(CARL, ELLE, FIONA), model.getFilteredPersonList());

        //using payment attendance status -> returns not equal
        NameContainsKeywordsPredicate predicate4 = preparePredicate("pa/NOT PAID"
                + "pa/NOT PAID a/ABSENT");
        FindCommand command4 = new FindCommand(predicate4);
        expectedModel.updateFilteredPersonList(predicate4);
        assertNotEquals(Arrays.asList(CARL, ELLE, FIONA), model.getFilteredPersonList());
    }

```
###### \java\seedu\address\logic\parser\EditCommandParserTest.java
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
###### \java\seedu\address\logic\parser\FilterCommandParserTest.java
``` java
public class FilterCommandParserTest {
    private FilterCommandParser parser = new FilterCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFilterCommand() {
        // no leading and trailing whitespaces
        FilterCommand expectedFilterCommand =
                new FilterCommand(new ContainsKeywordsPredicate(Arrays.asList("pa/PAID", "a/ABSENT")));
        assertParseSuccess(parser, "pa/PAID a/ABSENT", expectedFilterCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n pa/PAID \n \t a/ABSENT  \t", expectedFilterCommand);
    }
}
```
###### \java\seedu\address\logic\parser\ParserUtilTest.java
``` java
    @Test
    public void parseAttendance_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseAttendance((String) null));
    }

    @Test
    public void parseAttendance_invalidValue_throwsParseException() {
        Assert.assertThrows(ParseException.class, () -> ParserUtil.parseAttendance(INVALID_ATTENDANCE));
    }

    @Test
    public void parseAttendance_validValueWithoutWhitespace_returnsAttendance() throws Exception {
        Attendance expectedAttendance = new Attendance(VALID_ATTENDANCE);
        assertEquals(expectedAttendance, ParserUtil.parseAttendance(VALID_ATTENDANCE));
    }

    @Test
    public void parseAttendance_validValueWithWhitespace_returnsTrimmedAttendance() throws Exception {
        String attendanceWithWhitespace = WHITESPACE + VALID_ATTENDANCE + WHITESPACE;
        Attendance expectedAttendance = new Attendance(VALID_ATTENDANCE);
        assertEquals(expectedAttendance, ParserUtil.parseAttendance(attendanceWithWhitespace));
    }
```
###### \java\seedu\address\logic\parser\ParserUtilTest.java
``` java
    @Test
    public void parsePayment_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parsePayment((String) null));
    }

    @Test
    public void parsePayment_invalidValue_throwsParseException() {
        Assert.assertThrows(ParseException.class, () -> ParserUtil.parsePayment(INVALID_PAYMENT));
    }

    @Test
    public void parsePayment_validValueWithoutWhitespace_returnsPayment() throws Exception {
        Payment expectedPayment = new Payment(VALID_PAYMENT);
        assertEquals(expectedPayment, ParserUtil.parsePayment(VALID_PAYMENT));
    }

    @Test
    public void parsePayment_validValueWithWhitespace_returnsTrimmedPayment() throws Exception {
        String paymentWithWhitespace = WHITESPACE + VALID_PAYMENT + WHITESPACE;
        Payment expectedPayment = new Payment(VALID_PAYMENT);
        assertEquals(expectedPayment, ParserUtil.parsePayment(paymentWithWhitespace));
    }
```
###### \java\seedu\address\model\person\AttendanceTest.java
``` java
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
        assertTrue(Attendance.isValidAttendance("N.A."));
    }
}
```
###### \java\seedu\address\model\person\ContainsKeywordsPredicateTest.java
``` java
public class ContainsKeywordsPredicateTest {
    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        ContainsKeywordsPredicate firstPredicate = new ContainsKeywordsPredicate(firstPredicateKeywordList);
        ContainsKeywordsPredicate secondPredicate = new ContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        ContainsKeywordsPredicate firstPredicateCopy = new ContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different person -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_containsKeywords_returnsTrue() {
        // payment
        // One keyword
        ContainsKeywordsPredicate predicate1 = new ContainsKeywordsPredicate(
                Collections.singletonList("pa/PAID"));
        assertTrue(predicate1.test(new PersonBuilder().withPayment("PAID").build()));

        // Multiple keywords
        predicate1 = new ContainsKeywordsPredicate(Arrays.asList("pa/PAID", "a/ABSENT"));
        assertTrue(predicate1.test(new PersonBuilder().withPayment("PAID")
                .withAttendance("ABSENT").build()));

        // Only one matching keyword
        predicate1 = new ContainsKeywordsPredicate(Arrays.asList("pa/PAID", "p/842389749"));
        assertTrue(predicate1.test(new PersonBuilder().withPayment("PAID").build()));

        // Mixed-case keywords
        predicate1 = new ContainsKeywordsPredicate(Arrays.asList("pa/PaId", "a/AbSENt"));
        assertTrue(predicate1.test(new PersonBuilder().withPayment("PAID")
                .withAttendance("ABSENT").build()));

        // attendance
        // One keyword
        ContainsKeywordsPredicate predicate2 = new ContainsKeywordsPredicate(
                Collections.singletonList("a/ABSENT"));
        assertTrue(predicate2.test(new PersonBuilder().withAttendance("ABSENT").build()));

        // Multiple keywords
        predicate2 = new ContainsKeywordsPredicate(Arrays.asList("a/ABSENT", "pa/N.A."));
        assertTrue(predicate2.test(new PersonBuilder().withAttendance("ABSENT")
                .withPayment("N.A.").build()));

        // Only one matching keyword
        predicate2 = new ContainsKeywordsPredicate(Arrays.asList("a/ABSENT", "n/Alice"));
        assertTrue(predicate2.test(new PersonBuilder().withAttendance("ABSENT").build()));

        // tags
        // One keyword
        ContainsKeywordsPredicate predicate3 = new ContainsKeywordsPredicate(
                Collections.singletonList("t/GUEST"));
        assertTrue(predicate3.test(new PersonBuilder().withTags("GUEST").build()));

        // Multiple keywords
        predicate3 = new ContainsKeywordsPredicate(Arrays.asList("t/GUEST",
                "t/NoSeafood"));
        assertTrue(predicate3.test(new PersonBuilder().withTags("GUEST", "NoSeafood").build()));

        // Only one matching keyword
        predicate3 = new ContainsKeywordsPredicate(Arrays.asList("t/GUEST",
                "e/blahblahblah@gmail.com"));
        assertTrue(predicate3.test(new PersonBuilder().withTags("GUEST").build()));
    }

    @Test
    public void test_doesNotContainKeywords_returnsFalse() {
        // Zero keywords
        ContainsKeywordsPredicate predicate = new ContainsKeywordsPredicate(Collections.emptyList());
        //assertFalse(predicate.test(new PersonBuilder().withPayment("PAID").build()));

        // Non-matching keyword
        predicate = new ContainsKeywordsPredicate(Collections.singletonList("a/N.A>"));
        assertFalse(predicate.test(new PersonBuilder().withAttendance("N.A.").build()));

        //Payment
        predicate = new ContainsKeywordsPredicate(Collections.singletonList("pa/PAID!"));
        assertFalse(predicate.test(new PersonBuilder().withPayment("PAID").build()));

        //Attendance
        predicate = new ContainsKeywordsPredicate(Collections.singletonList("a/NA"));
        assertFalse(predicate.test(new PersonBuilder().withAttendance("ABSENT").build()));

        // Keywords match payment, tags, but does not match attendance
        predicate = new ContainsKeywordsPredicate(Arrays.asList("pa/PAID", "a/PRESENT"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").withPhone("85455255")
                .withEmail("aliceblah@gmail.com").withPayment("PAID").withAttendance("ABSENT").build()));
    }
}
```
###### \java\seedu\address\model\person\NameContainsKeywordsPredicateTest.java
``` java
        // phone
        // One keyword
        NameContainsKeywordsPredicate predicate2 = new NameContainsKeywordsPredicate(
                Collections.singletonList("p/85455255"));
        assertTrue(predicate2.test(new PersonBuilder().withPhone("85455255").build()));

        // Multiple keywords
        predicate2 = new NameContainsKeywordsPredicate(Arrays.asList("p/85455255", "p/85455256"));
        assertTrue(predicate2.test(new PersonBuilder().withPhone("85455255").build()));
        assertTrue(predicate2.test(new PersonBuilder().withPhone("85455256").build()));

        // Only one matching keyword
        predicate2 = new NameContainsKeywordsPredicate(Arrays.asList("p/85455256", "p/85455257"));
        assertTrue(predicate2.test(new PersonBuilder().withPhone("85455256").build()));

        // email
        // One keyword
        NameContainsKeywordsPredicate predicate3 = new NameContainsKeywordsPredicate(
                Collections.singletonList("e/aliceblah@gmail.com"));
        assertTrue(predicate3.test(new PersonBuilder().withEmail("aliceblah@gmail.com").build()));

        // Multiple keywords
        predicate3 = new NameContainsKeywordsPredicate(Arrays.asList("e/aliceblah@gmail.com",
                "e/bobblah@gmail.com"));
        assertTrue(predicate3.test(new PersonBuilder().withEmail("aliceblah@gmail.com").build()));
        assertTrue(predicate3.test(new PersonBuilder().withEmail("bobblah@gmail.com").build()));

        // Only one matching keyword
        predicate3 = new NameContainsKeywordsPredicate(Arrays.asList("e/bobblah@gmail.com",
                "e/carol@u.nus.edu"));
        assertTrue(predicate3.test(new PersonBuilder().withEmail("bobblah@gmail.com").build()));
```
###### \java\seedu\address\model\person\PaymentTest.java
``` java
public class PaymentTest {
    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Attendance(null));
    }

    @Test
    public void constructor_invalidPayment_throwsIllegalArgumentException() {
        String invalidPayment = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Payment(invalidPayment));
    }

    @Test
    public void isValidPayment() {
        // null address
        Assert.assertThrows(NullPointerException.class, () -> Payment.isValidPayment(null));

        // invalid Payment
        assertFalse(Payment.isValidPayment("")); // empty string
        assertFalse(Payment.isValidPayment(" ")); // spaces only
        assertFalse(Payment.isValidPayment("NOT PAID")); // spaces between words

        // valid Payment
        assertTrue(Payment.isValidPayment("PAID"));
        assertTrue(Payment.isValidPayment("NOTPAID"));
        assertTrue(Payment.isValidPayment("PENDING"));
        assertTrue(Payment.isValidPayment("N.A."));
    }
}
```
###### \java\seedu\address\testutil\EditPersonDescriptorBuilder.java
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
