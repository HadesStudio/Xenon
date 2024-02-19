package net.ascend.core.bukkit.profile.note;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.util.TimeUtil;
import net.ascend.core.bukkit.lib.Messages;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created By LeandroSSJ
 * Created on 06/07/2023
 * Protect Name Xenon
 */
public class NoteListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(event.getPlayer().getUniqueId());
        if (profile != null) {
            if (profile.getNotes().size() > 0) {
                Locale.NOTES_CHECK_MESSAGE.forEach(message -> {
                    if (!message.toLowerCase().contains("%notes%")) {
                        Messages.sendMessage(message.replace("%player%", profile.getName()), "xenon.staff.notes");
                    } else {
                        AtomicInteger id = new AtomicInteger(1);
                        profile.getNotes().forEach(note -> {
                            Messages.sendMessage(Locale.NOTES_CHECK_FORMAT
                                    .replace("%reason%", note.getAddedReason())
                                    .replace("%id%", String.valueOf(id.getAndIncrement()))
                                    .replace("%addedBy%", note.getAddedBy())
                                    .replace("%addedAt%", TimeUtil.formatIntoCalendarString(new Date(note.getAddedAt())))
                                    .replace("%addedServer%", note.getAddedServer()), "xenon.staff.notes");
                        });
                    }
                });
            }
        }
    }
}
