package me.rootdeibis.orewards.api.guifactory.events;

import me.rootdeibis.orewards.api.guifactory.GUIButton;
import me.rootdeibis.orewards.api.guifactory.GUIHolder;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GUIFactoryClickEvent extends Event implements Cancellable {


    private final GUIHolder holder;
    private final GUIButton button;

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    private boolean isCancelled = false;

    public GUIFactoryClickEvent(GUIHolder holder, GUIButton button) {
        this.holder = holder;
        this.button = button;
    }

    public GUIHolder getHolder() {
        return holder;
    }

    public GUIButton getButton() {
        return button;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }


    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        isCancelled = b;
    }
}
