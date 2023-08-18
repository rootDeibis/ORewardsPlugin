package me.rootdeibis.orewards;

import me.rootdeibis.commonlib.factory.gui.holders.GuiContainer;
import me.rootdeibis.commonlib.factory.gui.listener.GuiControllerListener;
import me.rootdeibis.orewards.api.ORewardsCore;
import me.rootdeibis.orewards.api.commands.CommandLoader;
import me.rootdeibis.orewards.api.rewards.menus.GuiTaskUpdater;
import me.rootdeibis.orewards.commands.ORewardsCMD;
import me.rootdeibis.orewards.hook.ORewardsExpansion;
import me.rootdeibis.orewards.listeners.CheckPlayerListener;
import me.rootdeibis.orewards.utils.VersionChecker;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.*;

public class ORewardsMain extends JavaPlugin {

    private static Plugin instance;

    private static ORewardsCore oRewardsCore;

    private static BukkitAudiences adventure;


    private static final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    private static ScheduledFuture<?> guiUpdaterTask;

    public static boolean PLACEHOLDERAPI_SUPPORT = false;


    public static void resumeTask() {
        if (!ORewardsMain.getCore().getFileManager().use("config.yml").getBoolean("rewardGuiOptions.disable-live-cooldown")) {
            if(guiUpdaterTask == null || guiUpdaterTask.isCancelled()) {
                guiUpdaterTask = service.scheduleAtFixedRate(new GuiTaskUpdater(), 1,1, TimeUnit.SECONDS);

            }
        }


    }

    public static void stopTask() {
        if (guiUpdaterTask != null && !guiUpdaterTask.isCancelled()) {
            guiUpdaterTask.cancel(true);
        }
    }

    @Override
    public void onEnable() {
        instance = this;
        adventure = BukkitAudiences.create(this);

        oRewardsCore = new ORewardsCore(this);

        oRewardsCore.load();
        oRewardsCore.getDatabaseLoader().load();

        this.registerListeners();

        CommandLoader.register(ORewardsCMD.class);

        if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            PLACEHOLDERAPI_SUPPORT = true;

            new ORewardsExpansion().register();
        }



        ORewardsLogger.print(
                "&6 ██████╗ ██████╗ ███████╗██╗    ██╗ █████╗ ██████╗ ██████╗ ███████╗",
                "&6██╔═══██╗██╔══██╗██╔════╝██║    ██║██╔══██╗██╔══██╗██╔══██╗██╔════╝",
                "&6██║   ██║██████╔╝█████╗  ██║ █╗ ██║███████║██████╔╝██║  ██║███████╗",
                "&6██║   ██║██╔══██╗██╔══╝  ██║███╗██║██╔══██║██╔══██╗██║  ██║╚════██║",
                "&6╚██████╔╝██║  ██║███████╗╚███╔███╔╝██║  ██║██║  ██║██████╔╝███████║",
                "&6 ╚═════╝ ╚═╝  ╚═╝╚══════╝ ╚══╝╚══╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═════╝ ╚══════╝",
                        "",
                        "&dCurrent version:" + this.getDescription().getVersion(),"",
                        "&bSupport in: https://discord.gg/rxvsppR2ss"
        );

        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            VersionChecker versionChecker = new VersionChecker(this, "93216");

            if(versionChecker.isLatestVersion()) {
                ORewardsLogger.send("&aYou are using the latest version.");
            } else {
                ORewardsLogger.send(String.format("&cYou are not using the latest version, consider upgrading. Your current version is %s and the latest version is %s.", this.getDescription().getVersion(), versionChecker.getLatestVersion()));
            }
        });




    }

    @Override
    public void onDisable() {
        this.secureDisable();
    }

    public static ORewardsCore getCore() {
        return oRewardsCore;
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new GuiControllerListener(), this);
        Bukkit.getPluginManager().registerEvents(new CheckPlayerListener(), this);
    }


    private void secureDisable() {


        stopTask();

        adventure.close();

        oRewardsCore.getDatabaseLoader().getDatabase().disconnect();

        GuiContainer.getContainers()
                .stream().
                forEach(e -> {
                    e.getInventory().clear();
                });
    }

    public static BukkitAudiences getAdventure() {
        return adventure;
    }


    public static ScheduledFuture<?> getGuiUpdaterTask() {
        return guiUpdaterTask;
    }


    public static Plugin getMain() {
        return instance;
    }
}
