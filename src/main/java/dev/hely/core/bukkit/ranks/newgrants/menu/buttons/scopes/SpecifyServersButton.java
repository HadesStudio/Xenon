package dev.hely.core.bukkit.ranks.newgrants.menu.buttons.scopes;

import com.google.common.collect.Lists;
import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.ranks.newgrants.Grant;
import dev.hely.core.bukkit.ranks.newgrants.menu.GrantScopesMenu;
import dev.hely.core.bukkit.ranks.newgrants.menu.GrantViewSelectMenu;
import dev.hely.core.bukkit.ranks.newgrants.menu.grant.GrantMenu;
import dev.hely.core.bukkit.ranks.newgrants.menu.grant.GrantServersMenu;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.button.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class SpecifyServersButton extends Button {
    Profile profile;
    Grant grant;

    @Override
    public ItemStack getItemStack(Player player) {
        return ItemMaker.of(Material.SIGN).setDisplayName(OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.select-server.buttons.specific-servers.display-name")).setLore(OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("menus.select-server.buttons.specific-servers.lore")).build();
    }

    @Override
    public void onClick(Player player, int slot, ClickType clickType) {
        new GrantServersMenu(new GrantScopesMenu(new GrantMenu(profile), profile, grant), profile, grant, Lists.newArrayList()).openMenu(player);
        Button.Sound.AFFIRMATIVE.toPlayer(player);
    }
}
