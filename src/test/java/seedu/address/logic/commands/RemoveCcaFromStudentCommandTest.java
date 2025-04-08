package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalCcas.CCA_NAME_BASKETBALL;
import static seedu.address.testutil.TypicalCcas.CCA_NAME_TENNIS;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Set;
import java.util.stream.Collectors;

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
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for RemoveCcaFromStudentCommand.
 */
public class RemoveCcaFromStudentCommandTest {

    private Model model;
    private Model expectedModel;
    private Person personToEdit; // Person who will have CCA removed

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        // Assuming the first person (index 1) has Basketball initially. Adjust if needed.
        personToEdit = expectedModel.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
    }

    @Test
    public void execute_validInputUnfilteredList_success() {
        // Build the expected person: copy original person and remove Basketball CCA info
        Set<CcaInformation> originalCcas = personToEdit.getCcaInformations();
        Set<CcaInformation> expectedCcas = originalCcas.stream()
                .filter(info -> !info.getCca().getCcaName().equals(CCA_NAME_BASKETBALL))
                .collect(Collectors.toSet());
        Person expectedPerson = new PersonBuilder(personToEdit).withCcaInformations(expectedCcas).build();

        // Update the expected model
        expectedModel.setPerson(personToEdit, expectedPerson);

        // Create and execute the command
        RemoveCcaFromStudentCommand command = new RemoveCcaFromStudentCommand(INDEX_FIRST_PERSON, CCA_NAME_BASKETBALL);
        String expectedMessage = String.format(RemoveCcaFromStudentCommand.MESSAGE_REMOVE_CCA_SUCCESS,
                Messages.format(expectedPerson.getName()), Messages.format(CCA_NAME_BASKETBALL));

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validInputFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON); // Filter list to show only the person to edit
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON); // Filter list to show only the person to edit

        // Build the expected person and model as in the unfiltered test
        Set<CcaInformation> originalCcas = personToEdit.getCcaInformations();
        Set<CcaInformation> expectedCcas = originalCcas.stream()
                .filter(info -> !info.getCca().getCcaName().equals(CCA_NAME_BASKETBALL))
                .collect(Collectors.toSet());
        Person expectedPerson = new PersonBuilder(personToEdit).withCcaInformations(expectedCcas).build();
        expectedModel.setPerson(personToEdit, expectedPerson); // Set person in expected model

        // Create and execute the command
        RemoveCcaFromStudentCommand command = new RemoveCcaFromStudentCommand(INDEX_FIRST_PERSON, CCA_NAME_BASKETBALL);
        String expectedMessage = String.format(RemoveCcaFromStudentCommand.MESSAGE_REMOVE_CCA_SUCCESS,
                Messages.format(expectedPerson.getName()), Messages.format(CCA_NAME_BASKETBALL));

        // model will have filter reset, expectedModel does not need filter reset for comparison
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_ccaNotInPersonUnfilteredList_failure() {
        // Assuming the first person does not have Tennis
        RemoveCcaFromStudentCommand command = new RemoveCcaFromStudentCommand(INDEX_FIRST_PERSON, CCA_NAME_TENNIS);
        assertCommandFailure(command, model, Messages.MESSAGE_CCA_NOT_IN_PERSON);
    }

    @Test
    public void execute_ccaNotInPersonFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);

        // Assuming the first person does not have Tennis
        RemoveCcaFromStudentCommand command = new RemoveCcaFromStudentCommand(INDEX_FIRST_PERSON, CCA_NAME_TENNIS);
        assertCommandFailure(command, model, Messages.MESSAGE_CCA_NOT_IN_PERSON);
    }


    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        // Use a CCA name that exists in the model but apply to invalid person index
        RemoveCcaFromStudentCommand command = new RemoveCcaFromStudentCommand(outOfBoundIndex, CCA_NAME_BASKETBALL);
        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON; // Index 2 is out of bounds for filtered list of size 1
        RemoveCcaFromStudentCommand command = new RemoveCcaFromStudentCommand(outOfBoundIndex, CCA_NAME_BASKETBALL);
        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_ccaNotFoundInModel_failure() {
        // Assume "Unknown CCA" does not exist in TypicalCcas/AddressBook
        CcaName nonExistentCcaName = new CcaName("Unknown CCA");
        RemoveCcaFromStudentCommand command = new RemoveCcaFromStudentCommand(INDEX_FIRST_PERSON, nonExistentCcaName);
        // This fails because the CCA itself isn't found in the model's CCA list
        assertCommandFailure(command, model, Messages.MESSAGE_CCA_NOT_FOUND);
    }

    @Test
    public void equals() {
        final RemoveCcaFromStudentCommand standardCommand = new RemoveCcaFromStudentCommand(INDEX_FIRST_PERSON,
                CCA_NAME_BASKETBALL);

        // same values -> returns true
        RemoveCcaFromStudentCommand commandWithSameValues = new RemoveCcaFromStudentCommand(INDEX_FIRST_PERSON,
                CCA_NAME_BASKETBALL);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new RemoveCcaFromStudentCommand(INDEX_SECOND_PERSON, CCA_NAME_BASKETBALL)));

        // different cca name -> returns false
        assertFalse(standardCommand.equals(new RemoveCcaFromStudentCommand(INDEX_FIRST_PERSON, CCA_NAME_TENNIS)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        CcaName ccaName = CCA_NAME_BASKETBALL;
        RemoveCcaFromStudentCommand command = new RemoveCcaFromStudentCommand(index, ccaName);
        String expected = RemoveCcaFromStudentCommand.class.getCanonicalName() + "{studentIndex=" + index
                + ", ccaName=" + ccaName + "}";
        assertEquals(expected, command.toString());
    }
}
