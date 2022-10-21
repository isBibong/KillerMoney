package net.diecode.killermoney.commands.subcommands.km;

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
                        add(KMCommandType.KM);
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
        sender.sendMessage(ChatColor.GREEN + "[ " + ChatColor.GRAY + "KillerMoney - help" + ChatColor.GREEN + " ]");
        sender.sendMessage(ChatColor.GREEN + "/km help" + ChatColor.GRAY + " | These help messages");
        sender.sendMessage(ChatColor.GREEN + "/km enable-messages <on/off>" + ChatColor.GRAY + " | You can enable or disable the kill messages");
    }
}
