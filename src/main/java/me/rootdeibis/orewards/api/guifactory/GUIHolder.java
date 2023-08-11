package me.rootdeibis.orewards.api.guifactory;

import me.rootdeibis.orewards.ORewardsMain;
import me.rootdeibis.orewards.utils.AdvetureUtils;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class GUIHolder implements InventoryHolder {

    private Inventory inventory;

    private final UUID holderId = UUID.randomUUID();
    private final LinkedList<GUIButton> buttons = new LinkedList<>();

    private static final LinkedList<GUIHolder> holders = new LinkedList<>();


    private String title = "GUIHolder Inventory";
    private int rows = 5;

    public GUIHolder(int rows, String title) {
        this.title = title;
        this.rows = rows;

    }

    public GUIHolder() {


    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }



    public void addButtons(GUIButton... btn) {
        this.buttons.addAll(Arrays.asList(btn));
    }

    public void removeButton(Predicate<GUIButton> btn) {
        this.buttons.removeIf(btn);
    }

    public void build() {
        if(this.inventory == null) {
            this.inventory = Bukkit.createInventory(this, rows * 9, AdvetureUtils.translate(this.title));
            register(this);
            this.checkTask();
        }


        this.getButtons().forEach(btn -> {
            btn.build();

            if(!btn.getItemStack().isSimilar(this.inventory.getItem(btn.getBtnSlot()))) {
                this.inventory.setItem(btn.getBtnSlot(), btn.getItemStack());
            } else {
                this.inventory.getItem(btn.getBtnSlot()).setItemMeta(btn.getItemStack().getItemMeta());
            }



        });
    }


    public UUID getHolderId() {
        return holderId;
    }

    public GUIButton getButtonBySlot(int slot) {
        return this.buttons.stream().filter(btn -> btn.getBtnSlot() == slot).findFirst().orElse(null);
    }

    public LinkedList<GUIButton> getButtons() {
        return buttons;
    }

    @Override
    public Inventory getInventory() {

        return this.inventory;
    }

    public static void register(GUIHolder holder) {
        holders.add(holder);
    }

    public static void unregister(UUID holder) {
        holders.removeIf(h -> h.getHolderId().equals(holder));
    }

    public static List<GUIHolder> getOpenedHolders() {
        return holders;
    }

    private void checkTask() {
        if(ORewardsMain.getGuiUpdaterTask() == null || !Bukkit.getScheduler().isCurrentlyRunning(ORewardsMain.getGuiUpdaterTask().getTaskId())) {
            ORewardsMain.resumeTask();
        }
    }
}
