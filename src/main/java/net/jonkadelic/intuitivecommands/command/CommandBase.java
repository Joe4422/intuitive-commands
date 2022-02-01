package net.jonkadelic.intuitivecommands.command;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.Level;

public abstract class CommandBase
{
	public String[] triggers;
	public String helpText;

	public CommandBase(final String[] triggers, final String helpText)
	{
		this.triggers = triggers;
		this.helpText = helpText;
	}

	public String formatSuggestion(final int index, final String suggestion)
	{
		return suggestion + " ";
	}

	public String getNextHelpPart(final String[] args)
	{
		return "";
	}

	public String[] getSuggestions(final Minecraft minecraft, final Level level, final PlayerBase player, final String[] args)
	{
		return new String[0];
	}

	public abstract boolean runCommand(Minecraft minecraft, Level level, PlayerBase player, String[] args) throws Exception;

}