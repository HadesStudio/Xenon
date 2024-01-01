package net.ascend.core.bukkit.profile.squads.commands.subcommands;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class SquadChatCommand extends SubCommand {
    public SquadChatCommand() {
        super("chat", Collections.singletonList("c"));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_ONLY_PLAYER));
            return;
        }
        Player player = (Player)sender;
        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (profile.getSquad() == null) {
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.not-in-squad")));
            return;
        }
        boolean inSquadChat = !profile.isSquadChat();
        profile.setSquadChat(inSquadChat);

        player.sendMessage(inSquadChat ? CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.chat.enabled")) : CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.chat.disabled")));
        profile.save();
    }
}


