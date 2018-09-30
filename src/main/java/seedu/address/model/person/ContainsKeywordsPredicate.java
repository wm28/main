package seedu.address.model.person;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;

/**
 * Tests that a {@code Person}'s {@code Tags} matches all of the keywords given.
 */
public class ContainsKeywordsPredicate {
    private final List<String> keywords;

    public ContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }


    public boolean test(Person person) {
        return keywords.stream()
                .allMatch(keyword -> java.util.Set(person.getTags(), keyword));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ContainsKeywordsPredicate // instanceof handles nulls
                && keywords.equals(((ContainsKeywordsPredicate) other).keywords)); // state check
    }
}
