package me.rootdeibis.orewards.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;

public class HeadTool {


    public static void applyTextures(ItemStack skull, String url) {
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();

        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);

        gameProfile.getProperties().put("textures", new Property("textures", url));


        Field profileField;

        try {

            profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skullMeta, gameProfile);

        }catch (Exception e) {
            e.printStackTrace();
        }

        skull.setItemMeta(skullMeta);
    }

}
