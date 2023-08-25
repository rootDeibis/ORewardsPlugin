package me.rootdeibis.orewards.api.rewards.menus;

import me.rootdeibis.commonlib.factory.gui.button.GuiButton;
import me.rootdeibis.commonlib.factory.gui.button.GuiButtonData;
import me.rootdeibis.commonlib.factory.gui.context.GUIClickContext;
import me.rootdeibis.commonlib.utils.XMaterial;
import me.rootdeibis.orewards.ORewardsMain;
import me.rootdeibis.orewards.api.configuration.RFile;
import me.rootdeibis.orewards.api.rewards.menus.categories.CategoriesContainer;
import me.rootdeibis.orewards.utils.AdvetureUtils;
import me.rootdeibis.orewards.utils.HeadTool;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ActionButton extends GuiButton {




    public enum Type {
        CLOSE,
        BACK_TO_MAIN
    }

    private final Type action;
    private final String path;
    private final RFile config;

    private final GuiButtonData<ItemStack> data = new GuiButtonData<>();

    public ActionButton(Type action ,String path, RFile config) {
        this.action = action;
        this.path = path;
        this.config = config;
    }
    @Override
    public GuiButtonData<?> getData() {
        ItemStack itemStack = XMaterial.matchXMaterial(config.getString(this.path + "material")).orElse(XMaterial.BEDROCK).parseItem();


        this.data.setMaterial(itemStack);

        this.data.setDisplayName(AdvetureUtils.translate(config.getString(this.path + "displayname")));

        this.data.setLore(AdvetureUtils.translate(config.getStringList(this.path + "lore")));
        this.data.setAmount(1);

        String identifier = config.getString( this.path + "textures");
        if (this.data.getTextures() == null && identifier!= null && identifier.length() > 0) {
            Bukkit.getScheduler().runTaskAsynchronously(ORewardsMain.getMain(), () -> {

                HeadTool.loadTextures(identifier);
                this.data.setTextures(HeadTool.getSkullMetaCache().get(identifier));
            });
        }


        return data;
    }
    @Override
    public void onClick(GUIClickContext context) {
        Player player = (Player) context.getEvent().getWhoClicked();

        switch (this.action) {
            case CLOSE:
                player.getOpenInventory().close();
                break;
            case BACK_TO_MAIN:
                new CategoriesContainer(player).show(player);
                break;
        }

    }


    public static GuiButton of(Type type) {
        RFile rFile = ORewardsMain.getCore().getFileManager().use("categories.yml");

        return type == Type.CLOSE ?
                new ActionButton(Type.CLOSE, "action_buttons.close.", rFile) :
                new ActionButton(Type.BACK_TO_MAIN, "action_buttons.back.", rFile);

    }








}
