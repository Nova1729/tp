package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalCcas.CCA_NAME_BASKETBALL;
import static seedu.address.testutil.TypicalCcas.CCA_NAME_TENNIS;
import static seedu.address.testutil.TypicalCcas.TENNIS;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.cca.CcaInformation;
import seedu.address.model.cca.CcaName;
import seedu.address.model.person.Person;
import seedu.address.model.role.Role;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for AddCcaToStudentCommand.
 */
public class AddCcaToStudentCommandTest {

    private Model model;
    private Model expectedModel;
    private Person personToEdit; // Person who will have CCA added

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        // Assuming the second person (index 2) does NOT have Tennis initially
        // and the Tennis CCA exists in the model. Adjust index if needed.
        personToEdit = expectedModel.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
    }

    @Test
    public void execute_validInputUnfilteredList_success() {
        // Build the expected person: copy original person and add Tennis CCA info
        Set<CcaInformation> expectedCcas = new HashSet<>(personToEdit.getCcaInformations());
        CcaInformation tennisInfo = new CcaInformation(TENNIS, new Role(Role.DEFAULT_ROLE_NAME),
                TENNIS.createNewAttendance());
        expectedCcas.add(tennisInfo);
        Person expectedPerson = new PersonBuilder(personToEdit).withCcaInformations(expectedCcas).build();

        // Update the expected model
        expectedModel.setPerson(personToEdit, expectedPerson);

        // Create and execute the command
        AddCcaToStudentCommand command = new AddCcaToStudentCommand(INDEX_SECOND_PERSON, CCA_NAME_TENNIS);
        String expectedMessage = String.format(AddCcaToStudentCommand.MESSAGE_ADD_CCA_SUCCESS,
                Messages.format(expectedPerson.getName()), Messages.format(CCA_NAME_TENNIS));

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validInputFilteredList_success() {
        // personToEdit is correctly identified based on INDEX_SECOND_PERSON in the original list (in setUp)
        showPersonAtIndex(model, INDEX_SECOND_PERSON); // Filter list to show only this person
        showPersonAtIndex(expectedModel, INDEX_SECOND_PERSON); // Filter list to show only this person

        // Build the expected person and model as before
        Set<CcaInformation> expectedCcas = new HashSet<>(personToEdit.getCcaInformations());
        CcaInformation tennisInfo = new CcaInformation(TENNIS, new Role(Role.DEFAULT_ROLE_NAME),
                TENNIS.createNewAttendance());
        expectedCcas.add(tennisInfo);
        Person expectedPerson = new PersonBuilder(personToEdit).withCcaInformations(expectedCcas).build();
        expectedModel.setPerson(personToEdit, expectedPerson); // Set person in expected model

        // Create and execute the command targeting INDEX_FIRST_PERSON within the *filtered* list
        AddCcaToStudentCommand command = new AddCcaToStudentCommand(INDEX_FIRST_PERSON, CCA_NAME_TENNIS);
        String expectedMessage = String.format(AddCcaToStudentCommand.MESSAGE_ADD_CCA_SUCCESS,
                Messages.format(expectedPerson.getName()), Messages.format(CCA_NAME_TENNIS));

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateCcaUnfilteredList_failure() {
        // Assuming the first person already has Basketball
        AddCcaToStudentCommand command = new AddCcaToStudentCommand(INDEX_FIRST_PERSON, CCA_NAME_BASKETBALL);
        assertCommandFailure(command, model, AddCcaToStudentCommand.MESSAGE_CCA_ALREADY_PRESENT);
    }

    @Test
    public void execute_duplicateCcaFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        // Assuming the first person already has Basketball
        AddCcaToStudentCommand command = new AddCcaToStudentCommand(INDEX_FIRST_PERSON, CCA_NAME_BASKETBALL);
        assertCommandFailure(command, model, AddCcaToStudentCommand.MESSAGE_CCA_ALREADY_PRESENT);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        AddCcaToStudentCommand command = new AddCcaToStudentCommand(outOfBoundIndex, CCA_NAME_TENNIS);
        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON; // Index 2 is out of bounds for filtered list of size 1
        AddCcaToStudentCommand command = new AddCcaToStudentCommand(outOfBoundIndex, CCA_NAME_TENNIS);
        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_ccaNotFoundInModel_failure() {
        // Assume "Unknown CCA" does not exist in TypicalCcas/AddressBook
        CcaName nonExistentCcaName = new CcaName("Unknown CCA");
        AddCcaToStudentCommand command = new AddCcaToStudentCommand(INDEX_FIRST_PERSON, nonExistentCcaName);
        assertCommandFailure(command, model, Messages.MESSAGE_CCA_NOT_FOUND);
    }

    @Test
    public void equals() {
        final AddCcaToStudentCommand standardCommand = new AddCcaToStudentCommand(INDEX_FIRST_PERSON,
                CCA_NAME_BASKETBALL);

        // same values -> returns true
        AddCcaToStudentCommand commandWithSameValues = new AddCcaToStudentCommand(INDEX_FIRST_PERSON,
                CCA_NAME_BASKETBALL);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new AddCcaToStudentCommand(INDEX_SECOND_PERSON, CCA_NAME_BASKETBALL)));

        // different cca name -> returns false
        assertFalse(standardCommand.equals(new AddCcaToStudentCommand(INDEX_FIRST_PERSON, CCA_NAME_TENNIS)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        CcaName ccaName = CCA_NAME_BASKETBALL;
        AddCcaToStudentCommand command = new AddCcaToStudentCommand(index, ccaName);
        String expected = AddCcaToStudentCommand.class.getCanonicalName() + "{studentIndex=" + index
                + ", ccaName=" + ccaName + "}";
        assertEquals(expected, command.toString());
    }
}

