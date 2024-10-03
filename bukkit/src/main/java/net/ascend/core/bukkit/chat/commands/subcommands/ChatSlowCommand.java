package net.ascend.core.bukkit.chat.commands.subcommands;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class ChatSlowCommand extends SubCommand {
    public ChatSlowCommand() {
        super("slow", Collections.singletonList("slowchat"), "xenon.staff.chat.slow");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("chat-command.slow-chat.usage")));
            return;
        }

        if (!this.checkNumber(sender, args[0])) return;
        int time = Math.abs(Integer.parseInt(args[0]));

        if (time == 0) {
            XenonBukkit.getInstance().getChatManager().setSlowTime(0);
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("chat-command.slow-chat.disabled-slow")));
            return;
        }

        XenonBukkit.getInstance().getChatManager().setSlowTime(time);

        sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("chat-command.slow-chat.slowed-chat").replace("%time%", String.valueOf(time))));
        for (String message : XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("chat-command.slow-chat.broadcast")) {
            Bukkit.broadcastMessage(CC.translate(message).replace("%player%", sender.getName()));
        }
    }
}