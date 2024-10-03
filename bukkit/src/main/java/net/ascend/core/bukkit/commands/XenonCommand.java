package net.ascend.core.bukkit.commands;

import net.ascend.core.bukkit.commands.menu.OxygenMenu;
import net.ascend.core.bukkit.util.command.BaseCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class XenonCommand extends BaseCommand {

    private final List<UUID> uuidList = Arrays.asList(
            UUID.fromString("d0ba628c-4d44-4b3b-aba9-4f62979ff303"), // LEANDROSSJ
            UUID.fromString("c1f714e6-1ba1-45c3-ad0f-aee338d6c822")); //DevDipin

    public XenonCommand(){
        super("xenon", Collections.singletonList("core"), true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            Player player = (Player) sender;
            if (player.isOp()) {
                new OxygenMenu(player).openMenu(player);
            } else {
                for (UUID admin : uuidList) {
                    if (admin.equals(player.getUniqueId())) {
                        new OxygenMenu(player).openMenu(player);
                    }
                }
            }
        }
    }
}
