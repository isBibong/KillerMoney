package net.diecode.killermoney.objects;

import net.diecode.killermoney.BukkitMain;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class KMPlayer {

    private static File dataDir = new File(BukkitMain.getInstance().getDataFolder(), "/players/");

    private Player player;
    private File file;
    private FileConfiguration config;

    private boolean enableMessages = true;

    public KMPlayer(Player p) {
        player = p;
        file = new File(dataDir.getPath() + "/" + p.getUniqueId().toString() + ".yml");

        if (file.exists()) {
            config = YamlConfiguration.loadConfiguration(file);

            loadConfig();
        } else {
            try {
                file.createNewFile();
                config = YamlConfiguration.loadConfiguration(file);

                config.set("Name", player.getName());
                config.set("UUID", player.getUniqueId().toString());
                config.set("Settings.Enable-messages", true);

                saveConfig();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public Player getPlayer() {
        return player;
    }

    public File getFile() {
        return file;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void saveConfig() {
        try {
            getConfig().save(getFile());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadConfig() {
        enableMessages = config.getBoolean("Settings.Enable-messages");
    }

    public boolean isEnableMessages() {
        return enableMessages;
    }

    public void setEnableMessages(boolean enableMessages) {
        this.enableMessages = enableMessages;

        getConfig().set("Settings.Enable-messages", enableMessages);
    }
}
