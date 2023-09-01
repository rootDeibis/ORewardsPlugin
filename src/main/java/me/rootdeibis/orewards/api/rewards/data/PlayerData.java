package me.rootdeibis.orewards.api.rewards.data;

import me.rootdeibis.orewards.ORewardsMain;
import me.rootdeibis.orewards.api.configuration.RFile;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerData {

    private final UUID uuid;

    private HashMap<String, String> player_data = new HashMap<>();

    private RFile config;


    public PlayerData(UUID player) {
        this.uuid = player;
        this.config = ORewardsMain.getCore().getFileManager().use("player_data/" + this.uuid.toString() + ".yml");


        if (!this.config.getFile().exists()) {
            this.config.create();


        } else {
            this.config.load();

        }


        if (this.config.contains("data")) {


            this.config.getConfigurationSection("data").getKeys(false)
                    .forEach(key -> player_data.put(key, String.valueOf(this.config.get("data." + key))));
        }

        if (!this.player_data.containsKey("createdAt")) {
            player_data.put("createdAt", String.valueOf(new Date().getTime()));
        }

    }

    public void set(String key, Object value) {
        player_data.put(key, String.valueOf(value));
    }

    public String get(String key) {

        return player_data.get(key);
    }

    public boolean contains(String key) {
        return player_data.containsKey(key);
    }

    public void save() {
        for (Map.Entry<String, String> entry : this.player_data.entrySet()) {

            this.config.set("data." + entry.getKey(), entry.getValue());

            this.config.save();

        }


    }


}
