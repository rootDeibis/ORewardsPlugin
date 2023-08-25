package me.rootdeibis.orewards.api.rewards.menus.category;

import me.rootdeibis.orewards.ORewardsMain;
import me.rootdeibis.orewards.api.configuration.RFile;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class CategoryConfig {

    private final String path;
    private final RFile config = ORewardsMain.getCore().getFileManager().use("categories.yml");

    private final String name;
    public CategoryConfig(String name) {
        this.name = name;
        this.path = "CategoryList." + name + ".";
    }

    public String getName() {
        return name;
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

    public ConfigurationSection getDecorationSection() {
        return this.config.contains(this.path + "decoration.DisplayItem") ? this.config.getConfigurationSection(this.path + "decoration.DisplayItem") : this.config.getConfigurationSection(this.path + "decoration");
    }

    public boolean hasDecoration() {
        return this.config.isConfigurationSection(this.path + "decoration") || this.config.isConfigurationSection(this.path + "decoration.DisplayItem");
    }
    public String getPath() {
        return path;
    }

    public RFile getConfig() {
        return config;
    }

    public static CategoryConfig loadFromName(String name) {
        RFile conf = ORewardsMain.getCore().getFileManager().use("categories.yml");

        if (!conf.contains("CategoryList." + name)) return null;

        return new CategoryConfig(name);

    }


}
