package net.diecode.killermoney.functions;

import net.diecode.killermoney.Utils;
import net.diecode.killermoney.events.KMCExpDropEvent;
import net.diecode.killermoney.objects.CExpProperties;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CExpHandler implements Listener {

    @EventHandler
    public void onCExpDrop(KMCExpDropEvent e) {
        if (e.isCancelled()) {
            return;
        }

        e.getLocation().getWorld().spawn(e.getLocation(), ExperienceOrb.class).setExperience(e.getValue());
    }

    public static void process(CExpProperties cExpProperties, Location location, Player killer) {
        if (cExpProperties.chanceGen()) {
            int value = Utils.randomNumber(cExpProperties.getMinAmount(), cExpProperties.getMaxAmount());

            Bukkit.getPluginManager().callEvent(new KMCExpDropEvent(location, value, killer));
        }
    }

}
