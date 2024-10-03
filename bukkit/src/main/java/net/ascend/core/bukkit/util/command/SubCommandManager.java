package net.ascend.core.bukkit.util.command;

import net.ascend.core.bukkit.chat.commands.ChatCommand;
import net.ascend.core.bukkit.modules.ModuleLoader;
import net.ascend.core.bukkit.profile.commands.ProfileCommand;
import net.ascend.core.bukkit.profile.myprofile.commands.FriendsCommand;
import net.ascend.core.bukkit.rank.rank.command.RankCommand;
import net.ascend.core.bukkit.server.command.ServerManagerCommand;
import net.ascend.core.bukkit.tag.command.TagCommand;

public class SubCommandManager implements ModuleLoader {

    private ProfileCommand profileCommand;
    private RankCommand rankCommand;
    private TagCommand tagCommand;
    private ChatCommand chatCommand;
    private ServerManagerCommand serverManagerCommand;


    @Override
    public void onEnable() {
        this.profileCommand = new ProfileCommand();
        this.rankCommand = new RankCommand();
        this.tagCommand = new TagCommand();
        this.chatCommand = new ChatCommand();
        this.serverManagerCommand = new ServerManagerCommand();
    }

    @Override
    public void onDisable() {
        this.profileCommand.disable();
        this.rankCommand.disable();
        this.tagCommand.disable();
        this.chatCommand.disable();
        this.serverManagerCommand.disable();
    }

    @Override
    public void onReload() {

    }
}
