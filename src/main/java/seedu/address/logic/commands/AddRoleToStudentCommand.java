package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CCA;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.address.model.role.Role.DEFAULT_ROLE_NAME;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.cca.Cca;
import seedu.address.model.person.Person;
import seedu.address.model.role.Role;

public class AddRoleToStudentCommand extends Command {

    public static final String COMMAND_WORD = "add_r";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a role to the student identified "
            + "by the index number used in the displayed student list. "
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_CCA + "CCA_NAME "
            + PREFIX_ROLE + "ROLE\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_CCA + "Basketball "
            + PREFIX_ROLE + "Center";

    public static final String MESSAGE_ADD_ROLE_TO_STUDENT_SUCCESS = "Added role to student: %1$s";
    public static final String MESSAGE_ROLE_ALREADY_ASSIGNED = "This student already has a role in this CCA.";
    public static final String MESSAGE_CANNOT_ASSIGN_DEFAULT_ROLE = "Cannot assign a default role "
            + DEFAULT_ROLE_NAME + " to a student.";

    private final Index studentIndex;
    private final Cca cca;
    private final Role role;

    /**
     * @param studentIndex of the student in the filtered student list to add role
     * @param cca of the CCA to add role
     * @param role of the role to add
     */
    public AddRoleToStudentCommand(Index studentIndex, Cca cca, Role role) {
        requireNonNull(studentIndex);
        requireNonNull(cca);
        requireNonNull(role);
        this.studentIndex = studentIndex;
        this.cca = cca;
        this.role = role;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (studentIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToAddRole = lastShownList.get(studentIndex.getZeroBased());

        if (!personToAddRole.isDefaultRoleInCca(cca)) {
            throw new CommandException(MESSAGE_ROLE_ALREADY_ASSIGNED);
        }

        if (role.isDefaultRole()) {
            throw new CommandException(MESSAGE_CANNOT_ASSIGN_DEFAULT_ROLE);
        }

        Person personWithAddedRole = personToAddRole.addRole(cca, role);

        try {

            model.setPerson(personToAddRole, personWithAddedRole);

        } catch (IllegalArgumentException e) {
            throw new CommandException(Messages.MESSAGE_CCA_NOT_FOUND);
        }

        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_ADD_ROLE_TO_STUDENT_SUCCESS, Messages.format(personWithAddedRole)));
    }
}
