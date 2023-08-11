package me.rootdeibis.orewards.api.guifactory.listeners;

import me.rootdeibis.orewards.ORewardsMain;
import me.rootdeibis.orewards.api.guifactory.GUIButton;
import me.rootdeibis.orewards.api.guifactory.GUIHolder;
import me.rootdeibis.orewards.utils.AdvetureUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class GUIFactoryListener implements Listener {



    @EventHandler
    public void onJoin(AsyncPlayerPreLoginEvent e) {

        if(!ORewardsMain.getRewardManager().checkPlayer(e.getUniqueId())) {
            e.setKickMessage(AdvetureUtils.translate("&eORewards &7> &cWe were unable to upload your data, please report this to the administrators."));
            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
        }

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
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getInventory().getHolder() instanceof GUIHolder) {
            GUIHolder holder = (GUIHolder) e.getInventory().getHolder();

            GUIHolder.unregister(holder.getHolderId());

        }
    }
}
