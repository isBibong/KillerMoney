package net.diecode.killermoney.events;

import net.diecode.killermoney.functions.MultiplierHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class KMGlobalMultiplierChangeEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private double newValue;
    private int minute;
    private CommandSender sender;
    private boolean cancelled;

    public KMGlobalMultiplierChangeEvent(double newValue, int minute, CommandSender sender) {
        this.newValue = newValue;
        this.minute = minute;
        this.sender = sender;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public double getCurrentValue() {
        return MultiplierHandler.getGlobalMultiplier();
    }

    public double getNewValue() {
        return newValue;
    }

    public void setNewValue(double newValue) {
        this.newValue = newValue;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public CommandSender getSender() {
        return sender;
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
