package net.ascend.core.bukkit.filter.menu;

import com.mongodb.client.model.Filters;
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.filter.Filter;
import net.ascend.core.bukkit.filter.menu.editor.FilterEditorMenu;
import net.ascend.core.bukkit.util.TimeUtil;
import net.ascend.core.bukkit.lib.CC;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.button.Button;
import net.ascend.core.bukkit.lib.menu.button.implement.PageButton;
import net.ascend.core.bukkit.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class FiltersMenu extends PaginatedMenu {

    @Override
    public String getName() {
        return "Filter Editor";
    }

    @Override
    public String getTitle(Player player) {
        return "Filter Editor";
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        List<Filter> filters = new ArrayList<>(XenonBukkit.getInstance().getFilterManager().getFilters());
        filters.sort(Comparator.comparing(Filter::getName));
        filters.forEach(filter -> buttons.put(buttons.size(), new Filter_Button(filter)));
        return buttons;

    }

    @Override
    public Map<Integer, Button> getGlobalContent(Player player) {
        Map<Integer, Button> menuContent = new HashMap<>();

        menuContent.put(0, Button.getPlaceholder());
        menuContent.put(1, Button.getPlaceholder());
        menuContent.put(2, Button.getPlaceholder());
        menuContent.put(3, Button.getPlaceholder());
        menuContent.put(4, Button.getPlaceholder());
        menuContent.put(5, Button.getPlaceholder());
        menuContent.put(6, Button.getPlaceholder());
        menuContent.put(7, Button.getPlaceholder());
        menuContent.put(8, Button.getPlaceholder());

        menuContent.put(18, new PageButton(-1, this));
        menuContent.put(19, Button.getPlaceholder());
        menuContent.put(20, Button.getPlaceholder());
        menuContent.put(21, Button.getPlaceholder());
        menuContent.put(22, new New_Filter());
        menuContent.put(23, Button.getPlaceholder());
        menuContent.put(24, Button.getPlaceholder());
        menuContent.put(25, Button.getPlaceholder());
        menuContent.put(26, new PageButton(1, this));


        return menuContent;
    }

    @AllArgsConstructor
    public static class Filter_Button extends Button {
        final Filter filter;

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of("PAPER");
            builder.setData((short) 0);
            builder.setDisplayName("&6" + filter.getName() + " - Messsage Filter");
            builder.setLore(Arrays.asList(
                    "&7All information regarding this",
                    "&7chat filter will be listed below.",
                    "&7",
                    "&6Enabled&7: " + isEnabled(),
                    "&6Phrase&7: &f" + filter.getPhrase(),
                    "&6Match&7: &f" + filter.getMatch().getDisplay(),
                    "&6Mute&7: &f" + filter.getMuteType().getDisplay(),
                    "&6Duration&7: &f" + TimeUtil.getDetailedTime(filter.getDuration()),
                    "&6Case Sensitive&7: " + isCaseSensitive(),
                    "&7",
                    "&7Left-Click to edit this filter.",
                    "&7Right-Click to delete this filter."

            ));
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType.isLeftClick()) {
                new FilterEditorMenu(filter).openMenu(player);
            } else {
                player.sendMessage(CC.translate("&aYou have successfully deleted the &l" + filter.getName() + " Filter&a."));

                XenonBukkit.getInstance().getFilterManager().removeFilter(filter);
            }
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isRightClick();
        }

        public String isEnabled() {
            if (filter.isEnabled()) {
                return "&aEnabled";
            } else {
                return "&cDisabled";
            }
        }

        public String isCaseSensitive() {
            if (filter.isCaseSensitive()) {
                return "&aYes";
            } else {
                return "&cNo";
            }
        }
    }

    @AllArgsConstructor
    public static class New_Filter extends Button {
        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of("EMERALD");
            builder.setData((short) 0);
            builder.setDisplayName("&6New Message Filter");
            builder.setLore(Arrays.asList(
                    "&7Click here to create a new message",
                    "&7filter."

            ));
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType.isLeftClick()) {
                player.closeInventory();
                player.sendMessage(CC.translate("&aPlease type in chat the message you wish to block:"));

                ConversationFactory factory = new ConversationFactory(XenonBukkit.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {
                    public String getPromptText(ConversationContext context) {
                        return "";
                    }

                    @Override
                    public Prompt acceptInput(ConversationContext cc, String s) {
                        Filter filter = new Filter(s, s);
                        filter.save();

                        XenonBukkit.getInstance().getFilterManager().registerAllFilters();

                        player.sendMessage(CC.translate("&aYou have successfully created a new filter that blocks '&l" + s + "&a'."));
                        new FiltersMenu().openMenu(player);
                        return Prompt.END_OF_CONVERSATION;
                    }
                }).withLocalEcho(false).withEscapeSequence("/null").withTimeout(20).thatExcludesNonPlayersWithMessage("Go away evil console!");

                Conversation con = factory.buildConversation(player);
                player.beginConversation(con);
            }
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isRightClick();
        }
    }
}
