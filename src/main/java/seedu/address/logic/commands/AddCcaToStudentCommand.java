package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CCA_NAME;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.cca.Cca;
import seedu.address.model.cca.CcaName;
import seedu.address.model.person.Person;

/**
 * Adds a CCA to a student identified using their displayed index from the address book.
 * The CCA must exist in the address book's CCA list, and the student must not already be enrolled in it.
 * A default role and attendance record (based on the CCA's total sessions) will be assigned.
 */
public class AddCcaToStudentCommand extends Command {

    public static final String COMMAND_WORD = "add_c";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a CCA to the student identified "
            + "by the index number used in the displayed student list.\n"
            + "The CCA must already exist in the CCA list.\n"
            + "Parameters: STUDENT_INDEX (must be a positive integer) "
            + PREFIX_CCA_NAME + "CCA_NAME\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_CCA_NAME + "Basketball";

    public static final String MESSAGE_ADD_CCA_SUCCESS = "Added CCA %2$s to student: %1$s";
    public static final String MESSAGE_CCA_ALREADY_PRESENT = "This student is already enrolled in this CCA.";

    private final Index studentIndex;
    private final CcaName ccaName;

    /**
     * Creates an AddCcaToStudentCommand to add the specified {@code CcaName} to the student at
     * the specified {@code studentIndex}.
     */
    public AddCcaToStudentCommand(Index studentIndex, CcaName ccaName) {
        requireAllNonNull(studentIndex, ccaName);
        this.studentIndex = studentIndex;
        this.ccaName = ccaName;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownPersonList = model.getFilteredPersonList();

        if (studentIndex.getZeroBased() >= lastShownPersonList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
        if (!model.hasCca(ccaName)) {
            throw new CommandException(Messages.MESSAGE_CCA_NOT_FOUND);
        }

        Person personToAddCca = lastShownPersonList.get(studentIndex.getZeroBased());
        Cca targetCca = model.getCca(ccaName);

        if (personToAddCca.hasCca(targetCca)) {
            throw new CommandException(MESSAGE_CCA_ALREADY_PRESENT);
        }

        Person personWithAddedCca = personToAddCca.addCca(targetCca);

        model.setPerson(personToAddCca, personWithAddedCca);
        return new CommandResult(String.format(MESSAGE_ADD_CCA_SUCCESS,
                Messages.format(personWithAddedCca.getName()), Messages.format(ccaName)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddCcaToStudentCommand)) {
            return false;
        }

        AddCcaToStudentCommand otherCommand = (AddCcaToStudentCommand) other;

        return studentIndex.equals(otherCommand.studentIndex)
                && ccaName.equals(otherCommand.ccaName);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("studentIndex", studentIndex)
                .add("ccaName", ccaName)
                .toString();
    }
}
