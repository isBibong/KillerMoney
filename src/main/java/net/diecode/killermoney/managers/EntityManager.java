package net.diecode.killermoney.managers;

import net.diecode.killermoney.BukkitMain;
import net.diecode.killermoney.configs.DefaultConfig;
import net.diecode.killermoney.functions.*;
import net.diecode.killermoney.objects.*;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class EntityManager implements Listener {

    private static HashMap<EntityType, EntityProperties> entityProperties = new HashMap<EntityType, EntityProperties>();
    private static HashMap<UUID, ArrayList<EntityDamage>> entityDamages = new HashMap<UUID, ArrayList<EntityDamage>>();

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        Player killerP = e.getEntity().getKiller();
        Player victimP = null;
        LivingEntity victim = e.getEntity();
        World world = victim.getWorld();
        EntityType victimType = e.getEntityType();
        EntityProperties ep = getEntityProperties(victimType);

        // Anti farming check
        if (AntiFarmingHandler.getSpawnedEntities().contains(victim.getUniqueId())) {
            AntiFarmingHandler.getSpawnedEntities().remove(victim.getUniqueId());

            return;
        }

        // Major criteria check
        if (!isCriteriaOk(ep, victim.getWorld().getName())) {
            return;
        }

        // The killer is not a player
        if (killerP == null) {
            return;
        }

        // Check player gamemode
        if (!DefaultConfig.getAllowedGameModes().contains(killerP.getGameMode())) {
            return;
        }

        if (victimType == EntityType.PLAYER) {
            victimP = (Player)victim;

            if (killerP.equals(victimP)) {
                return;
            }
        }

        // MobArena support
        if ((BukkitMain.getMobArenaHandler() != null) && DefaultConfig.isDisableRewardsInArena()) {
            if (BukkitMain.getMobArenaHandler().isPlayerInArena(killerP)) {
                return;
            }
        }

        for (WorldProperties wp : ep.getWorldProperties()) {
            if (wp.getWorlds().contains("*") || wp.getWorlds().contains(world.getName())) {
                ArrayList<EntityDamage> damagers = entityDamages.get(victim.getUniqueId());

                // Money reward
                if (wp.getMoneyProperties() != null && wp.getMoneyProperties().isEnabled()) {

                    MoneyHandler.process(wp.getMoneyProperties(), damagers, victim, killerP);
                }

                // Custom item drop reward
                if (wp.getCItemProperties() != null && wp.getCItemProperties().isEnabled()) {

                    if (!wp.getCItemProperties().isKeepDefaultItems()) {
                        e.getDrops().clear();
                    }

                    CItemHandler.process(wp.getCItemProperties(), victim, killerP, victim.getLocation());
                }

                // Custom command execution reward
                if (wp.getCCommandProperties() != null && wp.getCCommandProperties().isEnabled()) {

                    CCommandHandler.process(wp.getCCommandProperties(), victim, killerP);
                }

                // Custom exp drop
                if (wp.getCExpProperties() != null && wp.getCExpProperties().isEnabled()) {
                    e.setDroppedExp(0);

                    CExpHandler.process(wp.getCExpProperties(), victim.getLocation(), killerP);
                }

                // Cash transfer
                if (wp instanceof PlayerWorldProperties) {
                    PlayerWorldProperties pwp = (PlayerWorldProperties)wp;

                    if (pwp.getCashTransferProperties() != null && pwp.getCashTransferProperties().isEnabled()) {
                        CashTransferHandler.process(pwp.getCashTransferProperties(), damagers, killerP, victimP);
                    }
                }
            }
        }

        removeEntityFromDamages(victim.getUniqueId());
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof LivingEntity)) {
            return;
        }

        Player damager = null;
        LivingEntity entity = (LivingEntity)e.getEntity();
        EntityProperties ep = getEntityProperties(entity.getType());

        if (e.getDamager() instanceof Player) {
            damager = (Player)e.getDamager();
        }

        if (e.getDamager() instanceof Projectile) {
            Projectile shoot = (Projectile)e.getDamager();

            if (shoot.getShooter() instanceof Player) {
                damager = (Player)shoot.getShooter();
            }
        }

        if (damager == null) {
           return;
        }

        if (!damager.equals(entity)) {
            if (isCriteriaOk(ep, entity.getWorld().getName())) {
                increaseDamage(entity, damager.getUniqueId(), e.getDamage());
            }
        }
    }

    public static void increaseDamage(LivingEntity entity, UUID uuid, double dmg) {
        UUID euuid = entity.getUniqueId();

        if (entity.getHealth() < dmg) {
            dmg = entity.getHealth();
        }

        BigDecimal damage = new BigDecimal(dmg).setScale(2, BigDecimal.ROUND_HALF_EVEN);

        if (entityDamages.containsKey(euuid)) {
            ArrayList<EntityDamage> damages = entityDamages.get(euuid);

            for (EntityDamage ed : damages) {
                if (ed.getPlayerUUID().equals(uuid)) {
                    ed.increaseDamage(damage);

                    return;
                }
            }

            damages.add(new EntityDamage(uuid, damage));
        } else {
            ArrayList<EntityDamage> damages = new ArrayList<EntityDamage>();

            damages.add(new EntityDamage(uuid, damage));

            entityDamages.put(euuid, damages);
        }
    }

    public static void removeEntityFromDamages(UUID euuid) {
        if (entityDamages.containsKey(euuid)) {
            entityDamages.remove(euuid);
        }
    }

    public static boolean isCriteriaOk(EntityProperties ep, String world) {
        if (ep == null) {
            return false;
        }

        for (WorldProperties wp : ep.getWorldProperties()) {
            if (wp.getWorlds().contains("*") || wp.getWorlds().contains(world)) {
                return true;
            }

        }

        return false;
    }

    public static HashMap<EntityType, EntityProperties> getEntityProperties() {
        return entityProperties;
    }

    public static EntityProperties getEntityProperties(EntityType entityType) {
        return entityProperties.get(entityType);
    }

    public static WorldProperties getWorldProperties(EntityProperties entityProperties, String world) {
        for (WorldProperties wp : entityProperties.getWorldProperties()) {
            if (wp.getWorlds().contains("*") || wp.getWorlds().contains(world)) {
                return wp;
            }
        }

        return null;
    }

    public static HashMap<UUID, ArrayList<EntityDamage>> getEntityDamages() {
        return entityDamages;
    }
}
