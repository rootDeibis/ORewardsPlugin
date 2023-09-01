package me.rootdeibis.orewards.listeners;

import me.rootdeibis.orewards.ORewardsMain;
import me.rootdeibis.orewards.api.configuration.RFile;
import me.rootdeibis.orewards.api.rewards.data.PlayerData;
import me.rootdeibis.orewards.api.rewards.data.PlayerDataManager;
import me.rootdeibis.orewards.api.rewards.player.PlayerReward;
import me.rootdeibis.orewards.utils.AdvetureUtils;
import me.rootdeibis.orewards.utils.DurationParser;
import me.rootdeibis.orewards.utils.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Date;

public class CheckPlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        PlayerData playerData = PlayerDataManager.load(e.getPlayer().getUniqueId());

        playerData.set("lastJoin", new Date().getTime());



            Bukkit.getScheduler().runTaskAsynchronously(ORewardsMain.getMain(), () -> {
                if(!ORewardsMain.getCore().getRewardManager().checkPlayer(e.getPlayer().getUniqueId())) {
                    e.getPlayer().kickPlayer(AdvetureUtils.translate("&eORewards &7> &cWe were unable to load your data, please report this to the administrators."));
                } else {
                    RFile config = ORewardsMain.getCore().getFileManager().use("config.yml");


                    if (config.getBoolean("Messages.WelcomeRewardAvailable.Enabled")) {

                        PlayerReward playerReward = ORewardsMain.getCore().getRewardManager().getPlayerReward(e.getPlayer().getUniqueId());


                        int availableRewards = playerReward.getAvailables().size();

                        Placeholders placeholders = new Placeholders();

                        placeholders.add("rewards_availables", availableRewards);
                        placeholders.add("player_name", e.getPlayer().getName());



                        if (availableRewards > 0 ) {
                            AdvetureUtils.sender(e.getPlayer(), placeholders.apply(config.getString("Messages.WelcomeRewardAvailable.Availables")));
                        } else {
                            AdvetureUtils.sender(e.getPlayer(), placeholders.apply(config.getString("Messages.WelcomeRewardAvailable.NoAvailable")));
                        }

                    }
                }
            });

    }




    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        PlayerData playerData = PlayerDataManager.load(e.getPlayer().getUniqueId());
        playerData.set("expireOn", DurationParser.addToDate("2mo").getTime());

        long lastTime = Long.parseLong(playerData.get("lastJoin"));
        long logoutTime = new Date().getTime();



        String currentPlayTime = playerData.get("playTime");

        long playTime = lastTime + logoutTime;
        if (currentPlayTime != null) {
            playTime = Long.parseLong(currentPlayTime) + lastTime + logoutTime;
        }
        playerData.set("playTime",playTime);

        playerData.save();

        Bukkit.getScheduler().runTaskAsynchronously(ORewardsMain.getMain(), () -> {
            if(!ORewardsMain.getCore().getRewardManager().checkPlayer(e.getPlayer().getUniqueId())) {
                e.getPlayer().kickPlayer(AdvetureUtils.translate("&eORewards &7> &cWe were unable to load your data, please report this to the administrators."));
            } else {

            }
        });
    }
}
