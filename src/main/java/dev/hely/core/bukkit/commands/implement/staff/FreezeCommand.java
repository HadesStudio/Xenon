package dev.hely.core.bukkit.commands.implement.staff;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.commands.implement.player
//   Date: Monday, September 04, 2023 - 7:32 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.utils.command.BaseCommand;
import dev.hely.lib.CC;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

@Setter
@Getter
public class FreezeCommand extends BaseCommand {
    public static boolean serverFrozen = false;

    public FreezeCommand(){
        super("freeze", Collections.singletonList("freezeplayer"),"oxygen.freeze", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player senderPlayer = (Player) sender;
        if (args.length < 1) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("frozen-command.command.usage")));
            return;
        }

        if (args[0].equalsIgnoreCase("server")) {
            if (senderPlayer.hasPermission("oxygen.freeze.server")) {
                if (serverFrozen) { // unfreeze the server then ;p
                    int playerSize = 0;
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());
                        if (!player.isOnline() || profile == null) { // just making sure they didnt log off after check started
                            return;
                        }
                        profile.setFrozen(false);
                        profile.save(false);

                        player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("frozen-command.command.server-unfrozen")));
                        playerSize = playerSize + 1;
                    }
                    serverFrozen = false;
                    sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("frozen-command.command.unfroze-server").replace("%players%", String.valueOf(playerSize))));
                } else { // server is not frozen rn, so we want to freeze it (hopefully)
                    int playerSize = 0;
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());
                        if (!player.isOnline() || profile == null || player.hasPermission("oxygen.freeze.bypass")) { // just making sure they didnt log off after check started
                            return;
                        }
                        profile.setFrozen(true);
                        profile.save(false);

                        player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("frozen-command.command.server-frozen")));
                        playerSize = playerSize + 1;
                    }
                    serverFrozen = true;
                    sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("frozen-command.command.freeze-server").replace("%players%", String.valueOf(playerSize))));
                }
            } else {
                senderPlayer.sendMessage(CC.translate(Locale.COMMANDS_NO_PERMISSION));
            }
        } else {
            Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(args[0]);
            if (profile == null) {
                sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[0]));
                return;
            }

            if (profile.isFrozen()) { // unfreeze them
                if (Bukkit.getPlayer(profile.getName()).isOnline()) {
                    Player player = Bukkit.getPlayer(profile.getName());
                    player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("frozen-command.unfrozen").replace("%player%", sender.getName())));
                }
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("frozen-command.unfroze-player").replace("%player%", profile.getName())));
                profile.setFrozen(false);
                profile.save(false);
            } else { // freeze them
                if (Bukkit.getPlayer(profile.getName()).isOnline()) {
                    Player player = Bukkit.getPlayer(profile.getName());
                    player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("frozen-command.frozen").replace("%player%", sender.getName())));
                }
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("frozen-command.freeze-player").replace("%player%", profile.getName())));
                profile.setFrozen(true);
                profile.save(false);
            }
        }
    }
}
