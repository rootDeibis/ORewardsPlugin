package me.rootdeibis.orewards.api.rewards;

import me.rootdeibis.orewards.ORewardsMain;
import me.rootdeibis.orewards.api.Files.RDirectory;

import java.util.ArrayList;
import java.util.List;

public class RewardManager {

    private final List<Reward> rewards = new ArrayList<>();
    private final RDirectory rewardsDir;

    public RewardManager() {
        this.rewardsDir = ORewardsMain.getFileManager().dir("rewards");
    }


    public void loadRewardsInDirectory() {
        this.rewardsDir.getRFiles().forEach((name, file) -> {
            rewards.add(new Reward(name.replaceAll(".yml", "")));
            System.out.println(name);
        });
    }

    public List<Reward> getRewards() {
        return rewards;
    }

    public Reward getReward(String name) {
        return rewards.stream().filter(r -> r.getName().equals(name)).findFirst().orElse(null);
    }
}
