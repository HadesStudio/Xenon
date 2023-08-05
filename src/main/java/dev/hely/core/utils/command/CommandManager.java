package dev.hely.core.utils.command;

import dev.hely.core.bukkit.commands.implement.player.NameMCCommand;
import dev.hely.core.bukkit.commands.implement.player.ProfileCommand;
import dev.hely.core.bukkit.commands.implement.player.SeenCommand;
import dev.hely.core.bukkit.commands.implement.punishment.*;
import dev.hely.core.bukkit.commands.implement.punishment.undo.UnBanCommand;
import dev.hely.core.bukkit.commands.implement.punishment.undo.UnBlacklistCommand;
import dev.hely.core.bukkit.commands.implement.punishment.undo.UnMuteCommand;
import dev.hely.core.bukkit.commands.implement.staff.AdminChatCommand;
import dev.hely.core.bukkit.commands.implement.staff.StaffChatCommand;
import dev.hely.core.bukkit.modules.ModuleLoader;
import dev.hely.core.global.profile.antivpn.VPNCommand;
import dev.hely.core.global.profile.commands.EditProfileCommand;
import dev.hely.core.global.profile.log.commands.LogsCommand;
import dev.hely.core.global.profile.profilesystem.commands.FriendsCommand;
import dev.hely.core.global.profile.profilesystem.commands.NotificationCommand;
import dev.hely.core.global.profile.profilesystem.commands.NotificationsCommand;
import dev.hely.core.global.ranks.rank.commands.RankCommand;
import dev.hely.core.bukkit.staff.requests.commands.HelpopCommand;
import dev.hely.core.bukkit.staff.requests.commands.ReportCommand;
import dev.hely.core.bukkit.staff.requests.commands.staff.RequestsCommand;
import dev.hely.core.global.tag.command.TagCommand;
import dev.hely.core.utils.command.utils.ClassWrapper;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.defaults.BukkitCommand;

public class CommandManager implements ModuleLoader {
    private CommandMap commandMap;
    private boolean enabled;

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void reload() {
    }

    @Override
    public void enable() {
        commandMap = getCommandMap();
        setupCommand();
    }

    @Override
    public void disable() {
        this.enabled = false;
    }

    @Override
    public String getName() {
        return "Command";
    }

    public void setupCommand() {
        registerCommand(new RankCommand());
        registerCommand(new TagCommand());

        registerCommand(new BlacklistCommand());
        registerCommand(new BanCommand());
        registerCommand(new TempBanCommand());
        registerCommand(new MuteCommand());
        registerCommand(new KickCommand());
        registerCommand(new WarnCommand());
        registerCommand(new UnBlacklistCommand());
        registerCommand(new UnBanCommand());
        registerCommand(new UnMuteCommand());
        registerCommand(new VPNCommand());
        registerCommand(new HistoryCommand());
        registerCommand(new HelpopCommand());
        registerCommand(new LogsCommand());
        registerCommand(new EditProfileCommand());
        registerCommand(new ProfileCommand());
        registerCommand(new RequestsCommand());
        registerCommand(new ReportCommand());
        registerCommand(new AdminChatCommand());
        registerCommand(new StaffChatCommand());
        registerCommand(new NameMCCommand());
        registerCommand(new SeenCommand());

        registerCommand(new FriendsCommand());
        registerCommand(new NotificationCommand());
        registerCommand(new NotificationsCommand());

    }


    private CommandMap getCommandMap() {
        return (CommandMap) new ClassWrapper(Bukkit.getServer()).getField("commandMap").get();
    }

    public void registerCommand(BukkitCommand command) {
        this.commandMap.register("oxygen", command);
    }
}
