package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AMOUNT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CCA_NAME;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.cca.Amount;
import seedu.address.model.cca.Cca;
import seedu.address.model.cca.CcaName;
import seedu.address.model.person.Person;

/**
 * Records the attendance of a student in a CCA.
 */
public class RecordAttendanceCommand extends Command {
    public static final String COMMAND_WORD = "attend";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Records the attendance of a student in a CCA. "
            + "Parameters: "
            + "STUDENT_INDEX (must be a positive integer) "
            + PREFIX_CCA_NAME + "CCA_NAME "
            + PREFIX_AMOUNT + "AMOUNT\n"
            + "Example: " + COMMAND_WORD + " 1 " + PREFIX_CCA_NAME + "Basketball " + PREFIX_AMOUNT + "1";

    public static final String MESSAGE_SUCCESS = "Recorded attendance for: %1$s in %2$s for %3$s sessions.";
    public static final String MESSAGE_EXCEEDING_AMOUNT = "The amount of attendance recorded exceeds the total "
            + "number of sessions in the CCA.";

    private final Index studentIndex;
    private final CcaName ccaName;
    private final Amount amount;

    /**
     * Creates a RecordAttendanceCommand to record the specified {@code Attendance}
     */
    public RecordAttendanceCommand(Index studentIndex, CcaName ccaName, Amount amount) {
        requireNonNull(studentIndex);
        requireNonNull(ccaName);
        requireNonNull(amount);
        this.studentIndex = studentIndex;
        this.ccaName = ccaName;
        this.amount = amount;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (!model.hasCca(ccaName)) {
            throw new CommandException(Messages.MESSAGE_CCA_NOT_FOUND);
        }
        Cca cca = model.getCca(ccaName);

        if (studentIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
        Person student = lastShownList.get(studentIndex.getZeroBased());
        if (!student.hasCca(ccaName)) {
            throw new CommandException(Messages.MESSAGE_CCA_NOT_IN_PERSON);
        } else if (!student.canAttend(cca, amount)) {
            throw new CommandException(MESSAGE_EXCEEDING_AMOUNT);
        }
        Person updatedStudent = student.attend(cca, amount);
        model.setPerson(student, updatedStudent);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(student.getName()),
                Messages.format(ccaName), Messages.format(amount)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof RecordAttendanceCommand)) {
            return false;
        }

        RecordAttendanceCommand otherRecordAttendanceCommand = (RecordAttendanceCommand) other;
        return studentIndex.equals(otherRecordAttendanceCommand.studentIndex)
                && ccaName.equals(otherRecordAttendanceCommand.ccaName)
                && amount.equals(otherRecordAttendanceCommand.amount);
    }
}
