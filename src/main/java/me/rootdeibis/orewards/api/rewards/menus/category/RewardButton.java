package me.rootdeibis.orewards.api.rewards.menus.category;

import me.rootdeibis.commonlib.factory.gui.button.GuiButton;
import me.rootdeibis.commonlib.factory.gui.button.GuiButtonData;
import me.rootdeibis.commonlib.factory.gui.context.GUIClickContext;
import me.rootdeibis.commonlib.utils.XMaterial;
import me.rootdeibis.orewards.ORewardsMain;
import me.rootdeibis.orewards.api.configuration.RFile;
import me.rootdeibis.orewards.api.rewards.Reward;
import me.rootdeibis.orewards.api.rewards.player.PlayerReward;
import me.rootdeibis.orewards.utils.AdvetureUtils;
import me.rootdeibis.orewards.utils.DurationParser;
import me.rootdeibis.orewards.utils.HeadTool;
import me.rootdeibis.orewards.utils.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class RewardButton extends GuiButton {

    private final GuiButtonData<ItemStack> data = new GuiButtonData<>();
    private final PlayerReward playerReward;
    private final Reward reward;
    private final Placeholders placeholders;



    public RewardButton(PlayerReward playerReward, Reward reward, Placeholders placeholders) {
        this.playerReward = playerReward;
        this.reward = reward;
        this.placeholders = placeholders;


    }

    private String getStatusPath() {
        return reward.getStatus(this.playerReward.getUUID()).path() + ".";
    }


    @Override
    public GuiButtonData<ItemStack> getData() {
        String statusPath = "DisplayOptions." + getStatusPath() + ".";
        RFile config = this.reward.getRewardConfig();


        ItemStack itemStack = XMaterial.matchXMaterial(config.getString(statusPath + "material")).orElse(XMaterial.BEDROCK).parseItem();

        this.data.setMaterial(itemStack);

        this.data.setDisplayName(AdvetureUtils.translate(this.placeholders.apply(config.getString(statusPath + "displayname"))));

        this.data.setLore(AdvetureUtils.translate(this.placeholders.apply(config.getStringList(statusPath + "lore"))));
        this.data.setAmount(1);
        String identifier = config.getString( statusPath + "textures");

        if (this.data.getTextures() == null && identifier != null) {
            Bukkit.getScheduler().runTaskAsynchronously(ORewardsMain.getMain(), () -> {

                HeadTool.loadTextures(identifier);

                this.data.setTextures(HeadTool.getSkullMetaCache().get(identifier));
            });
        }

        return this.data;
    }


    @Override
    public void onClick(GUIClickContext e) {
        RFile config = ORewardsMain.getCore().getFileManager().use("config.yml");


        Reward.Status status = reward.getStatus(this.playerReward.getUUID());
        Player player = (Player) e.getEvent().getWhoClicked();



        if(status == Reward.Status.AVAILABLE) {

            playerReward.setRewardUntil(reward.getName(), DurationParser.addToDate(reward.getTime()).getTime());



            reward.claim(player,
                    Arrays.stream(reward.getActions()).map(placeholders::apply)
                            .toArray(String[]::new)
            );



            if (config.getBoolean("rewardGuiOptions.close-on-claim")) {
                e.getContainer().close(player);
            }

            playerReward.saveUntil(reward.getName());
        }

        player.playSound(player.getLocation(), reward.getStatusSound(player.getUniqueId()), 1,2);

    }
}
