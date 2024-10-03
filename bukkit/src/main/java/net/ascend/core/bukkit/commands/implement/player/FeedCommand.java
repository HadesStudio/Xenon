package net.ascend.core.bukkit.commands.implement.player;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.util.command.BaseCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class FeedCommand extends BaseCommand {
    public FeedCommand(){
        super("feed", Collections.singletonList("feedplayer"),"xenon.feed", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player senderPlayer = (Player) sender;
        if (args.length < 1) {
            Player player = (Player) sender;
            player.setFoodLevel(20);
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("feed-command.feed-self")));
            return;
        }

        if (args[0].equalsIgnoreCase("all")) {
            if (senderPlayer.hasPermission("xenon.feed.all")) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player == null || !player.isOnline()) { // just making sure they didnt log off after check started
                        return;
                    }

                    player.setFoodLevel(20);
                    player.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("feed-command.feed-self")));
                }
            } else {
                senderPlayer.sendMessage(CC.translate(Locale.COMMANDS_NO_PERMISSION));
            }
        } else {
            if (senderPlayer.hasPermission("xenon.feed.others")) {
                Player player = Bukkit.getPlayer(args[0]);
                if (player == null || !player.isOnline()) {
                    sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("feed-command.not-online")));
                    return;
                }
                player.setFoodLevel(20);
                sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("feed-command.feed-other").replace("%player%", player.getName())));
                player.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("feed-command.feed-self")));
            } else {
                senderPlayer.sendMessage(CC.translate(Locale.COMMANDS_NO_PERMISSION));
            }
        }
    }
}
