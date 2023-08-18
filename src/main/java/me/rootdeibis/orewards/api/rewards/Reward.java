package me.rootdeibis.orewards.api.rewards;

import me.rootdeibis.commonlib.utils.XSound;
import me.rootdeibis.orewards.ORewardsMain;
import me.rootdeibis.orewards.api.configuration.RFile;
import me.rootdeibis.orewards.api.rewards.player.PlayerReward;
import me.rootdeibis.orewards.utils.AdvetureUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.UUID;

public class Reward {

    private final String CMD_IDENTIFICATOR = "cmd:";
    private final String MSG_IDENTIFICATOR = "msg:";
    private final String MINIMSG_IDENTIFICATOR = "minimsg:";



    public enum Status {

        AVAILABLE("Available"),
        NO_AVAILABLE("NoAvailable"),

        PERMISSION("NeedPermission");

        private final String status;

        Status(String status) {
            this.status = status;
        }

        public String path() {
            return status;
        }
    }
    private final String name;
    private final RFile rewardConfig;

    public Reward(String name) {
        this.name = name;
        this.rewardConfig = ORewardsMain.getCore().getFileManager().dir("rewards").use(name + ".yml");
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return this.rewardConfig.getString("RewardOptions.displayname");
    }

    public String getTime() {
        String time = this.rewardConfig.getString("RewardOptions.time");

        return time != null ? time : "99999mo";
    }

    public boolean displayInCategories() {
        return this.rewardConfig.getBoolean("RewardOptions.displayInCategory");
    }

    public String getPermission() {

        String permission = this.rewardConfig.getString("RewardOptions.permission");

        return permission != null ? permission : "none";

    }

    public int getDisplaySlot() {
        return this.rewardConfig.getInt("RewardOptions.slot");
    }


    public Status getStatus(UUID player) {
        RewardManager rewardManager = ORewardsMain.getCore().getRewardManager();
        PlayerReward playerReward = rewardManager.getPlayerReward(player);

        Player p = Bukkit.getPlayer(player);

        if(!this.getPermission().equals("none") && p != null && !p.hasPermission(this.getPermission())) return Status.PERMISSION;

        Date currentDate = new Date();
        Date untilDate = new Date(playerReward.getRewardUntil(this.getName()));

        if(currentDate.after(untilDate)) return Status.AVAILABLE;

        return Status.NO_AVAILABLE;

    }

    public Sound getStatusSound(UUID player) {
        String soundPath = "RewardOptions.Sounds.";
        XSound sound = XSound.BLOCK_ANVIL_BREAK;

        switch (getStatus(player)) {
            case AVAILABLE:
                sound = XSound.matchXSound(this.rewardConfig.getString(soundPath + Status.AVAILABLE.path())).orElse(sound);
                break;
            case NO_AVAILABLE:
                sound = XSound.matchXSound(this.rewardConfig.getString(soundPath + Status.NO_AVAILABLE.path())).orElse(sound);
                break;
            case PERMISSION:
                sound = XSound.matchXSound(this.rewardConfig.getString(soundPath + Status.PERMISSION.path())).orElse(sound);
                break;
        }

        return sound.parseSound();
    }

    public String[] getActions() {
        return this.rewardConfig.getConfigurationSection("RewardsList").getKeys(false).stream()
                .map(r -> this.rewardConfig.getString("RewardsList." + r))
                .toArray(String[]::new);
    }




    public RFile getRewardConfig() {
        return rewardConfig;
    }

    public void claim(Player player, String... actions) {


        String[] a = actions.length == 0 ? this.getActions() : actions;

        for (String action : a) {

            if(action.startsWith(MSG_IDENTIFICATOR)) {
                player.sendMessage(AdvetureUtils.translate(action.replaceAll(MSG_IDENTIFICATOR, "")));
            } else if(action.startsWith(MINIMSG_IDENTIFICATOR)) {

                AdvetureUtils.sender(player, action.replaceAll(MINIMSG_IDENTIFICATOR, ""));
            } else {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), action.replaceAll(CMD_IDENTIFICATOR, ""));
            }

        }

    }

}
