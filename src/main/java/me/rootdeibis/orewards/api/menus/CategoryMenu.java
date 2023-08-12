package me.rootdeibis.orewards.api.menus;

import me.rootdeibis.orewards.ORewardsMain;
import me.rootdeibis.orewards.api.guifactory.GUIButton;
import me.rootdeibis.orewards.api.guifactory.GUIHolder;
import me.rootdeibis.orewards.api.rewards.Reward;
import me.rootdeibis.orewards.api.rewards.player.PlayerReward;
import me.rootdeibis.orewards.utils.DurationParser;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CategoryMenu extends GUIHolder {
    private final Categories.CategoryConfig categoryConfig;
    private final List<Reward> rewards;


    private final UUID viewer;

    public CategoryMenu(Categories.CategoryConfig categoryConfig, UUID viewer) {
        super(categoryConfig.getGUIRows(), categoryConfig.getGUITitle());

        ORewardsMain.getRewardManager().checkPlayer(viewer);

        this.categoryConfig = categoryConfig;

        this.rewards = ORewardsMain.getRewardManager().getRewards().stream().filter(r -> categoryConfig.getRewardsNames().contains(r.getName())).collect(Collectors.toList());
        this.viewer = viewer;

        this.loadRewardsButtons();
    }

    public void loadRewardsButtons() {

        PlayerReward playerReward = ORewardsMain.getRewardManager().getPlayerReward(this.viewer);
        this.rewards.forEach(reward -> {


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

        this.build();
    }



}
