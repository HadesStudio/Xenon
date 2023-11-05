package dev.hely.core.bukkit.commands.implement.staff;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.profile.packet.implement.AdminChatPacket;
import dev.hely.core.bukkit.utils.ChatUtil;
import dev.hely.core.bukkit.utils.command.BaseCommand;
import dev.hely.lib.util.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

/**
 * Created By LeandroSSJ
 * Created on 12/07/2023
 * Protect Name OxygenGlobal
 */
public class AdminChatCommand extends BaseCommand {

    public AdminChatCommand(){
        super("adminchat", Collections.singletonList("ac"), "oxygen.adminchat", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (args.length == 0) {
            profile.getStaffProfile().setAdminChat(!profile.getStaffProfile().isAdminChat());
            player.sendMessage(profile.getStaffProfile().isAdminChat() ? CC.translate(Locale.ADMIN_CHAT_ENABLED) : CC.translate(Locale.ADMIN_CHAT_DISABLED));
            return;
        }

        OxygenBukkit.getInstance().getRedisModule().sendPacket(new AdminChatPacket(
                player.getName(),
                profile.getCurrentServer(),
                ChatUtil.joinArray(args, " ", 1)));
    }
}
