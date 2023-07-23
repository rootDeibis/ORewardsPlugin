package me.rootdeibis.orewards.api.guifactory;

import me.rootdeibis.orewards.ORewardsMain;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.function.Predicate;

public class GUIHolder implements InventoryHolder {

    private final Inventory inventory;
    private final Set<GUIButton> buttons = new HashSet<>();

    private final BukkitTask task;
    public GUIHolder(int rows, String title) {
        this.inventory = Bukkit.createInventory(this, rows * 8, title);

        this.task = Bukkit.getScheduler().runTaskTimer(ORewardsMain.getMain(), new GuiTaskUpdater(this), 20L, 20L);
    }


    public void cancelTask() {
        this.task.cancel();
    }


    public void addButtons(GUIButton... btn) {
        this.buttons.addAll(Arrays.asList(btn));
    }

    public void removeButton(Predicate<GUIButton> btn) {
        this.buttons.removeIf(btn);
    }

    public void build() {
        for (GUIButton button : this.buttons) {
            this.inventory.setItem(button.getBtnSlot(), button.getItemStack());
        }
    }


    public GUIButton getButtonBySlot(int slot) {
        return this.buttons.stream().filter(btn -> btn.getBtnSlot() == slot).findFirst().orElse(null);
    }

    public Set<GUIButton> getButtons() {
        return buttons;
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

    public static List<GUIHolder> getOpenedHolders() {
        List<GUIHolder> opened_holders = new ArrayList<>();

        for(Player player : Bukkit.getOnlinePlayers()) {
            if(player.getOpenInventory().getTopInventory().getHolder() instanceof GUIHolder) {
                opened_holders.add((GUIHolder) player.getOpenInventory().getTopInventory().getHolder());
            }
        }

        return opened_holders;
    }
}
