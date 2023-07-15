package dev.hely.core.commands.implement.staff;

import dev.hely.core.Oxygen;
import dev.hely.core.configuration.Locale;
import dev.hely.core.profile.Profile;
import dev.hely.core.profile.packet.implement.StaffChatPacket;
import dev.hely.core.utils.ChatUtil;
import dev.hely.core.utils.command.BaseCommand;
import dev.hely.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

/**
 * Created By LeandroSSJ
 * Created on 12/07/2023
 * Protect Name Oxygen
 */
public class StaffChatCommand extends BaseCommand {

    public StaffChatCommand(){
        super("staffchat", Collections.singletonList("sc"), "oxygen.staffchat", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Profile profile = Oxygen.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (args.length == 0) {
            profile.getStaffProfile().setStaffChat(!profile.getStaffProfile().isStaffChat());
            player.sendMessage(profile.getStaffProfile().isStaffChat() ? CC.translate(Locale.STAFF_CHAT_ENABLED) : CC.translate(Locale.STAFF_CHAT_DISABLED));
            return;
        }

        Oxygen.getInstance().getRedisModule().sendPacket(new StaffChatPacket(
                player.getDisplayName(),
                Oxygen.getInstance().getServerManager().getServerName(),
                ChatUtil.joinArray(args, " ", 1)));
    }
}
