package dev.hely.core.bukkit.configuration;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.lib.configuration.Config;
import dev.hely.core.bukkit.punishment.broadcast.PunishmentBroadcast;
import dev.hely.core.bukkit.utils.ChatUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created By LeandroSSJ
 * Created on 07/07/2023
 * Protect Name OxygenGlobal
 */
public class Locale {

    public static String COMMANDS_NO_PERMISSION;
    public static String COMMANDS_ONLY_PLAYER;
    public static String COMMANDS_ONLY_CONSOLE;
    public static String COMMANDS_ARGUMENT_NOT_FOUND;
    public static String COMMANDS_PLAYER_NEVER_PLAYED;
    public static String COMMANDS_OFFLINE_PLAYER;
    public static String COMMANDS_INVALID_NUMBER;

    public static List<String> TAG_USAGE;
    public static String TAG_CREATE_USAGE;
    public static String TAG_CREATE_ALREADY_EXIST;
    public static String TAG_CREATE_CREATED;
    public static String TAG_REMOVE_USAGE;
    public static String TAG_DONT_EXISTS;
    public static String TAG_REMOVE_REMOVED;
    public static String TAG_SETPREFIX_USAGE;
    public static String TAG_SETPREFIX_UPDATED;
    public static String TAG_SETWEIGHT_USAGE;
    public static String TAG_SETWEIGHT_UPDATED;
    public static String TAG_IMPORT_IMPORTED;
    public static String TAG_EXPORT_EXPORTED;

    public static String ADMIN_CHAT_ENABLED;
    public static String ADMIN_CHAT_DISABLED;
    public static String ADMIN_CHAT_FORMAT;

    public static String STAFF_CHAT_ENABLED;
    public static String STAFF_CHAT_DISABLED;
    public static String STAFF_CHAT_FORMAT;

    public static String STAFF_MESSAGES_CONNECT;
    public static String STAFF_MESSAGES_DISCONNECT;
    public static String STAFF_MESSAGES_SWITCH;

    public static Map<String, PunishmentBroadcast> PUNISHMENT_BROADCAST;
    public static Map<String, String> PUNISHMENT_KICK;

    public static String PUNISHMENTS_MUTED_PERMANENT;
    public static String PUNISHMENTS_MUTED_TEMP;
    public static String PUNISHMENTS_WARNED_PERMANENT;
    public static String PUNISHMENTS_WARNED_TEMP;

    public static String PUNISHMENTS_DEFAULT_REASON;
    public static boolean PUNISHMENTS_JOIN_ALLOW_BANNED;
    public static boolean PUNISHMENTS_JOIN_ALLOW_BLACKLISTED;

    public static String PUNISHMENT_BLACKLIST_USAGE;
    public static String PUNISHMENT_BLACKLIST_ALREADY_BLACKLISTED;
    public static String PUNISHMENT_BAN_USAGE;
    public static String PUNISHMENT_BAN_ALREADY_BANNED;
    public static String PUNISHMENT_TEMPBAN_USAGE;
    public static String PUNISHMENT_TEMPBAN_ALREADY_BANNED;
    public static String PUNISHMENT_MUTE_USAGE;
    public static String PUNISHMENT_MUTE_ALREADY_MUTED;
    public static String PUNISHMENT_WARN_USAGE;
    public static String PUNISHMENT_KICK_USAGE;
    public static String PUNISHMENT_UNBLACKLIST_USAGE;
    public static String PUNISHMENT_UNBLACKLIST_NOT_BLACKLISTED;
    public static String PUNISHMENT_UNBAN_USAGE;
    public static String PUNISHMENT_UNBAN_NOT_BANNED;
    public static String PUNISHMENT_UNMUTE_USAGE;
    public static String PUNISHMENT_UNMUTE_NOT_MUTED;

    public static String HISTORY_USAGE;

    public static String REQUEST_INVALID_MESSAGE;
    public static String HELPOP_USAGE;
    public static String HELPOP_STAFF_ALERT;
    public static String HELPOP_CREATED;
    public static String REQUEST_CLOSED;
    public static String REQUEST_PLAYER_ALERT_CLOSED;

    public static String REQUEST_CLAIMED;
    public static String REQUEST_CLAIMED_BROADCAST;
    public static String REQUEST_UNCLAIMED;
    public static String REQUEST_ALREADY_CLAIMED;

    public static String REPORT_USAGE;
    public static String REPORT_STAFF_ALERT;
    public static String REPORT_CREATED;
    public static String REPORT_NOT_ONLINE;
    public static String REQUEST_NOTE_PROMT;
    public static String REQUEST_NOTE_ADDED;
    public static String REQUEST_CLOSED_BROADCAST;
    public static String EDITPROFILE_USAGE;

    public static List<String> NOTES_USAGE;
    public static String NOTES_ADD_ADDED;
    public static String NOTES_REMOVE_REMOVED;
    public static String NOTES_CLEAR_CLEARED;
    public static String NOTES_DONT_EXISTS;
    public static String NOTES_INVALID_ID;
    public static List<String> NOTES_CHECK_MESSAGE;
    public static String NOTES_CHECK_FORMAT;

    public static String DAY_SET;
    public static String NIGHT_SET;
    public static String SUNSET_SET;

    public static String SOCIAL_SPY_FORMAT;

    public static void load(){
        Config language = OxygenBukkit.getInstance().getLanguageConfig();
        Config config = OxygenBukkit.getInstance().getConfiguration();

        SOCIAL_SPY_FORMAT = language.getConfiguration().getString("social-spy.format");


        DAY_SET = language.getConfiguration().getString("day-command.set");
        NIGHT_SET = language.getConfiguration().getString("night-command.set");
        SUNSET_SET = language.getConfiguration().getString("sunset-command.set");

        NOTES_CHECK_FORMAT = language.getConfiguration().getString("notes-command.check.format");
        NOTES_CHECK_MESSAGE = language.getConfiguration().getStringList("notes-command.check.message");
        NOTES_INVALID_ID = language.getConfiguration().getString("notes-command.invalid-id");
        NOTES_DONT_EXISTS = language.getConfiguration().getString("notes-command.dont-exists");
        NOTES_CLEAR_CLEARED = language.getConfiguration().getString("notes-command.clear.cleared");
        NOTES_REMOVE_REMOVED = language.getConfiguration().getString("notes-command.remove.removed");
        NOTES_ADD_ADDED = language.getConfiguration().getString("notes-command.add.added");
        NOTES_USAGE = language.getConfiguration().getStringList("notes-command.usage");

        EDITPROFILE_USAGE = language.getConfiguration().getString("edit-profile.usage");
        REQUEST_CLOSED_BROADCAST = language.getConfiguration().getString("requests.closed-broadcast");
        REQUEST_UNCLAIMED = language.getConfiguration().getString("requests.claim.unclaimed");
        REQUEST_ALREADY_CLAIMED = language.getConfiguration().getString("requests.claim.already-claimed");

        REQUEST_NOTE_PROMT = language.getConfiguration().getString("requests.type-note");
        REQUEST_NOTE_ADDED = language.getConfiguration().getString("requests.note-added");


        REPORT_USAGE = language.getConfiguration().getString("requests.report.usage");
        REPORT_STAFF_ALERT = language.getConfiguration().getString("requests.report.broadcast");
        REPORT_CREATED = language.getConfiguration().getString("requests.report.created");
        REPORT_NOT_ONLINE = language.getConfiguration().getString("requests.report.not-online");

        REQUEST_INVALID_MESSAGE = language.getConfiguration().getString("requests.invalid-message");
        HELPOP_USAGE = language.getConfiguration().getString("requests.helpop.usage");
        HELPOP_STAFF_ALERT = language.getConfiguration().getString("requests.helpop.broadcast");
        HELPOP_CREATED = language.getConfiguration().getString("requests.helpop.created");
        REQUEST_CLOSED = language.getConfiguration().getString("requests.closed");
        REQUEST_PLAYER_ALERT_CLOSED = language.getConfiguration().getString("requests.closed-player");
        REQUEST_CLAIMED = language.getConfiguration().getString("requests.claim.claimed");
        REQUEST_CLAIMED_BROADCAST = language.getConfiguration().getString("requests.claim.broadcast");


        HISTORY_USAGE = language.getConfiguration().getString("history.usage");

        COMMANDS_NO_PERMISSION = language.getConfiguration().getString("commands.no-permission");
        COMMANDS_ONLY_PLAYER = language.getConfiguration().getString("commands.only-player");
        COMMANDS_ONLY_CONSOLE = language.getConfiguration().getString("commands.only-console");
        COMMANDS_ARGUMENT_NOT_FOUND = language.getConfiguration().getString("commands.argument-not-found");
        COMMANDS_PLAYER_NEVER_PLAYED = language.getConfiguration().getString("commands.never-played");
        COMMANDS_OFFLINE_PLAYER = language.getConfiguration().getString("commands.offline-player");
        COMMANDS_INVALID_NUMBER = language.getConfiguration().getString("commands.invalid-integer");

        PUNISHMENT_BLACKLIST_USAGE = language.getConfiguration().getString("blacklist.usage");
        PUNISHMENT_BLACKLIST_ALREADY_BLACKLISTED = language.getConfiguration().getString("blacklist.already-blacklisted");

        PUNISHMENT_BAN_USAGE = language.getConfiguration().getString("ban.usage");
        PUNISHMENT_BAN_ALREADY_BANNED = language.getConfiguration().getString("ban.already-banned");

        PUNISHMENT_TEMPBAN_USAGE = language.getConfiguration().getString("temp-ban.usage");
        PUNISHMENT_TEMPBAN_ALREADY_BANNED = language.getConfiguration().getString("ban.already-banned");

        PUNISHMENT_MUTE_USAGE = language.getConfiguration().getString("mute.usage");
        PUNISHMENT_MUTE_ALREADY_MUTED = language.getConfiguration().getString("mute.already-muted");

        PUNISHMENT_WARN_USAGE = language.getConfiguration().getString("warn.usage");
        PUNISHMENT_KICK_USAGE = language.getConfiguration().getString("kick.usage");

        PUNISHMENT_UNBLACKLIST_USAGE = language.getConfiguration().getString("un-blacklist.usage");
        PUNISHMENT_UNBLACKLIST_NOT_BLACKLISTED = language.getConfiguration().getString("un-blacklist.not-blacklisted");

        PUNISHMENT_UNBAN_USAGE = language.getConfiguration().getString("un-ban.usage");
        PUNISHMENT_UNBAN_NOT_BANNED = language.getConfiguration().getString("un-ban.not-banned");

        PUNISHMENT_UNMUTE_USAGE = language.getConfiguration().getString("un-mute.usage");
        PUNISHMENT_UNMUTE_NOT_MUTED = language.getConfiguration().getString("un-mute.not-muted");

        PUNISHMENTS_JOIN_ALLOW_BANNED = config.getConfiguration().getBoolean("punishments.join.allow-banned");
        PUNISHMENTS_JOIN_ALLOW_BLACKLISTED = config.getConfiguration().getBoolean("punishments.join.allow-blacklisted");

        PUNISHMENTS_DEFAULT_REASON = config.getConfiguration().getString("punishments.default-reason");

        PUNISHMENTS_MUTED_PERMANENT = language.getConfiguration().getString("punishments.muted.permanent");
        PUNISHMENTS_MUTED_TEMP = language.getConfiguration().getString("punishments.muted.temp");
        PUNISHMENTS_WARNED_PERMANENT = language.getConfiguration().getString("punishments.warned.permanent");
        PUNISHMENTS_WARNED_TEMP = language.getConfiguration().getString("punishments.warned.temp");

        PUNISHMENT_BROADCAST = new HashMap<>();

        language.getConfiguration().getConfigurationSection("punishments.broadcast").getKeys(false).forEach(key -> {
            String broadcast = language.getConfiguration().getString("punishments.broadcast." + key + ".message");
            String hoverMessage = ChatUtil.buildDescription(language.getConfiguration().getStringList("punishments.broadcast." + key + ".hover.message")).replace(", ", "\n");
            boolean hover = language.getConfiguration().getBoolean("punishments.broadcast." + key + ".hover.enabled");

            PUNISHMENT_BROADCAST.put(key, new PunishmentBroadcast(broadcast, hover, hoverMessage));
        });

        PUNISHMENT_KICK = new HashMap<>();

        language.getConfiguration().getConfigurationSection("punishments.kick").getKeys(false).forEach(key -> {
            PUNISHMENT_KICK.put(key, ChatUtil.buildDescription(language.getConfiguration().getStringList("punishments.kick." + key)));
        });


        TAG_USAGE = language.getConfiguration().getStringList("tag.usage");
        TAG_DONT_EXISTS = language.getConfiguration().getString("tag.dont-exists");
        TAG_CREATE_USAGE = language.getConfiguration().getString("tag.create.usage");
        TAG_CREATE_ALREADY_EXIST = language.getConfiguration().getString("tag.create.already-exist");
        TAG_CREATE_CREATED = language.getConfiguration().getString("tag.create.created");
        TAG_REMOVE_USAGE = language.getConfiguration().getString("tag.remove.usage");
        TAG_REMOVE_REMOVED = language.getConfiguration().getString("tag.remove.removed");
        TAG_SETPREFIX_USAGE = language.getConfiguration().getString("tag.setprefix.usage");
        TAG_SETPREFIX_UPDATED = language.getConfiguration().getString("tag.setprefix.updated");
        TAG_SETWEIGHT_USAGE = language.getConfiguration().getString("tag.setweight.usage");
        TAG_SETWEIGHT_UPDATED = language.getConfiguration().getString("tag.setweight.updated");
        TAG_IMPORT_IMPORTED = language.getConfiguration().getString("tag.import.imported");
        TAG_EXPORT_EXPORTED = language.getConfiguration().getString("tag.export.exported");

        ADMIN_CHAT_ENABLED = language.getConfiguration().getString("admin-chat.enabled");
        ADMIN_CHAT_DISABLED = language.getConfiguration().getString("admin-chat.disabled");
        ADMIN_CHAT_FORMAT = language.getConfiguration().getString("admin-chat.format");

        STAFF_CHAT_ENABLED = language.getConfiguration().getString("staff-chat.enabled");
        STAFF_CHAT_DISABLED = language.getConfiguration().getString("staff-chat.disabled");
        STAFF_CHAT_FORMAT = language.getConfiguration().getString("staff-chat.format");

        STAFF_MESSAGES_CONNECT = language.getConfiguration().getString("staff-messages.connect");
        STAFF_MESSAGES_SWITCH = language.getConfiguration().getString("staff-messages.switch");
        STAFF_MESSAGES_DISCONNECT = language.getConfiguration().getString("staff-messages.disconnect");
    }
}
