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


        holders.stream().filter(container -> container.getInventory() != null && container.getInventory().getViewers().size() > 0).forEach(container -> container.getButtons().forEach((slot, btn) -> {
            container.updateButton(slot);
        }));

    }
}
