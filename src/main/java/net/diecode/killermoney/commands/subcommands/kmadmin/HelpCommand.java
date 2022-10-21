package net.diecode.killermoney.commands.subcommands.kmadmin;

import net.diecode.killermoney.enums.KMCommandType;
import net.diecode.killermoney.enums.SenderType;
import net.diecode.killermoney.objects.KMSubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public class HelpCommand extends KMSubCommand {

    public HelpCommand(String command) {
        super(
                new ArrayList<KMCommandType>()
                {
                    {
                        add(KMCommandType.KM_ADMIN);
                    }
                },
                command
        );

        minArgs = 0;
        senderType = SenderType.ANYONE;
    }

    @Override
    public void run(CommandSender cs, String[] args) {
        showHelp(cs);
    }

    public static void showHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + "[ " + ChatColor.GRAY + "KillerMoney Admin - help" + ChatColor.GREEN + " ]");
        sender.sendMessage(ChatColor.GREEN + "/kmadmin help" + ChatColor.GRAY + " | These help messages");
        sender.sendMessage(ChatColor.GREEN + "/kmadmin info" + ChatColor.GRAY + " | Show general info about KillerMoney");
        sender.sendMessage(ChatColor.GREEN + "/kmadmin reload" + ChatColor.GRAY + " | Reload configs");
        sender.sendMessage(ChatColor.GREEN + "/kmadmin limit" + ChatColor.GRAY + " | Reset limits");
        sender.sendMessage(ChatColor.GREEN + "/kmadmin multiplier <function> [value]" + ChatColor.GRAY + " | Set a custom multiplier for given amount of time");
    }
}
