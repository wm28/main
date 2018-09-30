package seedu.address.logic.commands;

import seedu.address.commons.util.CsvUtil;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.CsvParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Imports multiple guests into the guest list of the current event via a CSV file
 */
public class ImportCommand extends Command {

    public static final String COMMAND_WORD = "import";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Imports guests into current event through a CSV file. "
            + "Parameters: FILE_PATH\n"
            + "Example: " + COMMAND_WORD + " guestlist.csv";

    public static final String MESSAGE_IMPORT_CSV_RESULT = "Successfully imported %1$d of %2$d guests from %3$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book";

    private Path csvFile;
    private List<String> guestData;
    private int numberGuest;
    private int numberSucess;

    public ImportCommand(String fileName){
        csvFile = Paths.get(fileName);
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException{
        requireNonNull(model);

        try{
            guestData = CsvUtil.getDataLinesFromFile(csvFile);
        } catch (IOException e){
            throw new CommandException(e.getMessage());
        }

        numberGuest = guestData.size();
        numberSucess = numberGuest;

        for(String guest: guestData){
            try {
                Person toAdd = new CsvParser(guest).parse();
                addPerson(toAdd,model);
            } catch (ParseException pe){
                numberSucess--;
            } catch (CommandException e){
                numberSucess--;
            }
        }

        model.commitAddressBook();
        return new CommandResult(String.format(MESSAGE_IMPORT_CSV_RESULT, numberSucess, numberGuest, csvFile.getFileName()));
    }

    /**
     * Adds a person to the addressbook
     */
    private void addPerson(Person toAdd, Model model) throws CommandException{
        if (model.hasPerson(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }
        model.addPerson(toAdd);
    }
}
