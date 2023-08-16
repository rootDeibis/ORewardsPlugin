package me.rootdeibis.orewards.api.rewards.menus.categories;

import me.rootdeibis.commonlib.factory.gui.holders.GuiContainer;
import me.rootdeibis.orewards.ORewardsMain;
import me.rootdeibis.orewards.api.configuration.RFile;
import me.rootdeibis.orewards.utils.AdvetureUtils;

public class CategoriesContainer extends GuiContainer {

    private final RFile config;

    public CategoriesContainer() {
        this.config = ORewardsMain.getCore().getFileManager().use("categories.yml");
        load();


    }

    @Override
    public String getTitle() {
        return AdvetureUtils.translate(config.getString("CategoryGUIOptions.GUITitle"));
    }

    @Override
    public int getSize() {
        return config.getInt("CategoryGUIOptions.rows");
    }
    
    private void load() {

        config.getConfigurationSection("CategoryList")
                .getKeys(false)
                .stream().filter(r -> !r.equalsIgnoreCase("decoration"))
                .forEach(key -> this.addButton(new CategoryButton(key)));
        
        config.getIntegerList("CategoryList.decoration.DisplayItem.slot").forEach(slot -> this.addButton(new CategoryButton("decoration", slot)));
        }
    }

