package seedu.address.ui;

import java.util.List;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.converters.error.ImportError;

/**
 * Controller for a import report window.
 */
public class ImportReportWindow extends UiPart<Stage> {

    private static final Logger logger = LogsCenter.getLogger(ImportReportWindow.class);
    private static final String FXML = "ImportReportWindow.fxml";
    private List<ImportError> errors;

    @FXML
    private TableView<ImportError> importErrorTable;

    @FXML
    private TableColumn<ImportError, String> inputDataColumn;

    @FXML
    private TableColumn<ImportError, String> errorMessageColumn;

    public ImportReportWindow(List<ImportError> errors) {
        super(FXML, new Stage());
        this.errors = errors;
        setConnections();
    }

    @FXML
    private void handleCloseImportReportWindow() {
        getRoot().close();
        logger.fine("Import report closed");

    }

    /**
     * Shows the import report window
     */
    public void show() {
        logger.fine("Showing import report");
        getRoot().show();
    }

    private void setConnections() {
        errorMessageColumn.setCellValueFactory(importError -> importError.getValue().errorMessageProperty());
        inputDataColumn.setCellValueFactory(importError -> importError.getValue().dataInputProperty());
        importErrorTable.setItems(FXCollections.observableArrayList(errors));
    }
}

