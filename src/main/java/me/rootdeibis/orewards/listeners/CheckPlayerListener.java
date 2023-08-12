package me.rootdeibis.orewards.listeners;

import me.rootdeibis.orewards.ORewardsMain;
import me.rootdeibis.orewards.utils.AdvetureUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class CheckPlayerListener implements Listener {

    @EventHandler
    public void onJoin(AsyncPlayerPreLoginEvent e) {

        if(!ORewardsMain.getRewardManager().checkPlayer(e.getUniqueId())) {
            e.setKickMessage(AdvetureUtils.translate("&eORewards &7> &cWe were unable to load your data, please report this to the administrators."));
            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
        }

    }
}
