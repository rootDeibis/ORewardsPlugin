package me.rootdeibis.orewards.api.gui;


import me.rootdeibis.orewards.ORewardsMain;

import java.util.List;

public class GuiTaskUpdater implements Runnable {



    public GuiTaskUpdater() {

    }


    @Override
    public void run() {
        List<GUIHolder> holders = GUIHolder.getOpenedHolders();


        if(holders.size() == 0) {
            ORewardsMain.stopTask();
        }


        holders.forEach(GUIHolder::build);

    }
}
