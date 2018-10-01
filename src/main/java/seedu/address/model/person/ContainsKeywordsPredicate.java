package seedu.address.model.person;

import java.util.HashSet;
import java.util.List;

import seedu.address.commons.util.StringUtil;
import seedu.address.model.tag.Tag;

/**
 * Tests that a {@code Person}'s {@code Tags} matches all of the keywords given.
 */
public class ContainsKeywordsPredicate {
    private final List<String> keywords;

    public ContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }


    public boolean test(Person person) {
        HashSet<seedu.address.model.tag.Tag> set = new HashSet<>(person.getTags());
        String str_tags = null;

        for (Tag tag : set) str_tags += tag.tagName;
        
        final String tag_str = str_tags;
        return keywords.stream()
                .allMatch(keyword -> StringUtil.containsWordIgnoreCase(tag_str, keyword));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ContainsKeywordsPredicate // instanceof handles nulls
                && keywords.equals(((ContainsKeywordsPredicate) other).keywords)); // state check
    }
}
