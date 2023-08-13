package me.rootdeibis.orewards.commands;

import me.rootdeibis.orewards.api.commands.annotations.CoreCommand;
import me.rootdeibis.orewards.api.rewards.menus.Categories;
import me.rootdeibis.orewards.api.rewards.menus.CategoryMenu;
import me.rootdeibis.orewards.utils.AdvetureUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ORewardsSubCMD {


    private final String ONLY_PLAYER_MESSAGE = AdvetureUtils.translate("&eORewards &7> &cOnly players can perform this action.");
    private final String CATEGORY_UNKNOWN_MESSAGE = AdvetureUtils.translate("&eORewards &7> &cThere is no category called '%s', check the name.");

    private final String PLAYER_OFFLINE_MESSAGE = AdvetureUtils.translate("&eORewards &7> &cPlayer '%s' is not online");
    @CoreCommand(name = "open", permission = "orewards.cmd.open")
    public boolean onOpenCommand(CommandSender sender, String[] args) {
        boolean isPlayer = sender instanceof Player;

        if (args.length == 0) {

            if (isPlayer) {

                Player player = (Player) sender;

                Categories categoriesMenu = new Categories(player.getUniqueId());

                player.openInventory(categoriesMenu.getInventory());

                return true;

            } else {

                sender.sendMessage(ONLY_PLAYER_MESSAGE);

            }

        } else if(args.length == 1) {

            if (isPlayer) {
                Player player = (Player) sender;

                String categoryName = args[0];
                Categories.CategoryConfig categoryConfig = Categories.CategoryConfig.loadFromName(categoryName);

                if(categoryConfig != null) {
                    CategoryMenu categoryMenu = new CategoryMenu(categoryConfig, player.getUniqueId());


                    player.openInventory(categoryMenu.getInventory());

                    return true;
                } else {
                    sender.sendMessage(String.format(CATEGORY_UNKNOWN_MESSAGE, categoryName));
                }


            } else {
                sender.sendMessage(ONLY_PLAYER_MESSAGE);
            }


        } else if(args.length == 2) {
            Player player = Bukkit.getPlayer(args[1]);

            if(player != null) {
                String categoryName = args[0];
                Categories.CategoryConfig categoryConfig = Categories.CategoryConfig.loadFromName(categoryName);

                if(categoryConfig != null) {
                    CategoryMenu categoryMenu = new CategoryMenu(categoryConfig, player.getUniqueId());


                    player.openInventory(categoryMenu.getInventory());

                    return true;
                } else {
                    sender.sendMessage(String.format(CATEGORY_UNKNOWN_MESSAGE, categoryName));
                }
            } else {
                sender.sendMessage(String.format(PLAYER_OFFLINE_MESSAGE, args[1]));
            }
        }

        return false;

    }

}
