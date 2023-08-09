package me.rootdeibis.orewards.api.guifactory.listeners;

import me.rootdeibis.orewards.api.guifactory.GUIButton;
import me.rootdeibis.orewards.api.guifactory.GUIHolder;
import me.rootdeibis.orewards.api.menus.Categories;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.*;

public class GUIFactoryListener implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        e.getPlayer().openInventory(new Categories().getInventory());
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {

        if(e.getClickedInventory() != null && e.getClickedInventory().getHolder() instanceof GUIHolder) {
            GUIHolder holder = (GUIHolder)  e.getClickedInventory().getHolder();
            GUIButton btn = holder.getButtonBySlot(e.getSlot());


            e.setCancelled(true);

            if(btn != null) {


                btn.getClickHandler().apply(e);


            }

        }

    }

    @EventHandler
    public void onInventoryInteract(InventoryMoveItemEvent e) {
        if(e.getDestination() != null && e.getDestination().getHolder() instanceof GUIHolder ||
                e.getSource() != null && e.getSource().getHolder() instanceof GUIHolder) {

           e.setCancelled(true);
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
