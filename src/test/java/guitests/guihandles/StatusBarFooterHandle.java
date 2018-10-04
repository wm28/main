package guitests.guihandles;

import org.controlsfx.control.StatusBar;

import javafx.scene.Node;

/**
 * A handle for the {@code StatusBarFooter} at the footer of the application.
 */
public class StatusBarFooterHandle extends NodeHandle<Node> {
    public static final String STATUS_BAR_PLACEHOLDER = "#statusbarPlaceholder";

    private static final String SYNC_STATUS_ID = "#syncStatus";
    private static final String TOTAL_PERSONS_STATUS_ID = "#totalPersonsStatus";
    private static final String SAVE_LOCATION_STATUS_ID = "#saveLocationStatus";

    private final StatusBar syncStatusNode;
    private final StatusBar totalPersonsStatusNode;
    private final StatusBar saveLocationNode;

    private String lastRememberedSyncStatus;
    private String getLastRememberedTotalPersonsStatus;
    private String lastRememberedSaveLocation;

    public StatusBarFooterHandle(Node statusBarFooterNode) {
        super(statusBarFooterNode);

        syncStatusNode = getChildNode(SYNC_STATUS_ID);
        totalPersonsStatusNode = getChildNode(TOTAL_PERSONS_STATUS_ID);
        saveLocationNode = getChildNode(SAVE_LOCATION_STATUS_ID);
    }

    /**
     * Returns the text of the sync status portion of the status bar.
     */
    public String getSyncStatus() {
        return syncStatusNode.getText();
    }

    /**
     * Returns the text of the 'total persons' portion of the status bar.
     * Note: This code was adapted from the example implementation provide by @yamgent from SE-EDU
     */
    public String getTotalPersonsStatus() {
        return totalPersonsStatusNode.getText();
    }

    /**
     * Returns the text of the 'save location' portion of the status bar.
     */
    public String getSaveLocation() {
        return saveLocationNode.getText();
    }

    /**
     * Remembers the content of the sync status portion of the status bar.
     */
    public void rememberSyncStatus() {
        lastRememberedSyncStatus = getSyncStatus();
    }

    /**
     * Returns true if the current content of the sync status is different from the value remembered by the most recent
     * {@code rememberSyncStatus()} call.
     */
    public boolean isSyncStatusChanged() {
        return !lastRememberedSyncStatus.equals(getSyncStatus());
    }

    /**
     * Remembers the content of the 'total' persons portion of the status bar
     * Note: This code was adapted from the example implementation provide by @yamgent from SE-EDU
     */
    public void rememberTotalPersonsStatus() {
        getLastRememberedTotalPersonsStatus = getTotalPersonsStatus();
    }

    /**
     * Returns true if the current content of the 'total persons' is different from the value remembered
     * by the most recent {@code rememberTotalPersonsStatus()} call.
     *
     * Note: This code was adapted from the example implementation provide by @yamgent from SE-EDU
     */
    public boolean isTotalPersonsStatusChanged() {
        return !getLastRememberedTotalPersonsStatus.equals(getTotalPersonsStatus());
    }

    /**
     * Remembers the content of the 'save location' portion of the status bar.
     */
    public void rememberSaveLocation() {
        lastRememberedSaveLocation = getSaveLocation();
    }

    /**
     * Returns true if the current content of the 'save location' is different from the value remembered by the most
     * recent {@code rememberSaveLocation()} call.
     */
    public boolean isSaveLocationChanged() {
        return !lastRememberedSaveLocation.equals(getSaveLocation());
    }
}
