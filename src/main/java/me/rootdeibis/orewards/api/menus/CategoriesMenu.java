package me.rootdeibis.orewards.api.menus;


import me.rootdeibis.orewards.ORewardsMain;
import me.rootdeibis.orewards.api.Files.RFile;
import me.rootdeibis.orewards.api.guifactory.GUIButton;
import me.rootdeibis.orewards.api.guifactory.GUIHolder;

public class CategoriesMenu extends GUIHolder {


    private RFile config;

    public CategoriesMenu() {
        config = ORewardsMain.getFileManager().use("categories.yml");

        this.setRows(config.getInt("CategoryGUIOptions.rows"));
        this.setTitle(config.getString("CategoryGUIOptions.GUITitle"));

        this.loadCategoriesButtons();
    }

    public void loadCategoriesButtons() {


        config.getConfigurationSection("CategoryList").getKeys(false)
                .forEach(categoryKey -> {

                    String categoryPath = "CategoryList." + categoryKey + ".";

                    String GUITitle = categoryPath + "GUITitle";

                    String GUISize = categoryPath + "rows";


                    GUIButton guiButton = new GUIButton();

                    guiButton.setDataFrom(config, categoryPath + "DisplayItem");

                    if(categoryKey.equalsIgnoreCase("decoration")) {

                        config.getIntegerList(categoryPath + "DisplayItem." + "slot").forEach(slot -> {

                            GUIButton decorationSlot = guiButton.clone();

                            decorationSlot.setSlot(slot);

                            this.addButtons(decorationSlot);

                        });

                    } else {
                        this.addButtons(guiButton);
                    }



                });

        this.build();

    }
}
