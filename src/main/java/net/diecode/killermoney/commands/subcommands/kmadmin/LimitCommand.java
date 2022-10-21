package net.diecode.killermoney.commands.subcommands.kmadmin;

import net.diecode.killermoney.enums.KMCommandType;
import net.diecode.killermoney.enums.KMPermission;
import net.diecode.killermoney.enums.LanguageString;
import net.diecode.killermoney.enums.SenderType;
import net.diecode.killermoney.functions.LimitHandler;
import net.diecode.killermoney.objects.KMSubCommand;
import net.diecode.killermoney.managers.LanguageManager;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public class LimitCommand extends KMSubCommand {

    public LimitCommand(String command) {
        super(
                new ArrayList<KMCommandType>()
                {
                    {
                        add(KMCommandType.KM_ADMIN);
                    }
                },
                command
        );

        permission = KMPermission.ADMIN;
        minArgs = 1;
        usage = LanguageManager.cGet(LanguageString.COMMANDS_KMADMIN_COMMAND_LIMIT_USAGE);
        senderType = SenderType.ANYONE;
    }

    @Override
    public void run(CommandSender cs, String[] args) {
        String subCommand = args[0];

        if (subCommand.equalsIgnoreCase("reset") || subCommand.equalsIgnoreCase("clear")) {
            LimitHandler.reset();

            LanguageManager.send(cs, LanguageString.COMMANDS_KMADMIN_LIMIT_RESET);
        }
    }
}
