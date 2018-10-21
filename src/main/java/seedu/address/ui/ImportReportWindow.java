package seedu.address.ui;

import java.util.ArrayList;
import java.util.logging.Logger;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;


/**
 * Controller for a import report window.
 */
public class ImportReportWindow extends UiPart<Stage> {

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "ImportReportWindow.fxml";

    @FXML
    private TableView<ImportError> importErrorTable;

    @FXML
    private TableColumn<ImportError, String> inputDataColumn;

    @FXML
    private TableColumn<ImportError, String> errorMessageColumn;

    public ImportReportWindow(Stage root) {
        super(FXML, root);
        setConnections();
    }

    public ImportReportWindow() {
        this(new Stage());
    }

    @FXML
    private void handleImportReportWindow() {
        getRoot().close();
    }

    /**
     * Shows the import report window
     */
    public void show() {
        logger.fine("Showing import report");
        getRoot().show();
    }

    private void setConnections() {
        ArrayList<ImportError> errorList = new ArrayList<>();
        errorList.add(new ImportError("testing1", "testing\ntesting1"));
        errorList.add(new ImportError("testing2", "testing\ntesting2"));
        errorList.add(new ImportError("Testing3", "testing\ntesting3"));
        errorList.add(new ImportError("testing4", "testing\ntesting4"));
        errorList.add(new ImportError("testing5", "testing\ntesting5"));
        errorList.add(new ImportError("testing6", "testing\ntesting6"));
        errorList.add(new ImportError("testing7", "testing\ntesting7"));
        errorList.add(new ImportError("testing8", "testing\ntesting8"));

        errorMessageColumn.setCellValueFactory(importError -> importError.getValue().errorMessageProperty());
        inputDataColumn.setCellValueFactory(importError -> importError.getValue().dataInputProperty());
        importErrorTable.setItems(FXCollections.observableArrayList(errorList));

    }


    public static class ImportError {
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
}

