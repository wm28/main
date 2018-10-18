package seedu.address.logic.converters.fileformats.csv;

import seedu.address.logic.converters.fileformats.AdaptedPerson;

//@@author wm28
/**
 * Represents a Csv-formatted person
 */
public class CsvAdaptedPerson extends AdaptedPerson {
    private final String csvFormattedPerson;

    public CsvAdaptedPerson(String csvFormattedPerson) {
        this.csvFormattedPerson = csvFormattedPerson;
    }

    @Override
    public String getFormattedString() {
        return csvFormattedPerson;
    }
}
//@@author
