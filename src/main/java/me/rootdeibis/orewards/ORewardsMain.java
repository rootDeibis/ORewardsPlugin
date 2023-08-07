package me.rootdeibis.orewards;

import me.rootdeibis.orewards.api.Files.FileManager;
import me.rootdeibis.orewards.api.guifactory.GUIHolder;
import me.rootdeibis.orewards.api.guifactory.listeners.GUIFactoryListener;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.stream.Collectors;

public class ORewardsMain extends JavaPlugin {

    private static Plugin instance;

    private static FileManager fileManager;
    private static BukkitAudiences adventure;


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

        this.registerListeners();

        adventure = BukkitAudiences.create(this);





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

    public static Plugin getMain() {
        return instance;
    }
}
