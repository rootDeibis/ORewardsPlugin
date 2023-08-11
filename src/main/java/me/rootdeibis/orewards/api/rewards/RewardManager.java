package me.rootdeibis.orewards.api.rewards;

import me.rootdeibis.orewards.ORewardsLogger;
import me.rootdeibis.orewards.ORewardsMain;
import me.rootdeibis.orewards.api.Files.RDirectory;
import me.rootdeibis.orewards.api.rewards.db.IDatabase;
import me.rootdeibis.orewards.api.rewards.player.PlayerReward;
import me.rootdeibis.orewards.utils.DurationParser;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RewardManager {

    private final List<Reward> rewards = new ArrayList<>();
    private final List<PlayerReward> playerRewards = new ArrayList<>();
    private final RDirectory rewardsDir;

    public RewardManager() {
        this.rewardsDir = ORewardsMain.getFileManager().dir("rewards");
    }


    public void loadRewardsInDirectory() {
        this.rewardsDir.getRFiles().forEach((name, file) -> {
            rewards.add(new Reward(name.replaceAll(".yml", "")));

        });
    }
    public Reward getReward(String name) {
        return rewards.stream().filter(r -> r.getName().equals(name)).findFirst().orElse(null);
    }

    public List<Reward> getRewards() {
        return rewards;
    }



    public PlayerReward getPlayerReward(UUID player) {
        if(playerRewards.stream().noneMatch(p -> p.getUUID().equals(player))) {
            playerRewards.add(new PlayerReward(player));
        }

        return playerRewards.stream().filter(p -> p.getUUID().equals(player)).findFirst().orElse(null);
    }


    public boolean checkPlayer(UUID uuid) {
        IDatabase db = ORewardsMain.getDB();

        if(!db.isTested()) return false;
        if(playerRewards.stream().anyMatch(r -> r.getUUID().equals(uuid))) return true;

        boolean result =  false;

        PlayerReward rewardsCache = this.getPlayerReward(uuid);

        for (Reward reward : ORewardsMain.getRewardManager().getRewards()) {

            long until = db.get(reward, uuid);




            if(until != 0) {
                result = true;
            } else {

                if(db.create(reward, uuid)) {
                    result = true;
                    until = DurationParser.addToDate("1s").getTime();
                } else {
                    ORewardsLogger.send("Failed to create row in " + reward.getName() + " where uuid(" + uuid + ")");
                }

            }


            rewardsCache.setRewardUntil(reward.getName(), until);

        }




        return result;
    }

}
