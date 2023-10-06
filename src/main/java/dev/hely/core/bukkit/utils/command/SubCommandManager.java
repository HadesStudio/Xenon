package dev.hely.core.bukkit.utils.command;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.utils.command
//   Date: Saturday, August 05, 2023 - 3:52 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.chat.commands.ChatCommand;
import dev.hely.core.bukkit.modules.ModuleLoader;
import dev.hely.core.bukkit.profile.commands.ProfileCommand;
import dev.hely.core.bukkit.profile.notifications.commands.NotificationsAdminSubCommand;
import dev.hely.core.bukkit.profile.profilesystem.commands.FriendsCommand;
import dev.hely.core.bukkit.profile.squads.commands.SquadCommands;
import dev.hely.core.bukkit.ranks.rank.commands.RankCommand;
import dev.hely.core.bukkit.tag.command.TagCommand;
import dev.hely.core.bukkit.utils.command.sub.SubCommand;

import java.util.ArrayList;
import java.util.List;

public class SubCommandManager implements ModuleLoader {
    private ProfileCommand profileCommand;
    private RankCommand rankCommand;
    private TagCommand tagCommand;
    private FriendsCommand friendsCommand;
    private ChatCommand chatCommand;
    private NotificationsAdminSubCommand notificationsAdminSubCommand;
    private SquadCommands squadCommands;

    @Override
    public void enable() {
        this.profileCommand = new ProfileCommand();
        this.rankCommand = new RankCommand();
        this.tagCommand = new TagCommand();
        this.friendsCommand = new FriendsCommand();
        this.chatCommand = new ChatCommand();
        this.notificationsAdminSubCommand = new NotificationsAdminSubCommand();
        this.squadCommands = new SquadCommands();
    }

    @Override
    public void disable() {
        this.profileCommand.disable();
        this.rankCommand.disable();
        this.tagCommand.disable();
        this.friendsCommand.disable();
        this.chatCommand.disable();
        this.notificationsAdminSubCommand.disable();
        this.squadCommands.disable();
    }

    @Override
    public void reload() {

    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public String getName() {
        return "Sub-Command Manager";
    }
}
