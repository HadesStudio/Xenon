package net.ascend.core.bukkit.rank.grant.menu.grant.duration;

import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.rank.grant.Grant;
import net.ascend.core.bukkit.rank.grant.promts.GrantDurationPrompt;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.button.Button;
import lombok.AllArgsConstructor;
import net.ascend.core.bukkit.server.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@AllArgsConstructor
public class TemporaryDurationButton extends Button {

    private final Profile profile;
    private final Grant grant;
    private final List<Server> servers;

    @Override
    public ItemStack getItemStack(Player player) {
        return ItemMaker.of("WOOL")
                .setDisplayName("&6Temporary Grant")
                .setLore(
                        "&7By selecting this, you will have",
                        "&7to setup which servers you want",
                        "&7your permission active on!",
                        "",
                        "&7Left-Click to select this option.").build();
    }

    @Override
    public void onClick(Player player, int slot, ClickType clickType) {
        player.closeInventory();

        grant.setAddedBy(player.getUniqueId());

        for (Server server : servers) {
            grant.getServers().add(server.getName());
        }

        new GrantDurationPrompt(profile, grant).startPrompt(player);
    }
}
