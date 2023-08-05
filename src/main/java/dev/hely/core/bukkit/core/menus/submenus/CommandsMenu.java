package dev.hely.core.bukkit.core.menus.submenus;

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

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.utils.command.BaseCommand;
import dev.hely.lib.CC;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.button.Button;
import dev.hely.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
    private List<BaseCommand> baseCommands;

    @Override
    public String getName() {
        return "Command Settings";
    }

    @Override
    public String getTitle(Player player) {
        return "Command Settings";
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        this.baseCommands.clear();
        this.baseCommands.addAll(OxygenBukkit.getInstance().getCommandManager().getDisabledCommands());
        this.baseCommands.addAll(OxygenBukkit.getInstance().getCommandManager().getLoadedCommands());

        this.baseCommands.forEach(baseCommand -> buttons.put(buttons.size(), new CommandButton(baseCommand)));

        return buttons;
    }


    @AllArgsConstructor
    private static class CommandButton extends Button {

        private final OxygenBukkit plugin = OxygenBukkit.getInstance();
        private BaseCommand baseCommand;

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker itemMaker = ItemMaker.of(Material.STAINED_GLASS_PANE);
            itemMaker.setDisplayName((plugin.getCommandManager().isDisabled(baseCommand) ? "&c" : "&a") + CC.capitalize(baseCommand.getName()) + " Command");
            itemMaker.setData((short) (plugin.getCommandManager().isDisabled(baseCommand) ? 14 : 5));
            itemMaker.addLore(" ");
            itemMaker.addLore("&6Currently&7: " + (plugin.getCommandManager().isDisabled(baseCommand) ? "&cDisabled" : "&aActive"));
            itemMaker.addLore(" ");
            itemMaker.addLore("&6Name&7: &f" + baseCommand.getName());
            itemMaker.addLore("&6Permission&7: &f" + baseCommand.getPermission());

            List<String> aliases = new ArrayList<>(baseCommand.getAliases());

            if (!aliases.isEmpty()) {
                itemMaker.addLore("&6Aliases&7: ");
                aliases.forEach(alias -> {
                    itemMaker.addLore(" &7* &f" + alias);
                });
            }
            itemMaker.addLore(" ");
            itemMaker.addLore("&6Click to Toggle!");

            return itemMaker.build();
        }


        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (!plugin.getCommandManager().isDisabled(baseCommand)) {
                List<String> disabledCommandList = plugin.getConfiguration().getConfiguration().getStringList("command-system.disabled-commands");
                if (disabledCommandList.stream().noneMatch(source -> source.equalsIgnoreCase(baseCommand.getName()) && Arrays.stream(baseCommand.getAliases().toArray(new String[0])).noneMatch(alias -> alias.equalsIgnoreCase(source)))) {
                    disabledCommandList.add(baseCommand.getName());
                }

                plugin.getConfiguration().getConfiguration().set("command-system.disabled-commands", disabledCommandList);
                plugin.getCommandManager().unregisterCommand(baseCommand);

            } else {
                List<String> disabledCommandList = plugin.getConfiguration().getConfiguration().getStringList("command-system.disabled-commands");
                disabledCommandList.removeIf(source -> source.equalsIgnoreCase(baseCommand.getName())
                        || Arrays.stream(baseCommand.getAliases().toArray(new String[0])).anyMatch(alias -> alias.equalsIgnoreCase(source)));

                plugin.getConfiguration().getConfiguration().set("command-system.disabled-commands", disabledCommandList);
                plugin.getCommandManager().registerCommand(baseCommand);
            }

            try {
                plugin.getConfiguration().save();
            } catch (IOException | InvalidConfigurationException e) {
                ConsoleCommandSender bukkitCommandSender = Bukkit.getConsoleSender();

                bukkitCommandSender.sendMessage(CC.translate("&c-------------------------------------"));
                bukkitCommandSender.sendMessage(CC.translate("&6Oxygen Core - Save Configuration Error"));
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
