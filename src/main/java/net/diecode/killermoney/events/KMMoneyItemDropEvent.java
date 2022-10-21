package net.diecode.killermoney.events;

import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class KMMoneyItemDropEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private ItemStack itemStack;
    private Location location;
    private boolean cancelled;

    public KMMoneyItemDropEvent(ItemStack itemStack, Location location) {
        this.itemStack = itemStack;
        this.location = location;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public Location getLocation() {
        return location;
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
