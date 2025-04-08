package seedu.address.model;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.cca.Cca;
import seedu.address.model.cca.CcaName;
import seedu.address.model.person.Person;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    boolean hasPerson(Person person);

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     * It ignores if the person is the same as {@code otherPerson}.
     */
    boolean hasPersonExcept(Person person, Person otherPerson);

    /**
     * Returns true if all the CCAs of the given person are valid.
     * The person must exist in the address book.
     */
    boolean isValidPersonCcas(Person person);

    /**
     * Deletes the given person.
     * The person must exist in the address book.
     */
    void deletePerson(Person target);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the address book.
     */
    void addPerson(Person person);

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    void setPerson(Person target, Person editedPerson);

    /**
     * Returns true if a cca with the same identity as {@code cca} exists in the address book.
     */
    boolean hasCca(Cca cca);

    /**
     * Returns true if a cca with the same name as {@code ccaName} exists in the address book.
     */
    boolean hasCca(CcaName ccaName);

    /**
     * Adds the given cca.
     * {@code cca} must not already exist in the address book.
     */
    void addCca(Cca cca);

    /**
     * Deletes the given cca.
     * The cca must exist in the address book.
     */
    void deleteCca(Cca target);

    /**
     * Gets the cca with the same name as {@code ccaName} in the address book.
     * The cca must exist in the address book.
     */
    Cca getCca(CcaName ccaName);

    /**
     * Replaces the given cca {@code target} with {@code editedCca}.
     * {@code target} must exist in the address book.
     * The cca identity of {@code editedCca} must not be the same as another existing cca in the address book.
     */
    void setCca(Cca target, Cca editedCca);

    /** Returns an unmodifiable view of the cca list */
    ObservableList<Cca> getCcaList();

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);
}
