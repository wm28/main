package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.commons.util.CsvUtil;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.converters.CsvConverter;
import seedu.address.logic.converters.exceptions.PersonEncodingException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

//@@author wm28
/**
 * Exports currently filtered guest list to a CSV file
 */
public class ExportCommand extends Command {

    public static final String COMMAND_WORD = "export";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Exports guests to a CSV file. "
            + "Parameters: FILE_PATH\n"
            + "Example: " + COMMAND_WORD + " guestlist.csv";
    public static final String MESSAGE_EXPORT_CSV_RESULT = "Successfully exported %1$d/%2$d guests to %3$s";
    public static final String MESSAGE_NO_PERSONS = "There are no persons to export!";

    private Path csvFile;
    private CsvConverter personConverter;
    private int totalPersons;
    private int successfulExports;

    public ExportCommand(String fileName, CsvConverter personConverter) {
        assert fileName != null : "FileName cannot be null";
        assert personConverter != null : "personConverter cannot be null";
        this.personConverter = personConverter;
        csvFile = Paths.get(fileName);
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        ObservableList<Person> filteredList = model.getFilteredPersonList();
        if (filteredList.size() == 0) {
            throw new CommandException(MESSAGE_NO_PERSONS);
        }
        totalPersons = filteredList.size();
        successfulExports = totalPersons;

        try {
            List<String> result = exportPersons(filteredList);
            CsvUtil.saveDataLinesToFile(csvFile, result);
        } catch (IOException ioe) {
            throw new CommandException(ioe.getMessage(), ioe);
        }

        return new CommandResult(String.format(MESSAGE_EXPORT_CSV_RESULT,
                successfulExports, totalPersons, csvFile.toString()));
    }

    /**
     * Exports persons to csv-formatted strings
     */
    private List<String> exportPersons(ObservableList<Person> personList) {
        List<String> result = new ArrayList<>();
        for (Person person : personList) {
            try {
                result.add(personConverter.encodePerson(person));
            } catch (PersonEncodingException pee) {
                successfulExports--;
            }
        }
        return result;
    }
}
//@@author
