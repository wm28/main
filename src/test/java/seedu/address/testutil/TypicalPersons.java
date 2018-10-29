package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_ATTENDANCE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ATTENDANCE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ATTENDANCE_DANNY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_DANNY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_DANNY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PAYMENT_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PAYMENT_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PAYMENT_DANNY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_DANNY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_DIET_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_DIET_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.person.Person;


/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalPersons {

    public static final Person ALICE = new PersonBuilder().withName("Alice Pauline")
            .withAttendance("PRESENT").withEmail("alice@gmail.com")
            .withPhone("94351253")
            .withPayment("PENDING")
            .withTags("VEGETARIAN", "NoNuts").build();
    public static final Person BENSON = new PersonBuilder().withName("Benson Meier")
            .withAttendance("ABSENT")
            .withEmail("johnd@gmail.com").withPhone("98765432")
            .withPayment("PAID")
            .withTags("NORMAL", "NoSeafood", "GUEST").build();
    public static final Person CARL = new PersonBuilder().withName("Carl Kurz").withPhone("95352563")
            .withEmail("heinz@gmail.com").withAttendance("PRESENT")
            .withPayment("NOT PAID").build();
    public static final Person DANIEL = new PersonBuilder().withName("Daniel Meier").withPhone("87652533")
            .withEmail("cornelia@gmail.com").withPayment("PAID")
            .withAttendance("ABSENT").withTags("NORMAL", "VIP").build();
    public static final Person ELLE = new PersonBuilder().withName("Elle Meyer").withPhone("9482224")
            .withEmail("werner@gmail.com").withAttendance("PRESENT").withPayment("NOT PAID").build();
    public static final Person FIONA = new PersonBuilder().withName("Fiona Kunz").withPhone("9482427")
            .withEmail("lydia@gmail.com").withAttendance("ABSENT").withPayment("PAID").build();
    public static final Person GEORGE = new PersonBuilder().withName("George Best").withPhone("9482442")
            .withEmail("anna@gmail.com").withAttendance("PRESENT").withPayment("PENDING").build();

    //Csv formatted person
    public static final String CSV_ALICE = "Alice Pauline,94351253,alice@gmail.com,PENDING,PRESENT,VEGETARIAN,NoNuts";
    public static final String CSV_BENSON = "Benson Meier,98765432,johnd@gmail.com,PAID,ABSENT,NORMAL,NoSeafood,GUEST";
    public static final String CSV_CARL = "Carl Kurz,95352563,heinz@gmail.com,NOT PAID,PRESENT";
    public static final String CSV_DANIEL = "Daniel Meier,87652533,cornelia@gmail.com,PAID,ABSENT,NORMAL,VIP";
    public static final String CSV_ELLE = "Elle Meyer,9482224,werner@gmail.com,NOT PAID,PRESENT";
    public static final String CSV_FIONA = "Fiona Kunz,9482427,lydia@gmail.com,PAID,ABSENT";
    public static final String CSV_GEORGE = "George Best,9482442,anna@gmail.com,PENDING,PRESENT";

    // Manually added
    public static final Person HOON = new PersonBuilder().withName("Hoon Meier").withPhone("8482424")
            .withEmail("stefan@gmail.com").withAttendance("PRESENT").withPayment("NOT PAID").build();
    public static final Person IDA = new PersonBuilder().withName("Ida Mueller").withPhone("8482131")
            .withEmail("hans@gmail.com").withAttendance("ABSENT").withPayment("PENDING").build();

    // Manually added - Person's details found in {@code CommandTestUtil}
    public static final Person AMY = new PersonBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
            .withEmail(VALID_EMAIL_AMY).withAttendance(VALID_ATTENDANCE_AMY).withTags(VALID_TAG_DIET_AMY)
            .withPayment(VALID_PAYMENT_AMY).build();
    public static final Person BOB = new PersonBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
            .withEmail(VALID_EMAIL_BOB).withAttendance(VALID_ATTENDANCE_BOB).withTags(VALID_TAG_DIET_BOB)
            .withPayment(VALID_PAYMENT_BOB).build();
    public static final Person DANNY = new PersonBuilder().withName(VALID_NAME_DANNY).withPhone(VALID_PHONE_DANNY)
            .withEmail(VALID_EMAIL_DANNY).withAttendance(VALID_ATTENDANCE_DANNY).withTags(VALID_TAG_FRIEND)
            .withPayment(VALID_PAYMENT_DANNY).build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalPersons() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Person person : getTypicalPersons()) {
            ab.addPerson(person);
        }
        return ab;
    }

    public static List<Person> getTypicalPersons() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
