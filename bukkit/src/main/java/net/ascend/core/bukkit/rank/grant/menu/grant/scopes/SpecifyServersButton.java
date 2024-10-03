package net.ascend.core.bukkit.rank.grant.menu.grant.scopes;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.rank.grant.Grant;
import net.ascend.core.bukkit.rank.grant.menu.grant.GrantDurationMenu;
import net.ascend.core.bukkit.rank.grant.menu.grant.GrantScopesMenu;
import net.ascend.core.bukkit.rank.grant.menu.grant.GrantMenu;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.button.Button;
import net.ascend.core.bukkit.server.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SpecifyServersButton extends Button {

    private final Profile profile;
    private final Grant grant;
    private final List<Server> serverList;

    private Server selectedServer;

    public SpecifyServersButton(Profile profile, Grant grant, List<Server> serverList) {
        this.profile = profile;
        this.grant = grant;
        this.serverList = serverList;
    }


    @Override
    public ItemStack getItemStack(Player player) {
        List<String> lore = new ArrayList<>();
        lore.add("&7By selecting this, you will");
        lore.add("&7will have to specify which");
        lore.add("&7servers you want this grant on!");
        lore.add(" ");

        for (Server server : XenonBukkit.getInstance().getServerManager().getServers()) {
            lore.add((this.serverList.equals(selectedServer) ? "&9" + server.getName() : (this.serverList.equals(server) ? "&a" : "&f") + server.getName()));
        }

        lore.add(" ");
        lore.add("&7Right click to move to up");
        lore.add("&7Left click to move to down");
        lore.add("&7Middle click to add/remove server.");
        lore.add("&7Shift-Left-Click to go to duration menu");
        lore.add(" ");

        return ItemMaker.of("SIGN")
                .setDisplayName("&6Select Servers")
                .setLore(lore)
                .build();
    }



    @Override
    public void onClick(Player player, int slot, ClickType clickType) {
        if (clickType == ClickType.RIGHT) {
            int next = this.serverList.indexOf(selectedServer) + 1;
            if (next >= this.serverList.size()) {
                next = 0;
            }

            selectedServer = this.serverList.get(next);
        } else if (clickType == ClickType.LEFT) {
            int next = this.serverList.indexOf(selectedServer) - 1;
            if (next <= 0) {
                next = 0;
            }
            selectedServer = this.serverList.get(next);
        } else if (clickType == ClickType.MIDDLE) {
            if (this.serverList.contains(selectedServer)) {
                this.serverList.remove(selectedServer);
            }
            else {
                this.serverList.add(selectedServer);
            }
        } else if (clickType == ClickType.SHIFT_LEFT) {
            new GrantDurationMenu(new GrantScopesMenu(new GrantMenu(profile), profile, grant), profile, grant, serverList).openMenu(player);
            Button.Sound.AFFIRMATIVE.toPlayer(player);
        }
    }

    @Override
    public boolean toUpdate(Player player, int slot, ClickType clickType) {
        return clickType == ClickType.MIDDLE || clickType == ClickType.LEFT || clickType == ClickType.RIGHT;
    }
}
