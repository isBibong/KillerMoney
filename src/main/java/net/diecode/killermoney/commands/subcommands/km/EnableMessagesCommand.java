package net.diecode.killermoney.commands.subcommands.km;

import net.diecode.killermoney.Utils;
import net.diecode.killermoney.enums.KMCommandType;
import net.diecode.killermoney.enums.LanguageString;
import net.diecode.killermoney.enums.SenderType;
import net.diecode.killermoney.managers.KMPlayerManager;
import net.diecode.killermoney.managers.LanguageManager;
import net.diecode.killermoney.objects.KMPlayer;
import net.diecode.killermoney.objects.KMSubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class EnableMessagesCommand extends KMSubCommand {

    public EnableMessagesCommand(String command) {
        super(
                new ArrayList<KMCommandType>()
                {
                    {
                        add(KMCommandType.KM);
                    }
                },
                command
        );

        minArgs = 0;
        senderType = SenderType.PLAYER;

        acceptableValues = new ArrayList<String>()
        {
            {
                add("on");
                add("off");
                add("true");
                add("false");
                add("enable");
                add("disable");
                add("1");
                add("0");
            }
        };
    }

    @Override
    public void run(CommandSender cs, String[] args) {
        KMPlayer kmp = KMPlayerManager.getKMPlayer((Player)cs);

        if (args.length == 0) {
            if (kmp.isEnableMessages()) {
                LanguageManager.send(cs, LanguageString.COMMANDS_KM_MESSAGES_CURRENTLY_ENABLED);
            } else {
                LanguageManager.send(cs, LanguageString.COMMANDS_KM_MESSAGES_CURRENTLY_DISABLED);
            }

            return;
        }

        if (!Utils.isValidInput(acceptableValues, args[0])) {
            LanguageManager.send(cs, LanguageString.COMMANDS_SHARED_INVALID_VALUE);

            return;
        }

        boolean value = Utils.getBoolean(args[0]);

        kmp.setEnableMessages(value);
        kmp.saveConfig();

        if (kmp.isEnableMessages()) {
            LanguageManager.send(cs, LanguageString.COMMANDS_KM_MESSAGES_ENABLED);
        } else {
            LanguageManager.send(cs, LanguageString.COMMANDS_KM_MESSAGES_DISABLED);
        }
    }
}
