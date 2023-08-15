package me.rootdeibis.orewards.utils;

import me.rootdeibis.orewards.ORewardsLogger;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class VersionChecker {

    private final String currentVersion;

    private final String resourceId;
    private String spigotVersion;

    public VersionChecker(Plugin plugin, String spigotResourId) {

        this.resourceId = spigotResourId;
        this.currentVersion = plugin.getDescription().getVersion();
        this.spigotVersion = "__";

        this.request();

    }

    public boolean isLatestVersion() {
        return this.spigotVersion.equals(this.currentVersion);

    }

    public String getLatestVersion() {
        return this.spigotVersion;
    }

    private void request() {

        String SPIGOT_URL = "https://api.spigotmc.org/legacy/update.php?resource=";
        try {



            HttpURLConnection con = (HttpURLConnection) new URL(SPIGOT_URL + this.resourceId).openConnection();

            int connection_timeout = 1500;

            con.setConnectTimeout(connection_timeout);
            con.setReadTimeout(connection_timeout);


            this.spigotVersion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();


        } catch (Exception e) {
            ORewardsLogger.send("&cCould not verify if it is the latest version, check your internet connection.");
        }

    }
}
