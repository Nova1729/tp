package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Adds a person to the address book.
 */
public class CreateStudentCommand extends Command {

    public static final String COMMAND_WORD = "create_s";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a student to the student list. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_ADDRESS + "ADDRESS \n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@gmail.com "
            + PREFIX_ADDRESS + "311, Clementi Ave 2, #02-25";

    public static final String MESSAGE_SUCCESS = "New student added: %1$s";

    private final Person toCreate;

    /**
     * Creates a CreateStudentCommand to add the specified {@code Person}.
     */
    public CreateStudentCommand(Person person) {
        requireNonNull(person);
        toCreate = person;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasPerson(toCreate)) {
            throw new CommandException(Messages.MESSAGE_DUPLICATE_PERSON);
        }

        model.addPerson(toCreate);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toCreate)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CreateStudentCommand)) {
            return false;
        }

        CreateStudentCommand otherCreateStudentCommand = (CreateStudentCommand) other;
        return toCreate.equals(otherCreateStudentCommand.toCreate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toCreate", toCreate)
                .toString();
    }
}
