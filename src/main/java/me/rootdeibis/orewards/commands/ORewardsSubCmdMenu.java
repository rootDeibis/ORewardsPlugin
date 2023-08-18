package me.rootdeibis.orewards.commands;

import me.rootdeibis.orewards.ORewardsMain;
import me.rootdeibis.orewards.api.commands.annotations.CoreCommand;
import me.rootdeibis.orewards.api.rewards.menus.categories.CategoriesContainer;
import me.rootdeibis.orewards.api.rewards.menus.category.CategoryConfig;
import me.rootdeibis.orewards.api.rewards.menus.category.RewardCategoryContainer;
import me.rootdeibis.orewards.utils.AdvetureUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class ORewardsSubCmdMenu {


    private final String ONLY_PLAYER_MESSAGE = AdvetureUtils.translate("&eORewards &7> &cOnly players can perform this action.");
    private final String CATEGORY_UNKNOWN_MESSAGE = AdvetureUtils.translate("&eORewards &7> &cThere is no category called '%s', check the name.");

    private final String PLAYER_OFFLINE_MESSAGE = AdvetureUtils.translate("&eORewards &7> &cPlayer '%s' is not online");
    @CoreCommand(name = "open", permission = "orewards.cmd.open")
    public boolean onOpenCommand(CommandSender sender, String[] args) {
        boolean isPlayer = sender instanceof Player;


        Set<String> categories = ORewardsMain.getCore().getRewardManager().getCategories();

        if (args.length == 0) {

            if (isPlayer) {

                Player player = (Player) sender;

                CategoriesContainer container = new CategoriesContainer(player);
                container.show(player);

                return true;

            } else {

                sender.sendMessage(ONLY_PLAYER_MESSAGE);

            }

        } else if(args.length == 1) {

            if (isPlayer) {
                Player player = (Player) sender;

                String categoryName = args[0];

                if (categories.stream().anyMatch(categoryName::equals)) {

                    CategoryConfig categoryConfig = CategoryConfig.loadFromName(categoryName);
                    RewardCategoryContainer container = new RewardCategoryContainer(categoryConfig, player);

                    container.show(player);

                } else {

                    sender.sendMessage(String.format(CATEGORY_UNKNOWN_MESSAGE, categoryName));

                }




                /*if(!CategoryMenu.show(categoryName, player)) {
                    sender.sendMessage(String.format(CATEGORY_UNKNOWN_MESSAGE, categoryName));
                }*/


            } else {
                sender.sendMessage(ONLY_PLAYER_MESSAGE);
            }


        } else if(args.length == 2) {
            Player player = Bukkit.getPlayer(args[1]);

            if(player != null) {
                String categoryName = args[0];

                if (categories.stream().anyMatch(categoryName::equals)) {

                    CategoryConfig categoryConfig = CategoryConfig.loadFromName(categoryName);
                    RewardCategoryContainer container = new RewardCategoryContainer(categoryConfig, player);

                    container.show(player);

                } else {

                    sender.sendMessage(String.format(CATEGORY_UNKNOWN_MESSAGE, categoryName));

                }
            } else {
                sender.sendMessage(String.format(PLAYER_OFFLINE_MESSAGE, args[1]));
            }
        }

        return false;

    }


    @CoreCommand(name = "menu", permission = "orewards.cmd.menu")
    public boolean onMenuCommand(CommandSender sender, String[] args) {

        if(sender instanceof Player) {
            Player player = (Player) sender;


            CategoriesContainer container = new CategoriesContainer(player);
            container.show(player);



            return true;
        }

        sender.sendMessage(ONLY_PLAYER_MESSAGE);

        return false;
    }



}
