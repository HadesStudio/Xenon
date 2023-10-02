package dev.hely.core.bukkit.ranks.newgrants.menu.buttons.scopes;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.ranks.newgrants.Grant;
import dev.hely.core.bukkit.ranks.newgrants.promts.GrantDurationPrompt;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.button.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@AllArgsConstructor
public class TemporaryDurationButton extends Button {
    Profile profile;
    Grant grant;
    List<String> servers;

    @Override
    public ItemStack getItemStack(Player player) {
        return ItemMaker.of(Material.BOOK_AND_QUILL).setDisplayName(OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.select-duration.buttons.temporary.display-name")).setLore(OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("menus.select-duration.buttons.temporary.lore")).build();
    }

    @Override
    public void onClick(Player player, int slot, ClickType clickType) {
        player.closeInventory();

        grant.setAddedBy(player.getUniqueId());
        grant.getServers().addAll(servers);

        new GrantDurationPrompt(profile, grant).startPrompt(player);
    }
}
