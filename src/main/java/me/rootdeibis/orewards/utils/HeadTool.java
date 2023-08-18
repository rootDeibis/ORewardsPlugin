package me.rootdeibis.orewards.utils;


import com.google.gson.JsonParser;
import me.rootdeibis.commonlib.utils.SkullUtils;
import me.rootdeibis.commonlib.utils.XMaterial;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.HashMap;


public class HeadTool {

    private static final HashMap<String, String> skullMetaCache = new HashMap<>();

    public static void loadTextures(String identifier) {
        resolveTextures(XMaterial.PLAYER_HEAD.parseItem().getItemMeta(), identifier);
    }

    public static HashMap<String, String> getSkullMetaCache() {
        return skullMetaCache;
    }

    public static SkullMeta resolveTextures(ItemMeta meta, String identifier) {
        if (!skullMetaCache.containsKey(identifier)) {

            String value = identifier;

            switch (SkullUtils.detectSkullValueType(identifier)) {
                case NAME:
                    value = loadFromUsername(identifier);
                    break;
                case UUID:
                    value = resolveTexturesFromId(identifier);
                    break;
            }

            skullMetaCache.put(identifier, value);
        }


        return SkullUtils.applySkin(meta, skullMetaCache.get(identifier));
    }

    private static String loadFromUsername(String user) {
        String ProfileRequest = RequestUtils.get("https://api.mojang.com/users/profiles/minecraft/" + user);

        String ProfileID = new JsonParser().parse(ProfileRequest).getAsJsonObject().get("id").getAsString();


        return resolveTexturesFromId(ProfileID);
    }

    private static String resolveTexturesFromId(String ProfileID) {
        String TextureRequest = RequestUtils.get("https://sessionserver.mojang.com/session/minecraft/profile/" + ProfileID);
        String textures = new JsonParser().parse(TextureRequest).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject().get("value").getAsString();

        return textures;
    }

}
