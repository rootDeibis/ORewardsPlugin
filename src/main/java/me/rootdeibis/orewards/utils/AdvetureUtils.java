package me.rootdeibis.orewards.utils;

import me.rootdeibis.orewards.ORewardsMain;

import net.kyori.adventure.text.minimessage.MiniMessage;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AdvetureUtils {

    public static void sender(CommandSender sender, String message) {
        ORewardsMain.getAdventure().sender(sender).sendMessage(MiniMessage.miniMessage().deserialize(message));

    }

    public static String translate(String str) {
        return ChatColor.translateAlternateColorCodes('&', LegacyComponentSerializer.legacyAmpersand().serialize(MiniMessage.miniMessage().deserialize(str)));
    }

    public static String[] translate(String... str) {
        return Arrays.stream(str).map(AdvetureUtils::translate).toArray(String[]::new);
    }

    public static List<String> translate(List<String> list) {
        return list.stream().map(AdvetureUtils::translate).collect(Collectors.toList());
    }
}
