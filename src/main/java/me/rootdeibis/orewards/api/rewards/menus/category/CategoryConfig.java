package me.rootdeibis.orewards.api.rewards.menus.category;

import me.rootdeibis.orewards.ORewardsMain;
import me.rootdeibis.orewards.api.configuration.RFile;

import java.util.List;

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


    public static CategoryConfig loadFromName(String name) {
        RFile conf = ORewardsMain.getCore().getFileManager().use("categories.yml");

        if (!conf.contains("CategoryList." + name)) return null;

        return new CategoryConfig(name);

    }
}
