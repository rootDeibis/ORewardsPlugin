package me.rootdeibis.orewards.api.rewards.data;

import me.rootdeibis.orewards.ORewardsMain;
import me.rootdeibis.orewards.api.configuration.RDirectory;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class PlayerDataManager {


    private static HashMap<UUID, PlayerData> players_data = new HashMap<>();


    public static PlayerData load(UUID uuid) {
        if (!players_data.containsKey(uuid)) players_data.put(uuid, new PlayerData(uuid));


        return players_data.get(uuid);
    }

    public static void clean(String limit) {

        RDirectory dir = ORewardsMain.getCore().getFileManager().dir("player_data");

        dir.loadFilesIndirectory();


        dir.getRFiles().forEach((f, d) -> {

            if (d.contains("data.expireOn")) {

                Date expireOn = new Date(d.getLong("data.expireOn"));
                Date current = new Date();

                if (current.after(expireOn)) {
                    d.getFile().delete();
                }

            }

        });

    }



}
