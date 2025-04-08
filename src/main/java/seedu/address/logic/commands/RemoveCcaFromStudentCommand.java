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
 * Removes a CCA from a student identified using it's displayed index from the address book.
 */
public class RemoveCcaFromStudentCommand extends Command {

    public static final String COMMAND_WORD = "remove_c";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Removes a CCA from the student identified "
            + "by the index number used in the displayed student list.\n"
            + "Parameters: STUDENT_INDEX (must be a positive integer) "
            + PREFIX_CCA_NAME + "CCA_NAME\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_CCA_NAME + "Basketball";

    public static final String MESSAGE_REMOVE_CCA_SUCCESS = "Removed CCA %2$s from student: %1$s";

    private final Index studentIndex;
    private final CcaName ccaName;

    /**
     * Creates a RemoveCcaFromStudentCommand to remove the specified {@code CcaName} from the student at
     * the specified {@code studentIndex}.
     */
    public RemoveCcaFromStudentCommand(Index studentIndex, CcaName ccaName) {
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
        Person personToRemoveCca = lastShownPersonList.get(studentIndex.getZeroBased());

        if (!model.hasCca(ccaName)) {
            throw new CommandException(Messages.MESSAGE_CCA_NOT_FOUND);
        }

        Cca targetCca = model.getCca(ccaName);

        if (!personToRemoveCca.hasCca(targetCca)) {
            throw new CommandException(Messages.MESSAGE_CCA_NOT_IN_PERSON);
        }

        Person personWithRemovedCca = personToRemoveCca.removeCca(targetCca);
        model.setPerson(personToRemoveCca, personWithRemovedCca);
        return new CommandResult(String.format(MESSAGE_REMOVE_CCA_SUCCESS,
                Messages.format(personWithRemovedCca.getName()), Messages.format(ccaName)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof RemoveCcaFromStudentCommand)) {
            return false;
        }
        RemoveCcaFromStudentCommand otherCommand = (RemoveCcaFromStudentCommand) other;
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
