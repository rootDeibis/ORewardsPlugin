package me.rootdeibis.orewards.api.rewards.menus;


import me.rootdeibis.commonlib.factory.gui.holders.GuiContainer;
import me.rootdeibis.orewards.ORewardsMain;

import java.util.List;

public class GuiTaskUpdater implements Runnable {



    public GuiTaskUpdater() {

    }


    @Override
    public void run() {
        List<GuiContainer> holders = GuiContainer.getContainers();


        if(holders.size() == 0) {
            ORewardsMain.stopTask();
        }


        holders
                .forEach(container -> container.getButtons().forEach(container::updateButton));

    }
}
