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
        HashSet<seedu.address.model.tag.Tag> set;
        Tag[] tagsArray = new Tag[0];
        for (Tag tag : set = new HashSet<>(person.getTags())) {
            tagsArray = set.toArray(new Tag[]{tag});
        }

        Tag[] finalTagsArray = tagsArray;
        return keywords.stream()
                .allMatch(keyword -> StringUtil.containsWordIgnoreCase(finalTagsArray, keyword));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ContainsKeywordsPredicate // instanceof handles nulls
                && keywords.equals(((ContainsKeywordsPredicate) other).keywords)); // state check
    }
}
