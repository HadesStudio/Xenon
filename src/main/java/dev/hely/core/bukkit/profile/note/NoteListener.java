package dev.hely.core.bukkit.profile.note;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.utils.TimeUtil;
import dev.hely.lib.CC;
import dev.hely.lib.Messages;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created By LeandroSSJ
 * Created on 06/07/2023
 * Protect Name OxygenGlobal
 */
public class NoteListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(event.getPlayer().getUniqueId());
        if (profile != null) {
            if (profile.getNotes().size() > 0) {
                Locale.NOTES_CHECK_MESSAGE.forEach(message -> {
                    if (!message.toLowerCase().contains("%notes%")) {
                        Messages.sendMessage(message.replace("%player%", profile.getName()), "oxygen.staff.notes");
                    } else {
                        AtomicInteger id = new AtomicInteger(1);
                        profile.getNotes().forEach(note -> {
                            Messages.sendMessage(Locale.NOTES_CHECK_FORMAT
                                    .replace("%reason%", note.getAddedReason())
                                    .replace("%id%", String.valueOf(id.getAndIncrement()))
                                    .replace("%addedBy%", note.getAddedBy())
                                    .replace("%addedAt%", TimeUtil.formatIntoCalendarString(new Date(note.getAddedAt())))
                                    .replace("%addedServer%", note.getAddedServer()), "oxygen.staff.notes");
                        });
                    }
                });
            }
        }
    }
}
