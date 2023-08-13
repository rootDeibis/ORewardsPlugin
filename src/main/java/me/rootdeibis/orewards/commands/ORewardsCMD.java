package me.rootdeibis.orewards.commands;


import me.rootdeibis.orewards.api.rewards.menus.Categories;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ORewardsCMD implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        ((Player)commandSender).openInventory(new Categories(((Player)commandSender).getUniqueId()).getInventory());

        return false;
    }
}
