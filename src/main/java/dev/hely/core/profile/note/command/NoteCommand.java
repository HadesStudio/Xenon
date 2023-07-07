package dev.hely.core.profile.note.command;

import dev.hely.core.Oxygen;
import dev.hely.core.profile.Profile;
import dev.hely.core.profile.note.Note;
import dev.hely.core.utils.ChatUtil;
import dev.hely.core.utils.command.BaseCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created By LeandroSSJ
 * Created on 06/07/2023
 * Protect Name Oxygen
 */
public class NoteCommand extends BaseCommand {

    public NoteCommand() {
        super("notes", "oxygen.command.notes");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 2) {
            // USAGE NOTES
            return;
        }

        if(args.length == 2 && args[0].equalsIgnoreCase("check")) {
            Profile profile = Oxygen.getInstance().getProfileManager().getProfile(args[1]);

            if (profile == null) {
                return;
            }

            if (profile.getNotes().size() == 0) {
                // NOTES EMPTY
                return;
            }


        } else if(args.length == 3 && args[0].equalsIgnoreCase("remove")) {
            Profile profile = Oxygen.getInstance().getProfileManager().getProfile(args[1]);

            if (profile == null) {
                return;
            }

            int id = Integer.parseInt(args[2]);
            if (id <= 0) {
                // USE POSITIVE NUMBERS
                return;
            }

            if (id > profile.getNotes().size()) {
                // INVALID ID
                return;
            }
            profile.getNotes().remove(id - 1);
            profile.save();

        } else {
            if(!args[0].equalsIgnoreCase("add")) {
                // USAGE
                return;
            }

            if(args.length == 3) {
                Profile profile = Oxygen.getInstance().getProfileManager().getProfile(args[1]);

                if (profile == null) {
                    return;
                }

                Note note = new Note();
                note.setUniqueId(UUID.randomUUID());
                note.setAddedAt(System.currentTimeMillis());
                note.setAddedBy(sender instanceof Player ? sender.getName() : "Console");
                note.setAddedReason(ChatUtil.joinArray(args, " ", 3));
                note.setAddedServer(Oxygen.getInstance().getServerManager().getServerName());

                profile.getNotes().add(note);
                profile.save();
            }
        }
    }
}