package me.rootdeibis.orewards;

import me.rootdeibis.orewards.api.Files.FileManager;
import me.rootdeibis.orewards.api.Files.RDirectory;
import me.rootdeibis.orewards.api.Files.RFile;
import me.rootdeibis.orewards.api.commands.CommandLoader;
import me.rootdeibis.orewards.api.guifactory.GUIHolder;
import me.rootdeibis.orewards.api.guifactory.GuiTaskUpdater;
import me.rootdeibis.orewards.api.guifactory.listeners.GUIFactoryListener;
import me.rootdeibis.orewards.api.rewards.Reward;
import me.rootdeibis.orewards.api.rewards.RewardManager;
import me.rootdeibis.orewards.api.rewards.db.IDatabase;
import me.rootdeibis.orewards.api.rewards.db.MySQLDB;
import me.rootdeibis.orewards.api.rewards.db.SQLFileDB;
import me.rootdeibis.orewards.cmds.ORewardsCMD;
import me.rootdeibis.orewards.listeners.CheckPlayerListener;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.stream.Collectors;

public class ORewardsMain extends JavaPlugin {

    private static Plugin instance;

    private static FileManager fileManager;
    private static BukkitAudiences adventure;

    private static RewardManager rewardManager;
    private static BukkitTask guiUpdaterTask;

    private static IDatabase db;

    public static void resumeTask() {
        guiUpdaterTask = Bukkit.getScheduler().runTaskTimerAsynchronously(ORewardsMain.getMain(), new GuiTaskUpdater(), 0L, 20L);
    }

    public static void stopTask() {
        guiUpdaterTask.cancel();
    }

    @Override
    public void onEnable() {
        instance = this;

        adventure = BukkitAudiences.create(this);

        fileManager = new FileManager(this);

        fileManager.setResourcesPath("defaults.");

        RFile config = fileManager.use("config.yml")
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



        rewardManager = new RewardManager();

        rewardManager.loadRewardsInDirectory();



        new CommandLoader(ORewardsCMD.class).register();


        if(config.getBoolean("Options.MysqlConnection.Enabled")) {
            String host = config.getString("Options.MysqlConnection.Data.hostname");
            String port = config.getString("Options.MysqlConnection.Data.port");
            String username = config.getString("Options.MysqlConnection.Data.user");
            String password = config.getString("Options.MysqlConnection.Data.password");
            String db_name = config.getString("Options.MysqlConnection.Data.databaseName");

            db = new MySQLDB(db_name, username, password, host, port);

        } else {
            db = new SQLFileDB(new File(this.getDataFolder(),config.getString("Options.SQLConnection.FileName")));

        }

        if(db.isTested()) {
            db.checkTables(rewardManager.getRewards().stream().map(Reward::getName).toArray(String[]::new));
        } else {
            Bukkit.getPluginManager().disablePlugin(this);
        }

    }

    @Override
    public void onDisable() {
        this.secureDisable();
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new GUIFactoryListener(), this);
        Bukkit.getPluginManager().registerEvents(new CheckPlayerListener(), this);
    }


    private void secureDisable() {

        adventure.close();

        db.disconnect();

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

    public static BukkitTask getGuiUpdaterTask() {
        return guiUpdaterTask;
    }

    public static IDatabase getDB() {
        return db;
    }

    public static Plugin getMain() {
        return instance;
    }
}
