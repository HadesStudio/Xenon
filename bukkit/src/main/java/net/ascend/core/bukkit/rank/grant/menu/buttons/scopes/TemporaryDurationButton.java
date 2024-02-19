package net.ascend.core.bukkit.rank.grant.menu.buttons.scopes;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.rank.grant.Grant;
import net.ascend.core.bukkit.rank.grant.promts.GrantDurationPrompt;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.button.Button;
import lombok.AllArgsConstructor;
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
        return ItemMaker.of("WOOL")
                .setDisplayName(XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.select-duration.buttons.temporary.display-name")).setLore(XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("menus.select-duration.buttons.temporary.lore")).build();
    }

    @Override
    public void onClick(Player player, int slot, ClickType clickType) {
        player.closeInventory();

        grant.setAddedBy(player.getUniqueId());
        grant.getServers().addAll(servers);

        new GrantDurationPrompt(profile, grant).startPrompt(player);
    }
}
