package net.diecode.killermoney.events;

import net.diecode.killermoney.objects.CashTransferProperties;
import net.diecode.killermoney.objects.EntityDamage;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.ArrayList;

public class KMCashTransferProcessorEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private CashTransferProperties cashTransferProperties;
    private ArrayList<EntityDamage> damagers;
    private Player killer;
    private Player victim;
    private boolean cancelled;

    public KMCashTransferProcessorEvent(CashTransferProperties cashTransferProperties, ArrayList<EntityDamage> damagers,
                                        Player killer, Player victim) {
        this.cashTransferProperties = cashTransferProperties;
        this.damagers = damagers;
        this.killer = killer;
        this.victim = victim;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public CashTransferProperties getCashTransferProperties() {
        return cashTransferProperties;
    }

    public ArrayList<EntityDamage> getDamagers() {
        return damagers;
    }

    public Player getKiller() {
        return killer;
    }

    public Player getVictim() {
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
