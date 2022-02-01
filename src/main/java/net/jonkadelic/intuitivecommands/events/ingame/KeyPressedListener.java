package net.jonkadelic.intuitivecommands.events.ingame;

import net.fabricmc.loader.api.FabricLoader;
import net.jonkadelic.intuitivecommands.gui.ChatExtended;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.options.KeyBinding;
import net.modificationstation.stationapi.api.client.event.keyboard.KeyStateChangedEvent;
import org.lwjgl.input.Keyboard;

public class KeyPressedListener {

    @EventListener
    public void keyPressed(KeyStateChangedEvent event)
    {
        Minecraft mc = ((Minecraft) FabricLoader.getInstance().getGameInstance());
        KeyBinding openChat = mc.options.chatKey;

        if (Keyboard.getEventKeyState() && Keyboard.isKeyDown(openChat.key) && mc.currentScreen == null && mc.level != null && !mc.level.isClient) {
            ((Minecraft) FabricLoader.getInstance().getGameInstance()).openScreen(new ChatExtended());
        }
    }
}
