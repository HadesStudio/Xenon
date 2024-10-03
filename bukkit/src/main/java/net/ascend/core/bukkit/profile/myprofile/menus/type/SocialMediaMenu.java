package net.ascend.core.bukkit.profile.myprofile.menus.type;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.myprofile.SocialMediaType;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.Menu;
import net.ascend.core.bukkit.lib.menu.button.Button;
import lombok.AllArgsConstructor;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class SocialMediaMenu extends Menu {
    private Profile profile;

    @Override
    public String getName() {
        return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("social-media-menu.title")
                .replace("%profile_name%", profile.getName())
                .replace("%profile_color%", profile.getColor());
    }

    @Override
    public String getTitle(Player player) {
        return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("social-media-menu.title")
                .replace("%profile_name%", profile.getName())
                .replace("%profile_color%", profile.getColor());
    }

    @Override
    public int getSize() {
        return 9;
    }

    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        Map<Integer, Button> toReturn = new HashMap<>();

        toReturn.put(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("social-media-menu.buttons.discord.slot"), new Discord_Button(profile, SocialMediaType.DISCORD));
        toReturn.put(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("social-media-menu.buttons.twitter.slot"), new Twitter_Button(profile, SocialMediaType.TWITTER));
        toReturn.put(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("social-media-menu.buttons.youtube.slot"), new YouTube_Button(profile, SocialMediaType.YOUTUBE));
        toReturn.put(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("social-media-menu.buttons.twitch.slot"), new Twitch_Button(profile, SocialMediaType.TWITCH));
        toReturn.put(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("social-media-menu.buttons.instagram.slot"), new Twitch_Button(profile, SocialMediaType.INSTAGRAM));

        return toReturn;
    }

    @AllArgsConstructor
    public static class Discord_Button extends Button {
        private final Profile player_profile;
        private final SocialMediaType social_type;

        public String getMaterial() {
            return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("social-media-menu.buttons.discord.material");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("social-media-menu.buttons.discord.data");
        }

        public String getSkullOwner() {
            if (XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("social-media-menu.buttons.discord.material").equalsIgnoreCase("SKULL_ITEM")) {
                return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("social-media-menu.buttons.discord.skull-owner");
            } else {
                return "";
            }
        }


        @Override
        public ItemStack getItemStack(Player player) {
            String link = player_profile.getSocialMedia().getOrDefault(SocialMediaType.DISCORD, "Not Setup.");

            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("social-media-menu.buttons.discord.display_name"));
            for (String line : XenonBukkit.getInstance().getMenuConfig().getConfiguration().getStringList("social-media-menu.buttons.discord.lore")) {
                builder.addLore(line
                        .replace("%discord%", link));
            }
            builder.setOwner(getSkullOwner());
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType.name().contains("RIGHT")) {
                player.sendMessage(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.removed").replace("%type%", social_type.getDisplayName()));
                player_profile.getSocialMedia().remove(social_type);
                player_profile.save();
                return;
            }

            player.closeInventory();
            player.beginConversation(new ConversationFactory(XenonBukkit.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {
                public String getPromptText(ConversationContext context) {
                    return XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.setup-message").replace("&", "§").replace("%type%", social_type.getDisplayName());
                }

                @Override
                public Prompt acceptInput(ConversationContext cc, String s) {
                    Conversable conversable = cc.getForWhom();

                    if (s.equalsIgnoreCase("cancel")) {
                        player.sendMessage(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.cancelled-setup").replace("%type%", social_type.getDisplayName()));
                        return END_OF_CONVERSATION;
                    }

                    player_profile.getSocialMedia().put(social_type, s);
                    player_profile.save();

                    player.sendMessage(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.setup-link").replace("%type%", social_type.getDisplayName()));
                    return Prompt.END_OF_CONVERSATION;
                }
            }).withLocalEcho(false).withEscapeSequence("/null").withTimeout(120).buildConversation(player));
        }
    }


    @AllArgsConstructor
    public static class Twitter_Button extends Button {

        private final Profile player_profile;
        private final SocialMediaType social_type;

        public String getMaterial() {
            return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("social-media-menu.buttons.twitter.material");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("social-media-menu.buttons.twitter.data");
        }

        public String getSkullOwner() {
            if (XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("social-media-menu.buttons.twitter.material").equalsIgnoreCase("SKULL_ITEM")) {
                return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("social-media-menu.buttons.twitter.skull-owner");
            } else {
                return "";
            }
        }


        @Override
        public ItemStack getItemStack(Player player) {
            String link = player_profile.getSocialMedia().getOrDefault(social_type, "Not Setup.");

            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("social-media-menu.buttons.twitter.display_name"));
            for (String line : XenonBukkit.getInstance().getMenuConfig().getConfiguration().getStringList("social-media-menu.buttons.twitter.lore")) {
                builder.addLore(line
                        .replace("%link%", link));
            }
            builder.setOwner(getSkullOwner());
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType.name().contains("RIGHT")) {
                player.sendMessage(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.removed").replace("%type%", social_type.getDisplayName()));
                player_profile.getSocialMedia().remove(social_type);
                player_profile.save();
                return;
            }

            player.closeInventory();
            player.beginConversation(new ConversationFactory(XenonBukkit.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {
                public String getPromptText(ConversationContext context) {
                    return XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.setup-message").replace("&", "§").replace("%type%", social_type.getDisplayName());
                }

                @Override
                public Prompt acceptInput(ConversationContext cc, String s) {
                    Conversable conversable = cc.getForWhom();

                    if (s.equalsIgnoreCase("cancel")) {
                        player.sendMessage(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.cancelled-setup").replace("%type%", social_type.getDisplayName()));
                        return END_OF_CONVERSATION;
                    }

                    String link = s.replace("https://", "").replace("http://", "").replace("www.", "");
                    if (!link.startsWith(social_type.getMustStartWith())) {
                        player.sendMessage(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.invalid-link"));
                        return END_OF_CONVERSATION;
                    }

                    player_profile.getSocialMedia().put(social_type, s);
                    player_profile.save();

                    player.sendMessage(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.setup-link").replace("%type%", social_type.getDisplayName()));
                    return Prompt.END_OF_CONVERSATION;
                }
            }).withLocalEcho(false).withEscapeSequence("/null").withTimeout(120).buildConversation(player));
        }
    }

    @AllArgsConstructor
    public static class YouTube_Button extends Button {

        private final Profile player_profile;
        private final SocialMediaType social_type;

        public String getMaterial() {
            return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("social-media-menu.buttons.youtube.material");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("social-media-menu.buttons.youtube.data");
        }

        public String getSkullOwner() {
            if (XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("social-media-menu.buttons.youtube.material").equalsIgnoreCase("SKULL_ITEM")) {
                return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("social-media-menu.buttons.youtube.skull-owner");
            } else {
                return "";
            }
        }


        @Override
        public ItemStack getItemStack(Player player) {
            final String link = player_profile.getSocialMedia().getOrDefault(social_type, "Not Setup.");

            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("social-media-menu.buttons.youtube.display_name"));
            for (String line : XenonBukkit.getInstance().getMenuConfig().getConfiguration().getStringList("social-media-menu.buttons.youtube.lore")) {
                builder.addLore(line
                        .replace("%link%", link));
            }
            builder.setOwner(getSkullOwner());
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType.name().contains("RIGHT")) {
                player.sendMessage(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.removed").replace("%type%", social_type.getDisplayName()));
                player_profile.getSocialMedia().remove(social_type);
                player_profile.save();
                return;
            }

            player.closeInventory();
            player.beginConversation(new ConversationFactory(XenonBukkit.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {
                public String getPromptText(ConversationContext context) {
                    return XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.setup-message").replace("&", "§").replace("%type%", social_type.getDisplayName());
                }

                @Override
                public Prompt acceptInput(ConversationContext cc, String s) {
                    Conversable conversable = cc.getForWhom();

                    if (s.equalsIgnoreCase("cancel")) {
                        player.sendMessage(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.cancelled-setup").replace("%type%", social_type.getDisplayName()));
                        return END_OF_CONVERSATION;
                    }

                    String link = s.replace("https://", "").replace("http://", "").replace("www.", "");
                    if (!link.startsWith(social_type.getMustStartWith())) {
                        player.sendMessage(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.invalid-link"));
                        return END_OF_CONVERSATION;
                    }

                    player_profile.getSocialMedia().put(social_type, s);
                    player_profile.save();

                    player.sendMessage(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.setup-link").replace("%type%", social_type.getDisplayName()));
                    return Prompt.END_OF_CONVERSATION;
                }
            }).withLocalEcho(false).withEscapeSequence("/null").withTimeout(120).buildConversation(player));
        }
    }


    @AllArgsConstructor
    public static class Twitch_Button extends Button {
        private final Profile player_profile;
        private final SocialMediaType social_type;

        public String getMaterial() {
            return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("social-media-menu.buttons.twitch.material");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("social-media-menu.buttons.twitch.data");
        }

        public String getSkullOwner() {
            if (XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("social-media-menu.buttons.twitch.material").equalsIgnoreCase("SKULL_ITEM")) {
                return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("social-media-menu.buttons.twitch.skull-owner");
            } else {
                return "";
            }
        }


        @Override
        public ItemStack getItemStack(Player player) {
            final String link = player_profile.getSocialMedia().getOrDefault(social_type, "Not Setup.");

            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("social-media-menu.buttons.twitch.display_name"));
            for (String line : XenonBukkit.getInstance().getMenuConfig().getConfiguration().getStringList("social-media-menu.buttons.twitch.lore")) {
                builder.addLore(line
                        .replace("%link%", link)
                );
            }
            builder.setOwner(getSkullOwner());
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType.name().contains("RIGHT")) {
                player.sendMessage(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.removed").replace("%type%", social_type.getDisplayName()));
                player_profile.getSocialMedia().remove(social_type);
                player_profile.save();
                return;
            }
            player.closeInventory();
            player.beginConversation(new ConversationFactory(XenonBukkit.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {
                public String getPromptText(ConversationContext context) {
                    return XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.setup-message").replace("&", "§").replace("%type%", social_type.getDisplayName());
                }

                @Override
                public Prompt acceptInput(ConversationContext cc, String s) {
                    Conversable conversable = cc.getForWhom();

                    if (s.equalsIgnoreCase("cancel")) {
                        player.sendMessage(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.cancelled-setup").replace("%type%", social_type.getDisplayName()));
                        return END_OF_CONVERSATION;
                    }

                    String link = s.replace("https://", "").replace("http://", "").replace("www.", "");
                    if (!link.startsWith(social_type.getMustStartWith())) {
                        player.sendMessage(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.invalid-link"));
                        return END_OF_CONVERSATION;
                    }

                    player_profile.getSocialMedia().put(social_type, s);
                    player_profile.save();

                    player.sendMessage(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.setup-link").replace("%type%", social_type.getDisplayName()));
                    return Prompt.END_OF_CONVERSATION;
                }
            }).withLocalEcho(false).withEscapeSequence("/null").withTimeout(120).buildConversation(player));
        }
    }

    @AllArgsConstructor
    public static class Instagram_Button extends Button {

        private final Profile player_profile;
        private final SocialMediaType social_type;

        public String getMaterial() {
            return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("social-media-menu.buttons.instagram.material");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("social-media-menu.buttons.instagram.data");
        }

        public String getSkullOwner() {
            if (XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("social-media-menu.buttons.instagram.material").equalsIgnoreCase("SKULL_ITEM")) {
                return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("social-media-menu.buttons.instagram.skull-owner");
            } else {
                return "";
            }
        }


        @Override
        public ItemStack getItemStack(Player player) {
            String link = player_profile.getSocialMedia().getOrDefault(social_type, "Not Setup.");

            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("social-media-menu.buttons.instagram.display_name"));
            for (String line : XenonBukkit.getInstance().getMenuConfig().getConfiguration().getStringList("social-media-menu.buttons.instagram.lore")) {
                builder.addLore(line
                        .replace("%link%", link)
                );
            }
            builder.setOwner(getSkullOwner());
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType.name().contains("RIGHT")) {
                player.sendMessage(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.removed").replace("%type%", social_type.getDisplayName()));
                player_profile.getSocialMedia().remove(social_type);
                player_profile.save();
                return;
            }

            player.closeInventory();
            player.beginConversation(new ConversationFactory(XenonBukkit.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {
                public String getPromptText(ConversationContext context) {
                    return XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.setup-message").replace("&", "§").replace("%type%", social_type.getDisplayName());
                }

                @Override
                public Prompt acceptInput(ConversationContext cc, String s) {
                    Conversable conversable = cc.getForWhom();

                    if (s.equalsIgnoreCase("cancel")) {
                        player.sendMessage(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.cancelled-setup").replace("%type%", social_type.getDisplayName()));
                        return END_OF_CONVERSATION;
                    }

                    String link = s.replace("https://", "").replace("http://", "").replace("www.", "");
                    if (!link.startsWith(social_type.getMustStartWith())) {
                        player.sendMessage(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.invalid-link"));
                        return END_OF_CONVERSATION;
                    }

                    player_profile.getSocialMedia().put(social_type, s);
                    player_profile.save();

                    player.sendMessage(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.setup-link").replace("%type%", social_type.getDisplayName()));
                    return Prompt.END_OF_CONVERSATION;
                }
            }).withLocalEcho(false).withEscapeSequence("/null").withTimeout(120).buildConversation(player));
        }
    }
}