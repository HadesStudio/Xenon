package dev.hely.core.bukkit.ranks.newgrants.menu.buttons.scopes;

import com.google.common.collect.Lists;
import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.ranks.newgrants.Grant;
import dev.hely.core.bukkit.ranks.newgrants.menu.GrantScopesMenu;
import dev.hely.core.bukkit.ranks.newgrants.menu.grant.GrantMenu;
import dev.hely.core.bukkit.ranks.newgrants.menu.grant.GrantServersMenu;
import dev.hely.core.bukkit.ranks.newgrants.types.PermissionGrant;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.button.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class BungeePermissionButton extends Button {
    Profile profile;
    Grant grant;
    PermissionGrant permissionGrant;

    @Override
    public ItemStack getItemStack(Player player) {
        return ItemMaker.of(Material.QUARTZ).setDisplayName(OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.select-server.buttons.bungee-permission.display-name")).setLore(OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("menus.select-server.buttons.bungee-permission.lore")).build();
    }

    @Override
    public void onClick(Player player, int slot, ClickType clickType) {
        permissionGrant.setBungeecord(true);

        new GrantServersMenu(new GrantScopesMenu(new GrantMenu(profile), profile, grant), profile, permissionGrant, Lists.newArrayList()).openMenu(player);
        Button.Sound.AFFIRMATIVE.toPlayer(player);
    }
}
