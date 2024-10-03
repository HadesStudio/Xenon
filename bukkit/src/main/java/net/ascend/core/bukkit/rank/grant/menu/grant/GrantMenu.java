package net.ascend.core.bukkit.rank.grant.menu.grant;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.rank.grant.menu.grant.rank.RankGrantMenu;
import net.ascend.core.bukkit.rank.grant.menu.grant.tag.TagGrantMenu;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.Menu;
import net.ascend.core.bukkit.lib.menu.button.Button;
import lombok.AllArgsConstructor;
import net.ascend.core.bukkit.rank.grant.menu.grants.GrantViewSelectMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class GrantMenu extends Menu {

    private Profile profile;

    @Override
    public String getName() {
        return "Select Grant Type"; // FINISHED PROFILE NAME
    }

    @Override
    public String getTitle(Player player) {
        return "Select Grant Type"; // FINISHED PROFILE NAME
    }

    @Override
    public int getSize() {
        return 3 * 9;
    }

    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(11, new RankGrantsButton(profile));
        buttons.put(15, new TagGrantsMenu(profile));

        return buttons;
    }

    @AllArgsConstructor
    public static class RankGrantsButton extends Button {

        private final Profile profile;

        public String getMaterial() {
            return "LEATHER_CHESTPLATE";
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());

            builder.setDisplayName("&6Rank Grants");
            builder.setLore(
                    "&7This menu will direct you to where",
                    "&7you can see, or add a rank grant to",
                    "&7a players profile.",
                    "",
                    "&7Left-Click to view this menu.");

            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType == ClickType.LEFT) {
                new RankGrantMenu(profile).openMenu(player);
            }
        }
    }

    @AllArgsConstructor
    public static class TagGrantsMenu extends Button {

        private final Profile profile;

        public String getMaterial() {
            return "NAME_TAG";
        }

        public short getData() {
            return (short) 0;
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName("&6Tag Grants");
            builder.setLore(
                    "&7This menu will direct you to where",
                    "&7you can see, or add a tag grant to",
                    "&7a players profile.",
                    "",
                    "&7Left-Click to view this menu.");
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType == ClickType.LEFT) {
                new TagGrantMenu(profile).openMenu(player);
            }
        }
    }
}
