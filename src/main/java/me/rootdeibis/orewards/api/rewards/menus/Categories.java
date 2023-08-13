package me.rootdeibis.orewards.api.rewards.menus;


import me.rootdeibis.orewards.ORewardsMain;
import me.rootdeibis.orewards.api.configuration.RFile;
import me.rootdeibis.orewards.api.gui.GUIButton;
import me.rootdeibis.orewards.api.gui.GUIHolder;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class Categories extends GUIHolder {


    private RFile config;

    public Categories(UUID viewer) {
        config = ORewardsMain.getCore().getFileManager().use("categories.yml");

        this.setRows(config.getInt("CategoryGUIOptions.rows"));
        this.setTitle(config.getString("CategoryGUIOptions.GUITitle"));

        this.loadDecoration();
        this.loadCategoriesButtons();

        ORewardsMain.getCore().getRewardManager().checkPlayer(viewer);

        this.build();
    }

    public void loadCategoriesButtons() {

        Set<String> categoriesNames = config.getConfigurationSection("CategoryList").getKeys(false).stream().filter(e -> !e.equalsIgnoreCase("decoration")).collect(Collectors.toSet());


        categoriesNames.forEach(categoryKey -> {

                    CategoryConfig categoryConfig = new CategoryConfig(categoryKey);


                    GUIButton guiButton = new GUIButton();
                    guiButton.setDataFrom(config, categoryConfig.getPath() + "DisplayItem");


                    guiButton.onClick(e -> {
                        GUIHolder categoryMenu = new CategoryMenu(categoryConfig, e.getPlayer().getUniqueId());

                        e.getPlayer().openInventory(categoryMenu.getInventory());
                    });

                    this.addButtons(guiButton);

        });



    }

    public void loadDecoration() {

        List<Integer> slots = this.config.getIntegerList("CategoryList.decoration.DisplayItem.slot");

        slots.forEach(slot -> {
            GUIButton decorationBtn = new GUIButton();
            decorationBtn.setDataFrom(this.config, "CategoryList.decoration.DisplayItem");

            decorationBtn.setSlot(slot);

            this.addButtons(decorationBtn);
        });


    }

    public class CategoryConfig {

        private final String path;
        private final RFile config = ORewardsMain.getCore().getFileManager().use("categories.yml");

        public CategoryConfig(String name) {
            this.path = "CategoryList." + name + ".";
        }

        public String getGUITitle() {
            return this.config.getString(this.path + "GUITitle");
        }

        public int getGUIRows() {
            return this.config.getInt(this.path + "rows");
        }

        public List<String> getRewardsNames() {
            return this.config.getStringList(this.path +  "rewards");
        }

        public String getPath() {
            return path;
        }
    }
}
