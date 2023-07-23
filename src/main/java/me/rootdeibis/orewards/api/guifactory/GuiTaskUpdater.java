package me.rootdeibis.orewards.api.guifactory;


import org.bukkit.Bukkit;

public class GuiTaskUpdater implements Runnable {

    private final GUIHolder holder;

    public GuiTaskUpdater(GUIHolder holder) {
        this.holder = holder;
    }


    @Override
    public void run() {

       if(holder.getInventory().getViewers().size() == 0) {

           if(Bukkit.getScheduler().isCurrentlyRunning(this.holder.getTask().getTaskId())) {
               this.holder.cancelTask();
           }

       } else {

           holder.build();

       }


    }
}
