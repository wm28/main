package seedu.address.model.person;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import seedu.address.testutil.PersonBuilder;

public class NameContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        NameContainsKeywordsPredicate firstPredicate = new NameContainsKeywordsPredicate(firstPredicateKeywordList);
        NameContainsKeywordsPredicate secondPredicate = new NameContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        NameContainsKeywordsPredicate firstPredicateCopy = new NameContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different person -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_nameContainsKeywords_returnsTrue() {
        // name
        // One keyword
        NameContainsKeywordsPredicate predicate1 = new NameContainsKeywordsPredicate(
                Collections.singletonList("n/Alice"));
        assertTrue(predicate1.test(new PersonBuilder().withName("Alice Bob").build()));

        // Multiple keywords
        predicate1 = new NameContainsKeywordsPredicate(Arrays.asList("n/Alice", "n/Bob"));
        assertTrue(predicate1.test(new PersonBuilder().withName("Alice Bob").build()));

        // Only one matching keyword
        predicate1 = new NameContainsKeywordsPredicate(Arrays.asList("n/Bob", "n/Carol"));
        assertTrue(predicate1.test(new PersonBuilder().withName("Alice Carol").build()));

        // Mixed-case keywords
        predicate1 = new NameContainsKeywordsPredicate(Arrays.asList("n/aLIce", "n/bOB"));
        assertTrue(predicate1.test(new PersonBuilder().withName("Alice Bob").build()));

        //@@author Sarah
        // phone
        // One keyword
        NameContainsKeywordsPredicate predicate2 = new NameContainsKeywordsPredicate(
                Collections.singletonList("p/Alice"));
        assertTrue(predicate2.test(new PersonBuilder().withPhone("Alice Bob").build()));

        // Multiple keywords
        predicate2 = new NameContainsKeywordsPredicate(Arrays.asList("p/Alice", "p/Bob"));
        assertTrue(predicate2.test(new PersonBuilder().withPhone("Alice Bob").build()));

        // Only one matching keyword
        predicate2 = new NameContainsKeywordsPredicate(Arrays.asList("p/Bob", "p/Carol"));
        assertTrue(predicate2.test(new PersonBuilder().withPhone("Alice Carol").build()));

        // Mixed-case keywords
        predicate2 = new NameContainsKeywordsPredicate(Arrays.asList("p/aLIce", "p/bOB"));
        assertTrue(predicate2.test(new PersonBuilder().withPhone("Alice Bob").build()));

        // email
        // One keyword
        NameContainsKeywordsPredicate predicate3 = new NameContainsKeywordsPredicate(
                Collections.singletonList("e/Alice"));
        assertTrue(predicate3.test(new PersonBuilder().withEmail("Alice Bob").build()));

        // Multiple keywords
        predicate3 = new NameContainsKeywordsPredicate(Arrays.asList("e/Alice", "e/Bob"));
        assertTrue(predicate3.test(new PersonBuilder().withEmail("Alice Bob").build()));

        // Only one matching keyword
        predicate3 = new NameContainsKeywordsPredicate(Arrays.asList("e/Bob", "e/Carol"));
        assertTrue(predicate3.test(new PersonBuilder().withEmail("Alice Carol").build()));

        // Mixed-case keywords
        predicate3 = new NameContainsKeywordsPredicate(Arrays.asList("e/aLIce", "e/bOB"));
        assertTrue(predicate3.test(new PersonBuilder().withEmail("Alice Bob").build()));
        //@@author
    }

    @Test
    public void test_nameDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").build()));

        // Non-matching keyword
        predicate = new NameContainsKeywordsPredicate(Collections.singletonList("n/Carol"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Keywords match phone, email and attendance, but does not match name
        predicate = new NameContainsKeywordsPredicate(Arrays.asList("12345", "alice@gmail.com", "PRESENT"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").withPhone("12345")
                .withEmail("alice@gmail.com").withPayment("PENDING").withAttendance("PRESENT").build()));
    }
}
