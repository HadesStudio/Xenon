package net.ascend.core.bukkit.profile.note.command;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.menu.submenus.NotesMenu;
import net.ascend.core.bukkit.profile.note.Note;
import net.ascend.core.bukkit.util.ChatUtil;
import net.ascend.core.bukkit.util.TimeUtil;
import net.ascend.core.bukkit.util.command.BaseCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created By LeandroSSJ
 * Created on 06/07/2023
 * Protect Name Xenon
 */
public class NotesCommand extends BaseCommand {

    public NotesCommand() {
        super("notes", "xenon.command.notes");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 2) {
            Locale.NOTES_USAGE.forEach(s -> sender.sendMessage(CC.translate(s)));
            return;
        }

        if(args.length == 2 && args[0].equalsIgnoreCase("check")) {
            Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(args[1]);
            if (profile == null) {
                sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[1]));
                return;
            }

            if (profile.getNotes().size() == 0) {
                sender.sendMessage(CC.translate(Locale.NOTES_DONT_EXISTS));
                return;
            }

            if (sender instanceof ConsoleCommandSender) {
                Locale.NOTES_CHECK_MESSAGE.forEach(message -> {
                    if (!message.toLowerCase().contains("%notes%")) {
                        sender.sendMessage(CC.translate(message).replace("%player%", profile.getName()));
                    } else {
                        AtomicInteger id = new AtomicInteger(1);
                        profile.getNotes().forEach(note -> {
                            sender.sendMessage(CC.translate(Locale.NOTES_CHECK_FORMAT)
                                    .replace("%reason%", note.getAddedReason())
                                    .replace("%id%", String.valueOf(id.getAndIncrement()))
                                    .replace("%addedBy%", note.getAddedBy())
                                    .replace("%addedAt%", TimeUtil.formatIntoCalendarString(new Date(note.getAddedAt())))
                                    .replace("%addedServer%", note.getAddedServer()));
                        });
                    }
                });
            } else {
                new NotesMenu(profile).openMenu((Player) sender);
            }


        } else if(args.length == 2 && args[0].equalsIgnoreCase("clear")) {
            Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(args[1]);
            if (profile == null) {
                sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[1]));
                return;
            }

            if (profile.getNotes().size() == 0) {
                sender.sendMessage(CC.translate(Locale.NOTES_DONT_EXISTS));
                return;
            }

            sender.sendMessage(CC.translate(Locale.NOTES_CLEAR_CLEARED).replace("%player%", profile.getName()));


        }
        else if (args.length == 3 && args[0].equalsIgnoreCase("remove")) {
            Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(args[1]);
            if (profile == null) {
                sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[1]));
                return;
            }

            if (!this.checkNumber(sender, args[2])) return;
            int id = Math.abs(Integer.parseInt(args[2]));

            if (id > profile.getNotes().size()) {
                sender.sendMessage(CC.translate(Locale.NOTES_INVALID_ID));
                return;
            }

            sender.sendMessage(CC.translate(Locale.NOTES_REMOVE_REMOVED)
                    .replace("%id%", String.valueOf(id))
                    .replace("%player%", profile.getName()));

            profile.getNotes().remove(id - 1);
            profile.save();

        } else {
            if(!args[0].equalsIgnoreCase("add")) {
                Locale.NOTES_USAGE.forEach(s -> sender.sendMessage(CC.translate(s)));
                return;
            }

            if(args.length == 3) {
                Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(args[1]);
                if (profile == null) {
                    sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[1]));
                    return;
                }

                Note note = new Note();
                note.setUniqueId(UUID.randomUUID());
                note.setAddedAt(System.currentTimeMillis());
                note.setAddedBy(sender instanceof Player ? sender.getName() : "Console");
                note.setAddedReason(ChatUtil.joinArray(args, " ", 3));
                note.setAddedServer(XenonBukkit.getInstance().getServerConfig().getConfiguration().getString("server-name"));

                profile.getNotes().add(note);
                profile.save();

                sender.sendMessage(CC.translate(Locale.NOTES_ADD_ADDED)
                        .replace("%id%", String.valueOf(profile.getNotes().size()))
                        .replace("%player%", profile.getName()));
            }
        }
    }
}