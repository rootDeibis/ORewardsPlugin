package me.rootdeibis.orewards.api.rewards.menus.category;

import me.rootdeibis.commonlib.factory.gui.holders.GuiContainer;
import me.rootdeibis.orewards.ORewardsMain;
import me.rootdeibis.orewards.api.rewards.Reward;
import me.rootdeibis.orewards.api.rewards.player.PlayerReward;
import me.rootdeibis.orewards.utils.AdvetureUtils;
import me.rootdeibis.orewards.utils.DurationParser;
import me.rootdeibis.orewards.utils.Placeholders;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class RewardCategoryContainer extends GuiContainer {

    private final CategoryConfig categoryConfig;
    private final Player player;

    private final List<Reward> rewards;
    public RewardCategoryContainer(CategoryConfig categoryConfig, Player player) {
        this.categoryConfig = categoryConfig;
        this.player = player;
        this.rewards = ORewardsMain.getCore().getRewardManager().getRewards().stream().filter(r -> categoryConfig.getRewardsNames().contains(r.getName()) && !r.displayInCategories()).collect(Collectors.toList());


        ORewardsMain.getCore().getRewardManager().checkPlayer(player.getUniqueId());


        this.load();

    }

    @Override
    public String getTitle() {
        return AdvetureUtils.translate(this.categoryConfig.getGUITitle());
    }

    @Override
    public int getSize() {
        return this.categoryConfig.getGUIRows();
    }



    private void load() {

        PlayerReward playerReward = ORewardsMain.getCore().getRewardManager().getPlayerReward(this.player.getUniqueId());




        for (Reward reward : this.rewards) {
            Placeholders placeholders = new Placeholders();

            placeholders.add("player_name", player.getName());

            placeholders.add("reward_displayname", reward.getDisplayName());

            placeholders.add("reward_cooldown", () -> DurationParser.format(playerReward.getRewardUntil(reward.getName())));

            this.addButton(reward.getDisplaySlot(),new RewardButton(playerReward, reward, placeholders));

        }

    }

    @Override
    public void show(Player player) {
        super.show(player);

        ORewardsMain.resumeTask();
    }
}
