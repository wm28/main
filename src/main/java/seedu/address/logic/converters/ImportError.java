package seedu.address.logic.converters;

import javafx.beans.property.SimpleStringProperty;

/**
 * Represents an import error
 */
public class ImportError {
    private final SimpleStringProperty dataInput;
    private final SimpleStringProperty errorMessage;

    public ImportError(String dataInput, String errorMessage) {
        this.dataInput = new SimpleStringProperty(dataInput);
        this.errorMessage = new SimpleStringProperty(errorMessage);
    }

    public String getDataInput() {
        return dataInput.get();
    }

    public SimpleStringProperty dataInputProperty() {
        return dataInput;
    }

    public void setDataInput(String dataInput) {
        this.dataInput.set(dataInput);
    }

    public String getErrorMessage() {
        return errorMessage.get();
    }

    public SimpleStringProperty errorMessageProperty() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage.set(errorMessage);
    }
}
