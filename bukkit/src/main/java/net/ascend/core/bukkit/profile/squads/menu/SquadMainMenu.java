package net.ascend.core.bukkit.profile.squads.menu;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.squads.Squad;
import net.ascend.core.bukkit.profile.squads.commands.prompts.NewSquadPrompt;
import net.ascend.core.bukkit.profile.squads.commands.prompts.SearchSquadPrompt;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.Menu;
import net.ascend.core.bukkit.lib.menu.button.Button;
import net.ascend.core.bukkit.lib.menu.fill.FillerTemplate;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class SquadMainMenu extends Menu {
    {
        setClickUpdate(true);
    }

    @Override
    public FillerTemplate getFillerTemplate() {
        return FillerTemplate.FILL;
    }

    Profile profile;

    @Override
    public String getName() {
        if (profile.getSquad() == null) {
            return XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.main-menu.title").replace("%online%", String.valueOf(1)).replace("%offline%", String.valueOf(1));
        } else {
            Squad squad = XenonBukkit.getInstance().getSquadManager().getSquad(profile.getSquad());
            return XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.main-menu.title").replace("%online%", String.valueOf(squad.getOnline())).replace("%offline%", String.valueOf(squad.getMembersAmount()));
        }
    }

    @Override
    public String getTitle(Player player) {
        if (profile.getSquad() == null) {
            return XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.main-menu.title").replace("%online%", String.valueOf(1)).replace("%offline%", String.valueOf(1));
        } else {
            Squad squad = XenonBukkit.getInstance().getSquadManager().getSquad(profile.getSquad());
            return XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.main-menu.title").replace("%online%", String.valueOf(squad.getOnline())).replace("%offline%", String.valueOf(squad.getMembersAmount()));
        }
    }

    @Override
    public int getSize() {
        return XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getInt("menus.main-menu.size") * 9;
    }

    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getInt("menus.main-menu.buttons.create-squad.slot"), new NewSquadButton(profile));
        buttons.put(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getInt("menus.main-menu.buttons.squad-invites.slot"), new SquadInvitesButton(profile));
        buttons.put(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getInt("menus.main-menu.buttons.find-squad.slot"), new FindSquadButton(profile));

        return buttons;
    }

    @AllArgsConstructor
    public static class NewSquadButton extends Button {
        final Profile profile;

        public String getMaterial() {
            return XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.main-menu.buttons.create-squad.material");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getInt("menus.main-menu.buttons.create-squad.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.main-menu.buttons.create-squad.display-name"));
            for (String line : XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getStringList("menus.main-menu.buttons.create-squad.lore")) {
                builder.addLore(line);
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            player.closeInventory();
            new NewSquadPrompt(profile).startPrompt(player);
        }
    }

    @AllArgsConstructor
    public static class SquadInvitesButton extends Button {
        final Profile profile;

        public String getMaterial() {
            return XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.main-menu.buttons.squad-invites.material");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getInt("menus.main-menu.buttons.squad-invites.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.main-menu.buttons.squad-invites.display-name"));
            for (String line : XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getStringList("menus.main-menu.buttons.squad-invites.lore")) {
                builder.addLore(line);
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
        }
    }

    @AllArgsConstructor
    public static class FindSquadButton extends Button {
        final Profile profile;

        public String getMaterial() {
            return XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.main-menu.buttons.find-squad.material");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getInt("menus.main-menu.buttons.find-squad.data");
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.main-menu.buttons.find-squad.display-name"));
            for (String line : XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getStringList("menus.main-menu.buttons.find-squad.lore")) {
                builder.addLore(line);
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            player.closeInventory();
            new SearchSquadPrompt(profile).startPrompt(player);
        }
    }
}
