package net.diecode.killermoney.events;

import net.diecode.killermoney.objects.CashTransferProperties;
import net.diecode.killermoney.objects.EntityDamage;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.math.BigDecimal;
import java.util.ArrayList;

public class KMLoseMoneyCashTransferEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private CashTransferProperties cashTransferProperties;
    private ArrayList<EntityDamage> damagers;
    private Player victim;
    private BigDecimal amount;
    private boolean cancelled;

    public KMLoseMoneyCashTransferEvent(CashTransferProperties cashTransferProperties, ArrayList<EntityDamage> damagers,
                                        Player victim, BigDecimal amount) {
        this.cashTransferProperties = cashTransferProperties;
        this.damagers = damagers;
        this.victim = victim;
        this.amount = amount;
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

    public Player getVictim() {
        return victim;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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
