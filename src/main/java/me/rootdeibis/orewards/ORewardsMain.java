package me.rootdeibis.orewards;

import me.rootdeibis.orewards.api.guifactory.GUIHolder;
import me.rootdeibis.orewards.api.guifactory.listeners.GUIFactoryListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.stream.Collectors;

public class ORewardsMain extends JavaPlugin {

    private static Plugin instance;

    @Override
    public void onEnable() {
        instance = this;

        this.registerListeners();



    }

    @Override
    public void onDisable() {
        this.secureDisable();
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new GUIFactoryListener(), this);
    }


    private void secureDisable() {
        GUIHolder.getOpenedHolders().stream()
                .filter(e -> e.getInventory() != null && e.getInventory().getViewers().size() > 0)
                .collect(Collectors.toSet())
                .forEach(g -> g.getInventory().clear());
    }

    public static Plugin getMain() {
        return instance;
    }
}
