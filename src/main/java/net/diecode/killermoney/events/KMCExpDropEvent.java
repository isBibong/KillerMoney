package net.diecode.killermoney.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class KMCExpDropEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private Location location;
    private int value;
    private Player killer;
    private boolean cancelled;

    public KMCExpDropEvent(Location location, int value, Player killer) {
        this.location = location;
        this.value = value;
        this.killer = killer;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Location getLocation() {
        return location;
    }

    public int getValue() {
        return value;
    }

    public Player getKiller() {
        return killer;
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
