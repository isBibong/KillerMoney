package net.diecode.killermoney.configs;

import net.diecode.killermoney.BukkitMain;

import java.io.File;

public class PlayersConfig {

    public PlayersConfig() {
        File dir = new File(BukkitMain.getInstance().getDataFolder(), "/players/");

        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

}
