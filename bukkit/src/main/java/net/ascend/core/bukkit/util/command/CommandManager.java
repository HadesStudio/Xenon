package net.ascend.core.bukkit.util.command;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.commands.implement.player.*;
import net.ascend.core.bukkit.commands.implement.player.time.DayCommand;
import net.ascend.core.bukkit.commands.implement.player.time.NightCommand;
import net.ascend.core.bukkit.commands.implement.player.time.SunSetCommand;
import net.ascend.core.bukkit.commands.implement.player.toggle.ToggleMessagesCommand;
import net.ascend.core.bukkit.commands.implement.player.toggle.ToggleSoundsCommand;
import net.ascend.core.bukkit.commands.implement.punishment.*;
import net.ascend.core.bukkit.commands.implement.punishment.undo.UnBanCommand;
import net.ascend.core.bukkit.commands.implement.punishment.undo.UnBlacklistCommand;
import net.ascend.core.bukkit.commands.implement.punishment.undo.UnMuteCommand;
import net.ascend.core.bukkit.commands.implement.staff.*;
import net.ascend.core.bukkit.commands.implement.staff.gamemodes.GamemodeAdventureCommand;
import net.ascend.core.bukkit.commands.implement.staff.gamemodes.GamemodeCreativeCommand;
import net.ascend.core.bukkit.commands.implement.staff.gamemodes.GamemodeSurvivalCommand;
import net.ascend.core.bukkit.commands.implement.staff.teleport.TeleportAllCommand;
import net.ascend.core.bukkit.commands.implement.staff.teleport.TeleportCommand;
import net.ascend.core.bukkit.commands.implement.staff.teleport.TeleportHereCommand;
import net.ascend.core.bukkit.commands.implement.staff.teleport.TeleportPossCommand;
import net.ascend.core.bukkit.filter.commands.FilterCommand;
import net.ascend.core.bukkit.profile.note.command.NotesCommand;
import net.ascend.core.bukkit.rank.grant.commands.GrantCommand;
import net.ascend.core.bukkit.rank.grant.commands.GrantsCommand;
import net.ascend.core.bukkit.server.commands.RunCommandCommand;
import net.ascend.core.bukkit.server.commands.SetSlotsCommand;
import net.ascend.core.bukkit.commands.OxygenCommand;
import net.ascend.core.bukkit.modules.ModuleLoader;
import net.ascend.core.bukkit.profile.commands.EditProfileCommand;
import net.ascend.core.bukkit.profile.log.commands.LogsCommand;
import net.ascend.core.bukkit.profile.profilesystem.commands.NotificationCommand;
import net.ascend.core.bukkit.profile.profilesystem.commands.NotificationsCommand;
import net.ascend.core.bukkit.profile.tfa.commands.AuthCommand;
import net.ascend.core.bukkit.profile.tfa.commands.RemoveAuthCommand;
import net.ascend.core.bukkit.server.queue.commands.*;
import net.ascend.core.bukkit.server.reboot.commands.RebootCommand;
import net.ascend.core.bukkit.staff.requests.commands.HelpopCommand;
import net.ascend.core.bukkit.staff.requests.commands.ReportCommand;
import net.ascend.core.bukkit.staff.requests.commands.staff.RequestsCommand;
import net.ascend.core.bukkit.vip.commands.VIPCommand;
import net.ascend.core.bukkit.vip.commands.VIPResetCommand;
import net.ascend.core.bukkit.lib.CC;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Getter
@Setter
public class CommandManager implements ModuleLoader {

    private XenonBukkit plugin;

    private List<BaseCommand> loadedCommands;
    private List<BaseCommand> disabledCommands;

    @Override
    public void onEnable() {
        this.plugin = XenonBukkit.getInstance();
        loadedCommands = new ArrayList<>();
        disabledCommands = new ArrayList<>();
        setupCommand();
    }

    @Override
    public void onDisable() {
        
    }

    @Override
    public void onReload() {

    }

    public void setupCommand() {
        loadedCommands.add(new OxygenCommand());

        loadedCommands.add(new VPNCommand());

        loadedCommands.add(new AdminChatCommand());
        loadedCommands.add(new StaffChatCommand());
        loadedCommands.add(new SeenCommand());

        loadedCommands.add(new AuthCommand());
        loadedCommands.add(new RemoveAuthCommand());
        loadedCommands.add(new GamemodeAdventureCommand());
        loadedCommands.add(new GamemodeCreativeCommand());
        loadedCommands.add(new GamemodeSurvivalCommand());
        loadedCommands.add(new NotesCommand());
        loadedCommands.add(new HideStaffCommand());
        loadedCommands.add(new StaffBuildCommand());
        loadedCommands.add(new StaffModeCommand());
        loadedCommands.add(new StaffHistoryCommand());
        loadedCommands.add(new RunCommandCommand());
        loadedCommands.add(new TeleportCommand());
        loadedCommands.add(new TeleportAllCommand());
        loadedCommands.add(new TeleportHereCommand());
        loadedCommands.add(new TeleportPossCommand());
        loadedCommands.add(new VanishCommand());

        loadedCommands.add(new SetSlotsCommand());
        loadedCommands.add(new HealCommand());
        loadedCommands.add(new FeedCommand());
        loadedCommands.add(new FreezeCommand());
        loadedCommands.add(new PlaytimeCommand());

        loadedCommands.add(new DayCommand());
        loadedCommands.add(new NightCommand());
        loadedCommands.add(new SunSetCommand());

        loadedCommands.add(new MessageCommand());
        loadedCommands.add(new ReplyCommand());
        loadedCommands.add(new ToggleMessagesCommand());
        loadedCommands.add(new ToggleSoundsCommand());

        loadedCommands.add(new NotificationCommand());
        loadedCommands.add(new NotificationsCommand());

        loadedCommands.add(new ProfileCommand());
        loadedCommands.add(new EditProfileCommand());
        loadedCommands.add(new HistoryCommand());
        loadedCommands.add(new LogsCommand());

        loadedCommands.add(new NameMCCommand());
        loadedCommands.add(new FilterCommand());

        loadedCommands.add(new QueueListCommand());
        loadedCommands.add(new JoinQueueCommand());
        loadedCommands.add(new LeaveQueueCommand());

        loadedCommands.add(new BlacklistCommand());
        loadedCommands.add(new BanCommand());
        loadedCommands.add(new TempBanCommand());
        loadedCommands.add(new MuteCommand());
        loadedCommands.add(new KickCommand());
        loadedCommands.add(new WarnCommand());

        loadedCommands.add(new UnBlacklistCommand());
        loadedCommands.add(new UnBanCommand());
        loadedCommands.add(new UnMuteCommand());

        loadedCommands.add(new AltsCommand());

        loadedCommands.add(new RequestsCommand());
        loadedCommands.add(new ReportCommand());
        loadedCommands.add(new HelpopCommand());
        loadedCommands.add(new RebootCommand());
        loadedCommands.add(new QueueToggleCommand());
        loadedCommands.add(new QueuePauseCommand());
        loadedCommands.add(new VIPCommand());
        loadedCommands.add(new VIPResetCommand());
        loadedCommands.add(new GrantsCommand());
        loadedCommands.add(new GrantCommand());

        loadedCommands.forEach(this::registerCommand);

        Bukkit.getConsoleSender().sendMessage(CC.translate("&aLoading " + loadedCommands.size() + " commands.."));
    }


    public void registerCommand(BukkitCommand command) {
        if (!plugin.getConfiguration().getConfiguration().getStringList("command-system.disabled-commands").contains(command.getName())) {
            if (this.getCommandMap() != null) {
                this.getCommandMap().register(plugin.getDescription().getName(), command);
            }
            this.disabledCommands.removeIf(baseCommand -> baseCommand.equals(command));
        } else {
            this.disabledCommands.add((BaseCommand) command);
        }
    }

    public void unregisterCommand(BaseCommand bukkitCommand) {
        this.loadedCommands.removeIf(baseCommand -> baseCommand.equals(bukkitCommand));

        if (this.getCommandMap() != null) {
            Command command = this.getCommandMap().getCommand(bukkitCommand.getName());

            if (command != null) {
                try {
                    Field field = getCommandMap().getClass().getDeclaredField("knownCommands");
                    field.setAccessible(true);

                    Map<String, Command> commands = (Map<String, Command>) field.get(getCommandMap());
                    commands.remove(command.getName().toLowerCase(Locale.ENGLISH).trim());
                    command.getAliases().forEach(alias -> {
                        commands.remove(alias.toLowerCase(Locale.ENGLISH).trim());
                    });

                    field.set(getCommandMap(), commands);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        this.disabledCommands.add(bukkitCommand);
    }

    public boolean isDisabled(BaseCommand baseCommand) {
        return this.disabledCommands.contains(baseCommand);
    }

    private CommandMap getCommandMap() {
        PluginManager manager = plugin.getServer().getPluginManager();
        try {
            Field field = SimplePluginManager.class.getDeclaredField("commandMap");
            field.setAccessible(true);
            return (CommandMap) field.get(manager);
        } catch (IllegalArgumentException | SecurityException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
