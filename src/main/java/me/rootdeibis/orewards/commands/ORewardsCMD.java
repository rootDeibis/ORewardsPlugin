package me.rootdeibis.orewards.commands;


import me.rootdeibis.orewards.api.commands.annotations.CoreCommand;
import me.rootdeibis.orewards.api.commands.annotations.CoreCommandLoader;
import me.rootdeibis.orewards.api.commands.annotations.CoreSubCommands;
import me.rootdeibis.orewards.api.commands.annotations.TabCompletion;
import me.rootdeibis.orewards.utils.AdvetureUtils;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;


@CoreCommandLoader
public class ORewardsCMD {


    @CoreCommand(name = "orewards", aliases = "orw", permission = "orewards.command")
    @CoreSubCommands(list = {ORewardsSubCMD.class})
    public boolean onCommand(CommandSender sender, String[] args) {

        if (args.length == 0) {

            sender.sendMessage(AdvetureUtils.translate("&eORewards &7> &cplease use /orewards help for more information."));
            return false;
        }

        return false;
    }

    @TabCompletion(target = TabCompletion.TargetType.MAIN, command = "orewards")
    public List<String> getCompletions() {
        return new ArrayList<>();
    }
}
