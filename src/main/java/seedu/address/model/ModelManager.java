package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.cca.Cca;
import seedu.address.model.cca.CcaName;
import seedu.address.model.person.Person;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return addressBook.hasPerson(person);
    }

    @Override
    public boolean hasPersonExcept(Person person, Person otherPerson) {
        requireAllNonNull(person, otherPerson);
        return addressBook.hasPersonExcept(person, otherPerson);
    }

    @Override
    public boolean isValidPersonCcas(Person person) {
        requireNonNull(person);
        return addressBook.isValidPersonCcas(person);
    }

    @Override
    public void deletePerson(Person target) {
        addressBook.removePerson(target);
    }

    @Override
    public void addPerson(Person person) {
        addressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        addressBook.setPerson(target, editedPerson);
    }

    @Override
    public boolean hasCca(Cca cca) {
        requireNonNull(cca);
        return addressBook.hasCca(cca);
    }

    @Override
    public boolean hasCca(CcaName ccaName) {
        requireNonNull(ccaName);
        return addressBook.hasCca(ccaName);
    }

    @Override
    public void addCca(Cca cca) {
        addressBook.addCca(cca);
    }

    @Override
    public void deleteCca(Cca target) {
        requireNonNull(target);
        addressBook.removeCca(target);
        removeCcaFromAllStudents(target);
    }

    private void removeCcaFromAllStudents(Cca cca) {
        for (Person person : addressBook.getPersonList()) {
            if (person.hasCca(cca.getCcaName())) {
                Person newPerson = person.removeCca(cca);
                addressBook.setPerson(person, newPerson);
            }
        }
    }

    @Override
    public Cca getCca(CcaName ccaName) {
        requireNonNull(ccaName);
        return addressBook.getCca(ccaName);
    }

    @Override
    public void setCca(Cca target, Cca editedCca) {
        requireAllNonNull(target, editedCca);
        addressBook.setCca(target, editedCca);

        // Checks if existing persons in the addressbook possess the cca to be replaced, thus replacing
        // their corresponding cca with the new cca.
        for (Person person : this.addressBook.getPersonList()) {
            if (person.hasCca(target)) {
                Person newPerson = person.updateCca(target, editedCca);
                addressBook.setPerson(person, newPerson);
            }
        }
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Cca} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Cca> getCcaList() {
        return addressBook.getCcaList();
    }

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return addressBook.equals(otherModelManager.addressBook)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredPersons.equals(otherModelManager.filteredPersons);
    }

}
