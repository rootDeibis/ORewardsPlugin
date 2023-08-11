package me.rootdeibis.orewards.cmds;


import me.rootdeibis.orewards.api.commands.annotations.CoreCommand;
import me.rootdeibis.orewards.api.commands.annotations.CoreCommandLoader;
import me.rootdeibis.orewards.api.commands.annotations.CoreSubCommands;
import me.rootdeibis.orewards.api.menus.Categories;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CoreCommandLoader
public class ORewardsCMD {

    @CoreCommand(name = "orewards", aliases = "orw")
    @CoreSubCommands(list = {})
    public boolean onCommand(CommandSender commandSender) {

        ((Player)commandSender).openInventory(new Categories().getInventory());

        return false;
    }
}
