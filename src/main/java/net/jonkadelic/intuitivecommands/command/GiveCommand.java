package net.jonkadelic.intuitivecommands.command;

import net.minecraft.block.BlockBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.Block;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.registry.Registry;

import java.util.*;

public class GiveCommand extends CommandBase
{
    public GiveCommand() {
        super(new String[] { "give" }, "/give <id> [quantity] [metadata]");
    }

    @Override
    public boolean runCommand(Minecraft minecraft, Level level, PlayerBase player, String[] args) throws Exception
    {
        String name = null;
        int id = 0;
        int quantity = 0;
        int metadata = 0;

        if (args.length < 2 || args.length > 4) return false;
        if (args.length >= 2)
        {
            name = args[1];

            // Attempt to find block
            Optional<BlockBase> block = BlockRegistry.INSTANCE.get(Identifier.of(name));

            if (block.isPresent())
            {
                BlockBase realBlock = block.get();
                id = realBlock.id;
                quantity = 64;
            }
            else
            {
                // Attempt to find item
                Optional<ItemBase> item = ItemRegistry.INSTANCE.get(Identifier.of(name));

                if (item.isPresent())
                {
                    ItemBase realItem = item.get();
                    id = realItem.id;
                    quantity = realItem.getMaxStackSize();
                }
                else
                {
                    return false;
                }
            }
        }
        if (args.length >= 3)
        {
            quantity = Integer.parseInt(args[2]);
        }
        if (args.length >= 4)
        {
            metadata = Integer.parseInt(args[3]);
        }

        ItemInstance instance = new ItemInstance(id, quantity, metadata);

        minecraft.overlay.addChatMessage("Giving " + quantity + " " + I18n.translate(instance.getTranslationKey() + ".name") + ".");

        player.inventory.addStack(instance);

        return true;
    }

    @Override
    public String[] getSuggestions(Minecraft minecraft, Level level, PlayerBase player, String[] args)
    {
        if (args.length == 2)
        {
            List<String> itemSuggestions = new ArrayList<>();

            // Check if we're entering a mod ID or a name
            if (args[1].contains(":")) // Name
            {
                String[] parts = args[1].split(":");
                String modId = parts[0];
                String name = null;

                if (parts.length >= 2) name = parts[1];

                // Attempt to get all blocks in mod ID
                for (Map.Entry<Identifier, BlockBase> registry : BlockRegistry.INSTANCE)
                {
                    if (name == null || (modId.equals(registry.getKey().modID.toString()) && registry.getKey().id.startsWith(name)))
                    {
                        itemSuggestions.add(registry.getKey().modID + ":" + registry.getKey().id);
                    }
                }

                // Attempt to get all items in mod ID
                for (Map.Entry<Identifier, ItemBase> registry : ItemRegistry.INSTANCE)
                {
                    if (name == null || (modId.equals(registry.getKey().modID.toString()) && registry.getKey().id.startsWith(name)))
                    {
                        itemSuggestions.add(registry.getKey().modID + ":" + registry.getKey().id);
                    }
                }
            }
            else
            {
                Set<String> modIds = new HashSet<>();
                String modId = null;

                if (args[1].length() > 0) modId = args[1];

                // Attempt to get all blocks in mod ID
                for (Map.Entry<Identifier, BlockBase> registry : BlockRegistry.INSTANCE)
                {
                    if (modId == null || registry.getKey().id.startsWith(modId))
                    {
                        modIds.add(registry.getKey().modID + ":");
                    }
                }

                // Attempt to get all items in mod ID
                for (Map.Entry<Identifier, ItemBase> registry : ItemRegistry.INSTANCE)
                {
                    if (modId == null || registry.getKey().id.startsWith(modId))
                    {
                        modIds.add(registry.getKey().modID + ":");
                    }
                }

                itemSuggestions.addAll(modIds);
            }

            return itemSuggestions.toArray(new String[0]);
        }

        return new String[0];
    }

    @Override
    public String getNextHelpPart(String[] args)
    {
        if (args.length == 1)
        {
            return "<id>";
        }
        else if (args.length == 2)
        {
            return "[quantity]";
        }
        else if (args.length == 3)
        {
            return "[metadata]";
        }

        return "";
    }

    @Override
    public String formatSuggestion(int index, String suggestion)
    {
        if (index == 1)
        {
            if (!suggestion.endsWith(":")) return suggestion + " ";
            else return suggestion;
        }

        return suggestion + " ";
    }
}
