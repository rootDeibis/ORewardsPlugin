package me.rootdeibis.orewards.hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.rootdeibis.orewards.ORewardsMain;
import me.rootdeibis.orewards.api.configuration.RFile;
import me.rootdeibis.orewards.api.rewards.Reward;
import me.rootdeibis.orewards.api.rewards.RewardManager;
import me.rootdeibis.orewards.api.rewards.player.PlayerReward;
import me.rootdeibis.orewards.utils.AdvetureUtils;
import me.rootdeibis.orewards.utils.DurationParser;
import me.rootdeibis.orewards.utils.Placeholders;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class ORewardsExpansion extends PlaceholderExpansion {


    public ORewardsExpansion() {

    }


    @Override
    public @NotNull String getIdentifier() {
        return "orewards";
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", ORewardsMain.getMain().getDescription().getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return ORewardsMain.getMain().getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        RewardManager rewardManager = ORewardsMain.getCore().getRewardManager();
        PlayerReward playerReward = rewardManager.getPlayerReward(player.getUniqueId());

        RFile config = ORewardsMain.getCore().getFileManager().use("config.yml");
        String placeholdersPath = "Messages.PlaceholderAPI.getRewardIsAvailable.";

        Placeholders placeholders = new Placeholders();

        if(params.contains("available_")) {

            String[] args = params.split("_");

            if(args.length > 1) {
                String rewardName = String.join("_", Arrays.stream(args, 1, args.length).toArray(String[]::new));


                Reward reward = rewardManager.getReward(rewardName);

                if(reward == null) return null;

                placeholders.add("reward_displayname", reward.getDisplayName());

                if(reward.getStatus(player.getUniqueId()) == Reward.Status.AVAILABLE) {


                    return AdvetureUtils.translate(config.getString(placeholdersPath + "Available"));

                } else {

                    return AdvetureUtils.translate(config.getString(placeholdersPath + "NoAvailable"));

                }

            } else {

                return null;

            }

        }

        if(params.contains("cooldown_")) {
            String[] args = params.split("_");

            if(args.length > 1) {
                String rewardName = String.join("_", Arrays.stream(args, 1, args.length).toArray(String[]::new));

                Reward reward = rewardManager.getReward(rewardName);

                if(reward != null && reward.getStatus(player.getUniqueId()) == Reward.Status.NO_AVAILABLE) {

                    return reward.getTime().equals("99999mo") ? null : DurationParser.format(playerReward.getRewardUntil(rewardName));

                } else {
                    return "0";
                }

            }
        }

        if(params.equalsIgnoreCase("availables")) {
            return String.valueOf(playerReward.getAvailables().size());
        }



        return null;
    }


}
