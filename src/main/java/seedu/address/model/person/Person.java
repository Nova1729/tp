package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.cca.Amount;
import seedu.address.model.cca.Attendance;
import seedu.address.model.cca.Cca;
import seedu.address.model.cca.CcaInformation;
import seedu.address.model.cca.CcaName;
import seedu.address.model.cca.SessionCount;
import seedu.address.model.cca.exceptions.CcaNotFoundException;
import seedu.address.model.cca.exceptions.DuplicateCcaException;
import seedu.address.model.role.Role;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final Set<CcaInformation> ccaInformations;

    /**
     * Constructs a {@code Person}.
     * Ensures all fields are non-null and initializes with a defensive copy of the provided set.
     *
     * @param name The person's name.
     * @param phone The person's phone number.
     * @param email The person's email address.
     * @param address The person's address.
     * @param ccaInformations The set of CCA-related information associated with the person.
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<CcaInformation> ccaInformations) {
        requireAllNonNull(name, phone, email, address, ccaInformations);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.ccaInformations = new HashSet<>(ccaInformations);
    }

    /**
     * Returns the person's name.
     *
     * @return A {@code Name} object representing the person's name.
     */
    public Name getName() {
        return name;
    }

    /**
     * Returns the person's phone number.
     *
     * @return A {@code Phone} object representing the person's phone number.
     */
    public Phone getPhone() {
        return phone;
    }

    /**
     * Returns the person's email address.
     *
     * @return An {@code Email} object representing the person's email address.
     */
    public Email getEmail() {
        return email;
    }

    /**
     * Returns the person's address.
     *
     * @return An {@code Address} object representing the person's address.
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Returns the CCA information associated with the specified CCA.
     * {@code cca} must exist in the person's CCA information.
     *
     * @param cca The CCA to retrieve information for.
     * @return The {@code CcaInformation} object associated with the specified CCA.
     * @throws CcaNotFoundException If the specified CCA is not found in the person's CCA information.
     */
    public CcaInformation getCcaInformation(Cca cca) {
        for (CcaInformation ccaInformation : ccaInformations) {
            if (ccaInformation.getCca().equals(cca)) {
                return ccaInformation;
            }
        }
        throw new CcaNotFoundException();
    }

    /**
     * Returns an unmodifiable view of the person's CCA information.
     * Prevents external modifications to maintain immutability.
     *
     * @return An unmodifiable {@code Set<CcaInformation>} associated with the person.
     */
    public Set<CcaInformation> getCcaInformations() {
        return Collections.unmodifiableSet(ccaInformations);
    }

    /**
     * Returns a list of CCAs associated with this person.
     * Extracts the Cca objects from the CcaInformation set.
     *
     * @return An unmodifiable list of Cca objects.
     */
    public List<Cca> getCcas() {
        List<Cca> ccas = new ArrayList<>();
        for (CcaInformation ccaInformation : ccaInformations) {
            ccas.add(ccaInformation.getCca());
        }
        return Collections.unmodifiableList(ccas);
    }

    /**
     * Returns the role of the person in the specified CCA.
     *
     * @param cca The CCA to retrieve the role for.
     * @return The {@code Role} object associated with the specified CCA.
     */
    public Role getRole(Cca cca) {
        CcaInformation ccaInformation = getCcaInformation(cca);
        return ccaInformation.getRole();
    }

    /**
     * Creates and returns a new Person object with the given CCA added.
     * It is the caller's responsibility to ensure the added CCA is not a duplicate
     * based on the application's definition (e.g., using hasCca before calling this).
     *
     * @param cca The CCA to add.
     * @return A new Person object instance with the specified CCA information added.
     * @throws DuplicateCcaException if CCA is already present in the person.
     */
    public Person addCca(Cca cca) {
        requireNonNull(cca);
        Attendance newAttendance = cca.createNewAttendance();

        if (hasCca(cca)) {
            throw new DuplicateCcaException();
        }

        CcaInformation newCcaInformation = new CcaInformation(cca, Role.DEFAULT_ROLE, newAttendance);
        Set<CcaInformation> newCcaInformations = new HashSet<>(ccaInformations);
        newCcaInformations.add(newCcaInformation);
        return new Person(this.name, this.phone, this.email, this.address, newCcaInformations);
    }

    /**
     * Removes the specified CCA from the person's CCA information based on CCA name identity.
     * If multiple entries exist for the same CCA name (due to past errors), this will remove all of them.
     *
     * @param cca The CCA to remove.
     * @return A new Person object with the specified CCA Information removed.
     * @throws CcaNotFoundException If the person is not enrolled in the CCA.
     */
    public Person removeCca(Cca cca) {
        requireNonNull(cca);
        Set<CcaInformation> newCcaInformations = new HashSet<>(ccaInformations);

        if (!hasCca(cca)) {
            throw new CcaNotFoundException();
        }

        for (CcaInformation currentInfo : this.ccaInformations) {
            if (currentInfo.isSameCca(cca)) {
                newCcaInformations.remove(currentInfo);
            }
        }

        return new Person(name, phone, email, address, newCcaInformations);
    }

    /**
     * Checks if the person can attend a specified number of sessions in the CCA.
     *
     * @param cca The CCA to check attendance for.
     * @param amount The number of sessions to check.
     * @return {@code true} if the person can attend the specified number of sessions, otherwise {@code false}.
     */
    public boolean canAttend(Cca cca, Amount amount) {
        if (!hasCca(cca)) {
            return false;
        }
        CcaInformation ccaInformation = getCcaInformation(cca);
        return ccaInformation.canAttend(amount);
    }

    /**
     * Updates the attendance record of the person for the specified CCA.
     *
     * @param cca The CCA to update attendance for.
     * @param amount The amount to add to the person's attendance record.
     * @return A new Person object with the updated attendance record.
     * @throws CcaNotFoundException If the person is not enrolled in the CCA.
     */
    public Person attend(Cca cca, Amount amount) {
        if (!hasCca(cca)) {
            throw new CcaNotFoundException();
        }
        CcaInformation ccaInformation = getCcaInformation(cca);
        CcaInformation newCcaInformation = ccaInformation.attend(amount);
        Set<CcaInformation> newCcaInformations = new HashSet<>(ccaInformations);
        newCcaInformations.remove(ccaInformation);
        newCcaInformations.add(newCcaInformation);
        return new Person(name, phone, email, address, newCcaInformations);
    }

    /**
     * Updates the specified CCA information in the person's record with the edited CCA details.
     * This includes updating the total number of sessions while preserving the existing role and sessions attended.
     * If the role in the edited CCA does not match the current role, it will default to a predefined role.
     *
     * @param target The original CCA to be updated. This contains the CCA's current details in the person's record.
     * @param editedCca The edited CCA containing the new details, including the total number of sessions.
     * @return A new {@code Person} object with the updated CCA information.
     */
    public Person updateCca(Cca target, Cca editedCca) {
        CcaInformation targetCcaInformation = getCcaInformation(target);

        Role currentRole = targetCcaInformation.getRole();
        if (!editedCca.hasRole(currentRole)) {
            currentRole = Role.DEFAULT_ROLE;
        }

        Attendance attendanceToUpdate = targetCcaInformation.getAttendance();
        SessionCount newTotalSessions = editedCca.getTotalSessions();

        Attendance newAttendance = attendanceToUpdate.updateTotalSessions(newTotalSessions);

        CcaInformation newCcaInformation = new CcaInformation(editedCca, currentRole, newAttendance);
        Set<CcaInformation> newCcaInformations = new HashSet<>(ccaInformations);
        newCcaInformations.remove(targetCcaInformation);
        newCcaInformations.add(newCcaInformation);
        return new Person(name, phone, email, address, newCcaInformations);
    }

    /**
     * Adds the specified role to the person for the specified CCA name.
     * The current {@code role} of the person in the CCA must be the default role.
     * {@code role} must be a valid role defined in the CCA and not the default role.
     * {@code cca} must exist in the person's CCA information.
     *
     * @param cca The CCA name to add role for.
     * @param role The role to add.
     * @return A new {@code Person} object with the specified CCA Information added with a role.
     */
    public Person addRole(Cca cca, Role role) {
        CcaInformation oldCcaInformation = getCcaInformation(cca);

        CcaInformation newCcaInformation = oldCcaInformation.addRole(role);

        // Replace old ccaInformation with new ccaInformation.
        Set<CcaInformation> newCcaInformations = ccaInformations;
        newCcaInformations.remove(oldCcaInformation);
        newCcaInformations.add(newCcaInformation);

        return new Person(name, phone, email, address, newCcaInformations);
    }

    /**
     * Removes the role of the person for the specified CCA.
     * The current {@code role} of the person in the CCA must not be the default role.
     * {@code cca} must exist in the person's CCA information.
     *
     * @param cca The CCA to remove role for.
     * @return A new {@code Person} object with the specified CCA Information deleted with a role.
     */
    public Person removeRole(Cca cca) {
        CcaInformation oldCcaInformation = getCcaInformation(cca);

        CcaInformation newCcaInformation = oldCcaInformation.removeRole();

        // Replace old ccaInformation with new ccaInformation.
        Set<CcaInformation> newCcaInformations = ccaInformations;
        newCcaInformations.remove(oldCcaInformation);
        newCcaInformations.add(newCcaInformation);

        return new Person(name, phone, email, address, newCcaInformations);
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     *
     * @param otherPerson The person to compare against.
     * @return {@code true} if both persons have the same name, otherwise {@code false}.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && (otherPerson.getName().equals(getName())
                        || otherPerson.getPhone().equals(getPhone())
                        || otherPerson.getEmail().equals(getEmail()));
    }

    /**
     * Returns true if the person has the default role in the specified CCA.
     *
     * @param cca The CCA to check for default role.
     * @return {@code true} if the person has the default role in the specified CCA, otherwise {@code false}.
     */
    public boolean isDefaultRoleInCca(Cca cca) {
        CcaInformation ccaInformation = getCcaInformation(cca);
        return ccaInformation.isDefaultRole();
    }

    /**
     * Returns true if the person has the specified CCA, checking by CCA identity (name only).
     * This defines a looser check suitable for enrollment status.
     *
     * @param cca The CCA to check for.
     * @return {@code true} if the person is enrolled in a CCA with the same name, otherwise {@code false}.
     */
    public boolean hasCca(Cca cca) {
        requireNonNull(cca);
        for (Cca c : getCcas()) { // getCcas() extracts Cca from CcaInformation
            // Use isSameCca (checks name only) instead of equals (checks name, roles, sessions)
            if (c.isSameCca(cca)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if the person has the specified CCA.
     *
     * @param ccaName The CCA name to check for.
     * @return {@code true} if the person has the specified CCA, otherwise {@code false}.
     */
    public boolean hasCca(CcaName ccaName) {
        requireNonNull(ccaName);
        for (Cca c : getCcas()) {
            if (c.getCcaName().equals(ccaName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if two persons are equal based on all identity and data fields.
     * Defines a stronger notion of equality.
     *
     * @param other The object to compare against.
     * @return {@code true} if both persons have the same details, otherwise {@code false}.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && address.equals(otherPerson.address)
                && ccaInformations.equals(otherPerson.ccaInformations);
    }

    /**
     * Generates a hash code for this person based on all identity and data fields.
     *
     * @return A hash code value for this person.
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, phone, email, address, ccaInformations);
    }

    /**
     * Returns a string representation of the person.
     *
     * @return A formatted string containing the person's details.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("ccainformations", ccaInformations)
                .toString();
    }

}
