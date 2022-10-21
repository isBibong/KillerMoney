package net.diecode.killermoney.managers;

import net.diecode.killermoney.configs.DefaultConfig;
import net.diecode.killermoney.configs.EntitiesConfig;
import net.diecode.killermoney.configs.LangConfig;
import net.diecode.killermoney.configs.PlayersConfig;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

public class ConfigManager {

    private static DefaultConfig defaultConfig;
    private static EntitiesConfig entitiesConfig;
    private static LangConfig langConfig;
    private static PlayersConfig playersConfig;

    public static void init() {
        defaultConfig = new DefaultConfig("config.yml");
        entitiesConfig = new EntitiesConfig("entities.yml");
        langConfig = new LangConfig("lang.yml");
        playersConfig = new PlayersConfig();

        checkVersionChanges();

        reloadConfigs();
    }

    private static void checkVersionChanges() {
        int version = DefaultConfig.getConfigVersion();

        if (version < 1) {
            DefaultConfig.getInstance().getConfig().set("Global-settings.Money.Item-drop.Anyone-can-pick-it-up", true);
            setEntityData("PARROT", "Parrot");

            version++;
        }

        if (version < 2) {
            setEntityData("COD", "Cod");
            setEntityData("DOLPHIN", "Dolphin");
            setEntityData("DROWNED", "Drowned");
            setEntityData("PHANTOM", "Phantom");
            setEntityData("PUFFERFISH", "Pufferfish");
            setEntityData("SALMON", "Salmon");
            setEntityData("TROPICAL_FISH", "Tropical fish");
            setEntityData("TURTLE", "Turtle");

            version++;
        }

        if (version < 3) {
            setEntityData("MAGMA_CUBE", "Magma cube");
            setEntityData("VINDICATOR", "Vindicator");

            version++;
        }

        if (version < 4) {
            setEntityData("CAT", "Cat");
            setEntityData("FOX", "Fox");
            setEntityData("PANDA", "Panda");
            setEntityData("PILLAGER", "Pillager");
            setEntityData("RAVAGER", "Ravager");
            setEntityData("TRADER_LLAMA", "Trader llama");
            setEntityData("WANDERING_TRADER", "Wandering trader");

            version++;
        }

        if (version < 5) {
            setEntityData("BEE", "Bee");

            version++;
        }

        if (version < 6) {
            setEntityData("HOGLIN", "Hoglin");
            setEntityData("PIGLIN", "Piglin");
            setEntityData("STRIDER", "Strider");
            setEntityData("ZOGLIN", "Zoglin");

            version++;
        }

        if (version != DefaultConfig.getConfigVersion()) {
            setConfigVersion(version);
        }

        DefaultConfig.getInstance().saveConfig();
    }

    private static void setConfigVersion(int newVersion) {
        DefaultConfig.getInstance().getConfig().set("Config-version", newVersion);
        DefaultConfig.getInstance().saveConfig();
    }

    private static void setEntityData(String entity, String langData) {
        FileConfiguration langCfg   = LangConfig.getInstance().getConfig();
        FileConfiguration entityCfg = EntitiesConfig.getInstance().getConfig();

        langCfg.set("ENTITIES." + entity, langData);

        entityCfg.set(entity + ".*.Money.Enabled", false);
        entityCfg.set(entity + ".*.Money.Chance", "100%");
        entityCfg.set(entity + ".*.Money.Value", "1 ? 100");

        entityCfg.set(entity + ".*.Custom-commands.Enabled", false);
        entityCfg.set(entity + ".*.Custom-commands.Commands.1.Command", "say {player} killed an entity!");
        entityCfg.set(entity + ".*.Custom-commands.Commands.1.Chance", "100%");

        entityCfg.set(entity + ".*.Custom-item-drop.Enabled", false);
        entityCfg.set(entity + ".*.Custom-item-drop.Keep-default-items", false);
        entityCfg.set(entity + ".*.Custom-item-drop.Items.1.ItemStack", new ItemStack(Material.GOLD_NUGGET));
        entityCfg.set(entity + ".*.Custom-item-drop.Items.1.Random-amount", "1 ? 5");
        entityCfg.set(entity + ".*.Custom-item-drop.Items.1.Chance", "100%");

        entityCfg.set(entity + ".*.Custom-exp-drop.Enabled", false);
        entityCfg.set(entity + ".*.Custom-exp-drop.Value", "10");
        entityCfg.set(entity + ".*.Custom-exp-drop.Chance", "100%");

        LangConfig.getInstance().saveConfig();
        EntitiesConfig.getInstance().saveConfig();
    }

    public static void reloadConfigs() {
        DefaultConfig.getInstance().load();
        EntitiesConfig.getInstance().load();
        LangConfig.getInstance().load();
    }

    public static DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    public static EntitiesConfig getEntitiesConfig() {
        return entitiesConfig;
    }

    public static LangConfig getLangConfig() {
        return langConfig;
    }
}
