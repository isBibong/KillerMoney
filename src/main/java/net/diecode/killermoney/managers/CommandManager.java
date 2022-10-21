package net.diecode.killermoney.managers;

import net.diecode.killermoney.enums.KMCommandType;
import net.diecode.killermoney.enums.KMPermission;
import net.diecode.killermoney.enums.LanguageString;
import net.diecode.killermoney.enums.SenderType;
import net.diecode.killermoney.objects.KMSubCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CommandManager {

    private static ArrayList<KMSubCommand> subCommands = new ArrayList<>();

    public static void onCommand(final CommandSender cs, Command cmd, String s, String[] tmpArgs, KMCommandType type) {
        KMSubCommand cm = null;

        if (tmpArgs.length > 0) {
            cm = getSubCommand(type, tmpArgs[0]);
        }

        if (cm == null) {
            cm = getSubCommand(type, "help");
        }

        if (cm != null) {
            if (cm.getSenderType() == SenderType.PLAYER) {
                if (!(cs instanceof Player)) {
                    LanguageManager.send(cs, LanguageString.COMMANDS_SHARED_THIS_COMMAND_ONLY_USABLE_BY_PLAYER);

                    return;
                }
            }

            if (cm.getSenderType() == SenderType.CONSOLE) {
                if (!(cs instanceof ConsoleCommandSender)) {
                    LanguageManager.send(cs, LanguageString.COMMANDS_SHARED_THIS_COMMAND_ONLY_USABLE_BY_CONSOLE);

                    return;
                }
            }

            if (cm.getPermission() != null && !cs.hasPermission(cm.getPermission().get())
                    && !cs.hasPermission(KMPermission.ADMIN.get())) {
                LanguageManager.send(cs, LanguageString.COMMANDS_SHARED_YOU_HAVE_NOT_ENOUGH_PERMISSION,
                        cm.getPermission().get());

                return;
            }

            if (tmpArgs.length <= cm.getMinArgs()) {
                // too few args
                if (cm.getUsage() != null) {
                    cs.sendMessage(cm.getUsage());
                }

                return;
            }

            String[] args = new String[tmpArgs.length - 1];

            for (int i = 0; i < args.length; i++) {
                args[i] = tmpArgs[i + 1];
            }

            cm.run(cs, args);
        }

    }

    public static void registerSubCommand(KMSubCommand sc) {
        if (getSubCommand(sc.getUsable().get(0), sc.getCommand()) == null) {
            subCommands.add(sc);
        }
    }

    public static KMSubCommand getSubCommand(KMCommandType type, String command) {
        for (KMSubCommand kms : subCommands) {
            if (!kms.getUsable().contains(type)) {
                continue;
            }

            if (!kms.getCommand().equalsIgnoreCase(command)) {
                if (kms.getAliases() == null ||kms.getAliases().isEmpty()) {
                    continue;
                }

                for (String alias : kms.getAliases()) {
                    if (alias.equalsIgnoreCase(command)) {
                        return kms;
                    }
                }
            } else {
                return kms;
            }
        }

        return null;
    }

    public static ArrayList<KMSubCommand> getSubCommands() {
        return subCommands;
    }
}
