package net.diecode.killermoney.api;

import net.diecode.killermoney.enums.KMCommandType;
import net.diecode.killermoney.managers.CommandManager;
import net.diecode.killermoney.objects.KMSubCommand;

public class CommandAPI {

    /**
     *
     * @return subcommand
     */
    public static KMSubCommand getSubCommand(KMCommandType type, String command) {
        return CommandManager.getSubCommand(type, command);
    }

}
