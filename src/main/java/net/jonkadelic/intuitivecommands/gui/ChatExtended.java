package net.jonkadelic.intuitivecommands.gui;

import net.jonkadelic.intuitivecommands.command.CommandBase;
import net.jonkadelic.intuitivecommands.command.CommandHandler;
import net.minecraft.client.gui.screen.ingame.Chat;
import net.minecraft.util.CharacterUtils;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;

public class ChatExtended extends Chat
{
    private String[] suggestions;
    private int suggestionIndex = 0;
    private int suggestionOffset = 0;
    private final int maxVisibleSuggestions = 20;
    private CommandBase currentCommand = null;

    @Override
    public void render(int mouseX, int mouseY, float delta)
    {
        super.render(mouseX, mouseY, delta);

        if (currentCommand != null && getText.endsWith(" "))
        {
            final int currentMessageLength = textManager.getTextWidth(getText);
            final String help = currentCommand.getNextHelpPart(getText.split(" "));
            drawTextWithShadow(textManager, help, currentMessageLength + 4 + textManager.getTextWidth("> "), height - 12, 0xB0B0B0);
        }

        if (suggestions != null && suggestions.length > 0)
        {
            final String substr = getText.substring(0, indexOfCurrentWordInMessage());
            final int x = textManager.getTextWidth(substr) + 4 + textManager.getTextWidth("> ");
            final String[] subSuggestions = Arrays.copyOfRange(suggestions, suggestionOffset, suggestionOffset + maxVisibleSuggestions);
            for (int q = 0; q < subSuggestions.length; q++)
            {
                String drawString = subSuggestions[q];
                if (drawString == null)
                {
                    continue;
                }
                if ((q == 0 && suggestionOffset > 0) || (q == maxVisibleSuggestions - 1 && suggestionOffset < suggestions.length - maxVisibleSuggestions))
                {
                    drawString = "...";
                }
                fill(x - 2, (height - 12) - ((q + 1) * 12) - 2, x + textManager.getTextWidth(drawString) + 2, (height - 12) - ((q + 1) * 12) + 12 - 2, q == suggestionIndex ? 0xFF808080 : 0xFF000000);
                drawTextWithShadow(textManager, drawString, x, (height - 12) - ((q + 1) * 12), q == suggestionIndex ? 0xF0F0F0 : 0x909090);
            }
        }
    }

    /**
     * Fetches the word index of the current word in the message being typed.
     * This behaves differently whether the message ends with a space - for example, in the message
     * "three blind mice", the current word index is 2, but in the message "three blind mice ", the
     * current word index is 3.
     * @return The index of the word currently being typed.
     */
    private int indexOfCurrentWordInMessage()
    {
        final String[] s = getText.split(" ");
        if (s.length == 0)
        {
            return 0;
        }
        else if (getText.endsWith(" "))
        {
            return getText.length();
        }
        else
        {
            return getText.lastIndexOf(s[s.length - 1]);
        }
    }

    @Override
    protected void keyPressed(char character, int key)
    {
        if (key == Keyboard.KEY_ESCAPE)
        {
            minecraft.openScreen(null);
            return;
        }
        else if (key == Keyboard.KEY_RETURN)
        {
            final String s = getText.trim();
            if (s.length() > 0)
            {
                if (!minecraft.isCommand(s))
                {
                    minecraft.player.sendChatMessage(s);
                }
            }

            minecraft.openScreen(null);
        }
        else if (key == Keyboard.KEY_TAB)
        {
            if (suggestions != null && suggestions.length > 0)
            {
                final int index = indexOfCurrentWordInMessage();
                final String substr = getText.substring(0, index);
                int messageIndex = getText.split(" ").length - 1;

                if (getText.endsWith(" "))
                {
                    messageIndex++;
                }

                if (currentCommand == null)
                {
                    getText = substr + suggestions[suggestionIndex + suggestionOffset] + " ";
                }
                else
                {
                    getText = substr + currentCommand.formatSuggestion(messageIndex, suggestions[suggestionIndex + suggestionOffset]);
                }

                updateSuggestions();
            }
        }
        else if (key == Keyboard.KEY_UP)
        {
            if (suggestions == null || suggestions.length == 0)
            {
                return;
            }
            if (suggestionIndex < maxVisibleSuggestions - 1 && suggestionIndex < suggestions.length - 1)
            {
                suggestionIndex++;
            }
            else if (suggestionOffset < suggestions.length - maxVisibleSuggestions)
            {
                suggestionOffset++;
            }
            if (suggestionIndex == maxVisibleSuggestions - 1 && suggestionOffset < suggestions.length - maxVisibleSuggestions)
            {
                suggestionIndex = maxVisibleSuggestions - 2;
                suggestionOffset++;
            }
        }
        else if (key == Keyboard.KEY_DOWN)
        {
            if (suggestions == null || suggestions.length == 0)
            {
                return;
            }
            if (suggestionIndex > 0)
            {
                suggestionIndex--;
            }
            else if (suggestionOffset > 0)
            {
                suggestionOffset--;
            }
            if (suggestionIndex == 0 && suggestionOffset > 0)
            {
                suggestionIndex = 1;
                suggestionOffset--;
            }
        }
        else if (key == Keyboard.KEY_PRIOR)
        {
            if (suggestions == null || suggestions.length == 0)
            {
                return;
            }
            suggestionOffset += maxVisibleSuggestions;
            suggestionIndex = maxVisibleSuggestions - 1;
            if (suggestionOffset < suggestions.length - maxVisibleSuggestions)
            {
                suggestionIndex--;
            }
            if (suggestionOffset >= suggestions.length - maxVisibleSuggestions)
            {
                suggestionOffset = suggestions.length - maxVisibleSuggestions;
            }
        }
        if (key == Keyboard.KEY_NEXT)
        {
            if (suggestions == null || suggestions.length == 0)
            {
                return;
            }
            suggestionOffset -= maxVisibleSuggestions;
            suggestionIndex = 0;
            if (suggestionOffset > 0)
            {
                suggestionIndex++;
            }
            if (suggestionOffset < 0)
            {
                suggestionOffset = 0;
            }
        }
        if (key == Keyboard.KEY_BACK && getText.length() > 0)
        {
            getText = getText.substring(0, getText.length() - 1);
            updateSuggestions();
        }
        if (CharacterUtils.validCharacters.indexOf(character) >= 0 && getText.length() < 100)
        {
            getText += character;
            updateSuggestions();
        }
    }

    /**
     * Updates the set of available suggestions based on the contents of getText.
     */
    private void updateSuggestions()
    {
        suggestionIndex = 0;
        suggestionOffset = 0;
        String[] s = getText.split(" ");
        currentCommand = null;

        if (getText.endsWith(" "))
        {
            final String[] newS = new String[s.length + 1];
            System.arraycopy(s, 0, newS, 0, s.length);
            newS[s.length] = "";
            s = newS;
        }

        if (s.length == 0 || !s[0].startsWith("/"))
        {
            suggestions = new String[0];
            return;
        }

        if (s.length == 1)
        {
            suggestions = CommandHandler.getInstance().getCommandSuggestions(s);
            Arrays.sort(suggestions);
            return;
        }

        final String commandText = s[0].substring(1);

        for (int i = 0; i < CommandHandler.getInstance().getCommandCount(); i++)
        {
            for (int j = 0; j < CommandHandler.getInstance().getCommandByIndex(i).triggers.length; j++)
            {
                if (CommandHandler.getInstance().getCommandByIndex(i).triggers[j].equals(commandText))
                {
                    currentCommand = CommandHandler.getInstance().getCommandByIndex(i);
                    break;
                }
            }

            if (currentCommand != null)
            {
                break;
            }
        }

        if (currentCommand == null)
        {
            return;
        }

        suggestions = currentCommand.getSuggestions(minecraft, minecraft.level, minecraft.player, s);
        Arrays.sort(suggestions);
    }
}
