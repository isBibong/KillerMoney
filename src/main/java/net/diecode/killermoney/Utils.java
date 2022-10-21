package net.diecode.killermoney;

import net.diecode.killermoney.enums.DivisionMethod;
import net.diecode.killermoney.enums.RunMethod;

import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.Random;

public class Utils {

    public static boolean chanceGenerator(double chance) {
        return (Math.random() * 100) < chance;
    }

    public static double randomNumber(double min, double max) {
        if (min == max) {
            return min;
        }

        double randomNumber = new Random().nextDouble();

        if (max > min) {
            return min + (max - min) * randomNumber;
        }

        if (min > max) {
            return max + (min - max) * randomNumber;
        }

        return 0;
    }

    public static int randomNumber(int min, int max) {
        if (min == max) {
            return min;
        }

        return new Random().nextInt(max - min) + min;
    }

    public static boolean inEntityEnum(String entity) {
        for (EntityType et : EntityType.values()) {
            if (et.name().equalsIgnoreCase(entity)) {
                return true;
            }
        }

        return false;
    }

    public static boolean inDivisionEnum(String division) {
        for (DivisionMethod div : DivisionMethod.values()) {
            if (div.name().equalsIgnoreCase(division)) {
                return true;
            }
        }

        return false;
    }

    public static boolean inRunMethodEnum(String runMethod) {
        for (RunMethod rm : RunMethod.values()) {
            if (rm.name().equalsIgnoreCase(runMethod)) {
                return true;
            }
        }

        return false;
    }

    public static double cleanChanceString(String str) {
        double chance = Double.valueOf(str.replaceAll("%", ""));

        if (chance > 100) {
            chance = 100;
        }

        if (chance < 1) {
            chance = 1;
        }

        return chance;
    }

    /*
    public static int getMoneyMultiplier(Player p) {
        int multiplier = 1;

        for (PermissionAttachmentInfo pai : p.getEffectivePermissions()) {
            String perm = pai.getPermission().toLowerCase();

            if (perm.contains(Permissions.MONEY_MULTIPLIER.getPerm())) {
                perm = perm.replace(Permissions.MONEY_MULTIPLIER.getPerm() + ".", "");
                int tmpMultiplier = Integer.valueOf(perm);

                if (tmpMultiplier > multiplier) {
                    multiplier = tmpMultiplier;
                }
            }
        }

        return multiplier;
    }
    */

    public static String getRemainingTimeHumanFormat(int remainingTime) {
        final int MINUTES_IN_DAY = 1440;
        final int MINUTES_IN_HOUR = 60;

        int minutes = remainingTime;

        int days = minutes / MINUTES_IN_DAY;
        minutes = minutes - (days * MINUTES_IN_DAY);

        int hours = minutes / MINUTES_IN_HOUR;
        minutes = minutes - (hours * MINUTES_IN_HOUR);

        StringBuilder sb = new StringBuilder();

        if (days > 0) {
            if (days == 1) {
                sb.append(days + " day");
            } else {
                sb.append(days + " days");
            }
            if (hours > 0 || minutes > 0) {
                sb.append(", ");
            }
        }

        if (hours > 0) {
            if (hours == 1) {
                sb.append(hours + " hour");
            } else {
                sb.append(hours + " hours");
            }
            if (minutes > 0) {
                sb.append(", ");
            }
        }

        if (minutes > 0) {
            if (minutes == 1) {
                sb.append(minutes + " minute");
            } else {
                sb.append(minutes + " minutes");
            }
        }

        if (sb.length() == 0) {
            sb.append("< 1 minute");
        }

        return sb.toString();
    }

    public static boolean isValidInput(ArrayList<String> list, String value) {
        for (String s : list) {
            if (s.equalsIgnoreCase(value)) {
                return true;
            }
        }

        return false;
    }

    public static boolean getBoolean(String value) {
        if (value.equalsIgnoreCase("on") || value.equalsIgnoreCase("true")
                || value.equalsIgnoreCase("enable") || value.equalsIgnoreCase("1")) {
            return true;
        }

        return false;
    }

}
