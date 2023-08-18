package me.rootdeibis.orewards.api.rewards;

import me.rootdeibis.orewards.ORewardsLogger;
import me.rootdeibis.orewards.ORewardsMain;
import me.rootdeibis.orewards.api.database.IDatabase;
import me.rootdeibis.orewards.api.rewards.player.PlayerReward;

import java.util.*;

public class RewardManager {

    private final List<Reward> rewards = new ArrayList<>();
    private final HashMap<UUID,PlayerReward> playerRewards = new HashMap<>();

    public RewardManager() {
    }


    public void loadRewardsInDirectory() {
        ORewardsMain.getCore().getFileManager().dir("rewards").getRFiles().forEach((name, file) -> {
            rewards.add(new Reward(name.replaceAll(".yml", "")));

        });
    }
    public Reward getReward(String name) {
        return rewards.stream().filter(r -> r.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public boolean existsReward(String name) {
        return rewards.stream().anyMatch(e -> e.getName().equals(name));
    }

    public List<Reward> getRewards() {
        return rewards;
    }




    public PlayerReward getPlayerReward(UUID player) {
        if(!playerRewards.containsKey(player)) {
            playerRewards.put(player,new PlayerReward(player));
        }

        return playerRewards.get(player);
    }


    public Set<String> getCategories() {
        return  ORewardsMain.getCore().getFileManager().use("categories.yml").getConfigurationSection("CategoryList").getKeys(false);
    }

    public boolean checkPlayer(UUID uuid) {

        if(playerRewards.containsKey(uuid)) return true;

        IDatabase db = ORewardsMain.getCore().getDatabaseLoader().getDatabase();

        if(!db.isTested()) return false;


        boolean result =  false;

        PlayerReward rewardsCache = new PlayerReward(uuid);

        for (Reward reward : ORewardsMain.getCore().getRewardManager().getRewards()) {

            long until = db.get(reward, uuid);


            if(until != 0) {
                result = true;
            } else {

                if(db.create(reward, uuid)) {
                    result = true;
                    until = new Date().getTime();
                } else {
                    ORewardsLogger.send("Failed to create row in " + reward.getName() + " where uuid(" + uuid + ")");
                }

            }


            rewardsCache.setRewardUntil(reward.getName(), until);

        }


        playerRewards.put(uuid,rewardsCache);

        return result;
    }

}
