package net.diecode.killermoney.objects;

import net.diecode.killermoney.Utils;

import java.util.HashMap;
import java.util.UUID;

public class CExpProperties {

    private int minAmount;
    private int maxAmount;
    private double chance;
    private String permission;
    private int limit;
    private boolean enabled;
    private HashMap<UUID, Integer> limitCounter = new HashMap<UUID, Integer>();

    public CExpProperties(int minAmount, int maxAmount, double chance, String permission, int limit, boolean enabled) {
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.chance = chance;
        this.permission = permission;
        this.limit = limit;
        this.enabled = enabled;
    }

    public int getMinAmount() {
        return minAmount;
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

    public boolean isEnabled() {
        return enabled;
    }

    public boolean chanceGen() {
        return Utils.chanceGenerator(this.chance);
    }

    public HashMap<UUID, Integer> getLimitCounter() {
        return limitCounter;
    }

    public boolean isReachedLimit(UUID uuid) {
        if (limit < 1) {
            return false;
        }

        if (limitCounter.containsKey(uuid)) {
            int current = limitCounter.get(uuid);

            if (current >= limit) {
                return true;
            }
        }

        return false;
    }

    public void increaseLimitCounter(UUID uuid, int counter) {
        if (limit == 0) {
            return;
        }

        if (limitCounter.containsKey(uuid)) {
            int current = limitCounter.get(uuid);

            limitCounter.put(uuid, current + counter);
        } else {
            limitCounter.put(uuid, counter);
        }
    }

    public int getCurrentLimitValue(UUID uuid) {
        if (limitCounter.containsKey(uuid)) {
            return limitCounter.get(uuid);
        }

        return 0;
    }
}
