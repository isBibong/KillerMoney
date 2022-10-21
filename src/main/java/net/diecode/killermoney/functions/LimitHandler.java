package net.diecode.killermoney.functions;

import net.diecode.killermoney.BukkitMain;
import net.diecode.killermoney.Utils;
import net.diecode.killermoney.configs.DefaultConfig;
import net.diecode.killermoney.enums.LanguageString;
import net.diecode.killermoney.events.KMLimitReachedEvent;
import net.diecode.killermoney.managers.EntityManager;
import net.diecode.killermoney.managers.LanguageManager;
import net.diecode.killermoney.objects.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;

public class LimitHandler implements Listener {

    private static int minuteLeftToReset = DefaultConfig.getLimitResetTime() * 60;

    public LimitHandler() {
        Bukkit.getScheduler().runTaskTimer(BukkitMain.getInstance(), new Runnable() {
            @Override
            public void run() {
                if (minuteLeftToReset < 1) {
                    reset();

                    minuteLeftToReset = DefaultConfig.getLimitResetTime() * 60;
                } else {
                    minuteLeftToReset--;
                }
            }
        }, 20L * 60, 20L * 60);
    }

    @EventHandler
    public void onLimitReached(KMLimitReachedEvent e) {
        MessageHandler.process(e.getKiller(), LanguageManager.cGet(LanguageString.GENERAL_LIMIT_REACHED,
                Utils.getRemainingTimeHumanFormat(getMinuteLeftToReset())));
    }

    public static void reset() {
        for (Map.Entry<EntityType, EntityProperties> properties : EntityManager.getEntityProperties().entrySet()) {
            EntityProperties ep = properties.getValue();

            for (WorldProperties wp : ep.getWorldProperties()) {
                // Clear money limit counter
                if (wp.getMoneyProperties() != null) {
                    wp.getMoneyProperties().getLimitCounter().clear();
                }

                // Clear CItem limit counter
                if (wp.getCItemProperties() != null) {
                    for (CItem cItem : wp.getCItemProperties().getCItems()) {
                        cItem.getLimitCounter().clear();
                    }
                }

                // Clear CCommand limit counter
                if (wp.getCCommandProperties() != null) {
                    for (CCommand cCommand : wp.getCCommandProperties().getCCommands()) {
                        cCommand.getLimitCounter().clear();
                    }
                }

                if (wp instanceof PlayerWorldProperties) {
                    PlayerWorldProperties pwp = (PlayerWorldProperties)wp;

                    if (pwp.getCashTransferProperties() != null) {
                        pwp.getCashTransferProperties().getLimitCounter().clear();
                    }
                }
            }
        }
    }

    public static int getMinuteLeftToReset() {
        return minuteLeftToReset;
    }
}
