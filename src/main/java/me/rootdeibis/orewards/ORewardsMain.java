package me.rootdeibis.orewards;

import me.rootdeibis.orewards.api.guifactory.GUIHolder;
import me.rootdeibis.orewards.api.guifactory.GuiTaskUpdater;
import me.rootdeibis.orewards.api.guifactory.listeners.GUIFactoryListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

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
        for (GUIHolder openedHolder : GUIHolder.getOpenedHolders()) {
            if(openedHolder.getInventory().getViewers().size() > 0) {
                for (HumanEntity viewer : openedHolder.getInventory().getViewers()) {
                    viewer.closeInventory();
                }
            }
        }
    }

    public static Plugin getMain() {
        return instance;
    }
}
