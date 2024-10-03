package net.ascend.core.bukkit.tag.menu;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import me.clip.placeholderapi.PlaceholderAPI;
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.lib.CC;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.button.Button;
import net.ascend.core.bukkit.lib.menu.button.implement.PageButton;
import net.ascend.core.bukkit.lib.menu.button.implement.PageFilterButton;
import net.ascend.core.bukkit.lib.menu.implement.ConfirmationMenu;
import net.ascend.core.bukkit.lib.menu.implement.FilterablePaginatedMenu;
import net.ascend.core.bukkit.lib.menu.implement.PageFilter;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.permission.ProfilePermission;
import net.ascend.core.bukkit.tag.Tag;
import net.ascend.core.bukkit.tag.TagType;
import net.ascend.core.bukkit.tag.prompt.TagBuyPrompt;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagMenu extends FilterablePaginatedMenu<Tag> {

    {
        setAutoUpdate(true);
    }

    @Override
    public String getName() {
        return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("tag-menu.title");
    }

    @Override
    public String getTitle(Player player) {
        return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("tag-menu.title");
    }

    public Map<Integer, Button> getGlobalContent(Player player) {
        Map<Integer, Button> menuContent = new HashMap<>();

        menuContent.put(0, Button.getPlaceholder());
        menuContent.put(1, Button.getPlaceholder());
        menuContent.put(2, Button.getPlaceholder());
        menuContent.put(3, Button.getPlaceholder());
        menuContent.put(4, new ResetTagButton());
        menuContent.put(5, Button.getPlaceholder());
        menuContent.put(6, Button.getPlaceholder());
        menuContent.put(7, Button.getPlaceholder());
        menuContent.put(8, Button.getPlaceholder());

        menuContent.put(36, new PageButton(-1, this));
        menuContent.put(37, Button.getPlaceholder());
        menuContent.put(38, Button.getPlaceholder());

        menuContent.put(39, Button.getPlaceholder());
        menuContent.put(40, new PageFilterButton<>(this));
        menuContent.put(41, Button.getPlaceholder());

        menuContent.put(42, Button.getPlaceholder());
        menuContent.put(43, Button.getPlaceholder());
        menuContent.put(44, new PageButton(1, this));

        return menuContent;
    }

    @Override
    public List<PageFilter<Tag>> generateFilters() {
        List<PageFilter<Tag>> filters = Lists.newArrayList();

        filters.add(new PageFilter<>("Filter: All Tags", (tag -> tag.getType() != null), true));
        for(TagType type : TagType.values()) {
            filters.add(new PageFilter<>("Filter: Tag " + CC.capitalize(type.name()), (tag -> tag.getType() == type), false));
        }

        return filters;
    }

    @Override
    public Map<Integer, Button> getFilteredButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        List<Tag> tags = XenonBukkit.getInstance().getTagManager().getTags();
        tags.sort(Comparator.comparingInt(Tag::getWeight));

        loop:
        for (Tag tag : tags) {
            for (PageFilter<Tag> filter : getFilters()) {
                if (filter.test(tag)) {
                    buttons.put(buttons.size(), new TagButton(tag));
                    continue loop;
                }
            }
        }

        return buttons;
    }

    public static class ResetTagButton extends Button {

        @Override
        public ItemStack getItemStack(Player player) {
            ConfigurationSection config = XenonBukkit.getInstance().getMenuConfig().getConfiguration().getConfigurationSection("tag-menu.buttons.reset_tag");

            ItemMaker builder = ItemMaker.of(config.getString("material"));
            builder.setData((short) config.getInt("data"));
            builder.setDisplayName(config.getString("display_name"));
            builder.setLore(config.getStringList("lore"));

            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

            if (profile.getTag() != null) {
                profile.setTag(null);
                profile.save();
            }

            player.sendMessage(CC.translate(Locale.TAG_MENU_RESET_TAG));
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }

    @AllArgsConstructor
    public static class TagButton extends Button {

        private final Tag tag;

        @Override
        public ItemStack getItemStack(Player player) {
            Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());
            ConfigurationSection config = XenonBukkit.getInstance().getMenuConfig().getConfiguration().getConfigurationSection("tag-menu.buttons.tag");

            ItemMaker builder = ItemMaker.of(config.getString("material"));
            builder.setData((short) config.getInt("data"));

            if (profile.canSelectTag(tag)) {
                builder.setDisplayName(config.getString("select.display_name").replace("%tag_name%", tag.getName()));
                builder.setLore(config.getStringList("select.lore").stream()
                        .map(line -> line.replace("%chat_format%", formatChatDisplay(player, tag))
                                .replace("%tag_coins%", String.valueOf(tag.getCoins()))
                                .replace("%tag_name%", tag.getName()))
                        .toArray(String[]::new));
            } else {
                if (profile.getCurrency() < tag.getCoins()) {
                    builder.setDisplayName(config.getString("cant_afford.display_name").replace("%tag_name%", tag.getName()));
                    builder.setLore(config.getStringList("cant_afford.lore").stream()
                            .map(line -> line.replace("%chat_format%", formatChatDisplay(player, tag))
                                    .replace("%tag_coins%", String.valueOf(tag.getCoins()))
                                    .replace("%tag_name%", tag.getName())
                                    .replace("%coins%", String.valueOf(tag.getCoins() - profile.getCurrency())))
                            .toArray(String[]::new));
                } else {
                    builder.setDisplayName(config.getString("click_buy.display_name").replace("%tag_name%", tag.getName()));
                    builder.setLore(config.getStringList("click_buy.lore").stream()
                            .map(line -> line.replace("%chat_format%", formatChatDisplay(player, tag))
                                    .replace("%tag_coins%", String.valueOf(tag.getCoins()))
                                    .replace("%tag_name%", tag.getName()))
                            .toArray(String[]::new));
                }
            }

            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());
            if (!profile.canSelectTag(tag)) {
                if (profile.getCurrency() < tag.getCoins()) {
                    int coins = tag.getCoins() - profile.getCurrency();

                    for (String string : Locale.TAG_MENU_DONT_HAVE_ENOUGH) {
                        string =  string.replace("%tag_name%", tag.getName());
                        string =  string.replace("%coins%", String.valueOf(coins));

                        player.sendMessage(CC.translate(string));
                    }
                    return;
                }

                for (String string : Locale.TAG_MENU_START_PROMPT) {
                    player.sendMessage(CC.translate(string));
                }

                new TagBuyPrompt(tag, (name) -> {
                    if (name.equals("me")) {

                        new ConfirmationMenu("Confirm Purchase?", (b) -> {
                            if (b) {
                                player.sendMessage(CC.translate(Locale.TAG_MENU_PURCHASED).replace("%tag_name%", tag.getName()));

                                profile.setCurrency(profile.getCurrency() - tag.getCoins());

                                ProfilePermission permission = new ProfilePermission(tag.getPermission(), "Global", true, -1, System.currentTimeMillis());
                                profile.getProfilePermissions().add(permission);
                                profile.save();
                            }
                        }).openMenu(player);

                    } else {
                        Player target = Bukkit.getPlayer(name);
                        if (target == null) {
                            player.sendMessage(CC.translate(Locale.COMMANDS_OFFLINE_PLAYER).replace("%player%", name));
                            return;
                        }

                        new ConfirmationMenu("Confirm Purchase?", (b) -> {
                            if (b) {
                                player.sendMessage(CC.translate(Locale.TAG_MENU_PURCHASED).replace("%tag_name%", tag.getName()));

                                profile.setCurrency(profile.getCurrency() - tag.getCoins());

                                ProfilePermission permission = new ProfilePermission(tag.getPermission(), "Global", true, -1, System.currentTimeMillis());
                                profile.getProfilePermissions().add(permission);
                                profile.save();
                            }
                        }).openMenu(player);

                    }
                }).startPrompt(player);
                return;
            }

            if (profile.getTag() != null && profile.getTag().equalsIgnoreCase(tag.getName())) {
                player.sendMessage(CC.translate(Locale.TAG_MENU_ALREADY_HAVE_APPLIED).replace("%tag_name%", tag.getName()));
                return;
            }

            profile.setTag(tag.getName());
            profile.save();

            player.sendMessage(CC.translate(Locale.TAG_MENU_APPLIED).replace("%tag_name%", tag.getName()));
        }
    }

    public static String formatChatDisplay(Player player, Tag tag) {
        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

        String chatFormat = CC.translate(XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("chat-system.chat-format")
                .replace("%tags%", tag != null ? tag.getPrefix() : "")
                .replace("%rank_prefix%", profile.getActiveRank().getPrefix())
                .replace("%rank_suffix%", profile.getActiveRank().getSuffix())
                .replace("%player%", player.getName())
        );

        return Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null ? PlaceholderAPI.setPlaceholders(player, chatFormat) + "Example" : chatFormat + "Example";
    }
}
