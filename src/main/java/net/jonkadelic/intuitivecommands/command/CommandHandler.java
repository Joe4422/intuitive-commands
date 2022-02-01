package net.jonkadelic.intuitivecommands.command;

import java.util.ArrayList;
import java.util.List;

public class CommandHandler
{
    private static CommandHandler instance = new CommandHandler();

    /**
     * Fetches the static CommandHandler instance.
     * @return
     */
    public static CommandHandler getInstance()
    {
        return instance;
    }

    static
    {
        instance.registerCommand(new TeleportCommand());
        instance.registerCommand(new GiveCommand());
    }

    private List<CommandBase> commands = new ArrayList<>();

    /**
     * Creates a new CommandHandler. Private so that getInstance() is used instead.
     */
    private CommandHandler()
    {
    }

    /**
     * Gets suggestions for what commands can be executed. Is searchable, e.g. if the player
     * types "/t" the suggestions would be "/tp" and "/teleport". If the player has typed
     * more than one word already, no suggestions will be shown.
     * @param args The current set of command arguments. Includes the command trigger itself.
     * @return The set of suggested commands based on the current user input.
     */
    public String[] getCommandSuggestions(final String[] args)
    {
        final List<String> suggestions = new ArrayList<>();

        if (args.length == 1)
        {
            for (CommandBase command : commands)
            {
                for (int j = 0; j < command.triggers.length; j++)
                {
                    if (command.triggers[j].startsWith(args[0].substring(1)))
                    {
                        suggestions.add("/" + command.triggers[j]);
                    }
                }
            }
        }

        return suggestions.toArray(new String[0]);
    }

    /**
     * Fetches the number of currently registered commands.
     * @return The number of commands currently registered to the CommandHandler.
     */
    public int getCommandCount()
    {
        return commands.size();
    }

    /**
     * Fetches a command from the command set by its index.
     * @param index The index of the command to access.
     * @return The command that was found at the given index.
     */
    public CommandBase getCommandByIndex(int index)
    {
        return commands.get(index);
    }

    /**
     * Registers a command to the CommandHandler, allowing it to appear and be handled by the chat window.
     * @param command The command to be registered.
     */
    public void registerCommand(CommandBase command)
    {
        commands.add(command);
    }
}
