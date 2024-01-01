package net.ascend.core.bukkit.filter.menu.editor;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.filter.menu.editor
//   Date: Thursday, September 14, 2023 - 1:44 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.filter.Filter;
import net.ascend.core.bukkit.filter.menu.editor.mutetype.MatchSelectionMenu;
import net.ascend.core.bukkit.filter.menu.editor.mutetype.MuteTypeSelectionMenu;
import net.ascend.core.bukkit.util.TimeUtil;
import net.ascend.core.bukkit.util.material.MaterialUtil;
import net.ascend.lib.menu.fill.FillerTemplate;
import net.ascend.lib.CC;
import net.ascend.lib.maker.ItemMaker;
import net.ascend.lib.menu.Menu;
import net.ascend.lib.menu.button.Button;
import lombok.AllArgsConstructor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class FilterEditorMenu extends Menu {
    {
        setClickUpdate(true);
    }
    @Override
    public FillerTemplate getFillerTemplate() {
        return FillerTemplate.FILL;
    }

    Filter filter;

    @Override
    public String getName() {
        return filter.getName() + " - Message Filter";
    }

    @Override
    public String getTitle(Player player) {
        return filter.getName() + " - Message Filter";
    }

    @Override
    public int getSize() {
        return 27;
    }

    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(10, new Rename_Filter(filter));
        buttons.put(11, new Phrase_Edit(filter));
        buttons.put(12, new Mute_Mode_Edit(filter));
        buttons.put(13, new Match_Edit(filter));
        buttons.put(14, new Duration_Edit(filter));
        buttons.put(15, new Case_Sensitive(filter));
        buttons.put(16, new Enabled(filter));

        return buttons;
    }

    @AllArgsConstructor
    public static class Rename_Filter extends Button {
        Filter filter;

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(Material.ANVIL);
            builder.setData((short) 0);
            builder.setDisplayName("&6Rename Filter");
            builder.setLore(Arrays.asList(
                    "&7Click here to edit this filter's",
                    "&7name.",
                    "&7",
                    "&6Current Name&7: &a" + filter.getName()
            ));
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            player.closeInventory();
            player.sendMessage(CC.translate("&6[Filter] &7Please type the new name of this filter in chat:"));
            ConversationFactory factory = new ConversationFactory(XenonBukkit.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {
                public String getPromptText(ConversationContext context) {
                    return "";
                }

                @Override
                public Prompt acceptInput(ConversationContext cc, String s) {
                    filter.setName(s);
                    filter.save();

                    player.sendMessage(CC.translate("&6[Filter] &7You have successfully changed the name of this filter to '&6" + s + "&7'."));
                    new FilterEditorMenu(filter).openMenu(player);
                    return Prompt.END_OF_CONVERSATION;
                }
            }).withLocalEcho(false).withEscapeSequence("/null").withTimeout(20).thatExcludesNonPlayersWithMessage("Go away evil console!");

            Conversation con = factory.buildConversation(player);
            player.beginConversation(con);
        }
    }

    @AllArgsConstructor
    public static class Phrase_Edit extends Button {
        Filter filter;

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(MaterialUtil.SIGN.getMaterial());
            builder.setData((short) 0);
            builder.setDisplayName("&6Change Filtered Text");
            builder.setLore(Arrays.asList(
                    "&7Click here to edit this filter's",
                    "&7text it filter's.",
                    "&7",
                    "&6Current Filtered Text&7: &a" + filter.getPhrase()
            ));
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            player.closeInventory();
            player.sendMessage(CC.translate("&6[Filter] &7Please type the new phrase of this filter in chat:"));
            ConversationFactory factory = new ConversationFactory(XenonBukkit.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {
                public String getPromptText(ConversationContext context) {
                    return "";
                }

                @Override
                public Prompt acceptInput(ConversationContext cc, String s) {
                    filter.setPhrase(s);
                    filter.save();

                    player.sendMessage(CC.translate("&6[Filter] &7You have successfully changed the filter phrase of this filter to '&6" + s + "&7'."));
                    new FilterEditorMenu(filter).openMenu(player);
                    return Prompt.END_OF_CONVERSATION;
                }
            }).withLocalEcho(false).withEscapeSequence("/null").withTimeout(20).thatExcludesNonPlayersWithMessage("Go away evil console!");

            Conversation con = factory.buildConversation(player);
            player.beginConversation(con);
        }
    }

    @AllArgsConstructor
    public static class Match_Edit extends Button {
        Filter filter;

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(Material.FISHING_ROD);
            builder.setData((short) 0);
            builder.setDisplayName("&6Match Type Editor");
            builder.setLore(Arrays.asList(
                    "&7Click here to edit this filter's",
                    "&7match type.",
                    "&7",
                    "&6Current Match Type&7: &a" + filter.getMatch().getDisplay()
            ));
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            new MatchSelectionMenu(filter).openMenu(player);
        }
    }

    @AllArgsConstructor
    public static class Mute_Mode_Edit extends Button {
        Filter filter;

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(Material.PAPER);
            builder.setData((short) 0);
            builder.setDisplayName("&6Change Mute Mode");
            builder.setLore(Arrays.asList(
                    "&7Click here to edit this filter's",
                    "&7mute mode.",
                    "&7",
                    "&6Current Mute Mode&7: &a" + filter.getMuteType().getDisplay()
            ));
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            new MuteTypeSelectionMenu(filter).openMenu(player);
        }
    }

    @AllArgsConstructor
    public static class Duration_Edit extends Button {
        Filter filter;

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(MaterialUtil.EMPTY_MAP.getMaterial());
            builder.setData((short) 0);
            builder.setDisplayName("&6Change Duration");
            builder.setLore(Arrays.asList(
                    "&7Click here to edit this filter's",
                    "&7duration.",
                    "&7",
                    "&6Current Mute Duration&7: &a" + TimeUtil.formatTimeMillis(filter.getDuration())
            ));
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            player.closeInventory();
            player.sendMessage(CC.translate("&6[Filter] &7Please type the new phrase of this filter in chat:"));
            ConversationFactory factory = new ConversationFactory(XenonBukkit.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {
                public String getPromptText(ConversationContext context) {
                    return "";
                }

                @Override
                public Prompt acceptInput(ConversationContext cc, String s) {
                    long duration = TimeUtil.parseTime(s);
                    if (duration == -1) {
                        player.sendMessage(CC.translate("&6[Filter] &7You have specified a invalid duration for this filter, please try again.. (Example: 1h15m)"));
                        new FilterEditorMenu(filter).openMenu(player);
                        return Prompt.END_OF_CONVERSATION;
                    }

                    filter.setDuration(duration);
                    filter.save();

                    player.sendMessage(CC.translate("&6[Filter] &7You have successfully changed the filter duration of this filter to '&6" + s + "&7'."));
                    new FilterEditorMenu(filter).openMenu(player);
                    return Prompt.END_OF_CONVERSATION;
                }
            }).withLocalEcho(false).withEscapeSequence("/null").withTimeout(20).thatExcludesNonPlayersWithMessage("Go away evil console!");

            Conversation con = factory.buildConversation(player);
            player.beginConversation(con);
        }
    }

    @AllArgsConstructor
    public static class Case_Sensitive extends Button {
        Filter filter;

        public int getData() {
            if (filter.isCaseSensitive()) {
                return DyeColor.LIME.getDyeData();
            } else {
                return DyeColor.GRAY.getDyeData();
            }
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(MaterialUtil.INK_SACK.getMaterial());
            builder.setData((short) getData());
            builder.setDisplayName("&6Change Case Sensitive");
            builder.setLore(Arrays.asList(
                    "&7Click here to toggle this filter's",
                    "&7case sensitive status.",
                    "&7",
                    "&6Case Sensitive&7: &a" + isCaseSensitive()
            ));
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (filter.isCaseSensitive()) {
                filter.setCaseSensitive(false);
                filter.save();
                player.updateInventory();
                player.sendMessage(CC.translate("&6[Filter] &7You have now &cdisabled &7this filter's case sensitive status!"));
            } else {
                filter.setCaseSensitive(true);
                filter.save();
                player.updateInventory();
                player.sendMessage(CC.translate("&6[Filter] &7You have now &aenabled &7this filter's case sensitive status!"));
            }
        }

        public String isCaseSensitive() {
            if (filter.isCaseSensitive()) {
                return "&aYes";
            } else {
                return "&cNo";
            }
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isRightClick() || clickType.isLeftClick();
        }
    }

    @AllArgsConstructor
    public static class Enabled extends Button {
        Filter filter;

        public int getData() {
            if (filter.isEnabled()) {
                return DyeColor.LIME.getDyeData();
            } else {
                return DyeColor.GRAY.getDyeData();
            }
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(MaterialUtil.INK_SACK.getMaterial());
            builder.setData((short) getData());
            builder.setDisplayName("&6Filter Enabled");
            builder.setLore(Arrays.asList(
                    "&7Click here to toggle this filter's",
                    "&7enabled status.",
                    "&7",
                    "&6Status&7: &a" + isEnabled()
            ));
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (filter.isEnabled()) {
                filter.setEnabled(false);
                filter.save();
                player.updateInventory();
                player.sendMessage(CC.translate("&6[Filter] &7You have now &cdisabled &7this filter!"));
            } else {
                filter.setEnabled(true);
                filter.save();
                player.updateInventory();
                player.sendMessage(CC.translate("&6[Filter] &7You have now &aenabled &7this filter!"));
            }
        }

        public String isEnabled() {
            if (filter.isEnabled()) {
                return "&aYes";
            } else {
                return "&cNo";
            }
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isRightClick() || clickType.isLeftClick();
        }
    }
}
