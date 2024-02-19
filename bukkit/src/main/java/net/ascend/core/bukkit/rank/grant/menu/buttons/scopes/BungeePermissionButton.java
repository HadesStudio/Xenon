package net.ascend.core.bukkit.rank.grant.menu.buttons.scopes;

import com.google.common.collect.Lists;
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.rank.grant.Grant;
import net.ascend.core.bukkit.rank.grant.menu.GrantScopesMenu;
import net.ascend.core.bukkit.rank.grant.menu.grant.GrantMenu;
import net.ascend.core.bukkit.rank.grant.menu.grant.GrantServersMenu;
import net.ascend.core.bukkit.rank.grant.types.PermissionGrant;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.button.Button;
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
        return ItemMaker.of("QUARTZ").setDisplayName(XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.select-server.buttons.bungee-permission.display-name")).setLore(XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("menus.select-server.buttons.bungee-permission.lore")).build();
    }

    @Override
    public void onClick(Player player, int slot, ClickType clickType) {
        permissionGrant.setBungeecord(true);

        new GrantServersMenu(new GrantScopesMenu(new GrantMenu(profile), profile, grant), profile, permissionGrant, Lists.newArrayList()).openMenu(player);
        Button.Sound.AFFIRMATIVE.toPlayer(player);
    }
}
