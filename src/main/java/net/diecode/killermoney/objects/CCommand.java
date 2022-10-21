package net.diecode.killermoney.objects;

import net.diecode.killermoney.Utils;

import java.util.HashMap;
import java.util.UUID;

public class CCommand {

    private String command;
    private String permission;
    private double chance;
    private int limit;
    private HashMap<UUID, Integer> limitCounter = new HashMap<UUID, Integer>();

    public CCommand(String command, String permission, double chance, int limit) {
        this.command = command;
        this.permission = permission;
        this.chance = chance;
        this.limit = limit;
    }

    public String getCommand() {
        return command;
    }

    public String getPermission() {
        return permission;
    }

    public double getChance() {
        return chance;
    }

    public int getLimit() {
        return limit;
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

    public void increaseLimitCounter(UUID uuid) {
        if (limit == 0) {
            return;
        }

        if (limitCounter.containsKey(uuid)) {
            int current = limitCounter.get(uuid);

            limitCounter.put(uuid, current + 1);
        } else {
            limitCounter.put(uuid, 1);
        }
    }

    public int getCurrentLimitValue(UUID uuid) {
        if (limitCounter.containsKey(uuid)) {
            return limitCounter.get(uuid);
        }

        return 0;
    }
}
