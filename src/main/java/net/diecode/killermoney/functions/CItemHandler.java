package net.diecode.killermoney.functions;

import net.diecode.killermoney.Utils;
import net.diecode.killermoney.enums.KMPermission;
import net.diecode.killermoney.enums.RunMethod;
import net.diecode.killermoney.events.KMCItemDropEvent;
import net.diecode.killermoney.objects.CItem;
import net.diecode.killermoney.objects.CItemProperties;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Random;

public class CItemHandler implements Listener {

    @EventHandler
    public void onItemDrop(KMCItemDropEvent e) {
        if (e.isCancelled()) {
            return;
        }

        e.getCItem().increaseLimitCounter(e.getKiller().getUniqueId(), e.getItemStack().getAmount());

        e.getLocation().getWorld().dropItemNaturally(e.getLocation(), e.getItemStack());
    }

    public static void process(CItemProperties cItemProperties, LivingEntity victim, Player killer, Location location) {
        ArrayList<CItem> cItems = new ArrayList<CItem>();

        if (cItemProperties.getRunMethod() == RunMethod.ALL) {
            for (CItem cItem : cItemProperties.getCItems()) {
                cItems.add(cItem);
            }
        } else {
            CItem cItem = cItemProperties.getCItems().get(new Random().nextInt(cItemProperties.getCItems().size()));

            cItems.add(cItem);
        }

        for (CItem cItem : cItems) {
            if (cItem.chanceGen()) {
                if ((cItem.getPermission() != null && !cItem.getPermission().isEmpty())
                        && (!killer.hasPermission(cItem.getPermission()))) {
                    continue;
                }

                if (!hasItemLimitBypass(killer)) {
                    if (cItem.isReachedLimit(killer.getUniqueId())) {
                        continue;
                    }
                }

                ItemStack item = cItem.getItemStack().clone();
                int remaningLimit = cItem.getLimit() - cItem.getCurrentLimitValue(killer.getUniqueId());

                if (cItem.getMinAmount() > 0 && cItem.getMaxAmount() > 0) {
                    int amount = Utils.randomNumber(cItem.getMinAmount(), cItem.getMaxAmount());

                    if ((cItem.getLimit() > 0) && (amount > remaningLimit)) {
                        amount = remaningLimit;
                    }

                    item.setAmount(amount);
                }

                Bukkit.getPluginManager().callEvent(new KMCItemDropEvent(cItem, item, killer, victim, location));
            }
        }
    }

    public static boolean hasItemLimitBypass(Player player) {
        return player.hasPermission(KMPermission.BYPASS_ITEM_LIMIT.get());
    }

}
