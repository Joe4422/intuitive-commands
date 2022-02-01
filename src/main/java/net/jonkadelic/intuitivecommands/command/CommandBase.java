package net.jonkadelic.intuitivecommands.command;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.Level;

/**
 * Base class that all Intuitive Commands commands derive from.
 * To make a command be picked up by the game, create an instance of it and call CommandHandler.getInstance().registerCommand().
 */
public abstract class CommandBase
{
	public String[] triggers;
	public String helpText;

	/**
	 * Creates a new CommandBase.
	 * @param triggers Array of keywords that select this command. One command can have multiple triggers, e.g. "tp" and "teleport".
	 * @param helpText Help text to be shown for this command, e.g. "/tp <x> <y> <z>".
	 */
	public CommandBase(final String[] triggers, final String helpText)
	{
		this.triggers = triggers;
		this.helpText = helpText;
	}

	/**
	 * Takes a suggestion to be appended to a command being typed, and formats it for appending. In other words, when the user
	 * presses tab on a suggestion, you can modify it in this function before it's appended to the chat input.
	 * @param index The word position of the suggestion in the current command, e.g. in "/tp 1 2 3" the "2" would have index 2.
	 * @param suggestion The suggestion string to be formatted.
	 * @return The formatted suggestion string.
	 */
	public String formatSuggestion(final int index, final String suggestion)
	{
		return suggestion + " ";
	}

	/**
	 * Determines the help text that should be displayed after the current input. For example, if the user has typed "/tp 1 2 ",
	 * the current suggestion will be "<z>", to indicate that they are now typing the Z coordinate.
	 * @param args The current set of command arguments. This includes the command trigger itself.
	 * @return The part of the help string to be displayed, e.g. "<z>".
	 */
	public String getNextHelpPart(final String[] args)
	{
		return "";
	}

	/**
	 * Determines the set of possible suggestions for a command parameter - for example, for "/kick <player>", the suggestions
	 * could be a set of all kickable players on the server.
	 * @param minecraft The Minecraft object.
	 * @param level The current level.
	 * @param player The player that's currently typing this command.
	 * @param args The current set of command arguments. This includes the command trigger itself.
	 * @return The set of suggestions that should be displayed.
	 */
	public String[] getSuggestions(final Minecraft minecraft, final Level level, final PlayerBase player, final String[] args)
	{
		return new String[0];
	}

	/**
	 * Executes a command with the given set of arguments.
	 * @param minecraft The Minecraft object.
	 * @param level The current level.
	 * @param player The player that invoked this command.
	 * @param args The invoked set of command arguments. This includes the command trigger itself.
	 * @return True if the command completed successfully, false otherwise.
	 * @throws Exception Any exception that may be thrown by this command. Will be handled and reported in the chat log.
	 */
	public abstract boolean runCommand(Minecraft minecraft, Level level, PlayerBase player, String[] args) throws Exception;

}