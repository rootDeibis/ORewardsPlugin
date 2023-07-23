package me.rootdeibis.orewards.api.guifactory;

import com.cryptomorin.xseries.XMaterial;
import me.rootdeibis.orewards.api.guifactory.functions.GuiClickFunction;
import me.rootdeibis.orewards.api.guifactory.functions.GuiUseObjFunction;
import me.rootdeibis.orewards.utils.HeadTool;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class GUIButton {

    private GuiClickFunction<InventoryClickEvent> clickHandler = (e) -> {};
    private GuiUseObjFunction<String> material_type = () -> "BEDROCK";
    private GuiUseObjFunction<Integer> material_amount = () -> 1;

    private GuiUseObjFunction<String> item_name = () -> "Undefined gui button name";
    private GuiUseObjFunction<List<String>> item_lore = ArrayList::new;

    private GuiUseObjFunction<String> skull_textures = () -> "";

    private final ItemStack itemStack = new ItemStack(Material.matchMaterial(material_type.apply()), this.material_amount.apply());


    private int button_slot = 0;
    public GUIButton(int slot) {
        this.button_slot = slot;
    }

    public GUIButton(int slot, FileConfiguration config, String path) {
        this.button_slot = slot;
        this.setDataFrom(config, path);
    }

    public GUIButton setType(GuiUseObjFunction<String> f_name) {
        this.material_type = f_name;

        return this;
    }

    public GUIButton setAmount(GuiUseObjFunction<Integer> f_amount) {
        this.material_amount = f_amount;

        return this;
    }

    public GUIButton setItemName(GuiUseObjFunction<String> item_name) {
        this.item_name = item_name;

        return this;
    }

    public GUIButton setItemLore(GuiUseObjFunction<List<String>> item_lore) {
        this.item_lore = item_lore;

        return this;
    }

    public void setSkullTextures(GuiUseObjFunction<String> skull_textures) {
        this.skull_textures = skull_textures;
    }

    public GuiUseObjFunction<String> getType() {
        return material_type;
    }

    public GuiUseObjFunction<Integer> getAmount() {
        return material_amount;
    }

    public GuiUseObjFunction<String> getItemName() {
        return item_name;
    }

    public GuiUseObjFunction<List<String>> getItemLore() {
        return item_lore;
    }

    public GuiUseObjFunction<String> getSkullTextures() {
        return skull_textures;
    }

    public GuiClickFunction<InventoryClickEvent> getClickHandler() {
        return clickHandler;
    }


    public int getBtnSlot() {
        return button_slot;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void build() {



        String materialName = this.material_type.apply();
        int materialAmount = this.material_amount.apply();

        String itemName = this.item_name.apply();
        List<String> itemLore = this.item_lore.apply();


        if(!materialName.equals(this.itemStack.getType().name())) {
            if(XMaterial.matchXMaterial(materialName).orElse(XMaterial.BEDROCK) != XMaterial.PLAYER_HEAD) {
                this.itemStack.setType(Objects.requireNonNull(XMaterial.matchXMaterial(this.material_type.apply()).orElse(XMaterial.BEDROCK).parseMaterial()));
            } else {

                HeadTool.applyTextures(this.itemStack, this.skull_textures.apply());

            }
        }

        if(materialAmount != this.getItemStack().getAmount())
            this.itemStack.setAmount(materialAmount);

        ItemMeta meta = this.getItemStack().getItemMeta();


        if(meta != null) {
            if(!itemName.equals(meta.getDisplayName()))
                meta.setDisplayName(itemName);
            if(meta.getLore() != null && !new HashSet<>(itemLore).containsAll(meta.getLore()))
                meta.setLore(itemLore);
        }





    }
    public void setDataFrom(FileConfiguration config, String path) {
        Function<String,Boolean> hasPath = (p) -> config.get(this.buildPath(path, p)) != null;

        if(hasPath.apply("type")) {
            this.material_type = () -> config.getString(this.buildPath(path, "type"));
        }

        if(hasPath.apply("amout")) {
            this.material_amount = () -> config.getInt(this.buildPath(path, "amount"));
        }

        if(hasPath.apply("name")) {
            this.item_name = () -> config.getString(this.buildPath(path, "name"));
        }

        if(hasPath.apply("lore")) {
            this.item_lore = () -> config.getStringList(this.buildPath(path, "lore"));
        }


    }

    private String buildPath(String... paths) {
        return String.join(".", paths);
    }
}
