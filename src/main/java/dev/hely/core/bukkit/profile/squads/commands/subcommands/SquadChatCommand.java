package dev.hely.core.bukkit.profile.squads.commands.subcommands;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.profile.squads.Squad;
import dev.hely.core.bukkit.utils.command.sub.SubCommand;
import dev.hely.lib.util.CC;
import org.apache.commons.lang.StringUtils;
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
        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (profile.getSquad() == null) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.not-in-squad")));
            return;
        }
        boolean inSquadChat = !profile.isSquadChat();
        profile.setSquadChat(inSquadChat);

        player.sendMessage(inSquadChat ? CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.chat.enabled")) : CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.chat.disabled")));
        profile.save();
    }
}


