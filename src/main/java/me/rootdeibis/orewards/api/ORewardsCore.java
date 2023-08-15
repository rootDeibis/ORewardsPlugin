package me.rootdeibis.orewards.api;

import me.rootdeibis.orewards.api.configuration.FileManager;
import me.rootdeibis.orewards.api.configuration.RDirectory;
import me.rootdeibis.orewards.api.database.DatabaseLoader;
import me.rootdeibis.orewards.api.rewards.RewardManager;
import org.bukkit.plugin.Plugin;

public class ORewardsCore {

    private final Plugin plugin;
    private final DatabaseLoader database;

    private final FileManager fileManager;

    private final RewardManager rewardManager;

    public ORewardsCore(Plugin instance) {
        this.plugin = instance;
        this.database = new DatabaseLoader();
        this.fileManager = new FileManager(instance).setResourcesPath("defaults.");
        this.rewardManager = new RewardManager();

    }

    public void load() {

        fileManager.setResourcesPath("defaults.");

        fileManager.use("config.yml")
                .setDefaults("config.yml").create();
        fileManager.use("categories.yml")
                .setDefaults("categories.yml")
                .create();


        String[] defaultsRewards = new String[]{"daily_reward.yml", "hourly_reward.yml", "monthly_reward.yml", "onetime_reward.yml", "vip_reward.yml", "weekly_reward.yml"};


        RDirectory rewardsDir = fileManager.dir("rewards");

        if(!rewardsDir.exists()) {
            rewardsDir
                    .createIfNoExists();

            rewardsDir.exportsDefaults(defaultsRewards);
        }



        rewardsDir.loadFilesIndirectory();

        this.rewardManager.loadRewardsInDirectory();
    }

    public DatabaseLoader getDatabaseLoader() {
        return database;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public RewardManager getRewardManager() {
        return rewardManager;
    }
}
