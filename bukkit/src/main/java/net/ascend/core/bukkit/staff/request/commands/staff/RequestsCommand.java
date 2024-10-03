package net.ascend.core.bukkit.staff.request.commands.staff;

import net.ascend.core.bukkit.staff.request.menu.RequestsMenu;
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

        new RequestsMenu().openMenu(player);
    }
}
