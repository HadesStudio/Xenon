package net.ascend.core.bukkit.rank.gson;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.rank.rank.perk.Perk;
import net.ascend.core.bukkit.rank.rank.permission.PermissionData;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created By LeandroSSJ
 * Created on 04/07/2023
 * Protect Name Xenon
 */
public class GSONUtil {

    public static final Type LIST_STRING_TYPE = new TypeToken<List<String>>() {}.getType();
    public static final Type PERMISSION_DATA = new TypeToken<List<PermissionData>>() {}.getType();
    public static final Type RANK_PERKS_DATA = new TypeToken<List<Perk>>() {}.getType();

    public static boolean isJson(String source, Type type) {
        try {
            XenonBukkit.PLAIN_GSON.fromJson(source, type);
            return true;
        } catch (JsonSyntaxException ex) {
            return false;
        }
    }
}
