package net.ascend.core.bukkit.commands.implement.staff;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.punishment.Punishment;
import net.ascend.core.bukkit.punishment.menu.StaffHistoryMenu;
import net.ascend.core.bukkit.util.command.BaseCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bson.Document;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class StaffHistoryCommand  extends BaseCommand {
    public StaffHistoryCommand() {
        super("staffhistory", Arrays.asList("viewbans", "staffbans", "viewbandata", "viewstaffbans"), "xenon.command.staffhistory", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("staff-history-command.usage")));
            return;
        }

        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(args[0]);
        if (profile == null) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[0]));
            return;
        }

        sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("staff-history-command.scanning").replace("%player%", profile.getName())));

        CompletableFuture.runAsync(() -> {
            Map<UUID, List<Punishment>> executedPunishments = new HashMap<>();
            MongoCursor<Document> cursor = XenonBukkit.getInstance().getMongoModule().getPunishmentsCollection().find(
                    Filters.eq("addedBy", profile.getUniqueId().toString())
            ).iterator();

            while (cursor.hasNext()) {
                Document document = cursor.next();
                Punishment punishment = new Punishment();
                punishment.load(document);

                UUID targetUUID = punishment.getTargetUUID();

                executedPunishments.computeIfAbsent(targetUUID, k -> new ArrayList<>()).add(punishment);
            }

            new StaffHistoryMenu(profile, executedPunishments).openMenu((Player) sender);
        }).exceptionally(ex -> {
            ex.printStackTrace();
            sender.sendMessage(CC.translate("An error occurred while fetching punishments."));
            return null;
        });
    }
}

