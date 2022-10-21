package net.diecode.killermoney.configs;

import net.diecode.killermoney.Logger;
import net.diecode.killermoney.Utils;
import net.diecode.killermoney.enums.DivisionMethod;
import net.diecode.killermoney.enums.RunMethod;
import net.diecode.killermoney.managers.EntityManager;
import net.diecode.killermoney.objects.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Set;

public class EntitiesConfig extends SuperConfig {

    private static EntitiesConfig instance;

    public EntitiesConfig(String fileName) {
        super(fileName);

        instance = this;
    }

    @Override
    public void load() {
        reload();
        EntityManager.getEntityProperties().clear();

        if (getConfig().getConfigurationSection("") == null) {
            Logger.warning("entities.yml is empty. Please fix it.");

            return;
        }

        Set<String> entitySet = getConfig().getConfigurationSection("").getKeys(false);

        for (String entity : entitySet) {
            if (!Utils.inEntityEnum(entity)) {
                Logger.warning("Unknown entity type: " + entity + ". Ignoring...");

                continue;
            }

            if (getConfig().getConfigurationSection(entity) == null) {
                Logger.warning("World setup is not found at " + entity + " entity. Ignoring...");

                continue;
            }

            EntityType entityType = EntityType.valueOf(entity.toUpperCase());
            Set<String> worldSet = getConfig().getConfigurationSection(entity).getKeys(false);
            EntityProperties entityProperties = EntityManager.getEntityProperties().get(entityType);
            RunMethod globalRM = RunMethod.ALL;

            if (entityProperties == null) {
                entityProperties = new EntityProperties(entityType);
            }

            for (String world : worldSet) {
                ArrayList<String> worlds = new ArrayList<String>();
                MoneyProperties moneyProperties = null;
                CCommandProperties cCommandProperties = null;
                CItemProperties cItemProperties = null;
                CExpProperties cExpProperties = null;
                CashTransferProperties cashTransferProperties = null;

                // World value
                if (world.equalsIgnoreCase("*")) {
                    worlds.add("*");
                } else {
                    if (world.contains(",")) {
                        String tmpWorld = world.replaceAll("\\s", "");
                        String[] splittedWorlds = tmpWorld.split(",");

                        for (String w : splittedWorlds) {
                            if (Bukkit.getWorld(w) != null) {
                                worlds.add(w);
                            } else {
                                Logger.warning("World is not found: " + w + ". Ignoring...");
                            }
                        }
                    } else {
                        if (Bukkit.getWorld(world) != null) {
                            worlds.add(world);
                        } else {
                            Logger.warning("World is not found: " + world + ". Ignoring...");
                        }
                    }
                }

                if (worlds.isEmpty()) {
                    continue;
                }

                if (getConfig().isSet(entity + "." + world + ".Run-method")) {
                    String rm = getConfig().getString((entity + "." + world + ".Run-method"));

                    if (Utils.inRunMethodEnum(rm)) {
                        globalRM = RunMethod.valueOf(rm.toUpperCase());
                    } else {
                        Logger.warning("Invalid \"Run-method\" option at " + entity + " in " + world + " section: "
                                + rm + ". Using default value: \"ALL\"");
                    }
                }

                // Set up money reward
                if (getConfig().isSet(entity + "." + world + ".Money")) {
                    boolean enabled = true;
                    double chance = 100;
                    double minMoney = 0;
                    double maxMoney = 0;
                    String permission = null;
                    int limit = 0;
                    DivisionMethod divisionMethod = DivisionMethod.LAST_HIT;

                    // Enabled value
                    if (getConfig().isSet(entity + "." + world + ".Money.Enabled")) {
                        enabled = getConfig().getBoolean(entity + "." + world + ".Money.Enabled");
                    }

                    // Chance value
                    if (getConfig().isSet(entity + "." + world + ".Money.Chance")) {
                        chance = Utils.cleanChanceString(getConfig().getString(entity + "." + world + ".Money.Chance"));
                    }

                    // Money value
                    if (getConfig().isSet(entity + "." + world + ".Money.Value")) {
                        String moneyValue = getConfig().getString(entity + "." + world + ".Money.Value")
                                .replaceAll("\\s", "");

                        if (moneyValue.contains("?")) {
                            String[] splittedValue = moneyValue.split("\\?");

                            minMoney = Double.valueOf(splittedValue[0]);
                            maxMoney = Double.valueOf(splittedValue[1]);
                        } else {
                            minMoney = Double.valueOf(moneyValue);
                            maxMoney = Double.valueOf(moneyValue);
                        }
                    } else {
                        Logger.warning("Missing money value at " + entity + " entity. Money reward disabled.");
                    }

                    // Permission value
                    if (getConfig().isSet(entity + "." + world + ".Money.Permission")) {
                        permission = getConfig().getString(entity + "." + world + ".Money.Permission");
                    }

                    // Limit value
                    if (getConfig().isSet(entity + "." + world + ".Money.Limit")) {
                        limit = getConfig().getInt(entity + "." + world + ".Money.Limit");
                    }

                    // Division method value
                    if (getConfig().isSet(entity + "." + world + ".Money.Division-method")) {
                        String div = getConfig().getString(entity + "." + world + ".Money.Division-method");

                        if (Utils.inDivisionEnum(div)) {
                            divisionMethod = DivisionMethod.valueOf(div.toUpperCase());
                        } else {
                            Logger.warning("Invalid \"Division-method\" option at " + entity + " in " + world
                                    + " section: " + div + ". Using default value: \"LAST_HIT\"");
                        }
                    }

                    // Link Money Properties object
                    if ((minMoney != 0) || (maxMoney != 0)) {
                        moneyProperties = new MoneyProperties(entityType, chance, minMoney, maxMoney, permission,
                                limit, divisionMethod, enabled);
                    }
                }

                // Set up custom command
                if (getConfig().isSet(entity + "." + world + ".Custom-commands")) {
                    boolean enabled = true;
                    RunMethod localRM = RunMethod.ALL;

                    // Enabled value
                    if (getConfig().isSet(entity + "." + world + ".Custom-commands.Enabled")) {
                        enabled = getConfig().getBoolean(entity + "." + world + ".Custom-commands.Enabled");
                    }

                    if (getConfig().isSet(entity + "." + world + ".Custom-commands.Run-method")) {
                        String rm = getConfig().getString((entity + "." + world + ".Custom-commands.Run-method"));

                        if (Utils.inRunMethodEnum(rm)) {
                            localRM = RunMethod.valueOf(rm.toUpperCase());
                        } else {
                            Logger.warning("Invalid \"Run-method\" option at " + entity + " in " + world
                                    + ", \"Custom-command\" section: " + rm + ". Using default value: \"ALL\"");
                        }
                    }

                    if (getConfig().isSet(entity + "." + world + ".Custom-commands.Commands")) {
                        Set<String> numberSet = getConfig().getConfigurationSection(entity + "." + world
                                + ".Custom-commands.Commands").getKeys(false);

                        ArrayList<CCommand> commands = new ArrayList<CCommand>();

                        for (String num : numberSet) {
                            String command;
                            String permission = null;
                            double chance = 100;
                            int limit = 0;

                            // Command value
                            if (getConfig().isSet(entity + "." + world + ".Custom-commands.Commands."
                                    + num + ".Command")) {
                                command = getConfig().getString(entity + "." + world
                                        + ".Custom-commands.Commands." + num + ".Command");
                            } else {
                                Logger.warning("Missing command value at " + entity
                                        + " entity. ID: \"" + num + "\". Command disabled.");

                                continue;
                            }

                            // Permission value
                            if (getConfig().isSet(entity + "." + world + ".Custom-commands.Commands."
                                    + num + ".Permission")) {
                                permission = getConfig().getString(entity + "." + world
                                        + ".Custom-commands.Commands." + num + ".Permission");
                            }

                            // Chance value
                            if (getConfig().isSet(entity + "." + world + ".Custom-commands.Commands."
                                    + num + ".Chance")) {
                                chance = Utils.cleanChanceString(getConfig().getString(entity + "." + world
                                        + ".Custom-commands.Commands." + num + ".Chance"));
                            }

                            // Limit value
                            if (getConfig().isSet(entity + "." + world + ".Custom-commands.Commands."
                                    + num + ".Limit")) {
                                limit = getConfig().getInt(entity + "." + world
                                        + ".Custom-commands.Commands." + num + ".Limit");
                            }

                            CCommand cCommand = new CCommand(command, permission, chance, limit);

                            commands.add(cCommand);
                        }

                        if (!commands.isEmpty()) {
                            cCommandProperties = new CCommandProperties(localRM, commands, enabled);
                        }
                    } else {
                        Logger.warning("Missing commands at " + entity + " entity. Custom commands feature disabled.");
                    }
                }

                // Set up custom item drop
                if (getConfig().isSet(entity + "." + world + ".Custom-item-drop")) {
                    boolean enabled = true;
                    boolean keepItem = true;
                    RunMethod localRM = RunMethod.ALL;

                    // Enabled value
                    if (getConfig().isSet(entity + "." + world + ".Custom-item-drop.Enabled")) {
                        enabled = getConfig().getBoolean(entity + "." + world + ".Custom-item-drop.Enabled");
                    }

                    if (getConfig().isSet(entity + "." + world + ".Custom-item-drop.Keep-default-items")) {
                        keepItem = getConfig().getBoolean(entity + "." + world
                                + ".Custom-item-drop.Keep-default-items");
                    }

                    if (getConfig().isSet(entity + "." + world + ".Custom-item-drop.Run-method")) {
                        String rm = getConfig().getString((entity + "." + world + ".Custom-item-drop.Run-method"));

                        if (Utils.inRunMethodEnum(rm)) {
                            localRM = RunMethod.valueOf(rm.toUpperCase());
                        } else {
                            Logger.warning("Invalid \"Run-method\" option at " + entity + " in " + world
                                    + ", \"Custom-item-drop\" section: " + rm + ". Using default value: \"ALL\"");
                        }
                    }

                    // Item values
                    if (getConfig().isSet(entity + "." + world + ".Custom-item-drop.Items")) {
                        Set<String> numberSet = getConfig().getConfigurationSection(entity + "." + world
                                + ".Custom-item-drop.Items").getKeys(false);

                        ArrayList<CItem> items = new ArrayList<CItem>();

                        for (String num : numberSet) {
                            ItemStack itemStack;
                            int minAmount = 0;
                            int maxAmount = 0;
                            double chance = 100;
                            String permission = null;
                            int limit = 0;

                            // ItemStack value
                            if (getConfig().isSet(entity + "." + world + ".Custom-item-drop.Items."
                                    + num + ".ItemStack")) {
                                itemStack = getConfig().getItemStack(entity + "." + world
                                        + ".Custom-item-drop.Items." + num + ".ItemStack");
                            } else {
                                Logger.warning("Missing ItemStack value at " + entity
                                        + " entity. ID: \"" + num + "\". Item drop disabled.");

                                continue;
                            }

                            // Random amount
                            if (getConfig().isSet(entity + "." + world + ".Custom-item-drop.Items."
                                    + num + ".Random-amount")) {
                                String amountValue = getConfig().getString(entity + "." + world
                                        + ".Custom-item-drop.Items." + num + ".Random-amount").replaceAll("\\s", "");

                                if (amountValue.contains("?")) {
                                    String[] splittedValue = amountValue.split("\\?");

                                    minAmount = Integer.valueOf(splittedValue[0]);
                                    maxAmount = Integer.valueOf(splittedValue[1]);
                                } else {
                                    minAmount = Integer.valueOf(amountValue);
                                    maxAmount = Integer.valueOf(amountValue);
                                }

                                if (minAmount < 1) {
                                    minAmount = 1;
                                }

                                if (maxAmount > 64) {
                                    maxAmount = 64;
                                }
                            }

                            // Permission value
                            if (getConfig().isSet(entity + "." + world + ".Custom-item-drop.Items."
                                    + num + ".Permission")) {
                                permission = getConfig().getString(entity + "." + world
                                        + ".Custom-item-drop.Items." + num + ".Permission");
                            }

                            // Chance value
                            if (getConfig().isSet(entity + "." + world + ".Custom-item-drop.Items."
                                    + num + ".Chance")) {
                                chance = Utils.cleanChanceString(getConfig().getString(entity + "." + world
                                        + ".Custom-item-drop.Items." + num + ".Chance"));
                            }

                            // Limit value
                            if (getConfig().isSet(entity + "." + world + ".Custom-item-drop.Items."
                                    + num + ".Limit")) {
                                limit = getConfig().getInt(entity + "." + world + ".Custom-item-drop.Items."
                                        + num + ".Limit");
                            }

                            CItem cItem = new CItem(itemStack, minAmount, maxAmount, chance, permission, limit);

                            items.add(cItem);
                        }

                        if (!items.isEmpty()) {
                            cItemProperties = new CItemProperties(keepItem, localRM, items, enabled);
                        }
                    } else {
                        Logger.warning("Missing commands at " + entity + " entity. Custom commands feature disabled.");
                    }
                }

                // Exp drop
                if (getConfig().isSet(entity + "." + world + ".Custom-exp-drop")) {
                    boolean enabled = true;
                    double chance = 100;
                    int minAmount = 0;
                    int maxAmount = 0;
                    String permission = null;
                    int limit = 0;

                    // Enabled value
                    if (getConfig().isSet(entity + "." + world + ".Custom-exp-drop.Enabled")) {
                        enabled = getConfig().getBoolean(entity + "." + world + ".Custom-exp-drop.Enabled");
                    }

                    // Value amount
                    if (getConfig().isSet(entity + "." + world + ".Custom-exp-drop.Value")) {
                        String value = getConfig().getString(entity + "." + world + ".Custom-exp-drop.Value")
                                .replaceAll("\\s", "");

                        if (value.contains("?")) {
                            String[] splittedValue = value.split("\\?");

                            minAmount = Integer.valueOf(splittedValue[0]);
                            maxAmount = Integer.valueOf(splittedValue[1]);
                        } else {
                            minAmount = Integer.valueOf(value);
                            maxAmount = Integer.valueOf(value);
                        }
                    }

                    // Chance value
                    if (getConfig().isSet(entity + "." + world + ".Custom-exp-drop.Chance")) {
                        chance = Utils.cleanChanceString(getConfig().getString(entity + "." + world + ".Custom-exp-drop.Chance"));
                    }

                    // Permission value
                    if (getConfig().isSet(entity + "." + world + ".Custom-exp-drop.Permission")) {
                        permission = getConfig().getString(entity + "." + world + ".Custom-exp-drop.Permission");
                    }

                    // Limit value
                    if (getConfig().isSet(entity + "." + world + ".Custom-exp-drop.Limit")) {
                        limit = getConfig().getInt(entity + "." + world + ".Custom-exp-drop.Limit");
                    }

                    cExpProperties = new CExpProperties(minAmount, maxAmount, chance, permission, limit, enabled);
                }

                // Set up cash transfer
                if (entityType == EntityType.PLAYER) {
                    boolean enabled = true;
                    double percent;
                    int maxAmount = 0;
                    double chance = 100;
                    String permission = null;
                    int limit = 0;
                    DivisionMethod divisionMethod = DivisionMethod.LAST_HIT;

                    // Enabled value
                    if (getConfig().isSet(entity + "." + world + ".Cash-transfer.Enabled")) {
                        enabled = getConfig().getBoolean(entity + "." + world + ".Cash-transfer.Enabled");
                    }

                    if (getConfig().isSet(entity + "." + world + ".Cash-transfer")) {
                        if (!getConfig().isSet(entity + "." + world + ".Cash-transfer.Percent")) {
                            Logger.warning("Missing percent value in cash transfer option. Cash transfer disabled.");
                        } else {
                            percent = Utils.cleanChanceString(getConfig().getString(entity + "."
                                    + world + ".Cash-transfer.Percent"));

                            if (percent < 0) {
                                percent = 0;
                            } else if (percent > 100) {
                                percent = 100;
                            }

                            if (getConfig().isSet(entity + "." + world + ".Cash-transfer.Max-amount")) {
                                maxAmount = getConfig().getInt(entity + "." + world + ".Cash-transfer.Max-amount");
                            }

                            if (getConfig().isSet(entity + "." + world + ".Cash-transfer.Chance")) {
                                chance = Utils.cleanChanceString(getConfig().getString(entity + "."
                                        + world + ".Cash-transfer.Chance"));
                            }

                            // Permission value
                            if (getConfig().isSet(entity + "." + world + ".Cash-transfer.Permission")) {
                                permission = getConfig().getString(entity + "." + world + ".Cash-transfer.Permission");
                            }

                            // Limit value
                            if (getConfig().isSet(entity + "." + world + ".Cash-transfer.Limit")) {
                                limit = getConfig().getInt(entity + "." + world + ".Cash-transfer.Limit");
                            }

                            // Division method value
                            if (getConfig().isSet(entity + "." + world + ".Cash-transfer.Division-method")) {
                                String div = getConfig().getString(entity + "." + world
                                        + ".Cash-transfer.Division-method");

                                if (Utils.inDivisionEnum(div)) {
                                    divisionMethod = DivisionMethod.valueOf(div.toUpperCase());
                                } else {
                                    Logger.warning("Invalid \"Division-method\" option at " + entity + " in " + world
                                            + " section: " + div + ". Using default value: \"LAST_HIT\"");
                                }
                            }

                            if (percent > 0) {
                                cashTransferProperties = new CashTransferProperties(percent, maxAmount, chance,
                                        permission, limit, divisionMethod, enabled);
                            }
                        }
                    }
                }

                WorldProperties worldProperties;

                if (entityType != EntityType.PLAYER) {
                    worldProperties = new WorldProperties(worlds, moneyProperties,
                            cCommandProperties, cItemProperties, cExpProperties);
                } else {
                    worldProperties = new PlayerWorldProperties(worlds, moneyProperties, cCommandProperties,
                            cItemProperties, cashTransferProperties, cExpProperties);
                }

                entityProperties.getWorldProperties().add(worldProperties);
            }

            EntityManager.getEntityProperties().put(entityType, entityProperties);
        }
    }

    public static EntitiesConfig getInstance() {
        return instance;
    }
}
