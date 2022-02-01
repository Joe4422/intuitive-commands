package net.jonkadelic.intuitivecommands.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.jonkadelic.intuitivecommands.command.CommandBase;
import net.jonkadelic.intuitivecommands.command.CommandHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.AbstractClientPlayer;
import net.minecraft.server.command.Command;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractClientPlayer.class)
public class SingleplayerCommandMixin
{
    @Shadow protected Minecraft minecraft;

    @Inject(at = @At("HEAD"), method = "sendChatMessage", remap = false)
    public void sendChatMessage(String message, CallbackInfo ci)
    {
        String[] split = message.split(" ");

        if (split[0].startsWith("/"))
        {
            String commandText = split[0].substring(1);
            CommandBase cmd = null;

            for (int i = 0; i < CommandHandler.getInstance().getCommandCount(); i++)
            {
                for (int j = 0; j < CommandHandler.getInstance().getCommandByIndex(i).triggers.length; j++)
                {
                    if (CommandHandler.getInstance().getCommandByIndex(i).triggers[j].equals(commandText))
                    {
                        cmd = CommandHandler.getInstance().getCommandByIndex(i);
                        break;
                    }
                }

                if (cmd != null) break;
            }

            try
            {
                if (cmd == null || !cmd.runCommand(minecraft, minecraft.level, minecraft.player, split))
                {
                    minecraft.overlay.addChatMessage("Invalid command or parameters. Type /help for a full list of commands.");
                }
            }
            catch (Exception e)
            {
                minecraft.overlay.addChatMessage("Invalid command or parameters. Type /help for a full list of commands.");
            }
        }
        else
        {
            minecraft.overlay.addChatMessage("<" + minecraft.player.name + "> " + message);
        }
    }
}
