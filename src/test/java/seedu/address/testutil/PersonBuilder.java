package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.person.Attendance;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Payment;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Alice Blah";
    public static final String DEFAULT_PHONE = "85455255";
    public static final String DEFAULT_EMAIL = "aliceblah@gmail.com";
    public static final String DEFAULT_PAYMENT = "PAID";
    public static final String DEFAULT_ATTENDANCE = "ABSENT";

    private Name name;
    private Phone phone;
    private Email email;
    private Payment payment;
    private Attendance attendance;
    private Set<Tag> tags;

    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        payment = new Payment(DEFAULT_PAYMENT);
        email = new Email(DEFAULT_EMAIL);
        attendance = new Attendance(DEFAULT_ATTENDANCE);
        tags = new HashSet<>();
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        name = personToCopy.getName();
        phone = personToCopy.getPhone();
        payment = personToCopy.getPayment();
        email = personToCopy.getEmail();
        attendance = personToCopy.getAttendance();
        tags = new HashSet<>(personToCopy.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Attendance} of the {@code Person} that we are building.
     */
    public PersonBuilder withAttendance(String attendance) {
        this.attendance = new Attendance(attendance);
        return this;
    }

    /**
     * Sets the {@code Payment} of the {@code Person} that we are building.
     */
    public PersonBuilder withPayment(String payment) {
        this.payment = new Payment(payment);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    public Person build() {
        return new Person(name, phone, email, payment, attendance, tags);
    }


}
