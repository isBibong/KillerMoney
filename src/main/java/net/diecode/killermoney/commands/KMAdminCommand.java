package net.diecode.killermoney.commands;

import net.diecode.killermoney.commands.subcommands.kmadmin.*;
import net.diecode.killermoney.enums.KMCommandType;
import net.diecode.killermoney.managers.CommandManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class KMAdminCommand implements CommandExecutor {

    public KMAdminCommand() {
        CommandManager.registerSubCommand(new HelpCommand("help"));
        CommandManager.registerSubCommand(new LimitCommand("limit"));
        CommandManager.registerSubCommand(new MultiplierCommand("multiplier"));
        CommandManager.registerSubCommand(new ReloadCommand("reload"));
    }

    @Override
    public boolean onCommand(final CommandSender cs, Command cmd, String s, String[] tmpArgs) {
        if (tmpArgs.length == 0) {
            HelpCommand.showHelp(cs);

            return true;
        }

        CommandManager.onCommand(cs, cmd, s, tmpArgs, KMCommandType.KM_ADMIN);

        return true;
    }

}
