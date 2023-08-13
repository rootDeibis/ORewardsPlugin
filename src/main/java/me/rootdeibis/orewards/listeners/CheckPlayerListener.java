package me.rootdeibis.orewards.listeners;

import me.rootdeibis.orewards.ORewardsMain;
import me.rootdeibis.orewards.utils.AdvetureUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class CheckPlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        Bukkit.getScheduler().runTaskAsynchronously(ORewardsMain.getMain(), () -> {
            if(!ORewardsMain.getCore().getRewardManager().checkPlayer(e.getPlayer().getUniqueId())) {
                e.getPlayer().kickPlayer(AdvetureUtils.translate("&eORewards &7> &cWe were unable to load your data, please report this to the administrators."));
            } else {

            }
        });
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Bukkit.getScheduler().runTaskAsynchronously(ORewardsMain.getMain(), () -> {
            if(!ORewardsMain.getCore().getRewardManager().checkPlayer(e.getPlayer().getUniqueId())) {
                e.getPlayer().kickPlayer(AdvetureUtils.translate("&eORewards &7> &cWe were unable to load your data, please report this to the administrators."));
            } else {

            }
        });
    }
}
