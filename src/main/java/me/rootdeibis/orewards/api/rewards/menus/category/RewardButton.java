package me.rootdeibis.orewards.api.rewards.menus.category;

import com.cryptomorin.xseries.XMaterial;
import me.rootdeibis.commonlib.factory.gui.holders.GuiButton;
import me.rootdeibis.commonlib.factory.gui.holders.context.GUIClickContext;
import me.rootdeibis.orewards.api.rewards.Reward;
import me.rootdeibis.orewards.api.rewards.player.PlayerReward;
import me.rootdeibis.orewards.utils.AdvetureUtils;
import me.rootdeibis.orewards.utils.DurationParser;
import me.rootdeibis.orewards.utils.Placeholders;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class RewardButton extends GuiButton {

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
    public int getSlot() {


        return this.reward.getDisplaySlot();
    }


    @Override
    public void resolveItemStack() {
        ItemStack itemStack = XMaterial.matchXMaterial(this.reward.getRewardConfig().getString("DisplayOptions." + getStatusPath() + "material")).orElse(XMaterial.BEDROCK).parseItem();
        this.setItemStack(itemStack);
    }

    @Override
    public Material getMaterial() {
        return null;
    }

    @Override
    public String getDisplayName() {
        return AdvetureUtils.translate(this.placeholders.apply(this.reward.getRewardConfig().getString("DisplayOptions." + getStatusPath() + "displayname")));
    }

    @Override
    public List<String> getLore() {
        return AdvetureUtils.translate(this.placeholders.apply(this.reward.getRewardConfig().getStringList("DisplayOptions." + getStatusPath() + "lore")));
    }

    @Override
    public int getAmount() {
        return 1;
    }

    @Override
    public void onClick(GUIClickContext e) {


        Reward.Status status = reward.getStatus(this.playerReward.getUUID());
        Player player = (Player) e.getEvent().getWhoClicked();



        if(status == Reward.Status.AVAILABLE) {

            playerReward.setRewardUntil(reward.getName(), DurationParser.addToDate(reward.getTime()).getTime());



            reward.claim(player,
                    Arrays.stream(reward.getActions()).map(placeholders::apply)
                            .toArray(String[]::new)
            );



            playerReward.saveUntil(reward.getName());
        }

        player.playSound(player.getLocation(), reward.getStatusSound(player.getUniqueId()), 1,2);

    }
}
