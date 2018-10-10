package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.person.Person;

import static seedu.address.logic.commands.CommandTestUtil.*;

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
