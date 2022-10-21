package net.diecode.killermoney.functions;

import net.diecode.killermoney.BukkitMain;
import net.diecode.killermoney.configs.DefaultConfig;
import net.diecode.killermoney.enums.DivisionMethod;
import net.diecode.killermoney.enums.KMPermission;
import net.diecode.killermoney.enums.LanguageString;
import net.diecode.killermoney.events.*;
import net.diecode.killermoney.managers.EconomyManager;
import net.diecode.killermoney.managers.LanguageManager;
import net.diecode.killermoney.objects.CashTransferProperties;
import net.diecode.killermoney.objects.EntityDamage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.math.BigDecimal;
import java.util.ArrayList;

public class CashTransferHandler implements Listener {

    @EventHandler
    public void onCashTransfer(KMCashTransferProcessorEvent e) {
        if (e.isCancelled()) {
            return;
        }

        BigDecimal moneySum = BigDecimal.ZERO;

        // For loop with all entity damagers
        for (EntityDamage ed : e.getDamagers()) {
            Player killer = Bukkit.getPlayer(ed.getPlayerUUID());

            if (killer == null) {
                continue;
            }

            moneySum = moneySum.add(ed.getCalculatedMoney());

            if (DefaultConfig.isMoneyItemDropEnabled()) {
                Bukkit.getPluginManager().callEvent(
                        new KMMoneyItemDropEvent(MoneyHandler.generateItemStack(ed.getCalculatedMoney(),
                                e.getVictim().getType(), killer), e.getVictim().getLocation()));

            } else {
                Bukkit.getPluginManager().callEvent(
                        new KMEarnMoneyCashTransferDepositEvent(e.getCashTransferProperties(), killer,
                                ed.getCalculatedMoney(), e.getVictim(), ed.getDamage()));
            }
        }

        moneySum = moneySum.setScale(DefaultConfig.getDecimalPlaces(), BigDecimal.ROUND_HALF_EVEN);

        Bukkit.getPluginManager().callEvent(new KMLoseMoneyCashTransferEvent(e.getCashTransferProperties(),
                e.getDamagers(), e.getVictim(), moneySum));
    }

    @EventHandler
    public void onEarnMoneyInstantDeposit(KMEarnMoneyCashTransferDepositEvent e) {
        if (e.isCancelled()) {
            return;
        }

        // Increase value of the limiter
        if (!hasMoneyLimitBypass(e.getKiller())) {
            e.getMoneyProperties().increaseLimitCounter(e.getKiller().getUniqueId(), e.getAmount());
        }

        // Deposit money
        EconomyManager.deposit(e.getKiller(), e.getAmount().doubleValue());

        // Send money earning message to player
        if (e.getKiller() != null && e.getKiller().isOnline()) {
            String message = LanguageManager.cGet(LanguageString.GENERAL_YOU_KILLED_A_PLAYER,
                    e.getVictim().getName(), e.getAmount().doubleValue(), e.getDamage().doubleValue());

            MessageHandler.process(e.getKiller(), message);
        }

    }

    @EventHandler
    public void onLoseMoneyCashTransfer(KMLoseMoneyCashTransferEvent e) {
        if (e.isCancelled()) {
            return;
        }

        StringBuilder killers = new StringBuilder();

        // Withdraw money
        EconomyManager.withdraw(e.getVictim(), e.getAmount());

        for (int i = 0; i < e.getDamagers().size(); i++) {
            EntityDamage ed = e.getDamagers().get(i);
            OfflinePlayer p = Bukkit.getOfflinePlayer(ed.getPlayerUUID());

            killers.append(p.getName());

            if (i < e.getDamagers().size() - 1) {
                killers.append(", ");
            }
        }

        // Send money losing message to player
        if (e.getVictim() != null && e.getVictim().isOnline()) {
            String message = LanguageManager.cGet(LanguageString.GENERAL_YOU_KILLED_BY_PLAYER, killers.toString(),
                    e.getAmount().doubleValue());

            MessageHandler.process(e.getVictim(), message);
        }

    }

    public static void process(CashTransferProperties ctp, ArrayList<EntityDamage> damagers, Player killer,
                               Player victim) {
        if (BukkitMain.getEconomy() == null) {
            return;
        }

        if (!ctp.chanceGen()) {
            return;
        }

        BigDecimal victimsMoney = new BigDecimal(BukkitMain.getEconomy().getBalance(victim));
        BigDecimal money = victimsMoney.divide(new BigDecimal(100), BigDecimal.ROUND_HALF_EVEN).multiply(
                new BigDecimal(ctp.getPercent())).setScale(DefaultConfig.getDecimalPlaces(), BigDecimal.ROUND_HALF_EVEN);

        if (ctp.getMaxAmount() != 0 && ctp.getMaxAmount() < money.intValue()) {
            money = new BigDecimal(ctp.getMaxAmount()).setScale(DefaultConfig.getDecimalPlaces(),
                    BigDecimal.ROUND_HALF_EVEN);
        }

        BigDecimal dividedMoney = BigDecimal.ZERO;
        BigDecimal remainingLimit;
        BigDecimal playerLimit;
        ArrayList<EntityDamage> filteredDamagers = new ArrayList<EntityDamage>();
        boolean hasLimitBypass = false;

            // Division method is SHARED
            if (ctp.getDivisionMethod() == DivisionMethod.SHARED) {
                for (EntityDamage ed : damagers) {
                    Player p = Bukkit.getPlayer(ed.getPlayerUUID());

                    if (p != null) {
                        if ((ctp.getPermission() != null && !ctp.getPermission().isEmpty())
                                && (!p.hasPermission(ctp.getPermission()))) {
                            continue;
                        }

                        hasLimitBypass = hasMoneyLimitBypass(p);
                    }

                    if (!hasLimitBypass) {
                        if (ctp.isReachedLimit(ed.getPlayerUUID())) {
                            if (DefaultConfig.isReachedLimitMessage()) {
                                Bukkit.getPluginManager().callEvent(new KMLimitReachedEvent(p, victim));
                            }

                            continue;
                        }
                    }

                    // Calculating the money
                    BigDecimal x = ed.getDamage().divide(new BigDecimal(victim.getMaxHealth()),
                            BigDecimal.ROUND_HALF_EVEN);

                    // Calculating remaining limit
                    remainingLimit = new BigDecimal(ctp.getLimit() - ctp.getCurrentLimitValue(ed.getPlayerUUID())
                            .doubleValue()).setScale(DefaultConfig.getDecimalPlaces(), BigDecimal.ROUND_HALF_EVEN);

                    // Divided money by victim damage
                    dividedMoney = new BigDecimal(money.multiply(x).doubleValue());

                    // Use multipliers ( Permission based and global )
                    //dividedMoney = dividedMoney.multiply(new BigDecimal(getMoneyMultiplier(p)));
                    //dividedMoney = dividedMoney.multiply(new BigDecimal(MultiplierHandler.getGlobalMultiplier()));

                    if (!hasLimitBypass) {
                        if ((ctp.getLimit() > 0) && (dividedMoney.compareTo(remainingLimit) == 1)) {
                            dividedMoney = remainingLimit;
                        }
                    }

                    // Set scale
                    dividedMoney = dividedMoney.setScale(DefaultConfig.getDecimalPlaces(), BigDecimal.ROUND_HALF_EVEN);

                    ed.setCalculatedMoney(dividedMoney);
                    filteredDamagers.add(ed);
                }
            } else {
                // Division method is LAST_HIT
                if ((ctp.getPermission() != null && !ctp.getPermission().isEmpty())
                        && (!killer.hasPermission(ctp.getPermission()))) {
                    return;
                }

                if (!hasMoneyLimitBypass(killer)) {
                    if (ctp.isReachedLimit(killer.getUniqueId())) {
                        if (DefaultConfig.isReachedLimitMessage()) {
                            Bukkit.getPluginManager().callEvent(new KMLimitReachedEvent(killer, victim));
                        }

                        return;
                    }
                }

                // Calculating remaining limit
                remainingLimit = new BigDecimal(ctp.getLimit() - ctp.getCurrentLimitValue(killer.getUniqueId())
                        .doubleValue()).setScale(DefaultConfig.getDecimalPlaces(), BigDecimal.ROUND_HALF_EVEN);

                dividedMoney = money;

                if (!hasMoneyLimitBypass(killer)) {
                    if ((ctp.getLimit() > 0) && (money.compareTo(remainingLimit) == 1)) {
                        dividedMoney = remainingLimit;
                    }
                }

                // Set scale
                dividedMoney = dividedMoney.setScale(DefaultConfig.getDecimalPlaces(), BigDecimal.ROUND_HALF_EVEN);

                filteredDamagers.add(new EntityDamage(killer.getUniqueId(), new BigDecimal(victim.getMaxHealth()),
                        dividedMoney));
            }

            if (!filteredDamagers.isEmpty()) {
                Bukkit.getPluginManager().callEvent(new KMCashTransferProcessorEvent(ctp, filteredDamagers, killer,
                        victim));
            }
    }

    public static boolean hasMoneyLimitBypass(Player player) {
        return player.hasPermission(KMPermission.BYPASS_MONEY_LIMIT_CASH_TRANSFER.get());
    }

}
