package net.diecode.killermoney.objects;

import net.diecode.killermoney.enums.KMCommandType;
import net.diecode.killermoney.enums.KMPermission;
import net.diecode.killermoney.enums.SenderType;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public abstract class KMSubCommand {

    protected ArrayList<KMCommandType> usable;
    protected String command;
    protected KMPermission permission;
    protected SenderType senderType;
    protected int minArgs;
    protected String usage;
    protected ArrayList<String> aliases;
    protected ArrayList<String> acceptableValues;

    public KMSubCommand(ArrayList<KMCommandType> usable, String command) {
        this.usable = usable;
        this.command = command;
    }

    public ArrayList<KMCommandType> getUsable() {
        return usable;
    }

    public String getCommand() {
        return command;
    }

    public KMPermission getPermission() {
        return permission;
    }

    public SenderType getSenderType() {
        return senderType;
    }

    public int getMinArgs() {
        return minArgs;
    }

    public String getUsage() {
        return usage;
    }

    public ArrayList<String> getAliases() {
        return aliases;
    }

    public ArrayList<String> getAcceptableValues() {
        return acceptableValues;
    }

    public abstract void run(CommandSender cs, String[] args);
}
