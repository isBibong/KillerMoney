package net.diecode.killermoney.functions;

import net.diecode.killermoney.enums.KMPermission;
import net.diecode.killermoney.enums.RunMethod;
import net.diecode.killermoney.events.KMCCommandExecutionEvent;
import net.diecode.killermoney.objects.CCommand;
import net.diecode.killermoney.objects.CCommandProperties;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Random;

public class CCommandHandler implements Listener {

    @EventHandler (priority = EventPriority.LOW)
    public void onCustomCommand(KMCCommandExecutionEvent e) {
        if (e.isCancelled()) {
            return;
        }

        String command = e.getCCommand().getCommand();

        if (command != null) {
            command = command.replace("{player}", e.getKiller().getName());

            e.getCCommand().increaseLimitCounter(e.getKiller().getUniqueId());

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }
    }

    public static void process(CCommandProperties cCommandProperties, LivingEntity livingEntity, Player killer) {
        ArrayList<CCommand> commands = new ArrayList<CCommand>();

        if (cCommandProperties.getRunMethod() == RunMethod.ALL) {
            for (CCommand cCommand : cCommandProperties.getCCommands()) {
                commands.add(cCommand);
            }
        } else {
            CCommand cCommand = cCommandProperties.getCCommands().get(
                    new Random().nextInt(cCommandProperties.getCCommands().size()));

            commands.add(cCommand);
        }

        for (CCommand cCommand : commands) {
            if (cCommand.chanceGen()) {
                if ((cCommand.getPermission() != null && !cCommand.getPermission().isEmpty())
                        && (!killer.hasPermission(cCommand.getPermission()))) {
                    continue;
                }

                if (!hasCommandLimitBypass(killer)) {
                    if (cCommand.isReachedLimit(killer.getUniqueId())) {
                        continue;
                    }
                }

                Bukkit.getPluginManager().callEvent(new KMCCommandExecutionEvent(cCommand, killer, livingEntity));
            }
        }
    }

    public static boolean hasCommandLimitBypass(Player player) {
        return player.hasPermission(KMPermission.BYPASS_COMMAND_LIMIT.get());
    }

}
