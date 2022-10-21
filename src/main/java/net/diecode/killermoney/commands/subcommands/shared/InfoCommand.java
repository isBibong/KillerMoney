package net.diecode.killermoney.commands.subcommands.shared;

import net.diecode.killermoney.BukkitMain;
import net.diecode.killermoney.enums.KMCommandType;
import net.diecode.killermoney.enums.SenderType;
import net.diecode.killermoney.objects.KMSubCommand;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public class InfoCommand extends KMSubCommand {

    public InfoCommand(String command) {
        super(
                new ArrayList<KMCommandType>()
                {
                    {
                        add(KMCommandType.KM);
                        add(KMCommandType.KM_ADMIN);
                    }
                },
                command
        );

        minArgs = 0;
        aliases = new ArrayList<String>() {
            {
                add("about");
            }
        };
        senderType = SenderType.ANYONE;
    }

    @Override
    public void run(CommandSender cs, String[] args) {
        cs.sendMessage(ChatColor.GREEN + "[ " + ChatColor.GRAY + "KillerMoney - info" + ChatColor.GREEN + " ]");
        cs.sendMessage(ChatColor.GRAY + "Developer: " + ChatColor.GREEN + "diecode" + ChatColor.GRAY + " | Twitter: " + ChatColor.GREEN + "@diecode");
        cs.sendMessage(ChatColor.GRAY + "PayPal for donation: " + ChatColor.GREEN + "https://www.paypal.me/krisztiangahor");
        cs.sendMessage(ChatColor.GRAY + "Spigot page: " + ChatColor.GREEN + "https://www.spigotmc.org/resources/killermoney.485/");
        cs.sendMessage(ChatColor.GRAY + "Plugin version: " + ChatColor.GREEN + BukkitMain.getInstance().getDescription().getVersion());
    }
}
