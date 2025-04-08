package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CCA_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.address.model.role.Role.DEFAULT_ROLE_NAME;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.cca.Cca;
import seedu.address.model.cca.CcaName;
import seedu.address.model.person.Person;
import seedu.address.model.role.Role;

/**
 * Adds a role of a specified CCA to a student identified using it's displayed index from the address book.
 */
public class AddRoleToStudentCommand extends Command {

    public static final String COMMAND_WORD = "add_r";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a role from the specified CCA to the student "
            + "identified by the index number used in the displayed student list. "
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_CCA_NAME + "CCA_NAME "
            + PREFIX_ROLE + "ROLE\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_CCA_NAME + "Basketball "
            + PREFIX_ROLE + "Center";

    public static final String MESSAGE_ADD_ROLE_TO_STUDENT_SUCCESS = "Added %2$s role to student: %1$s in %3$s CCA.";
    public static final String MESSAGE_ROLE_ALREADY_ASSIGNED = "This student already has a role in this CCA.";
    public static final String MESSAGE_CANNOT_ASSIGN_DEFAULT_ROLE = "Cannot assign a default role "
            + DEFAULT_ROLE_NAME + " to a student.";

    private final Index studentIndex;
    private final CcaName ccaName;
    private final Role role;

    /**
     * @param studentIndex of the student in the filtered student list to add role
     * @param ccaName of the CCA to add role
     * @param role of the role to add
     */
    public AddRoleToStudentCommand(Index studentIndex, CcaName ccaName, Role role) {
        requireNonNull(studentIndex);
        requireNonNull(ccaName);
        requireNonNull(role);
        this.studentIndex = studentIndex;
        this.ccaName = ccaName;
        this.role = role;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownPersonList = model.getFilteredPersonList();

        if (studentIndex.getZeroBased() >= lastShownPersonList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToAddRole = lastShownPersonList.get(studentIndex.getZeroBased());
        Cca targetCca = validateInputs(model, personToAddRole);

        Person personWithAddedRole = personToAddRole.addRole(targetCca, role);
        model.setPerson(personToAddRole, personWithAddedRole);

        return new CommandResult(String.format(
                MESSAGE_ADD_ROLE_TO_STUDENT_SUCCESS, Messages.format(personWithAddedRole.getName()),
                Messages.format(role), Messages.format(targetCca.getCcaName())));
    }

    /**
     * Validates the inputs for adding a role to a student's CCA.
     */
    private Cca validateInputs(Model model, Person personToAddRole) throws CommandException {
        if (!model.hasCca(ccaName)) {
            throw new CommandException(Messages.MESSAGE_CCA_NOT_FOUND);
        }
        Cca targetCca = model.getCca(ccaName);
        if (!personToAddRole.hasCca(targetCca)) {
            throw new CommandException(Messages.MESSAGE_CCA_NOT_IN_PERSON);
        }
        if (!personToAddRole.isDefaultRoleInCca(targetCca)) {
            throw new CommandException(MESSAGE_ROLE_ALREADY_ASSIGNED);
        }
        if (role.isDefaultRole()) {
            throw new CommandException(MESSAGE_CANNOT_ASSIGN_DEFAULT_ROLE);
        }
        if (!targetCca.hasRole(role)) {
            throw new CommandException(Messages.MESSAGE_ROLE_NOT_FOUND);
        }
        return targetCca;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AddRoleToStudentCommand)) {
            return false;
        }

        AddRoleToStudentCommand otherCommand = (AddRoleToStudentCommand) other;
        return studentIndex.equals(otherCommand.studentIndex)
                && ccaName.equals(otherCommand.ccaName)
                && role.equals(otherCommand.role);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("studentIndex", studentIndex)
                .add("ccaName", ccaName)
                .add("role", role)
                .toString();
    }
}
