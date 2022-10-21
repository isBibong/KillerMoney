package net.diecode.killermoney.events;

import net.diecode.killermoney.objects.EntityDamage;
import net.diecode.killermoney.objects.MoneyProperties;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.ArrayList;

public class KMMoneyProcessorEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private MoneyProperties moneyProperties;
    private ArrayList<EntityDamage> damagers;
    private LivingEntity victim;
    private boolean cancelled;

    public KMMoneyProcessorEvent(MoneyProperties moneyProperties, ArrayList<EntityDamage> killers,
                                 LivingEntity victim) {
        this.moneyProperties = moneyProperties;
        this.damagers = killers;
        this.victim = victim;
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

    public ArrayList<EntityDamage> getDamagers() {
        return damagers;
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
