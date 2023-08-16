package me.rootdeibis.orewards.api.rewards.menus.categories;

import com.cryptomorin.xseries.XMaterial;
import me.rootdeibis.commonlib.factory.gui.holders.GuiButton;
import me.rootdeibis.commonlib.factory.gui.holders.context.GUIClickContext;
import me.rootdeibis.orewards.ORewardsMain;
import me.rootdeibis.orewards.api.configuration.RFile;
import me.rootdeibis.orewards.api.rewards.menus.category.CategoryConfig;
import me.rootdeibis.orewards.api.rewards.menus.category.RewardCategoryContainer;
import me.rootdeibis.orewards.utils.AdvetureUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

class CategoryButton extends GuiButton {

    private final String name;
    private final String path;
    private final RFile config = ORewardsMain.getCore().getFileManager().use("categories.yml");

    public int slot;


    private boolean useCustomSlot = false;

    public CategoryButton(String name) {
        this.name = name;
        this.path = "CategoryList." + name + ".DisplayItem.";
    }

    public CategoryButton(String name, int slot) {
        this.name = name;
        this.path = "CategoryList." + name + ".DisplayItem.";
        this.slot = slot;
        this.useCustomSlot = true;
    }

    @Override
    public int getSlot() {
        return useCustomSlot ? this.slot : config.getInt(this.path + "slot");
    }

    @Override
    public void resolveItemStack() {
        ItemStack itemStack = XMaterial.matchXMaterial(config.getString(this.path + "material")).orElse(XMaterial.BEDROCK).parseItem();
        this.setItemStack(itemStack);
    }

    @Override
    public Material getMaterial() {
        return XMaterial.matchXMaterial(config.getString(this.path + "material")).orElse(XMaterial.BEDROCK).parseMaterial();
    }

    @Override
    public String getDisplayName() {
        return AdvetureUtils.translate(config.getString(this.path + "displayname"));
    }

    @Override
    public List<String> getLore() {
        return AdvetureUtils.translate(config.getStringList(this.path + "lore"));
    }

    @Override
    public int getAmount() {
        return 1;
    }

    @Override
    public void onClick(GUIClickContext context) {
        if (this.name.equals("decoration")) return;

        CategoryConfig categoryConfig = new CategoryConfig(this.name);

        Player player = (Player) context.getEvent().getWhoClicked();

        RewardCategoryContainer container = new RewardCategoryContainer(categoryConfig, player);


        container.show(player);

    }

}
