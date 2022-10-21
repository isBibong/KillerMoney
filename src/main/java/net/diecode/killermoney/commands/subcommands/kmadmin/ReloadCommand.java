package net.diecode.killermoney.commands.subcommands.kmadmin;

import net.diecode.killermoney.enums.KMCommandType;
import net.diecode.killermoney.enums.LanguageString;
import net.diecode.killermoney.enums.SenderType;
import net.diecode.killermoney.objects.KMSubCommand;
import net.diecode.killermoney.enums.KMPermission;
import net.diecode.killermoney.managers.ConfigManager;
import net.diecode.killermoney.managers.LanguageManager;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public class ReloadCommand extends KMSubCommand {

    public ReloadCommand(String command) {
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
        minArgs = 0;
        senderType = SenderType.ANYONE;
    }

    @Override
    public void run(CommandSender cs, String[] args) {
        ConfigManager.reloadConfigs();

        LanguageManager.send(cs, LanguageString.GENERAL_CONFIGS_ARE_RELOADED);
    }
}
