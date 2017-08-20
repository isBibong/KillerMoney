package net.diecode.killermoney.commands;

import net.diecode.killermoney.commands.subcommands.km.HelpCommand;
import net.diecode.killermoney.commands.subcommands.shared.InfoCommand;
import net.diecode.killermoney.enums.KMCommandType;
import net.diecode.killermoney.managers.CommandManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class KMCommand implements CommandExecutor {

    public KMCommand() {
        CommandManager.registerSubCommand(new HelpCommand("help"));

        // Shared
        CommandManager.registerSubCommand(new InfoCommand("info"));
    }

    @Override
    public boolean onCommand(final CommandSender cs, Command cmd, String s, String[] tmpArgs) {
        if (tmpArgs.length == 0) {
            HelpCommand.showHelp(cs);

            return true;
        }

        CommandManager.onCommand(cs, cmd, s, tmpArgs, KMCommandType.KM);

        return true;
    }

}
