package net.diecode.killermoney.events;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class KMLimitReachedEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private Player killer;
    private LivingEntity victim;
    private boolean cancelled;

    public KMLimitReachedEvent(Player killer, LivingEntity victim) {
        this.killer = killer;
        this.victim = victim;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getKiller() {
        return killer;
    }

    public LivingEntity getVictim() {
        return victim;
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
