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
        assertTrue(predicate1.test(new PersonBuilder().withName("Alice").build()));

        // Multiple keywords
        predicate1 = new NameContainsKeywordsPredicate(Arrays.asList("n/Alice", "n/Bob"));
        assertTrue(predicate1.test(new PersonBuilder().withName("Alice").build()));
        assertTrue(predicate1.test(new PersonBuilder().withName("Bob").build()));

        // Only one matching keyword
        predicate1 = new NameContainsKeywordsPredicate(Arrays.asList("n/Bob", "n/Carol"));
        assertTrue(predicate1.test(new PersonBuilder().withName("Bob").build()));

        // Mixed-case keywords
        predicate1 = new NameContainsKeywordsPredicate(Arrays.asList("n/aLIce", "n/bOB"));
        assertTrue(predicate1.test(new PersonBuilder().withName("Alice").build()));
        assertTrue(predicate1.test(new PersonBuilder().withName("Bob").build()));

        //@@author Sarah
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
        //@@author
    }

    @Test
    public void test_nameDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").build()));
        assertFalse(predicate.test(new PersonBuilder().withPhone("85455255").build()));
        assertFalse(predicate.test(new PersonBuilder().withEmail("aliceblah@gmail.com").build()));

        // Non-matching keyword
        predicate = new NameContainsKeywordsPredicate(Collections.singletonList("n/Carol"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").build()));

        //Wrong prefixes
        //Payment
        predicate = new NameContainsKeywordsPredicate(Collections.singletonList("pa/PAID"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").build()));
        assertFalse(predicate.test(new PersonBuilder().withPhone("85455255").build()));
        assertFalse(predicate.test(new PersonBuilder().withEmail("aliceblah@gmail.com").build()));

        //Attendance
        predicate = new NameContainsKeywordsPredicate(Collections.singletonList("a/ABSENT"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").build()));
        assertFalse(predicate.test(new PersonBuilder().withPhone("85455255").build()));
        assertFalse(predicate.test(new PersonBuilder().withEmail("aliceblah@gmail.com").build()));

        // Keywords match phone, email and attendance, but does not match name
        predicate = new NameContainsKeywordsPredicate(Arrays.asList("85455255", "aliceblah@gmail.com",
                "PAID", "ABSENT"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").withPhone("85455255")
                .withEmail("aliceblah@gmail.com").withPayment("PAID").withAttendance("ABSENT").build()));
    }
}
