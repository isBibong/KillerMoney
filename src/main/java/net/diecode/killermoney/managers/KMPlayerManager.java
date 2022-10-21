package net.diecode.killermoney.managers;

import net.diecode.killermoney.objects.KMPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.UUID;

public class KMPlayerManager implements Listener {

    public static HashMap<UUID, KMPlayer> kmPlayers = new HashMap<>();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        UUID pUUID = e.getPlayer().getUniqueId();

        if (!kmPlayers.containsKey(pUUID)) {
            kmPlayers.put(pUUID, new KMPlayer(e.getPlayer()));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        UUID pUUID = e.getPlayer().getUniqueId();

        if (kmPlayers.containsKey(pUUID)) {
            kmPlayers.remove(pUUID);
        }
    }

    public static HashMap<UUID, KMPlayer> getKMPlayers() {
        return kmPlayers;
    }

    public static KMPlayer getKMPlayer(Player p) {
        return kmPlayers.get(p.getUniqueId());
    }
}
