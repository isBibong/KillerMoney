package net.diecode.killermoney.managers;

import net.diecode.killermoney.BukkitMain;
import org.bukkit.entity.Player;

import java.math.BigDecimal;

public class EconomyManager {

    public static void deposit(Player player, double amount) {
        BukkitMain.getEconomy().depositPlayer(player, amount);
    }

    public static void deposit(Player player, BigDecimal amount) {
        BukkitMain.getEconomy().depositPlayer(player, amount.doubleValue());
    }

    public static void withdraw(Player player, double amount) {
        BukkitMain.getEconomy().withdrawPlayer(player, amount);
    }

    public static void withdraw(Player player, BigDecimal amount) {
        BukkitMain.getEconomy().withdrawPlayer(player, amount.doubleValue());
    }

}
