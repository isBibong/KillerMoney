package net.diecode.killermoney.events;

import net.diecode.killermoney.objects.CashTransferProperties;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.math.BigDecimal;

public class KMEarnMoneyCashTransferDepositEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private CashTransferProperties cashTransferProperties;
    private Player killer;
    private BigDecimal amount;
    private Player victim;
    private BigDecimal damage;
    private boolean cancelled;

    public KMEarnMoneyCashTransferDepositEvent(CashTransferProperties cashTransferProperties, Player killer,
                                               BigDecimal amount, Player victim, BigDecimal damage) {
        this.cashTransferProperties = cashTransferProperties;
        this.killer = killer;
        this.amount = amount;
        this.victim = victim;
        this.damage = damage;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public CashTransferProperties getMoneyProperties() {
        return cashTransferProperties;
    }

    public Player getKiller() {
        return killer;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Player getVictim() {
        return victim;
    }

    public BigDecimal getDamage() {
        return damage;
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
