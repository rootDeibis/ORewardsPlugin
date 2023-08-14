package me.rootdeibis.orewards.api.rewards.menus;


import me.rootdeibis.orewards.ORewardsMain;
import me.rootdeibis.orewards.api.configuration.RFile;
import me.rootdeibis.orewards.api.gui.GUIButton;
import me.rootdeibis.orewards.api.gui.GUIHolder;
import me.rootdeibis.orewards.api.rewards.Reward;
import me.rootdeibis.orewards.api.rewards.player.PlayerReward;
import me.rootdeibis.orewards.utils.DurationParser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class Categories extends GUIHolder {


    private RFile config;
    private final UUID viewer;

    public Categories(UUID viewer) {
        this.viewer = viewer;
        config = ORewardsMain.getCore().getFileManager().use("categories.yml");

        this.setRows(config.getInt("CategoryGUIOptions.rows"));
        this.setTitle(config.getString("CategoryGUIOptions.GUITitle"));



            this.loadDecoration();
            this.loadCategoriesButtons();



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

        PlayerReward playerReward = ORewardsMain.getCore().getRewardManager().getPlayerReward(this.viewer);

        ORewardsMain.getCore().getRewardManager().getRewards().stream().filter(Reward::displayInCategories).collect(Collectors.toList())
                .forEach(reward -> {


                    GUIButton.Placeholders placeholders = new GUIButton.Placeholders();

                    placeholders.add("reward_displayname", reward.getDisplayName());

                    placeholders.add("reward_cooldown", () -> DurationParser.format(playerReward.getRewardUntil(reward.getName())));

                    placeholders.add("player_name", Bukkit.getOfflinePlayer(this.viewer).getName());

                    GUIButton rewardBtn = new GUIButton();

                    rewardBtn.setDataLive(() -> new Object[]{ reward.getRewardConfig(), "DisplayOptions." + reward.getStatus(this.viewer).path()});


                    rewardBtn.setSlot(reward.getDisplaySlot());
                    rewardBtn.setPlaceholders(placeholders);


                    rewardBtn.onClick(e -> {


                        Reward.Status status = reward.getStatus(this.viewer);

                        if(status == Reward.Status.AVAILABLE) {

                            playerReward.setRewardUntil(reward.getName(), DurationParser.addToDate(reward.getTime()).getTime());
                            playerReward.saveUntil(reward.getName());


                            reward.claim(e.getPlayer(),
                                    Arrays.stream(reward.getActions()).map(placeholders::apply)
                                            .toArray(String[]::new)
                            );
                        }

                        e.getPlayer().playSound(e.getPlayer().getLocation(), reward.getStatusSound(this.viewer), 1,2);

                    });

                    this.addButtons(rewardBtn);
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

    public static void show(Player player) {
        if(ORewardsMain.getCore().getRewardManager().checkPlayer(player.getUniqueId())) {
            player.openInventory(new Categories(player.getUniqueId()).getInventory());
        }
    }

    public static class CategoryConfig {

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

            return new Categories.CategoryConfig(name);

        }
    }
}
