package me.rootdeibis.orewards.api.guifactory.listeners;

import me.rootdeibis.orewards.api.guifactory.GUIButton;
import me.rootdeibis.orewards.api.guifactory.GUIHolder;
import me.rootdeibis.orewards.api.guifactory.events.GUIFactoryClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class GUIFactoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {

        if(e.getClickedInventory() != null && e.getClickedInventory().getHolder() instanceof GUIHolder) {
            GUIHolder holder = (GUIHolder)  e.getClickedInventory().getHolder();
            GUIButton btn = holder.getButtonBySlot(e.getSlot());

            if(btn != null) {

                GUIFactoryClickEvent clickEvent = new GUIFactoryClickEvent(holder, btn);

                Bukkit.getPluginManager().callEvent(clickEvent);

                if(clickEvent.isCancelled()) return;

                btn.getClickHandler().apply(e);


            }

        }

    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getInventory().getHolder() instanceof GUIHolder) {
            GUIHolder holder = (GUIHolder) e.getInventory().getHolder();


            holder.cancelTask();

        }
    }
}
