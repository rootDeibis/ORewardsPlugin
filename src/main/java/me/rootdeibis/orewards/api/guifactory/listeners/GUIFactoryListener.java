package me.rootdeibis.orewards.api.guifactory.listeners;

import me.rootdeibis.orewards.api.guifactory.GUIButton;
import me.rootdeibis.orewards.api.guifactory.GUIHolder;
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


            e.setCancelled(true);

            if(btn != null) {


                btn.getClickHandler().apply(new GUIButton.ClickContext(holder, btn, e));


            }

        }

    }


    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getInventory().getHolder() instanceof GUIHolder) {
            GUIHolder holder = (GUIHolder) e.getInventory().getHolder();

            GUIHolder.unregister(holder.getHolderId());

        }
    }
}
