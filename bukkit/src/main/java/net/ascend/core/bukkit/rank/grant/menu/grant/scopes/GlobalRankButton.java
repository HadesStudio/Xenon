package net.ascend.core.bukkit.rank.grant.menu.grant.scopes;

import com.google.common.collect.Lists;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.rank.grant.Grant;
import net.ascend.core.bukkit.rank.grant.menu.grant.GrantDurationMenu;
import net.ascend.core.bukkit.rank.grant.menu.grant.GrantScopesMenu;
import net.ascend.core.bukkit.rank.grant.menu.grant.GrantMenu;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.button.Button;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class GlobalRankButton extends Button {

    private final Profile profile;
    private final Grant grant;

    @Override
    public ItemStack getItemStack(Player player) {
        return ItemMaker.of("EMERALD")
                .setDisplayName("&6Global Grant")
                .setLore(
                        "&7By selecting this, players across",
                        "&7the entire network will be able",
                        "&7to use this grant on any server!",
                        "",
                        "&7Left-Click to select this option.").build();
    }

    @Override
    public void onClick(Player player, int slot, ClickType clickType) {
        new GrantDurationMenu(new GrantScopesMenu(new GrantMenu(profile), profile, grant), profile, grant, Lists.newArrayList()).openMenu(player);

        Button.Sound.AFFIRMATIVE.toPlayer(player);
    }
}
