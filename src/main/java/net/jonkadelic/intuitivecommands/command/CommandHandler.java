package net.jonkadelic.intuitivecommands.command;

import java.util.ArrayList;
import java.util.List;

public class CommandHandler
{
    private static CommandHandler instance = new CommandHandler();

    public static CommandHandler getInstance()
    {
        return instance;
    }

    static
    {
        instance.registerCommand(new TeleportCommand());
    }

    private List<CommandBase> commands = new ArrayList<>();

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

    public int getCommandCount()
    {
        return commands.size();
    }

    public CommandBase getCommandByIndex(int index)
    {
        return commands.get(index);
    }

    public void registerCommand(CommandBase command)
    {
        commands.add(command);
    }
}
