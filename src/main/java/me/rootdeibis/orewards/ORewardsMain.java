package me.rootdeibis.orewards;

import me.rootdeibis.orewards.api.Files.FileManager;
import me.rootdeibis.orewards.api.Files.RDirectory;
import me.rootdeibis.orewards.api.guifactory.GUIHolder;
import me.rootdeibis.orewards.api.guifactory.listeners.GUIFactoryListener;
import me.rootdeibis.orewards.api.rewards.RewardManager;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.stream.Collectors;

public class ORewardsMain extends JavaPlugin {

    private static Plugin instance;

    private static FileManager fileManager;
    private static BukkitAudiences adventure;

    private static RewardManager rewardManager;


    @Override
    public void onEnable() {
        instance = this;

        fileManager = new FileManager(this);

        fileManager.setResourcesPath("defaults.");

        fileManager.use("config.yml")
                .setDefaults("config.yml").create();
        fileManager.use("categories.yml")
                .setDefaults("categories.yml")
                .create();


        String[] defaultsRewards = new String[]{"daily_reward.yml", "hourly_reward.yml", "monthly_reward.yml", "onetime_reward.yml", "vip_reward.yml", "weekly_reward.yml"};


        RDirectory rewardsDir = fileManager.dir("rewards");

        if(!rewardsDir.exists()) {
            fileManager.dir("rewards")
                    .createIfNoExists();

            rewardsDir.exportsDefaults(defaultsRewards);
        }

        rewardsDir.loadFilesIndirectory();

        this.registerListeners();

        adventure = BukkitAudiences.create(this);

        rewardManager = new RewardManager();

        rewardManager.loadRewardsInDirectory();



    }

    @Override
    public void onDisable() {
        this.secureDisable();
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new GUIFactoryListener(), this);
    }


    private void secureDisable() {
        adventure.close();
        GUIHolder.getOpenedHolders().stream()
                .filter(e -> e.getInventory() != null && e.getInventory().getViewers().size() > 0)
                .collect(Collectors.toSet())
                .forEach(g -> g.getInventory().clear());
    }

    public static FileManager getFileManager() {
        return fileManager;
    }

    public static BukkitAudiences getAdventure() {
        return adventure;
    }

    public static RewardManager getRewardManager() {
        return rewardManager;
    }

    public static Plugin getMain() {
        return instance;
    }
}
