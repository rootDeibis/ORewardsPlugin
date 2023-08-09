package me.rootdeibis.orewards.api.menus;

import me.rootdeibis.orewards.ORewardsMain;
import me.rootdeibis.orewards.api.guifactory.GUIButton;
import me.rootdeibis.orewards.api.guifactory.GUIHolder;
import me.rootdeibis.orewards.api.rewards.Reward;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CategoryMenu extends GUIHolder {
    private final Categories.CategoryConfig categoryConfig;
    private final List<Reward> rewards;

    private final UUID viewer;

    public CategoryMenu(Categories.CategoryConfig categoryConfig, UUID viewer) {
        super(categoryConfig.getGUIRows(), categoryConfig.getGUITitle());

        this.categoryConfig = categoryConfig;

        this.rewards = ORewardsMain.getRewardManager().getRewards().stream().filter(r -> categoryConfig.getRewardsNames().contains(r.getName())).collect(Collectors.toList());

        this.viewer = viewer;

        this.loadRewardsButtons();
    }

    public void loadRewardsButtons() {
        this.rewards.forEach(reward -> {


            Reward.Status status = reward.getStatus(this.viewer);

            String btnDataPath = "DisplayOptions." + status.path();

            GUIButton.Placeholders placeholders = new GUIButton.Placeholders();

            placeholders.add("reward_displayname", reward.getDisplayName());

            GUIButton rewardBtn = new GUIButton();

            rewardBtn.setDataFrom(reward.getRewardConfig(), btnDataPath);
            rewardBtn.setSlot(reward.getDisplaySlot());
            rewardBtn.setPlaceholders(placeholders);

            this.addButtons(rewardBtn);

        });

        this.build();
    }
}
