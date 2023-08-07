package me.rootdeibis.orewards.utils;

import com.cryptomorin.xseries.SkullUtils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.HashMap;


public class HeadTool {

    private static final HashMap<String, SkullMeta> skullMetaCache = new HashMap<>();

    public static void applyTextures(ItemStack skull, String identifier) {

        if(!skullMetaCache.containsKey(identifier)) {
            skullMetaCache.put(identifier, SkullUtils.applySkin(skull.getItemMeta(), identifier));
        }


        skull.setItemMeta(skullMetaCache.get(identifier));
    }

}
