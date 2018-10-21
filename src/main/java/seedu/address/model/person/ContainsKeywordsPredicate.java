package seedu.address.model.person;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.model.tag.Tag;

//@@author Sarah
/**
 * Tests that a {@code Person}'s {@code payment, attendance and tags etc.} matches all of the keywords given.
 */
public class ContainsKeywordsPredicate implements Predicate<Person> {
    private final List<String> keywords;
    private final ArrayList<String> checkKeywords = new ArrayList<>();

    public ContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    /**
     *
     * @param person containing details such as phone number, email address,
     *               payment status, attendance status and tags
     * @return the details that match keywords in the person's details, as mentioned above
     */
    public boolean test(Person person) {
        HashSet<seedu.address.model.tag.Tag> set = new HashSet<>(person.getTags());
        String strTags = "";

        int j = 0;

        checkKeywords.clear();

        for (int i = 0; i < keywords.size(); i++) {
            String str = keywords.get(i);
            String[] arrStr = str.split("/");

            if (arrStr[j].equals("pa")) {
                checkKeywords.add(i, arrStr[j + 1]);

                strTags += " ";
                strTags += person.getPayment();
            } else if (arrStr[j].equals("t")) {
                checkKeywords.add(i, arrStr[j + 1]);

                strTags = "";

                for (Tag tag : set) {
                    strTags += " ";
                    strTags += tag.tagName;
                }
            }

        }

        final String checkStr = strTags;

        return checkKeywords.stream()
                .allMatch(checkKeywords -> StringUtil.containsWordIgnoreCase(checkStr, checkKeywords));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ContainsKeywordsPredicate // instanceof handles nulls
                && keywords.equals(((ContainsKeywordsPredicate) other).keywords)); // state check
    }
}
