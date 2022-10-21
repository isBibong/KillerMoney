package net.diecode.killermoney.functions;

import net.diecode.killermoney.BukkitMain;
import net.diecode.killermoney.Utils;
import net.diecode.killermoney.configs.DefaultConfig;
import net.diecode.killermoney.enums.KMPermission;
import net.diecode.killermoney.enums.LanguageString;
import net.diecode.killermoney.events.KMGlobalMultiplierChangeEvent;
import net.diecode.killermoney.managers.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;

public class MultiplierHandler implements Listener {

    private static double globalMultiplier = 1;
    private static BukkitTask timer;
    private static int minuteLeft;

    @EventHandler
    public void onGlobalMultiplierChange(KMGlobalMultiplierChangeEvent e) {
        if (e.isCancelled()) {
            return;
        }

        if (e.getNewValue() == 1) {
            cancel();

            LanguageManager.send(e.getSender(), LanguageString.MULTIPLIER_CANCEL_CUSTOM_MULTIPLIER);
        } else {
            set(e.getNewValue(), e.getMinute());

            LanguageManager.send(e.getSender(), LanguageString.MULTIPLIER_SET_NEW_MULTIPLIER_VALUE,
                    MultiplierHandler.getGlobalMultiplier(), Utils.getRemainingTimeHumanFormat(e.getMinute()));
        }
    }

    private static void set(final double newMultiplier, int minute) {
        if (timer != null) {
            timer.cancel();
        }

        globalMultiplier = newMultiplier;
        minuteLeft = minute;

        timer = Bukkit.getScheduler().runTaskTimer(BukkitMain.getInstance(), new Runnable() {
            @Override
            public void run() {
                minuteLeft--;

                if (minuteLeft < 1) {
                    cancel();
                }
            }
        }, 20L * 60, 20L * 60);
    }

    private static void cancel() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        globalMultiplier = 1;
        minuteLeft = 0;
    }

    public static double getPermBasedMoneyMultiplier(Player p) {
        double biggest = 1;

        if (p != null) {
            for (Map.Entry<String, Double> multiplier : DefaultConfig.getPermBasedMoneyMultipliers().entrySet()) {
                if (p.hasPermission(KMPermission.MONEY_MULTIPLIER.get() + "." + multiplier.getKey())) {
                    if (biggest < multiplier.getValue()) {
                        biggest = multiplier.getValue();
                    }
                }
            }
        }

        return biggest;
    }

    public static double getPermBasedMoneyLimitMultiplier(Player p) {
        double biggest = 1;

        if (p != null) {
            for (Map.Entry<String, Double> multiplier : DefaultConfig.getPermBasedLimitMultipliers().entrySet()) {
                if (p.hasPermission(KMPermission.LIMIT_MONEY_MULTIPLIER.get() + "." + multiplier.getKey())) {
                    if (biggest < multiplier.getValue()) {
                        biggest = multiplier.getValue();
                    }
                }
            }
        }

        return biggest;
    }

    public static double getGlobalMultiplier() {
        return globalMultiplier;
    }

    public static BukkitTask getTimer() {
        return timer;
    }

    public static int getMinuteLeft() {
        return minuteLeft;
    }
}
