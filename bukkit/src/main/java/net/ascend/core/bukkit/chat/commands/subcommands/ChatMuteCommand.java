package net.ascend.core.bukkit.chat.commands.subcommands;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class ChatMuteCommand extends SubCommand {

    public ChatMuteCommand() {
        super("mute", Collections.singletonList("mutechat"), "xenon.staff.chat.mute");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (XenonBukkit.getInstance().getChatManager().isMuted()) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("chat-command.mute.already-muted")));
            return;
        }

        XenonBukkit.getInstance().getChatManager().setMuted(true);

        sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("chat-command.mute.muted-chat")));
        for (String message : XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("chat-command.mute.broadcast")) {
            sender.sendMessage(CC.translate(message).replace("%player%", sender.getName()));
        }
    }
}

