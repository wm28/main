package seedu.address.model.person;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import seedu.address.testutil.PersonBuilder;

//@@author Sarah
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
