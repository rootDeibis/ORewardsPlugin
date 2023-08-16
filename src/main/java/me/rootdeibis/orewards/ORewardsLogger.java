package me.rootdeibis.orewards;

import me.rootdeibis.orewards.utils.AdvetureUtils;
import org.bukkit.Bukkit;

import java.util.Arrays;

public class ORewardsLogger {

        private static final String prefix = "&eORewards &7> ";

        public static void send(String... messages) {
            Arrays.stream(messages).forEach(str -> Bukkit.getConsoleSender().sendMessage(AdvetureUtils.translate(prefix + str)));
        }

        public static void print(String... messages) {
            Arrays.stream(messages).forEach(str -> Bukkit.getConsoleSender().sendMessage(AdvetureUtils.translate(str)));

        }

}
