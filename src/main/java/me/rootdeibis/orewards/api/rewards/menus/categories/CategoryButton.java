package me.rootdeibis.orewards.api.rewards.menus.categories;


import me.rootdeibis.commonlib.factory.gui.button.GuiButton;
import me.rootdeibis.commonlib.factory.gui.button.GuiButtonData;
import me.rootdeibis.commonlib.factory.gui.context.GUIClickContext;
import me.rootdeibis.commonlib.utils.XMaterial;
import me.rootdeibis.orewards.ORewardsMain;
import me.rootdeibis.orewards.api.configuration.RFile;
import me.rootdeibis.orewards.api.rewards.menus.category.CategoryConfig;
import me.rootdeibis.orewards.api.rewards.menus.category.RewardCategoryContainer;
import me.rootdeibis.orewards.utils.AdvetureUtils;
import me.rootdeibis.orewards.utils.HeadTool;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

class CategoryButton extends GuiButton {

    private final String name;
    private final String path;
    private final RFile config = ORewardsMain.getCore().getFileManager().use("categories.yml");

    private final GuiButtonData<ItemStack> data = new GuiButtonData<>();


    public CategoryButton(String name) {
        this.name = name;
        this.path = "CategoryList." + name + ".DisplayItem.";
    }


    @Override
    public GuiButtonData<ItemStack> getData() {

        ItemStack itemStack = XMaterial.matchXMaterial(config.getString(this.path + "material")).orElse(XMaterial.BEDROCK).parseItem();


        this.data.setMaterial(itemStack);

        this.data.setDisplayName(AdvetureUtils.translate(config.getString(this.path + "displayname")));

        this.data.setLore(AdvetureUtils.translate(config.getStringList(this.path + "lore")));
        this.data.setAmount(1);

        String identifier = config.getString( this.path + "textures");
        if (this.data.getTextures() == null && identifier!= null) {
            Bukkit.getScheduler().runTaskAsynchronously(ORewardsMain.getMain(), () -> {

                HeadTool.loadTextures(identifier);
                this.data.setTextures(HeadTool.getSkullMetaCache().get(identifier));
            });
        }


        return this.data;
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
