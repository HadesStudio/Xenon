package net.ascend.core.bukkit.rank.grant.menu.buttons.servers;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.rank.grant.Grant;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.Menu;
import net.ascend.core.bukkit.lib.menu.button.Button;
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
        ItemMaker itemMaker = ItemMaker.of("FLINT_AND_STEEL");
        itemMaker.setDisplayName(XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.grant-servers.buttons.clear-servers.display-name"));
        for (String lore : XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("menus.grant-servers.buttons.clear-servers.lore")) {
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

