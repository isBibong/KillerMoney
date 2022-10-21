package net.diecode.killermoney.api;

import net.diecode.killermoney.configs.DefaultConfig;
import net.diecode.killermoney.configs.EntitiesConfig;
import net.diecode.killermoney.configs.LangConfig;
import net.diecode.killermoney.managers.ConfigManager;

public class ConfigAPI {

    /**
     *
     * @return Instance of config loader
     */
    public static DefaultConfig getDefaultConfig() {
        return DefaultConfig.getInstance();
    }

    /**
     *
     * @return Instance of entities config
     */
    public static EntitiesConfig getEntitiesConfig() {
        return EntitiesConfig.getInstance();
    }

    /**
     *
     * @return Instance of lang config
     */
    public static LangConfig getLangConfig() {
        return LangConfig.getInstance();
    }

    public static void reloadConfigs() {
        ConfigManager.reloadConfigs();
    }

}
