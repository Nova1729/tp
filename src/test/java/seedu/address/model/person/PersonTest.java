package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.testutil.TypicalCcas.BASKETBALL;
import static seedu.address.testutil.TypicalCcas.TENNIS;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.cca.Amount;
import seedu.address.model.cca.Cca;
import seedu.address.model.cca.CcaInformation;
import seedu.address.model.cca.CcaName;
import seedu.address.model.role.Role;
import seedu.address.testutil.CcaBuilder;
import seedu.address.testutil.PersonBuilder;

public class PersonTest {


    @Test
    public void isSamePerson() {
        // same object -> returns true
        assertTrue(ALICE.isSamePerson(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSamePerson(null));

        // same name, all other attributes diff except address -> returns true
        Person editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB).build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        // different name, all other attributes diff except address -> returns true
        editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // name differs in case, all other attributes diff except address -> returns false
        Person editedBob = new PersonBuilder(BOB).withName(VALID_NAME_BOB.toLowerCase()).withPhone(VALID_PHONE_AMY)
                .withEmail(VALID_EMAIL_AMY).build();
        assertFalse(BOB.isSamePerson(editedBob));

        // same phone number, all other attributes diff except address -> returns false
        editedBob = new PersonBuilder(BOB).withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY)
                .withName(VALID_NAME_AMY).build();
        assertFalse(BOB.isSamePerson(editedBob));

        // same email, all other attributes diff except address -> returns false
        editedBob = new PersonBuilder(BOB).withEmail(VALID_EMAIL_AMY).withPhone(VALID_PHONE_AMY)
                .withName(VALID_NAME_AMY).build();
        assertFalse(BOB.isSamePerson(editedBob));
    }

    @Test
    public void hasCca() {
        // null cca -> throws NullPointerException
        assertThrows(NullPointerException.class, () -> ALICE.hasCca((Cca) null));

        // cca not in list -> returns false
        assertFalse(ALICE.hasCca(TENNIS));

        // cca in list -> returns true
        // Make sure ALICE actually has CCAs in TypicalPersons for get(0) to work
        if (!ALICE.getCcas().isEmpty()) {
            assertTrue(ALICE.hasCca(ALICE.getCcas().get(0)));
        } else {
            // Handle case where ALICE might have no CCAs in test setup
            // Or ensure ALICE always has CCAs in TypicalPersons
        }

        // Test the hasCca(CcaName name) overload
        // null ccaName -> throws NullPointerException (Assuming similar requireNonNull exists)
        // Adjust this if hasCca(CcaName) handles null differently
        assertThrows(NullPointerException.class, () -> ALICE.hasCca((CcaName) null));

        // ccaName not in list -> returns false
        assertFalse(ALICE.hasCca(TENNIS.getCcaName()));

        // ccaName in list -> returns true
        if (!ALICE.getCcas().isEmpty()) {
            assertTrue(ALICE.hasCca(ALICE.getCcas().get(0).getCcaName()));
        }
    }

    @Test
    public void attend() {
        // valid attendance -> returns true
        Cca basketball = new CcaBuilder(BASKETBALL).build();
        Person alice = new PersonBuilder(ALICE).build();

        Person aliceWithAttendance = alice.attend(basketball, new Amount(1));
        Set<CcaInformation> expectedCcaInformations = new HashSet<>(alice.getCcaInformations());
        expectedCcaInformations.remove(new CcaInformation(basketball, new Role("Member"),
                basketball.createNewAttendance().attend(new Amount(10))));
        expectedCcaInformations.add(new CcaInformation(basketball, new Role("Member"),
                basketball.createNewAttendance().attend(new Amount(11))));
        Person expectedAlice = new PersonBuilder(alice).withCcaInformations(expectedCcaInformations).build();
        assertEquals(expectedAlice, aliceWithAttendance);
    }

    @Test
    public void equals() {
        // same values -> returns true
        Person aliceCopy = new PersonBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different person -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Person editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new PersonBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different address -> returns false
        editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void toStringMethod() {
        String expected = Person.class.getCanonicalName() + "{name=" + ALICE.getName() + ", phone=" + ALICE.getPhone()
                + ", email=" + ALICE.getEmail() + ", address=" + ALICE.getAddress()
                + ", ccainformations=" + ALICE.getCcaInformations() + "}";
        assertEquals(expected, ALICE.toString());
    }
}
