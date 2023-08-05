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

import dev.hely.core.bukkit.modules.ModuleLoader;
import dev.hely.core.global.profile.profilesystem.commands.FriendsCommand;
import dev.hely.core.global.ranks.rank.commands.RankCommand;
import dev.hely.core.global.tag.command.TagCommand;

public class SubCommandManager implements ModuleLoader {
    private RankCommand rankCommand;
    private TagCommand tagCommand;
    private FriendsCommand friendsCommand;

    public SubCommandManager() {
        this.rankCommand = new RankCommand();
        this.tagCommand = new TagCommand();
        this.friendsCommand = new FriendsCommand();
    }

    @Override
    public void enable() {
        this.rankCommand = new RankCommand();
        this.tagCommand = new TagCommand();
        this.friendsCommand = new FriendsCommand();
    }

    @Override
    public void disable() {
        this.rankCommand.disable();
        this.tagCommand.disable();
        this.friendsCommand.disable();
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
