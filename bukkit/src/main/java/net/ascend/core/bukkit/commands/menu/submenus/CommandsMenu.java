package net.ascend.core.bukkit.commands.menu.submenus;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.core.menus.submenus
//   Date: Saturday, August 05, 2023 - 3:41 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.commands.menu.OxygenMenu;
import net.ascend.core.bukkit.util.command.BaseCommand;
import net.ascend.core.bukkit.lib.CC;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.button.Button;
import net.ascend.core.bukkit.lib.menu.button.implement.InformationButton;
import net.ascend.core.bukkit.lib.menu.button.implement.PageButton;
import net.ascend.core.bukkit.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.*;

public class CommandsMenu extends PaginatedMenu {
    {
        setClickUpdate(true);
    }
    private List<BaseCommand> baseCommands = new ArrayList<>();

    @Override
    public String getName() {
        return "Command Settings";
    }

    @Override
    public String getTitle(Player player) {
        return "Command Settings";
    }

    public Map<Integer, Button> getGlobalContent(Player player) {
        Map<Integer, Button> menuContent = new HashMap<>();

        menuContent.put(0, new Back_Button());
        menuContent.put(1, Button.getPlaceholder());
        menuContent.put(2, Button.getPlaceholder());

        menuContent.put(3, Button.getPlaceholder());
        menuContent.put(4, new InfoButton());
        menuContent.put(5, Button.getPlaceholder());

        menuContent.put(6, Button.getPlaceholder());
        menuContent.put(7, Button.getPlaceholder());
        menuContent.put(8, Button.getPlaceholder());

        menuContent.put(36, Button.getPlaceholder());
        menuContent.put(37, new PageButton(-1, this));
        menuContent.put(38, Button.getPlaceholder());

        menuContent.put(39, Button.getPlaceholder());
        menuContent.put(40, new InformationButton(this));
        menuContent.put(41, Button.getPlaceholder());

        menuContent.put(42, Button.getPlaceholder());
        menuContent.put(43, new PageButton(1, this));
        menuContent.put(44, Button.getPlaceholder());

        return menuContent;
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        this.baseCommands.clear();
        this.baseCommands.addAll(XenonBukkit.getInstance().getCommandManager().getDisabledCommands());
        this.baseCommands.addAll(XenonBukkit.getInstance().getCommandManager().getLoadedCommands());

        this.baseCommands.forEach(baseCommand -> buttons.put(buttons.size(), new CommandButton(baseCommand)));

        return buttons;
    }

    @AllArgsConstructor
    public static class Back_Button extends Button {
        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of("BED");
            builder.setData((short) 0);
            builder.setDisplayName("&cBack");
            builder.setLore(Collections.singletonList(
                    "&7Click here to return to the main menu."
            ));
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            new OxygenMenu(player).openMenu(player);
        }
    }

    @AllArgsConstructor
    private static class InfoButton extends Button {
        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker itemMaker = ItemMaker.of("BOOK");
            itemMaker.setDisplayName("&6&lInformation");
            itemMaker.setData((short) 0);
            itemMaker.addFakeGlow();

            itemMaker.addLore("");
            itemMaker.addLore("&fThank you for using &6Xenon&7!");
            itemMaker.addLore("");
            itemMaker.addLore("&fIf you encounter any bugs, or have any");
            itemMaker.addLore("&fsuggestions please feel free to let us");
            itemMaker.addLore("&fknow in our &dDiscord Server&7!");
            itemMaker.addLore("&7");
            itemMaker.addLore("&fDiscord: &6ascendproyect.org/discord");
            return itemMaker.build();
        }


        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            player.sendMessage(CC.translate("&fDiscord Server: &6ascendproyect.org/discord"));
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }

    @AllArgsConstructor
    private static class CommandButton extends Button {

        private final XenonBukkit plugin = XenonBukkit.getInstance();
        private BaseCommand baseCommand;

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker itemMaker = ItemMaker.of("STAINED_GLASS_PANE");
            itemMaker.setDisplayName((plugin.getCommandManager().isDisabled(baseCommand) ? "&c" : "&6") + CC.capitalize(baseCommand.getName()) + " Command");
            itemMaker.setData((short) (plugin.getCommandManager().isDisabled(baseCommand) ? 14 : 5));
            itemMaker.addLore(" ");
            itemMaker.addLore("&6Currently&7: " + (plugin.getCommandManager().isDisabled(baseCommand) ? "&cDisabled" : "&aEnabled"));
            itemMaker.addLore(" ");
            itemMaker.addLore("&6Name&7: &f" + baseCommand.getName());
            if (baseCommand.getPermission() == null) {
                itemMaker.addLore("&6Permission&7: &fNone");
            } else {
                itemMaker.addLore("&6Permission&7: &f" + baseCommand.getPermission());
            }
            if (!plugin.getCommandManager().isDisabled(baseCommand)) {
                itemMaker.addFakeGlow();
            }
            List<String> aliases = new ArrayList<>(baseCommand.getAliases());

            if (!aliases.isEmpty()) {
                itemMaker.addLore("&6Command Aliases&7: ");
                aliases.forEach(alias -> {
                    itemMaker.addLore(" &7* &f/" + alias);
                });
            }
            itemMaker.addLore(" ");
            itemMaker.addLore((plugin.getCommandManager().isDisabled(baseCommand) ? "&6Left-Click &bto enable this command." : "&6Left-Click &bto disable this command."));

            return itemMaker.build();
        }


        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (!plugin.getCommandManager().isDisabled(baseCommand)) {
                if (baseCommand.getName().equalsIgnoreCase("Xenon")) {
                    player.sendMessage(CC.translate("&cSorry, you cannot disable the main &lXenon Command&c!"));
                    return;
                }
                List<String> disabledCommandList = plugin.getConfiguration().getConfiguration().getStringList("command-system.disabled-commands");
                if (disabledCommandList.stream().noneMatch(source -> source.equalsIgnoreCase(baseCommand.getName()) && Arrays.stream(baseCommand.getAliases().toArray(new String[0])).noneMatch(alias -> alias.equalsIgnoreCase(source)))) {
                    disabledCommandList.add(baseCommand.getName());
                }

                plugin.getConfiguration().getConfiguration().set("command-system.disabled-commands", disabledCommandList);
                plugin.getCommandManager().unregisterCommand(baseCommand);
                player.sendMessage(CC.translate("&cYou have successfully disabled the &l/" + baseCommand.getName() + " &ccommand."));
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4" + player.getName() + " &chas just disabled the &4/" + baseCommand.getName() + " command&c."));
            } else {
                List<String> disabledCommandList = plugin.getConfiguration().getConfiguration().getStringList("command-system.disabled-commands");
                disabledCommandList.removeIf(source -> source.equalsIgnoreCase(baseCommand.getName())
                        || Arrays.stream(baseCommand.getAliases().toArray(new String[0])).anyMatch(alias -> alias.equalsIgnoreCase(source)));

                plugin.getConfiguration().getConfiguration().set("command-system.disabled-commands", disabledCommandList);
                plugin.getCommandManager().registerCommand(baseCommand);
                player.sendMessage(CC.translate("&aYou have successfully enabled the &l/" + baseCommand.getName() + " &acommand."));
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4" + player.getName() + " &ahas just enabled the &4/" + baseCommand.getName() + " command&a."));
            }

            try {
                plugin.getConfiguration().save();
            } catch (IOException | InvalidConfigurationException e) {
                ConsoleCommandSender bukkitCommandSender = Bukkit.getConsoleSender();

                bukkitCommandSender.sendMessage(CC.translate("&c-------------------------------------"));
                bukkitCommandSender.sendMessage(CC.translate("&cXenon - Save Configuration Error"));
                bukkitCommandSender.sendMessage(CC.translate("&c"));
                bukkitCommandSender.sendMessage(CC.translate("&cThis is more than likely caused by a broken configuration file!"));
                bukkitCommandSender.sendMessage(CC.translate("&cError:"));
                e.printStackTrace();
                bukkitCommandSender.sendMessage(CC.translate("&c-------------------------------------"));
            }
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }
}
