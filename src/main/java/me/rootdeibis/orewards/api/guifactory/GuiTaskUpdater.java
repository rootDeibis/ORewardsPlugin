package me.rootdeibis.orewards.api.guifactory;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class GuiTaskUpdater implements Runnable {

    public GuiTaskUpdater() {

    }

    @Override
    public void run() {

        if(Bukkit.getOnlinePlayers().size() > 0) return;

        for(Player player : Bukkit.getOnlinePlayers()) {

            if(player.getOpenInventory().getTopInventory().getHolder() instanceof GUIHolder) {

                GUIHolder holder = (GUIHolder) player.getOpenInventory().getTopInventory();

                holder.build();

            }


        }


    }
}
