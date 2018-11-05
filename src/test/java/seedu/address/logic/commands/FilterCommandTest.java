package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.GEORGE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.ContainsKeywordsPredicate;

//@@author Sarah
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
        assertEquals(Arrays.asList(ALICE, BENSON, CARL, DANIEL
        , ELLE, FIONA, GEORGE), model.getFilteredPersonList());
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

    //@@author
    /**
     * Parses {@code userInput} into a {@code ContainsKeywordsPredicate}.
     */
    private ContainsKeywordsPredicate preparePredicate(String userInput) {
        return new ContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}
