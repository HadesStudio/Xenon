package net.ascend.core.bukkit.profile.permission.bukkit;

import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.permissions.ServerOperator;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created By LeandroSSJ
 * Created on 07/07/2023
 * Protect Name Xenon
 */
public class BukkitPermissibleBase extends PermissibleBase {

    public BukkitPermissibleBase(ServerOperator opable) {
        super(opable);
    }

    @Override
    public boolean hasPermission(String permission) {
        if (super.isOp() && !testPermission('-' + permission)) return true;
        if (super.isOp() || super.hasPermission("'*'") || super.hasPermission("*")) return true;
        if (super.hasPermission('-' + permission)) return false;

        if (permission.contains(".")) {
            char[] chars = permission.toCharArray();
            int i = 0;
            while (chars[i] != '.') {
                i++;
            }

            String substring = permission.substring(0, i);
            if (super.hasPermission(substring + ".*")) return true;
        }

        return super.hasPermission(permission);
    }

    protected boolean testPermission(String permission) {
        if (permission == null) throw new IllegalArgumentException("Permission context cannot be null");

        Map<String, PermissionAttachmentInfo> permissions = Maps.newHashMap();

        try {
            Field field = PermissibleBase.class.getDeclaredField("permissions");
            field.setAccessible(true);

            permissions = (Map<String, PermissionAttachmentInfo>) field.get(this);
            field.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        String context = permission.toLowerCase().trim();
        if (isPermissionSet(context)) {
            return permissions.get(context).getValue();
        } else {
            Permission bukkitPermission = Bukkit.getServer().getPluginManager().getPermission(context);
            if (bukkitPermission != null) {
                return bukkitPermission.getDefault().getValue(false);
            } else {
                return Permission.DEFAULT_PERMISSION.getValue(false);
            }
        }
    }
}
