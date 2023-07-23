package me.rootdeibis.orewards.api.guifactory;


import org.bukkit.scheduler.BukkitRunnable;

public class GuiTaskUpdater extends BukkitRunnable {

    private final GUIHolder holder;

    public GuiTaskUpdater(GUIHolder holder) {
        this.holder = holder;
    }


    @Override
    public void run() {

       if(holder.getInventory().getViewers().size() == 0) {

           this.cancel();

       } else {

           holder.build();

       }


    }
}
