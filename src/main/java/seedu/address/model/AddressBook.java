package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.cca.Cca;
import seedu.address.model.cca.CcaName;
import seedu.address.model.cca.UniqueCcaList;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    public static final String MESSAGE_CCA_NOT_FOUND = "At least one CCA in the student does not exist in the "
            + "address book.";
    private final UniquePersonList persons;
    private final UniqueCcaList ccas;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniquePersonList();
        ccas = new UniqueCcaList();
    }

    public AddressBook() {}

    /**
     * Creates an AddressBook using the Persons in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    /**
     * Validates that all CCAs in the given {@code person} exist in the UniqueCcaList.
     *
     * @param person the person to validate
     * @return true if all CCAs in the person exist in the UniqueCcaList, false otherwise
     */
    public boolean isValidPersonCcas(Person person) {
        requireNonNull(person);
        for (Cca cca : person.getCcas()) {
            if (!ccas.contains(cca)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Validates that all CCAs in the given {@code person} exist in the UniqueCcaList.
     *
     * @throws IllegalArgumentException if any CCA in the person does not exist in the UniqueCcaList.
     */
    private void validatePersonCcas(Person person) throws IllegalArgumentException {
        if (!isValidPersonCcas(person)) {
            throw new IllegalArgumentException(MESSAGE_CCA_NOT_FOUND);
        }
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the cca list with {@code ccas}.
     * {@code ccas} must not contain duplicate ccas.
     */
    public void setCcas(List<Cca> ccas) {
        this.ccas.setCcas(ccas);
    }

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        for (Person person : persons) {
            validatePersonCcas(person);
        }
        this.persons.setPersons(persons);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);

        setCcas(newData.getCcaList());
        setPersons(newData.getPersonList());
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     * It ignores if the person is the same as {@code otherPerson}.
     */
    public boolean hasPersonExcept(Person person, Person otherPerson) {
        requireNonNull(person);
        requireNonNull(otherPerson);
        return persons.containsExcept(person, otherPerson);
    }

    /**
     * Adds a person to the address book.
     * The person must not already exist in the address book.
     */
    public void addPerson(Person p) {
        validatePersonCcas(p);
        persons.add(p);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);
        validatePersonCcas(editedPerson);
        persons.setPerson(target, editedPerson);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removePerson(Person key) {
        persons.remove(key);
    }

    //// cca-level operations

    /**
     * Returns the Cca with the same name as {@code ccaName} in the address book.
     */
    public Cca getCca(CcaName ccaName) {
        requireNonNull(ccaName);
        return ccas.getCca(ccaName);
    }

    /**
     * Returns true if a cca with the same identity as {@code cca} exists in the address book.
     */
    public boolean hasCca(Cca cca) {
        requireNonNull(cca);
        return ccas.contains(cca);
    }

    /**
     * Returns true if a cca with the same name as {@code ccaName} exists in the address book.
     */
    public boolean hasCca(CcaName ccaName) {
        requireNonNull(ccaName);
        return ccas.contains(ccaName);
    }

    /**
     * Adds a cca to the address book.
     * The cca must not already exist in the address book.
     */
    public void addCca(Cca cca) {
        ccas.add(cca);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removeCca(Cca key) {
        ccas.remove(key);
    }

    /**
     * Replaces the given cca {@code target} in the list with {@code editedCca}.
     * {@code target} must exist in the address book.
     * The cca identity of {@code editedCca} must not be the same as another existing cca in the address book.
     */
    public void setCca(Cca target, Cca editedCca) {
        requireNonNull(editedCca);
        ccas.setCca(target, editedCca);
    }

    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("persons", persons)
                .toString();
    }

    @Override
    public ObservableList<Cca> getCcaList() {
        return ccas.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddressBook)) {
            return false;
        }

        AddressBook otherAddressBook = (AddressBook) other;
        return persons.equals(otherAddressBook.persons);
    }

    @Override
    public int hashCode() {
        return persons.hashCode();
    }
}
