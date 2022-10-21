package net.diecode.killermoney.configs;

import net.diecode.killermoney.BukkitMain;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public abstract class SuperConfig {

    private String fileName;
    private File file;
    private FileConfiguration config;

    public SuperConfig(String fileName) {
        this.fileName = fileName;
        this.file = new File(BukkitMain.getInstance().getDataFolder(), this.fileName);

        try {
            if (!this.file.exists()) {
                file.getParentFile().mkdirs();

                Files.copy(BukkitMain.getInstance().getResource("resources/" + this.fileName), file.toPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        config = YamlConfiguration.loadConfiguration(this.file);
    }

    public void saveConfig() {
        try {
            getConfig().save(getFile());
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
    }

    public File getFile() {
        return file;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public abstract void load();

    public void reload() {
        config = YamlConfiguration.loadConfiguration(this.file);
    }
}
