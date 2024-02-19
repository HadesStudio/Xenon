package net.ascend.core.bukkit.rank.grant.menu.buttons.scopes;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.rank.grant.Grant;
import net.ascend.core.bukkit.rank.grant.promts.GrantReasonPrompt;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.button.Button;
import lombok.AllArgsConstructor;
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
        return ItemMaker.of("WATCH").setDisplayName(XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.select-duration.buttons.permanent.display-name")).setLore(XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("menus.select-duration.buttons.permanent.lore")).build();
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
