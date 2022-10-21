package net.diecode.killermoney.events;

import net.diecode.killermoney.objects.CItem;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class KMCItemDropEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private CItem cItem;
    private ItemStack itemStack;
    private Player killer;
    private LivingEntity victim;
    private Location location;
    private boolean cancelled;

    public KMCItemDropEvent(CItem cItem, ItemStack itemStack, Player killer, LivingEntity victim, Location location) {
        this.cItem = cItem;
        this.itemStack = itemStack;
        this.killer = killer;
        this.victim = victim;
        this.location = location;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public CItem getCItem() {
        return cItem;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public Player getKiller() {
        return killer;
    }

    public LivingEntity getVictim() {
        return victim;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
