package dev.hely.core.bukkit.profile.profilesystem.menu.type;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.profile.profilesystem.menu.type
//   Date: Saturday, July 15, 2023 - 5:14 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.profile.profilesystem.impl.social.SocialMediaType;
import dev.hely.lib.CC;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.Menu;
import dev.hely.lib.menu.button.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
        return OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("social-media-menu.title").replace("%player%", profile.getName());
    }

    @Override
    public String getTitle(Player player) {
        return OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("social-media-menu.title").replace("%player%", profile.getName());
    }

    @Override
    public int getSize() {
        return OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getInt("social-media-menu.size");
    }

    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        final Map<Integer, Button> toReturn = new HashMap<>();

        toReturn.put(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getInt("social-media-menu.discord.slot"), new Discord_Button(profile, SocialMediaType.DISCORD));
        toReturn.put(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getInt("social-media-menu.twitter.slot"), new Twitter_Button(profile, SocialMediaType.TWITTER));
        toReturn.put(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getInt("social-media-menu.youtube.slot"), new YouTube_Button(profile, SocialMediaType.YOUTUBE));
        toReturn.put(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getInt("social-media-menu.twitch.slot"), new Twitch_Button(profile, SocialMediaType.TWITCH));
        toReturn.put(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getInt("social-media-menu.instagram.slot"), new Twitch_Button(profile, SocialMediaType.INSTAGRAM));

        return toReturn;
    }

    @AllArgsConstructor
    public static class Discord_Button extends Button {
        private final Profile player_profile;
        private final SocialMediaType social_type;

        public Material getMaterial() {
            if (OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("social-media-menu.discord.material") != null) {
                if (Material.getMaterial(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("social-media-menu.discord.material")) != null) {
                    return Material.valueOf(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("social-media-menu.discord.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'discord-button' is null, using a default value."));
                    return Material.SKULL_ITEM;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'discord-button' is broken, using a default value."));
                return Material.SKULL_ITEM;
            }
        }

        public short getData() {
            return (short) OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getInt("social-media-menu.discord.data.data");
        }

        public String getSkullOwner() {
            if (Material.getMaterial(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("social-media-menu.discord.material")) == Material.SKULL_ITEM) {
                return OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("social-media-menu.discord.data.skull-owner");
            } else {
                return "";
            }
        }


        @Override
        public ItemStack getItemStack(Player player) {
            final String link = player_profile.getSocialMedia().getOrDefault(SocialMediaType.DISCORD, "Not Setup.");

            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("social-media-menu.discord.name"));
            for (String line : OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getStringList("social-media-menu.discord.lore")) {
                builder.addLore(line
                        .replace("%discord%", link)
                );
            }
            builder.setOwner(getSkullOwner());
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType.name().contains("RIGHT")) {
                player.sendMessage(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.removed").replace("%type%", social_type.getDisplayName()));
                player_profile.getSocialMedia().remove(social_type);
                player_profile.save();
                return;
            }
            player.closeInventory();

            player.beginConversation(new ConversationFactory(OxygenBukkit.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {
                public String getPromptText(ConversationContext context) {
                    return OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.setup-message").replace("&", "§").replace("%type%", social_type.getDisplayName());
                }

                @Override
                public Prompt acceptInput(ConversationContext cc, String s) {
                    final Conversable conversable = cc.getForWhom();

                    if (s.equalsIgnoreCase("cancel")) {
                        player.sendMessage(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.cancelled-setup").replace("%type%", social_type.getDisplayName()));
                        return END_OF_CONVERSATION;
                    }

                    player_profile.getSocialMedia().put(social_type, s);
                    player_profile.save();

                    player.sendMessage(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.setup-link").replace("%type%", social_type.getDisplayName()));
                    return Prompt.END_OF_CONVERSATION;
                }
            }).withLocalEcho(false).withEscapeSequence("/null").withTimeout(120).buildConversation(player));
        }
    }


    @AllArgsConstructor
    public static class Twitter_Button extends Button {
        private final Profile player_profile;
        private final SocialMediaType social_type;

        public Material getMaterial() {
            if (OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("social-media-menu.twitter.material") != null) {
                if (Material.getMaterial(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("social-media-menu.twitter.material")) != null) {
                    return Material.valueOf(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("social-media-menu.twitter.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'twitter-button' is null, using a default value."));
                    return Material.SKULL_ITEM;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'twitter-button' is broken, using a default value."));
                return Material.SKULL_ITEM;
            }
        }

        public short getData() {
            return (short) OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getInt("social-media-menu.twitter.data.data");
        }

        public String getSkullOwner() {
            if (Material.getMaterial(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("social-media-menu.twitter.material")) == Material.SKULL_ITEM) {
                return OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("social-media-menu.twitter.data.skull-owner");
            } else {
                return "";
            }
        }


        @Override
        public ItemStack getItemStack(Player player) {
            final String link = player_profile.getSocialMedia().getOrDefault(social_type, "Not Setup.");

            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("social-media-menu.twitter.name"));
            for (String line : OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getStringList("social-media-menu.twitter.lore")) {
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
                player.sendMessage(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.removed").replace("%type%", social_type.getDisplayName()));
                player_profile.getSocialMedia().remove(social_type);
                player_profile.save();
                return;
            }
            player.closeInventory();

            player.beginConversation(new ConversationFactory(OxygenBukkit.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {
                public String getPromptText(ConversationContext context) {
                    return OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.setup-message").replace("&", "§").replace("%type%", social_type.getDisplayName());
                }

                @Override
                public Prompt acceptInput(ConversationContext cc, String s) {
                    final Conversable conversable = cc.getForWhom();

                    if (s.equalsIgnoreCase("cancel")) {
                        player.sendMessage(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.cancelled-setup").replace("%type%", social_type.getDisplayName()));
                        return END_OF_CONVERSATION;
                    }

                    final String link = s.replace("https://", "").replace("http://", "").replace("www.", "");

                    if (!link.startsWith(social_type.getMustStartWith())) {
                        player.sendMessage(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.invalid-link"));
                        return END_OF_CONVERSATION;
                    }

                    player_profile.getSocialMedia().put(social_type, s);
                    player_profile.save();

                    player.sendMessage(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.setup-link").replace("%type%", social_type.getDisplayName()));
                    return Prompt.END_OF_CONVERSATION;
                }
            }).withLocalEcho(false).withEscapeSequence("/null").withTimeout(120).buildConversation(player));
        }
    }

    @AllArgsConstructor
    public static class YouTube_Button extends Button {
        private final Profile player_profile;
        private final SocialMediaType social_type;

        public Material getMaterial() {
            if (OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("social-media-menu.youtube.material") != null) {
                if (Material.getMaterial(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("social-media-menu.youtube.material")) != null) {
                    return Material.valueOf(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("social-media-menu.youtube.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'youtube-button' is null, using a default value."));
                    return Material.SKULL_ITEM;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'youtube-button' is broken, using a default value."));
                return Material.SKULL_ITEM;
            }
        }

        public short getData() {
            return (short) OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getInt("social-media-menu.youtube.data.data");
        }

        public String getSkullOwner() {
            if (Material.getMaterial(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("social-media-menu.youtube.material")) == Material.SKULL_ITEM) {
                return OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("social-media-menu.youtube.data.skull-owner");
            } else {
                return "";
            }
        }


        @Override
        public ItemStack getItemStack(Player player) {
            final String link = player_profile.getSocialMedia().getOrDefault(social_type, "Not Setup.");

            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("social-media-menu.youtube.name"));
            for (String line : OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getStringList("social-media-menu.youtube.lore")) {
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
                player.sendMessage(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.removed").replace("%type%", social_type.getDisplayName()));
                player_profile.getSocialMedia().remove(social_type);
                player_profile.save();
                return;
            }
            player.closeInventory();

            player.beginConversation(new ConversationFactory(OxygenBukkit.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {
                public String getPromptText(ConversationContext context) {
                    return OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.setup-message").replace("&", "§").replace("%type%", social_type.getDisplayName());
                }

                @Override
                public Prompt acceptInput(ConversationContext cc, String s) {
                    final Conversable conversable = cc.getForWhom();

                    if (s.equalsIgnoreCase("cancel")) {
                        player.sendMessage(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.cancelled-setup").replace("%type%", social_type.getDisplayName()));
                        return END_OF_CONVERSATION;
                    }

                    final String link = s.replace("https://", "").replace("http://", "").replace("www.", "");

                    if (!link.startsWith(social_type.getMustStartWith())) {
                        player.sendMessage(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.invalid-link"));
                        return END_OF_CONVERSATION;
                    }

                    player_profile.getSocialMedia().put(social_type, s);
                    player_profile.save();

                    player.sendMessage(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.setup-link").replace("%type%", social_type.getDisplayName()));
                    return Prompt.END_OF_CONVERSATION;
                }
            }).withLocalEcho(false).withEscapeSequence("/null").withTimeout(120).buildConversation(player));
        }
    }


    @AllArgsConstructor
    public static class Twitch_Button extends Button {
        private final Profile player_profile;
        private final SocialMediaType social_type;

        public Material getMaterial() {
            if (OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("social-media-menu.twitch.material") != null) {
                if (Material.getMaterial(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("social-media-menu.twitch.material")) != null) {
                    return Material.valueOf(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("social-media-menu.twitch.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'twitch-button' is null, using a default value."));
                    return Material.SKULL_ITEM;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'twitch-button' is broken, using a default value."));
                return Material.SKULL_ITEM;
            }
        }

        public short getData() {
            return (short) OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getInt("social-media-menu.twitch.data.data");
        }

        public String getSkullOwner() {
            if (Material.getMaterial(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("social-media-menu.twitch.material")) == Material.SKULL_ITEM) {
                return OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("social-media-menu.twitch.data.skull-owner");
            } else {
                return "";
            }
        }


        @Override
        public ItemStack getItemStack(Player player) {
            final String link = player_profile.getSocialMedia().getOrDefault(social_type, "Not Setup.");

            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("social-media-menu.twitch.name"));
            for (String line : OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getStringList("social-media-menu.twitch.lore")) {
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
                player.sendMessage(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.removed").replace("%type%", social_type.getDisplayName()));
                player_profile.getSocialMedia().remove(social_type);
                player_profile.save();
                return;
            }
            player.closeInventory();

            player.beginConversation(new ConversationFactory(OxygenBukkit.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {
                public String getPromptText(ConversationContext context) {
                    return OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.setup-message").replace("&", "§").replace("%type%", social_type.getDisplayName());
                }

                @Override
                public Prompt acceptInput(ConversationContext cc, String s) {
                    final Conversable conversable = cc.getForWhom();

                    if (s.equalsIgnoreCase("cancel")) {
                        player.sendMessage(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.cancelled-setup").replace("%type%", social_type.getDisplayName()));
                        return END_OF_CONVERSATION;
                    }

                    final String link = s.replace("https://", "").replace("http://", "").replace("www.", "");

                    if (!link.startsWith(social_type.getMustStartWith())) {
                        player.sendMessage(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.invalid-link"));
                        return END_OF_CONVERSATION;
                    }

                    player_profile.getSocialMedia().put(social_type, s);
                    player_profile.save();

                    player.sendMessage(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.setup-link").replace("%type%", social_type.getDisplayName()));
                    return Prompt.END_OF_CONVERSATION;
                }
            }).withLocalEcho(false).withEscapeSequence("/null").withTimeout(120).buildConversation(player));
        }
    }

    @AllArgsConstructor
    public static class Instagram_Button extends Button {
        private final Profile player_profile;
        private final SocialMediaType social_type;

        public Material getMaterial() {
            if (OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("social-media-menu.instagram.material") != null) {
                if (Material.getMaterial(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("social-media-menu.instagram.material")) != null) {
                    return Material.valueOf(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("social-media-menu.instagram.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'instagram-button' is null, using a default value."));
                    return Material.SKULL_ITEM;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'instagram-button' is broken, using a default value."));
                return Material.SKULL_ITEM;
            }
        }

        public short getData() {
            return (short) OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getInt("social-media-menu.instagram.data.data");
        }

        public String getSkullOwner() {
            if (Material.getMaterial(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("social-media-menu.instagram.material")) == Material.SKULL_ITEM) {
                return OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("social-media-menu.instagram.data.skull-owner");
            } else {
                return "";
            }
        }


        @Override
        public ItemStack getItemStack(Player player) {
            final String link = player_profile.getSocialMedia().getOrDefault(social_type, "Not Setup.");

            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getString("social-media-menu.instagram.name"));
            for (String line : OxygenBukkit.getInstance().getProfileMenuConfig().getConfiguration().getStringList("social-media-menu.instagram.lore")) {
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
                player.sendMessage(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.removed").replace("%type%", social_type.getDisplayName()));
                player_profile.getSocialMedia().remove(social_type);
                player_profile.save();
                return;
            }
            player.closeInventory();

            player.beginConversation(new ConversationFactory(OxygenBukkit.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {
                public String getPromptText(ConversationContext context) {
                    return OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.setup-message").replace("&", "§").replace("%type%", social_type.getDisplayName());
                }

                @Override
                public Prompt acceptInput(ConversationContext cc, String s) {
                    final Conversable conversable = cc.getForWhom();

                    if (s.equalsIgnoreCase("cancel")) {
                        player.sendMessage(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.cancelled-setup").replace("%type%", social_type.getDisplayName()));
                        return END_OF_CONVERSATION;
                    }

                    final String link = s.replace("https://", "").replace("http://", "").replace("www.", "");

                    if (!link.startsWith(social_type.getMustStartWith())) {
                        player.sendMessage(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.invalid-link"));
                        return END_OF_CONVERSATION;
                    }

                    player_profile.getSocialMedia().put(social_type, s);
                    player_profile.save();

                    player.sendMessage(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("profile-system.social-system.setup-link").replace("%type%", social_type.getDisplayName()));
                    return Prompt.END_OF_CONVERSATION;
                }
            }).withLocalEcho(false).withEscapeSequence("/null").withTimeout(120).buildConversation(player));
        }
    }
}