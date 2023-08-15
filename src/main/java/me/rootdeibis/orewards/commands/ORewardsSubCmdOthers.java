package me.rootdeibis.orewards.commands;

import me.rootdeibis.orewards.ORewardsMain;
import me.rootdeibis.orewards.api.commands.annotations.CoreCommand;
import me.rootdeibis.orewards.api.rewards.player.PlayerReward;
import me.rootdeibis.orewards.utils.AdvetureUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ORewardsSubCmdOthers {


    private final String RELOAD_MESSAGE = "&eORewards &7> &aConfiguration and rewards files reloaded!";
    private final String RESET_USAGE_MESSAGE = "&eORewards &7> &cplease use /orewards reset <player_name> <all | reward_name>!";

    private final String PLAYER_OFFLINE_MESSAGE = "&eORewards &7> &cPlayer '%s' is not online";


    private final String INVALID_REWARD_NAME_MESSAGE  = "There is no reward with the name '%s'.";

    private final String RESET_ALL_REWARDS_MESSAGE = "&eORewards &7> &aAll %s player rewards were reset.";

    private final String REWARDS_RESET_MESSAGE = "&eORewards &7> &aThis reward was reset for %s.";

    @CoreCommand(name = "reload", permission = "orewards.cmd.reload")
    public boolean onReloadCommand(CommandSender sender) {

        ORewardsMain.getCore().getFileManager().reload();
        ORewardsMain.getCore().getRewardManager().loadRewardsInDirectory();
        sender.sendMessage(AdvetureUtils.translate(RELOAD_MESSAGE));

        return true;

    }

    @CoreCommand(name = "reset", permission = "orewards.cmd.reset")
    public boolean onResetCommand(CommandSender sender, String[] args) {

        if (args.length == 2) {

            Player player = Bukkit.getPlayer(args[0]);

            if (player != null) {
                PlayerReward playerReward = ORewardsMain.getCore().getRewardManager().getPlayerReward(player.getUniqueId());


               if(args[1].equals("all")) {

                   playerReward.getUntils().keySet().forEach(name -> {
                          playerReward.resetRewardUntil(name);
                          playerReward.saveUntil(name)
                          ;
                   });

                   sender.sendMessage(AdvetureUtils.translate(String.format(RESET_ALL_REWARDS_MESSAGE, player.getName())));

               } else {
                   if (ORewardsMain.getCore().getRewardManager().existsReward(args[1])) {



                       playerReward.resetRewardUntil(args[1]);
                       playerReward.saveUntil(args[1]);

                       sender.sendMessage(AdvetureUtils.translate(String.format(REWARDS_RESET_MESSAGE, player.getName())));

                   } else {
                       sender.sendMessage(AdvetureUtils.translate(String.format(INVALID_REWARD_NAME_MESSAGE, args[1])));
                   }
               }


            } else {
                sender.sendMessage(AdvetureUtils.translate(String.format(PLAYER_OFFLINE_MESSAGE, args[0])));
            }


        } else {
            sender.sendMessage(AdvetureUtils.translate(RESET_USAGE_MESSAGE));
        }

        return false;
    }

    @CoreCommand(name = "help", permission = "orewards.cmd.help")
    public boolean onHelpCommand(CommandSender sender) {


        sender.sendMessage(AdvetureUtils.translate(
                "&7============ [ &eORewards &7] ============ "
                + "\n&6List commands:\n\n"
                + "&d/orewards reset <player_name> <all - reward name> &7- Reset player specific or all rewards\n"
                + "&d/orewards open <category_name> (optional: <player_name>) &7- Open specific category menu\n"
                + "&d/orewards menu &7- Open categories menu\n"
                + "&d/orewards reload &7- Reload configuration and rewards files\n\n"
                + "====================================="
        ));

        return true;
    }
}
