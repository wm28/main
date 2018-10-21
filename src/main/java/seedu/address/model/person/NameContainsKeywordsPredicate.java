package seedu.address.model.person;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;

//@@author Sarah
/**
 * Tests that a {@code Person}'s {@code Name, Phone or Email} matches any of the keywords given.
 */
public class NameContainsKeywordsPredicate implements Predicate<Person> {
    private final List<String> keywords;
    private final ArrayList<String> checkKeywords = new ArrayList<>();

    public NameContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Person person) {
        String strToCheck = "";

        int j = 0;

        checkKeywords.clear();

        for (int i = 0; i < keywords.size(); i++) {
            String str = keywords.get(i);
            String[] arrStr = str.split("/");

            if (arrStr[j].equals("n")) {
                checkKeywords.add(i, arrStr[j + 1]);

                strToCheck += " ";
                strToCheck += person.getName();
            } else if (arrStr[j].equals("p")) {
                checkKeywords.add(i, arrStr[j + 1]);

                strToCheck += " ";
                strToCheck += person.getPhone();
            } else if (arrStr[j].equals("e")) {
                checkKeywords.add(i,
                        arrStr[j + 1]);

                strToCheck += " ";
                strToCheck += person.getEmail();
            }
        }

        final String checkStr = strToCheck;

        return checkKeywords.stream()
                .anyMatch(checkKeywords -> StringUtil.containsWordIgnoreCase(checkStr, checkKeywords));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof NameContainsKeywordsPredicate // instanceof handles nulls
                && keywords.equals(((NameContainsKeywordsPredicate) other).keywords)); // state check
    }

}
