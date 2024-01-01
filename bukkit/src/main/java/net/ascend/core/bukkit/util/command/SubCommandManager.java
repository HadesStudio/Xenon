package net.ascend.core.bukkit.util.command;

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

import net.ascend.core.bukkit.chat.commands.ChatCommand;
import net.ascend.core.bukkit.modules.ModuleLoader;
import net.ascend.core.bukkit.profile.commands.ProfileCommand;
import net.ascend.core.bukkit.profile.notifications.commands.NotificationsAdminSubCommand;
import net.ascend.core.bukkit.profile.profilesystem.commands.FriendsCommand;
import net.ascend.core.bukkit.profile.squads.commands.SquadCommands;
import net.ascend.core.bukkit.rank.rank.commands.RankCommand;
import net.ascend.core.bukkit.tag.command.TagCommand;

public class SubCommandManager implements ModuleLoader {

    private ProfileCommand profileCommand;
    private RankCommand rankCommand;
    private TagCommand tagCommand;
    private FriendsCommand friendsCommand;
    private ChatCommand chatCommand;
    private NotificationsAdminSubCommand notificationsAdminSubCommand;
    private SquadCommands squadCommands;

    @Override
    public void onEnable() {
        this.profileCommand = new ProfileCommand();
        this.rankCommand = new RankCommand();
        this.tagCommand = new TagCommand();
        this.friendsCommand = new FriendsCommand();
        this.chatCommand = new ChatCommand();
        this.notificationsAdminSubCommand = new NotificationsAdminSubCommand();
        this.squadCommands = new SquadCommands();
    }

    @Override
    public void onDisable() {
        this.profileCommand.disable();
        this.rankCommand.disable();
        this.tagCommand.disable();
        this.friendsCommand.disable();
        this.chatCommand.disable();
        this.notificationsAdminSubCommand.disable();
        this.squadCommands.disable();
    }

    @Override
    public void onReload() {

    }
}
