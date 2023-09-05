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
import dev.hely.core.bukkit.coins.coupons.commands.CouponCommand;
import dev.hely.core.bukkit.coins.sales.commands.StoreSaleCommand;
import dev.hely.core.bukkit.modules.ModuleLoader;
import dev.hely.core.bukkit.profile.commands.ProfileCommand;
import dev.hely.core.bukkit.profile.profilesystem.commands.FriendsCommand;
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
    private StoreSaleCommand storeSaleCommand;
    private ChatCommand chatCommand;
    private CouponCommand couponCommand;

    @Override
    public void enable() {
        this.profileCommand = new ProfileCommand();
        this.rankCommand = new RankCommand();
        this.tagCommand = new TagCommand();
        this.friendsCommand = new FriendsCommand();
        this.storeSaleCommand = new StoreSaleCommand();
        this.chatCommand = new ChatCommand();
        this.couponCommand = new CouponCommand();

    }

    @Override
    public void disable() {
        this.profileCommand.disable();
        this.rankCommand.disable();
        this.tagCommand.disable();
        this.friendsCommand.disable();
        this.storeSaleCommand.disable();
        this.chatCommand.disable();
        this.couponCommand.disable();
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
