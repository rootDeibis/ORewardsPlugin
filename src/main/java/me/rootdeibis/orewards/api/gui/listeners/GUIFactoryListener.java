package me.rootdeibis.orewards.api.gui.listeners;

import me.rootdeibis.orewards.api.gui.GUIButton;
import me.rootdeibis.orewards.api.gui.GUIHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class GUIFactoryListener implements Listener {




    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {

        if(e.getClickedInventory() == null || e.getClickedInventory().getHolder() == null) return;

        GUIHolder holder = GUIHolder.getMenu(e.getClickedInventory().getHolder());

        if(holder != null) {
            GUIButton btn = holder.getButtonBySlot(e.getSlot());


            e.setCancelled(true);

            if(btn != null) {

                btn.getClickHandler().apply(new GUIButton.ClickContext(holder, btn, e));


            }

        }

    }


    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        GUIHolder holder = GUIHolder.getMenu(e.getInventory().getHolder());

        if (holder != null) {


            GUIHolder.unregister(holder.getHolderId());

        }
    }
}
