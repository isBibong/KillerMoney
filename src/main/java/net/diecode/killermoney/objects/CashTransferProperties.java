package net.diecode.killermoney.objects;

import net.diecode.killermoney.Utils;
import net.diecode.killermoney.enums.DivisionMethod;
import net.diecode.killermoney.enums.KMPermission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.UUID;

public class CashTransferProperties {

    private double percent;
    private int maxAmount;
    private double chance;
    private String permission;
    private int limit;
    private DivisionMethod divisionMethod;
    private boolean enabled;
    private HashMap<UUID, BigDecimal> limitCounter = new HashMap<UUID, BigDecimal>();

    public CashTransferProperties(double percent, int maxAmount, double chance, String permission, int limit,
                                  DivisionMethod divisionMethod, boolean enabled) {
        this.percent = percent;
        this.maxAmount = maxAmount;
        this.chance = chance;
        this.permission = permission;
        this.limit = limit;
        this.divisionMethod = divisionMethod;
        this.enabled = enabled;
    }

    public double getPercent() {
        return percent;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public double getChance() {
        return chance;
    }

    public String getPermission() {
        return permission;
    }

    public int getLimit() {
        return limit;
    }

    public DivisionMethod getDivisionMethod() {
        return divisionMethod;
    }

    public HashMap<UUID, BigDecimal> getLimitCounter() {
        return limitCounter;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean chanceGen() {
        return Utils.chanceGenerator(this.chance);
    }

    public boolean isReachedLimit(UUID uuid) {
        if (limit < 1) {
            return false;
        }

        Player player = Bukkit.getPlayer(uuid);

        if (player.hasPermission(KMPermission.BYPASS_MONEY_LIMIT.get())) {
            return false;
        }

        if (limitCounter.containsKey(uuid)) {
            BigDecimal current = limitCounter.get(uuid);

            if (current.doubleValue() >= limit) {
                return true;
            }
        }

        return false;
    }

    public void increaseLimitCounter(UUID uuid, BigDecimal money) {
        if (limit == 0) {
            return;
        }

        if (limitCounter.containsKey(uuid)) {
            BigDecimal incremented = limitCounter.get(uuid).add(money);

            limitCounter.put(uuid, incremented);
        } else {
            limitCounter.put(uuid, money);
        }
    }

    public BigDecimal getCurrentLimitValue(UUID uuid) {
        if (limitCounter.containsKey(uuid)) {
            return limitCounter.get(uuid);
        }

        return BigDecimal.ZERO;
    }
}
