package dev.hely.core.bukkit.profile.conversations;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.profile.staff
//   Date: Wednesday, August 09, 2023 - 2:41 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.profile.Profile;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class MessageConversations {

    private final Profile profile;
    private final Map<UUID, Conversation> conversations;

    public MessageConversations(Profile profile) {
        this.profile = profile;
        this.conversations = new HashMap<>();
    }

    public boolean canBeMessagedBy(Player player) {
        return (!player.hasPermission("oxygen.staff")) && (player.hasPermission("oxygen.message.bypass")) && !this.profile.getIgnoredList().contains(player.getName());
    }

    public Conversation getOrCreateConversation(Player target) {
        Player sender = Bukkit.getPlayer(profile.getUniqueId());
        if (sender != null) {
            Conversation conversation = this.conversations.get(target.getUniqueId());
            if (conversation == null) {
                conversation = new Conversation(this.profile.getUniqueId(), target.getUniqueId());
            }

            return conversation;
        }
        return null;
    }

    public Conversation getLastRepliedConversation() {
        List<Conversation> list = this.conversations.values().stream().sorted(Comparator.comparingLong(Conversation::getLastMessageSentAt)).collect(Collectors.toList());
        Collections.reverse(list);
        return list.isEmpty() ? null : list.get(0);
    }
}
