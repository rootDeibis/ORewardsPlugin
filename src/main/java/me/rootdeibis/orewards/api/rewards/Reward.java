package me.rootdeibis.orewards.api.rewards;

import com.cryptomorin.xseries.XSound;
import me.rootdeibis.orewards.ORewardsMain;
import me.rootdeibis.orewards.api.Files.RFile;
import org.bukkit.Sound;

import java.util.UUID;

public class Reward {

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
        this.rewardConfig = ORewardsMain.getFileManager().dir("rewards").use(name + ".yml");
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return this.rewardConfig.getString("RewardOptions.displayname");
    }

    public String getTime() {
        return this.rewardConfig.getString("RewardOptions.time");
    }

    public String getPermission() {

        return this.rewardConfig.getString("RewardOptions.permission");

    }

    public int getDisplaySlot() {
        return this.rewardConfig.getInt("RewardOptions.slot");
    }


    public Status getStatus(UUID player) {
        return Status.AVAILABLE;
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



    public RFile getRewardConfig() {
        return rewardConfig;
    }
}
