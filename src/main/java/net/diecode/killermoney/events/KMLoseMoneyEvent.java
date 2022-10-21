package net.diecode.killermoney.events;

import net.diecode.killermoney.objects.MoneyProperties;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.math.BigDecimal;

public class KMLoseMoneyEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private MoneyProperties moneyProperties;
    private Player player;
    private BigDecimal amount;
    private LivingEntity victim;
    private BigDecimal damage;
    private boolean cancelled;

    public KMLoseMoneyEvent(MoneyProperties moneyProperties, Player player, BigDecimal amount,
                            LivingEntity victim, BigDecimal damage) {
        this.moneyProperties = moneyProperties;
        this.player = player;
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

    public MoneyProperties getMoneyProperties() {
        return moneyProperties;
    }

    public Player getPlayer() {
        return player;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LivingEntity getVictim() {
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
