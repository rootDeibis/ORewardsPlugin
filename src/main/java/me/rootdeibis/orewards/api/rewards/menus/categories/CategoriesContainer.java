package me.rootdeibis.orewards.api.rewards.menus.categories;

import me.rootdeibis.commonlib.factory.gui.holders.GuiContainer;
import me.rootdeibis.orewards.ORewardsMain;
import me.rootdeibis.orewards.api.configuration.RFile;
import me.rootdeibis.orewards.api.rewards.Reward;
import me.rootdeibis.orewards.api.rewards.menus.ActionButton;
import me.rootdeibis.orewards.api.rewards.menus.category.RewardButton;
import me.rootdeibis.orewards.api.rewards.player.PlayerReward;
import me.rootdeibis.orewards.utils.AdvetureUtils;
import me.rootdeibis.orewards.utils.DurationParser;
import me.rootdeibis.orewards.utils.Placeholders;
import org.bukkit.entity.Player;

public class CategoriesContainer extends GuiContainer {

    private final RFile config;

    private final Player player;
    public CategoriesContainer(Player player) {
        this.config = ORewardsMain.getCore().getFileManager().use("categories.yml");
        this.player = player;

        ORewardsMain.getCore().getRewardManager().checkPlayer(player.getUniqueId());

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
                .forEach(key -> {

                     int slot = config.getInt("CategoryList." + key + ".DisplayItem.slot");


                     this.addButton(slot, new CategoryButton(key));

                });

        if (config.contains("CategoryGUIOptions.close-btn-slot")) {
            this.addButton(config.getInt("CategoryGUIOptions.close-btn-slot"), ActionButton.of(ActionButton.Type.CLOSE));
        }

        
        if(config.contains("CategoryGUIOptions.decoration.DisplayItem.slot")) {
            config.getIntegerList("CategoryGUIOptions.decoration.DisplayItem.slot").forEach(slot -> this.addButton(slot,new CategoryButton("CategoryGUIOptions.decoration.DisplayItem.","decoration")));
        } else if (config.contains("CategoryList.decoration.DisplayItem")) {
            config.getIntegerList("CategoryList.decoration.DisplayItem.slot").forEach(slot -> this.addButton(slot,new CategoryButton("decoration")));
        }

        PlayerReward playerReward = ORewardsMain.getCore().getRewardManager().getPlayerReward(this.player.getUniqueId());

        ORewardsMain.getCore().getRewardManager().getRewards().stream().filter(Reward::displayInCategories)
                .forEach(reward -> {
                    Placeholders placeholders = new Placeholders();

                    placeholders.add("player_name", player.getName());

                    placeholders.add("reward_displayname", reward.getDisplayName());

                    placeholders.add("reward_cooldown", () -> DurationParser.format(playerReward.getRewardUntil(reward.getName())));

                    this.addButton(reward.getDisplaySlot(), new RewardButton(playerReward, reward, placeholders));
                });
    }

    @Override
    public void show(Player player) {
        super.show(player);

        ORewardsMain.resumeTask();
    }
}

