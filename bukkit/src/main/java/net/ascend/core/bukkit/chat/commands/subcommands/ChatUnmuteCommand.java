package net.ascend.core.bukkit.chat.commands.subcommands;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class ChatUnmuteCommand extends SubCommand {

    public ChatUnmuteCommand() {
        super("unmute", Collections.singletonList("unmutechat"), "xenon.staff.chat.unmute");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!XenonBukkit.getInstance().getChatManager().isMuted()) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("chat-command.unmuted.not-muted")));
            return;
        }

        XenonBukkit.getInstance().getChatManager().setMuted(false);

        sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("chat-command.unmuted.unmuted-chat")));
        for (String message : XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("chat-command.unmuted.broadcast")) {
            sender.sendMessage(CC.translate(message).replace("%player%", sender.getName()));
        }
    }
}
