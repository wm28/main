package seedu.address.model.person;

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

    public ContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    /**
     *
     * @param person containing details such as tags
     * @return the tags that match keywords in the person's tags
     */
    public boolean test(Person person) {
        HashSet<seedu.address.model.tag.Tag> set = new HashSet<>(person.getTags());
        String strTags = null;

        for (Tag tag : set) {
            strTags += tag.tagName;
        }

        final String tagStr = strTags;
        return keywords.stream()
                .allMatch(keywords -> StringUtil.containsWordIgnoreCase(tagStr, keywords));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ContainsKeywordsPredicate // instanceof handles nulls
                && keywords.equals(((ContainsKeywordsPredicate) other).keywords)); // state check
    }
}
