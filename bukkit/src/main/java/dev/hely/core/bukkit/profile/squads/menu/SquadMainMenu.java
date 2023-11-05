package dev.hely.core.bukkit.profile.squads.menu;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.profile.squads.Squad;
import dev.hely.core.bukkit.profile.squads.commands.prompts.NewSquadPrompt;
import dev.hely.core.bukkit.profile.squads.commands.prompts.SearchSquadPrompt;
import dev.hely.core.bukkit.utils.material.MaterialUtil;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.Menu;
import dev.hely.lib.menu.button.Button;
import dev.hely.lib.menu.fill.FillerTemplate;
import dev.hely.lib.util.CC;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
            return OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.main-menu.title").replace("%online%", String.valueOf(1)).replace("%offline%", String.valueOf(1));
        } else {
            Squad squad = OxygenBukkit.getInstance().getSquadManager().getSquad(profile.getSquad());
            return OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.main-menu.title").replace("%online%", String.valueOf(squad.getOnline())).replace("%offline%", String.valueOf(squad.getMembersAmount()));
        }
    }

    @Override
    public String getTitle(Player player) {
        if (profile.getSquad() == null) {
            return OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.main-menu.title").replace("%online%", String.valueOf(1)).replace("%offline%", String.valueOf(1));
        } else {
            Squad squad = OxygenBukkit.getInstance().getSquadManager().getSquad(profile.getSquad());
            return OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.main-menu.title").replace("%online%", String.valueOf(squad.getOnline())).replace("%offline%", String.valueOf(squad.getMembersAmount()));
        }
    }

    @Override
    public int getSize() {
        return OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getInt("menus.main-menu.size") * 9;
    }

    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getInt("menus.main-menu.buttons.create-squad.slot"), new NewSquadButton(profile));
        buttons.put(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getInt("menus.main-menu.buttons.squad-invites.slot"), new SquadInvitesButton(profile));
        buttons.put(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getInt("menus.main-menu.buttons.find-squad.slot"), new FindSquadButton(profile));

        return buttons;
    }

    @AllArgsConstructor
    public static class NewSquadButton extends Button {
        final Profile profile;

        public Material getMaterial() {
            if (OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.main-menu.buttons.create-squad.material") != null) {
                if (Material.getMaterial(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.main-menu.buttons.create-squad.material")) != null) {
                    return Material.valueOf(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.main-menu.buttons.create-squad.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'new-squad-button' is null, using a default value."));
                    return Material.NAME_TAG;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'profile-details-button' is broken, using a default value."));
                return Material.NAME_TAG;
            }
        }

        public short getData() {
            return (short) OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getInt("menus.main-menu.buttons.create-squad.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.main-menu.buttons.create-squad.display-name"));
            for (String line : OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getStringList("menus.main-menu.buttons.create-squad.lore")) {
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

        public Material getMaterial() {
            if (OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.main-menu.buttons.squad-invites.material") != null) {
                if (Material.getMaterial(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.main-menu.buttons.squad-invites.material")) != null) {
                    return Material.valueOf(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.main-menu.buttons.squad-invites.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'squad-invites-button' is null, using a default value."));
                    return MaterialUtil.SIGN.getMaterial();
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'squad-invites-button' is broken, using a default value."));
                return MaterialUtil.SIGN.getMaterial();
            }
        }

        public short getData() {
            return (short) OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getInt("menus.main-menu.buttons.squad-invites.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.main-menu.buttons.squad-invites.display-name"));
            for (String line : OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getStringList("menus.main-menu.buttons.squad-invites.lore")) {
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

        public Material getMaterial() {
            if (OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.main-menu.buttons.find-squad.material") != null) {
                if (Material.getMaterial(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.main-menu.buttons.find-squad.material")) != null) {
                    return Material.valueOf(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.main-menu.buttons.find-squad.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'find-squad-button' is null, using a default value."));
                    return Material.BOOK;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'find-squad-button' is broken, using a default value."));
                return Material.BOOK;
            }
        }

        public short getData() {
            return (short) OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getInt("menus.main-menu.buttons.find-squad.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.main-menu.buttons.find-squad.display-name"));
            for (String line : OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getStringList("menus.main-menu.buttons.find-squad.lore")) {
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
