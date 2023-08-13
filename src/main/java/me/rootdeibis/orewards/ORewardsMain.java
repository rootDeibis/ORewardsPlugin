package me.rootdeibis.orewards;

import me.rootdeibis.orewards.api.ORewardsCore;
import me.rootdeibis.orewards.api.commands.CommandLoader;
import me.rootdeibis.orewards.api.gui.GUIHolder;
import me.rootdeibis.orewards.api.gui.GuiTaskUpdater;
import me.rootdeibis.orewards.api.gui.listeners.GUIFactoryListener;
import me.rootdeibis.orewards.commands.ORewardsCMD;
import me.rootdeibis.orewards.listeners.CheckPlayerListener;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.stream.Collectors;

public class ORewardsMain extends JavaPlugin {

    private static Plugin instance;

    private static ORewardsCore oRewardsCore;

    private static BukkitAudiences adventure;

    private static BukkitTask guiUpdaterTask;


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

        oRewardsCore = new ORewardsCore(this);

        oRewardsCore.load();
        oRewardsCore.getDatabaseLoader().load();

        this.registerListeners();

        CommandLoader.register(ORewardsCMD.class);


    }

    @Override
    public void onDisable() {
        this.secureDisable();
    }

    public static ORewardsCore getCore() {
        return oRewardsCore;
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new GUIFactoryListener(), this);
        Bukkit.getPluginManager().registerEvents(new CheckPlayerListener(), this);
    }


    private void secureDisable() {

        adventure.close();

        oRewardsCore.getDatabaseLoader().getDatabase().disconnect();

        GUIHolder.getOpenedHolders().stream()
                .filter(e -> e.getInventory() != null && e.getInventory().getViewers().size() > 0)
                .collect(Collectors.toSet())
                .forEach(g -> g.getInventory().clear());
    }

    public static BukkitAudiences getAdventure() {
        return adventure;
    }


    public static BukkitTask getGuiUpdaterTask() {
        return guiUpdaterTask;
    }


    public static Plugin getMain() {
        return instance;
    }
}
