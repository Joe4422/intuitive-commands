package net.jonkadelic.intuitivecommands.command;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.Level;

public class TeleportCommand extends CommandBase
{
    public TeleportCommand()
    {
        super(new String[] {"tp"}, "/tp <x> <y> <z>");
    }

    @Override
    public boolean runCommand(Minecraft minecraft, Level level, PlayerBase player, String[] args) throws Exception
    {
        if (args.length != 4)
        {
            return false;
        }

        final int x = Integer.parseInt(args[1]);
        final int y = Integer.parseInt(args[2]);
        final int z = Integer.parseInt(args[3]);
        player.setPosition(x, y, z);

        minecraft.overlay.addChatMessage("Teleporting to x: " + x + ", y: " + y + ", z: " + z + ".");

        return true;
    }

    @Override
    public String getNextHelpPart(final String[] args)
    {
        if (args.length == 1)
        {
            return "<x>";
        }
        else if (args.length == 2)
        {
            return "<y>";
        }
        else if (args.length == 3)
        {
            return "<z>";
        }

        return "";
    }
}
