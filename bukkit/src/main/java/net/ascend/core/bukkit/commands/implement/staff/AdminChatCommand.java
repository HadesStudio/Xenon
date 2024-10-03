package net.ascend.core.bukkit.commands.implement.staff;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.packet.implement.staff.AdminChatPacket;
import net.ascend.core.bukkit.util.ChatUtil;
import net.ascend.core.bukkit.util.command.BaseCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

/**
 * Created By LeandroSSJ
 * Created on 12/07/2023
 * Protect Name Xenon
 */
public class AdminChatCommand extends BaseCommand {

    public AdminChatCommand(){
        super("adminchat", Collections.singletonList("ac"), "xenon.adminchat", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (args.length == 0) {
            profile.getStaffProfile().setAdminChat(!profile.getStaffProfile().isAdminChat());
            player.sendMessage(profile.getStaffProfile().isAdminChat() ? CC.translate(Locale.ADMIN_CHAT_ENABLED) : CC.translate(Locale.ADMIN_CHAT_DISABLED));
            return;
        }

        XenonBukkit.getInstance().getRedisModule().sendPacket(new AdminChatPacket(
                player.getName(),
                profile.getCurrentServer(),
                ChatUtil.joinArray(args, " ", 1)));
    }
}
