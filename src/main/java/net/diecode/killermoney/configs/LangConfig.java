package net.diecode.killermoney.configs;

import net.diecode.killermoney.enums.LanguageString;
import org.bukkit.ChatColor;

import java.util.HashMap;

public class LangConfig extends SuperConfig {

    private static LangConfig instance;
    private static HashMap<LanguageString, String> strings = new HashMap<LanguageString, String>();

    public LangConfig(String fileName) {
        super(fileName);

        instance = this;
    }

    @Override
    public void load() {
        reload();

        generate();
        strings.clear();

        if (getConfig().getConfigurationSection("") == null) {
            return;
        }

        for (String parent : getConfig().getConfigurationSection("").getKeys(false)) {
            if (getConfig().getConfigurationSection(parent) == null) {
                continue;
            }

            for (String langString : getConfig().getConfigurationSection(parent).getKeys(false)) {
                String str = (parent + "." + langString).replaceFirst("\\.", "_").toUpperCase();

                if (!LanguageString.isValid(str)) {
                    continue;
                }

                strings.put(LanguageString.valueOf(str),
                        ChatColor.translateAlternateColorCodes('&', getConfig().getString(parent + "." + langString)));
            }
        }
    }

    private void generate() {
        for (LanguageString ls : LanguageString.values()) {
            int firstLine = ls.name().indexOf("_");
            String parent = ls.name().substring(0, firstLine);
            String post = ls.name().substring(firstLine + 1, ls.name().length());

            if (!getConfig().isSet(parent + "." + post)) {
                getConfig().set(parent + "." + post, ls.getString());
            }

            saveConfig();
        }
    }

    public static LangConfig getInstance() {
        return instance;
    }

    public static HashMap<LanguageString, String> getStrings() {
        return strings;
    }
}
