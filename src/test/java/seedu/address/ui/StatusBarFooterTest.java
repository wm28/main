package seedu.address.ui;

import static org.junit.Assert.assertEquals;
import static seedu.address.testutil.EventsUtil.postNow;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.ui.StatusBarFooter.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

import org.junit.*;

import guitests.guihandles.StatusBarFooterHandle;
import seedu.address.commons.events.model.AddressBookChangedEvent;
import seedu.address.model.event.Event;
import seedu.address.testutil.AddressBookBuilder;

public class StatusBarFooterTest extends GuiUnitTest {

    private static final Path STUB_SAVE_LOCATION = Paths.get("Stub");
    private static final Path RELATIVE_PATH = Paths.get(".");

    private static final AddressBookChangedEvent EVENT_STUB = new AddressBookChangedEvent(
            new AddressBookBuilder().withPerson(ALICE).build());

    private static final int INITAL_TOTAL_PERSONS = 0;
    private static final String INITIAL_DAYS_LEFT = "NO DETAILS";
    private static final Event INITIAL_EVENT = new Event();
    private static final Clock originalClock = StatusBarFooter.getClock();
    private static final Clock injectedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());

    private StatusBarFooterHandle statusBarFooterHandle;

    @BeforeClass
    public static void setUpBeforeClass() {
        // inject fixed clock
        StatusBarFooter.setClock(injectedClock);
    }

    @AfterClass
    public static void tearDownAfterClass() {
        // restore original clock
        StatusBarFooter.setClock(originalClock);
    }

    @Before
    public void setUp() {
        StatusBarFooter statusBarFooter = new StatusBarFooter(STUB_SAVE_LOCATION, INITAL_TOTAL_PERSONS, INITIAL_EVENT);
        uiPartRule.setUiPart(statusBarFooter);

        statusBarFooterHandle = new StatusBarFooterHandle(statusBarFooter.getRoot());
    }
    @Ignore
    @Test
    public void display() {
        // initial state
        assertStatusBarContent(String.format(DAYS_LEFT_STATUS, INITIAL_DAYS_LEFT),
                RELATIVE_PATH.resolve(STUB_SAVE_LOCATION).toString(), SYNC_STATUS_INITIAL,
                String.format(TOTAL_PERSONS_STATUS, INITAL_TOTAL_PERSONS));

        // after address book is updated
        postNow(EVENT_STUB);
        assertStatusBarContent(String.format(DAYS_LEFT_STATUS, INITIAL_DAYS_LEFT), RELATIVE_PATH.resolve(STUB_SAVE_LOCATION).toString(),
                String.format(SYNC_STATUS_UPDATED, new Date(injectedClock.millis()).toString()),
                String.format(TOTAL_PERSONS_STATUS, EVENT_STUB.data.getPersonList().size()));
    }

    /**
     * Asserts that the save location matches that of {@code expectedSaveLocation}, and the
     * sync status matches that of {@code expectedSyncStatus},
     * the days left status matched that of {@code expectedDaysLeftStatus}and the total persons matches that
     * of {@code expectedTotalPersonsStatus}.
     */
    private void assertStatusBarContent(String expectedDaysLeftStatus, String expectedSaveLocation,
                                        String expectedSyncStatus, String expectedTotalPersonsStatus) {
        assertEquals(expectedSaveLocation, statusBarFooterHandle.getSaveLocation());
        assertEquals(expectedSyncStatus, statusBarFooterHandle.getSyncStatus());
        assertEquals(expectedTotalPersonsStatus, statusBarFooterHandle.getTotalPersonsStatus());
        assertEquals(expectedDaysLeftStatus, statusBarFooterHandle.getDaysLeftStatus());
        guiRobot.pauseForHuman();
    }

}
