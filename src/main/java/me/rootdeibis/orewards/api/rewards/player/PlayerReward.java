package me.rootdeibis.orewards.api.rewards.player;

import me.rootdeibis.orewards.ORewardsMain;
import me.rootdeibis.orewards.utils.DurationParser;
import org.bukkit.Bukkit;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class PlayerReward {

    private final UUID uuid;

    private final HashMap<String, Long> until_rewards = new HashMap<>();

    public PlayerReward(UUID uuid) {
        this.uuid = uuid;
    }


    public void setRewardUntil(String rewardName, long timeUntil) {
        this.until_rewards.put(rewardName, timeUntil);
    }

    public void saveUntil(String name) {
        Bukkit.getScheduler().runTaskAsynchronously(ORewardsMain.getMain(), () -> {
            ORewardsMain.getCore().getDatabaseLoader().getDatabase().update(name, this.until_rewards.get(name), this.uuid);
        });
    }

    public void resetRewardUntil(String rewardName) {
        this.until_rewards.put(rewardName, DurationParser.addToDate("1s").getTime());
    }

    public long getRewardUntil(String rewardName) {
        if(!this.until_rewards.containsKey(rewardName)) {
            this.until_rewards.put(rewardName, new Date().getTime());
        }
        return this.until_rewards.get(rewardName);
    }

    public UUID getUUID() {
        return uuid;
    }
}
