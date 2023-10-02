package dev.hely.core.bukkit.ranks.newgrants.menu.buttons.servers;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.ranks.newgrants.Grant;
import dev.hely.core.bukkit.ranks.newgrants.menu.GrantDurationMenu;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.Menu;
import dev.hely.lib.menu.button.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@AllArgsConstructor
public class GrantClearServersButton extends Button {
    Menu backMenu;
    Profile profile;
    Grant grant;
    List<String> servers;

    @Override
    public ItemStack getItemStack(Player player) {
        ItemMaker itemMaker = ItemMaker.of(Material.FLINT_AND_STEEL);
        itemMaker.setDisplayName(OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.grant-servers.buttons.clear-servers.display-name"));
        for (String lore : OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("menus.grant-servers.buttons.clear-servers.lore")) {
            itemMaker.addLore(lore.replace("%servers_size%", String.valueOf(servers.size())));
        }
        return itemMaker.build();
    }

    @Override
    public void onClick(Player player, int slot, ClickType clickType) {
        servers.clear();
        Sound.NEGATIVE.toPlayer(player);
    }

    @Override
    public boolean toUpdate(Player player, int slot, ClickType clickType) {
        return true;
    }
}

