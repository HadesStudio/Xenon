package net.ascend.core.bukkit.rank.grant.menu.grant.duration;

import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.rank.grant.Grant;
import net.ascend.core.bukkit.rank.grant.promts.GrantReasonPrompt;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.button.Button;
import lombok.AllArgsConstructor;
import net.ascend.core.bukkit.server.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@AllArgsConstructor
public class PermanentDurationButton extends Button {

    private final Profile profile;
    private final Grant grant;
    private final List<Server> servers;

    @Override
    public ItemStack getItemStack(Player player) {
        return ItemMaker.of("WATCH")
                .setDisplayName("&6Permanent Grant")
                .setLore(
                        "&7By selecting this, this permission",
                        "&7will last forever!",
                        "",
                        "&7Left-Click to select this option.").build();
    }

    @Override
    public void onClick(Player player, int slot, ClickType clickType) {
        player.closeInventory();

        grant.setAddedBy(player.getUniqueId());
        grant.setDuration(Integer.MAX_VALUE);

        for (Server server : servers) {
            grant.getServers().add(server.getName());
        }

        new GrantReasonPrompt(profile, grant).startPrompt(player);
    }
}
