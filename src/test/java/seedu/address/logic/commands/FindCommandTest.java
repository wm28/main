package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.NameContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void equals() {
        NameContainsKeywordsPredicate firstPredicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("first"));
        NameContainsKeywordsPredicate secondPredicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("second"));

        FindCommand findFirstCommand = new FindCommand(firstPredicate);
        FindCommand findSecondCommand = new FindCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindCommand findFirstCommandCopy = new FindCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        NameContainsKeywordsPredicate predicate = preparePredicate(" ");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    //@@author Sarah
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

    //@@author
    /**
     * Parses {@code userInput} into a {@code NameContainsKeywordsPredicate}.
     */
    private NameContainsKeywordsPredicate preparePredicate(String userInput) {
        return new NameContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}
