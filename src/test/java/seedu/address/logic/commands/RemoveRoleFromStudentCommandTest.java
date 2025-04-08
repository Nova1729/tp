package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CCA_NAME_ACTING;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CCA_NAME_BASKETBALL;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CCA_NAME_SWIMMING;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_VICE_PRESIDENT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalCcas.ACTING;
import static seedu.address.testutil.TypicalCcas.BASKETBALL;
import static seedu.address.testutil.TypicalCcas.SWIMMING;
import static seedu.address.testutil.TypicalPersons.ALICE;

import java.util.ArrayList;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.cca.Cca;
import seedu.address.model.cca.CcaName;
import seedu.address.model.person.Person;
import seedu.address.model.role.Role;
import seedu.address.testutil.CcaBuilder;
import seedu.address.testutil.ModelStub;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for RemoveCcaFromStudentCommand.
 */
public class RemoveRoleFromStudentCommandTest {
    @Test
    public void execute_validPersonRole_success() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();

        Cca validCca = new CcaBuilder(ACTING).build(); // has a valid role Vice-President
        // has a valid CcaInformation Acting with a role Vice-President
        Person validPerson = new PersonBuilder(ALICE).build();
        Role validRole = new Role(VALID_ROLE_VICE_PRESIDENT); // a role to be removed

        modelStub.addCca(validCca);
        modelStub.addPerson(validPerson);

        Index studentIndex = Index.fromOneBased(1);
        CcaName ccaName = new CcaName(VALID_CCA_NAME_ACTING);

        RemoveRoleFromStudentCommand command = new RemoveRoleFromStudentCommand(studentIndex, ccaName);
        CommandResult commandResult = command.execute(modelStub);

        assertEquals(String.format(RemoveRoleFromStudentCommand.MESSAGE_REMOVE_ROLE_FROM_STUDENT_SUCCESS,
                Messages.format(validPerson.getName()), Messages.format(validRole),
                Messages.format(validCca.getCcaName())), commandResult.getFeedbackToUser());
    }

    @Test
    public void execute_invalidStudentIndex_throwsCommandException() {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();

        Cca validCca = new CcaBuilder(ACTING).build(); // has a valid role Vice-President
        // has a valid CcaInformation acting with a role Vice-President
        Person validPerson = new PersonBuilder(ALICE).build();

        modelStub.addCca(validCca);
        modelStub.addPerson(validPerson);

        Index outOfBoundIndex = Index.fromOneBased(2);
        CcaName ccaName = new CcaName(VALID_CCA_NAME_ACTING);

        RemoveRoleFromStudentCommand command = new RemoveRoleFromStudentCommand(outOfBoundIndex, ccaName);
        assertThrows(CommandException.class, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, (
        ) -> command.execute(modelStub));
    }

    @Test
    public void execute_ccaNotFound_throwsCommandException() {

        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();

        Person validPerson = new PersonBuilder(ALICE).build();
        modelStub.addPerson(validPerson);
        // Model does not have the CCA swimming.

        Index studentIndex = Index.fromOneBased(1);
        CcaName ccaName = new CcaName(VALID_CCA_NAME_SWIMMING);

        RemoveRoleFromStudentCommand command = new RemoveRoleFromStudentCommand(studentIndex, ccaName);
        assertThrows(CommandException.class, Messages.MESSAGE_CCA_NOT_FOUND, (
        ) -> command.execute(modelStub));
    }

    @Test
    public void execute_ccaNotInPerson_throwsCommandException() {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();

        Cca validCca = new CcaBuilder(SWIMMING).build(); // present in the model, but not in the valid person
        Person validPerson = new PersonBuilder(ALICE).build(); // does not have a CcaInformation swimming
        modelStub.addPerson(validPerson);
        modelStub.addCca(validCca);

        Index studentIndex = Index.fromOneBased(1);
        CcaName ccaName = new CcaName(VALID_CCA_NAME_SWIMMING);

        RemoveRoleFromStudentCommand command = new RemoveRoleFromStudentCommand(studentIndex, ccaName);
        assertThrows(CommandException.class, Messages.MESSAGE_CCA_NOT_IN_PERSON, (
        ) -> command.execute(modelStub));
    }

    @Test
    public void execute_roleNotAssigned_throwsCommandException() {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();

        Cca validCca = new CcaBuilder(BASKETBALL).build();
        Person validPerson = new PersonBuilder(ALICE).build();
        modelStub.addPerson(validPerson);
        modelStub.addCca(validCca);

        Index studentIndex = Index.fromOneBased(1);
        CcaName ccaName = new CcaName(VALID_CCA_NAME_BASKETBALL);

        RemoveRoleFromStudentCommand command = new RemoveRoleFromStudentCommand(studentIndex, ccaName);
        assertThrows(CommandException.class, RemoveRoleFromStudentCommand.MESSAGE_ROLE_NOT_ASSIGNED, (
        ) -> command.execute(modelStub));
    }

    private class ModelStubAcceptingPersonAdded extends ModelStub {
        final ArrayList<Person> personsAdded = new ArrayList<>();
        final ArrayList<Cca> ccasAdded = new ArrayList<>();

        @Override
        public void addPerson(Person person) {
            requireNonNull(person);
            personsAdded.add(person);
        }

        @Override
        public void addCca(Cca cca) {
            requireNonNull(cca);
            ccasAdded.add(cca);
        }

        @Override
        public boolean hasCca(CcaName ccaName) {
            requireNonNull(ccaName);
            return ccasAdded.stream().anyMatch(cca -> cca.getCcaName().equals(ccaName));
        }

        @Override
        public Cca getCca(CcaName ccaName) {
            for (Cca cca : ccasAdded) {
                if (cca.getCcaName().equals(ccaName)) {
                    return cca;
                }
            }
            return null;
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            requireAllNonNull(target, editedPerson);
            personsAdded.remove(target);
            personsAdded.add(editedPerson);
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return FXCollections.observableList(personsAdded);
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            // Do nothing
        }
    }


}
