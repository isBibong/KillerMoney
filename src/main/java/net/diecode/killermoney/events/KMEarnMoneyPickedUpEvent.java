package net.diecode.killermoney.events;

import net.diecode.killermoney.objects.MoneyProperties;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.math.BigDecimal;

public class KMEarnMoneyPickedUpEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private MoneyProperties moneyProperties;
    private Player player;
    private BigDecimal amount;
    private boolean cancelled;

    public KMEarnMoneyPickedUpEvent(MoneyProperties moneyProperties, Player player, BigDecimal amount) {
        this.moneyProperties = moneyProperties;
        this.player = player;
        this.amount = amount;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public MoneyProperties getMoneyProperties() {
        return moneyProperties;
    }

    public Player getPlayer() {
        return player;
    }

    public BigDecimal getAmount() {
        return amount;
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
