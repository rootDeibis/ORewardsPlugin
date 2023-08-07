package me.rootdeibis.orewards.api.guifactory;

import me.rootdeibis.orewards.ORewardsMain;
import me.rootdeibis.orewards.utils.AdvetureUtils;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GUIHolder implements InventoryHolder {

    private Inventory inventory;
    private final Set<GUIButton> buttons = new HashSet<>();

    private String title = "GUIHolder Inventory";
    private int rows = 5;

    private final BukkitTask task;
    public GUIHolder(int rows, String title) {
        this.title = title;
        this.rows = rows;

        this.task = Bukkit.getScheduler().runTaskTimer(ORewardsMain.getMain(), new GuiTaskUpdater(this), 20L, 20L);
    }

    public GUIHolder() {
        this.task = Bukkit.getScheduler().runTaskTimer(ORewardsMain.getMain(), new GuiTaskUpdater(this), 20L, 20L);
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void cancelTask() {
        this.task.cancel();
    }

    public BukkitTask getTask() {
        return task;
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
        }

        this.getButtons().forEach(btn -> {
            btn.build();
            this.inventory.setItem(btn.getBtnSlot(), btn.getItemStack());
        });
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
        return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getOpenInventory)
                .filter(i -> i.getTopInventory() != null && i.getTopInventory() instanceof GUIHolder)
                .map(i -> (GUIHolder) i.getTopInventory())
                .collect(Collectors.toList());
    }
}
