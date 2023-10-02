package dev.hely.core.bukkit.ranks.newgrants.menu.buttons.scopes;

import com.google.common.collect.Lists;
import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.ranks.newgrants.Grant;
import dev.hely.core.bukkit.ranks.newgrants.menu.GrantScopesMenu;
import dev.hely.core.bukkit.ranks.newgrants.menu.grant.GrantMenu;
import dev.hely.core.bukkit.ranks.newgrants.menu.grant.GrantServersMenu;
import dev.hely.core.bukkit.ranks.newgrants.promts.GrantReasonPrompt;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.button.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@AllArgsConstructor
public class PermanentDurationButton extends Button {
    Profile profile;
    Grant grant;
    List<String> servers;

    @Override
    public ItemStack getItemStack(Player player) {
        return ItemMaker.of(Material.WATCH).setDisplayName(OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.select-duration.buttons.permanent.display-name")).setLore(OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("menus.select-duration.buttons.permanent.lore")).build();
    }

    @Override
    public void onClick(Player player, int slot, ClickType clickType) {
        player.closeInventory();

        grant.setAddedBy(player.getUniqueId());
        grant.getServers().addAll(servers);
        grant.setDuration(Integer.MAX_VALUE);

        new GrantReasonPrompt(profile, grant).startPrompt(player);
    }
}
