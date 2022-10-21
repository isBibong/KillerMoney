package net.diecode.killermoney.functions;

import net.diecode.killermoney.configs.DefaultConfig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.ArrayList;
import java.util.UUID;

public class AntiFarmingHandler implements Listener {

    private static ArrayList<UUID> spawnedEntities = new ArrayList<UUID>();

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER) {
            if (DefaultConfig.isAntiFarmingSpawner()) {
                spawnedEntities.add(event.getEntity().getUniqueId());

                return;
            }
        }

        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER_EGG) {
            if (DefaultConfig.isAntiFarmingSpawnerEgg()) {
                spawnedEntities.add(event.getEntity().getUniqueId());

                return;
            }
        }
    }

    public static ArrayList<UUID> getSpawnedEntities() {
        return spawnedEntities;
    }
}
