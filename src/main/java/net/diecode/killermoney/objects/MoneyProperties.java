package net.diecode.killermoney.objects;

import net.diecode.killermoney.Utils;
import net.diecode.killermoney.configs.DefaultConfig;
import net.diecode.killermoney.enums.DivisionMethod;
import net.diecode.killermoney.enums.KMPermission;
import net.diecode.killermoney.functions.MultiplierHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.UUID;

public class MoneyProperties {

    private EntityType entityType;
    private double chance;
    private double minMoney;
    private double maxMoney;
    private String permission;
    private int limit;
    private DivisionMethod divisionMethod;
    private boolean enabled;
    private HashMap<UUID, BigDecimal> limitCounter = new HashMap<UUID, BigDecimal>();

    public MoneyProperties(EntityType entityType, double chance, double minMoney, double maxMoney, String permission,
                           int limit, DivisionMethod divisionMethod, boolean enabled) {
        this.entityType = entityType;
        this.chance = chance;
        this.minMoney = minMoney;
        this.maxMoney = maxMoney;
        this.permission = permission;
        this.limit = limit;
        this.divisionMethod = divisionMethod;
        this.enabled = enabled;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public double getChance() {
        return chance;
    }

    public double getMinMoney() {
        return minMoney;
    }

    public double getMaxMoney() {
        return maxMoney;
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

    public boolean isEnabled() {
        return enabled;
    }

    public HashMap<UUID, BigDecimal> getLimitCounter() {
        return limitCounter;
    }

    public boolean chanceGen() {
        return Utils.chanceGenerator(this.chance);
    }

    public boolean isReachedLimit(UUID uuid) {
        if (limit < 1) {
            return false;
        }

        Player player = Bukkit.getPlayer(uuid);

        if (player != null) {
            if (player.hasPermission(KMPermission.BYPASS_MONEY_LIMIT.get())) {
                return false;
            }
        }

        BigDecimal limit = getLimit(player);

        if (limitCounter.containsKey(uuid)) {
            BigDecimal current = limitCounter.get(uuid);

            if (current.compareTo(limit) >= 0) {
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

    public BigDecimal getLimit(Player player) {
        if (player == null) {
            return new BigDecimal(limit);
        }

        return new BigDecimal(limit * MultiplierHandler.getPermBasedMoneyLimitMultiplier(player))
                .setScale(DefaultConfig.getDecimalPlaces(), BigDecimal.ROUND_HALF_EVEN);
    }
}
