package net.ascend.core.bukkit.staff.requests.commands.staff;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.staff.requests.commands.staff
//   Date: Sunday, July 30, 2023 - 6:10 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.core.bukkit.staff.menu.RequestsMainMenu;
import net.ascend.core.bukkit.util.command.BaseCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class RequestsCommand  extends BaseCommand {

    public RequestsCommand(){
        super("staffrequests", Arrays.asList("requests", "helpoprequests", "reports"), "xenon.requests", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        new RequestsMainMenu().openMenu(player);
    }
}
