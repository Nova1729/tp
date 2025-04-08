package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditStudentCommand.EditPersonDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditStudentCommand.
 */
public class EditStudentCommandTest {

    // Assume getTypicalAddressBook() provides persons, some possibly with CCAs.
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());
        Person lastPerson = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());

        // Start building based on the last person (copies all fields including CCAs)
        PersonBuilder personInList = new PersonBuilder(lastPerson);
        // Edit only Name and Phone, other fields (Email, Address, CCAs) remain the same
        Person editedPerson = personInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB).build();

        // Descriptor only includes Name and Phone
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).build();
        EditStudentCommand editStudentCommand = new EditStudentCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(
                EditStudentCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(lastPerson, editedPerson);

        assertCommandSuccess(editStudentCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        // Create command with an empty descriptor
        EditStudentCommand editStudentCommand = new EditStudentCommand(INDEX_FIRST_PERSON, new EditPersonDescriptor());
        // The person being "edited" is the original person, as no changes should occur
        Person personBeforeEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        // Expected message should format the original person details
        String expectedMessage = String.format(
                EditStudentCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(personBeforeEdit));

        // Expected model is identical to the original model before the command execution
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        // The command should succeed but result in no change to the model state
        assertCommandSuccess(editStudentCommand, model, expectedMessage, expectedModel);
        // Verify the person object in the list is still the same one
        assertEquals(personBeforeEdit, model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()));
    }


    @Test
    public void execute_duplicatePersonUnfilteredList_failure() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        // Create a descriptor matching the first person's identifying details (usually Name)
        // Assumes EditPersonDescriptorBuilder(person) extracts only Name,Phone,Email,Address
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(firstPerson).build();
        // Try to edit the second person to have the same identity as the first person
        EditStudentCommand editStudentCommand = new EditStudentCommand(INDEX_SECOND_PERSON, descriptor);

        assertCommandFailure(editStudentCommand, model, Messages.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePersonFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON); // Filter list to show only the first person

        // Get details of the second person from the *original* address book
        Person personInAddressBook = model.getAddressBook().getPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        // Create a descriptor matching the second person's identifying details
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(personInAddressBook).build();
        // Try to edit the first person (currently the only one in the filtered list)
        // to have the same identity as the second person
        EditStudentCommand editStudentCommand = new EditStudentCommand(INDEX_FIRST_PERSON, descriptor);

        assertCommandFailure(editStudentCommand, model, Messages.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        // Create an index that is one greater than the size of the list
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        // Descriptor with some valid field
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditStudentCommand editStudentCommand = new EditStudentCommand(outOfBoundIndex, descriptor);

        // Expect failure due to invalid index
        assertCommandFailure(editStudentCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book.
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON); // Filter list to show only one person
        Index outOfBoundIndex = INDEX_SECOND_PERSON; // Index 2 (0-based 1) is now out of bounds for the filtered list

        // Ensure that outOfBoundIndex is still within the bounds of the main address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        // Create the command with the out-of-bounds index for the filtered list
        EditStudentCommand editStudentCommand = new EditStudentCommand(outOfBoundIndex,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        // Expect failure due to invalid index within the context of the filtered list
        assertCommandFailure(editStudentCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    // TEST REMOVED: execute_nonExistentCcaName_failure() was here but is no longer valid for EditStudentCommand.

    @Test
    public void equals() {
        // Use the original DESC_AMY and DESC_BOB constants from CommandTestUtil
        // They are suitable as EditPersonDescriptor no longer holds CCAs.
        final EditStudentCommand standardCommand = new EditStudentCommand(INDEX_FIRST_PERSON, DESC_AMY);

        // same values -> returns true
        EditPersonDescriptor copyDescriptor = new EditPersonDescriptor(DESC_AMY);
        EditStudentCommand commandWithSameValues = new EditStudentCommand(INDEX_FIRST_PERSON, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditStudentCommand(INDEX_SECOND_PERSON, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditStudentCommand(INDEX_FIRST_PERSON, DESC_BOB)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        EditStudentCommand editStudentCommand = new EditStudentCommand(index, editPersonDescriptor);

        String expected = EditStudentCommand.class.getCanonicalName() + "{index=" + index + ", editPersonDescriptor="
                + editPersonDescriptor + "}";
        assertEquals(expected, editStudentCommand.toString());
    }
}
