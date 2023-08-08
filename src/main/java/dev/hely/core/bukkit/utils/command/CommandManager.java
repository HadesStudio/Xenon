package dev.hely.core.bukkit.utils.command;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.coins.commands.ShopCommand;
import dev.hely.core.bukkit.commands.implement.player.NameMCCommand;
import dev.hely.core.bukkit.commands.implement.player.ProfileCommand;
import dev.hely.core.bukkit.commands.implement.player.SeenCommand;
import dev.hely.core.bukkit.commands.implement.punishment.*;
import dev.hely.core.bukkit.commands.implement.punishment.undo.UnBanCommand;
import dev.hely.core.bukkit.commands.implement.punishment.undo.UnBlacklistCommand;
import dev.hely.core.bukkit.commands.implement.punishment.undo.UnMuteCommand;
import dev.hely.core.bukkit.commands.implement.staff.AdminChatCommand;
import dev.hely.core.bukkit.commands.implement.staff.StaffChatCommand;
import dev.hely.core.bukkit.commands.OxygenCommand;
import dev.hely.core.bukkit.modules.ModuleLoader;
import dev.hely.core.bukkit.commands.implement.staff.VPNCommand;
import dev.hely.core.bukkit.profile.commands.EditProfileCommand;
import dev.hely.core.bukkit.profile.log.commands.LogsCommand;
import dev.hely.core.bukkit.profile.profilesystem.commands.NotificationCommand;
import dev.hely.core.bukkit.profile.profilesystem.commands.NotificationsCommand;
import dev.hely.core.bukkit.ranks.grants.command.GrantsCommand;
import dev.hely.core.bukkit.server.command.ServersCommand;
import dev.hely.core.bukkit.staff.requests.commands.HelpopCommand;
import dev.hely.core.bukkit.staff.requests.commands.ReportCommand;
import dev.hely.core.bukkit.staff.requests.commands.staff.RequestsCommand;
import dev.hely.lib.CC;
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
    private OxygenBukkit plugin;
    private List<BaseCommand> loadedCommands;
    private List<BaseCommand> disabledCommands;

    @Override
    public void reload() {
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public void enable() {
        this.plugin = OxygenBukkit.getInstance();
        loadedCommands = new ArrayList<>();
        disabledCommands = new ArrayList<>();
        setupCommand();
    }

    @Override
    public void disable() {
        
    }

    @Override
    public String getName() {
        return "Command";
    }

    public void setupCommand() {
        loadedCommands.add(new OxygenCommand());
        loadedCommands.add(new BlacklistCommand());
        loadedCommands.add(new BanCommand());
        loadedCommands.add(new TempBanCommand());
        loadedCommands.add(new MuteCommand());
        loadedCommands.add(new KickCommand());
        loadedCommands.add(new WarnCommand());
        loadedCommands.add(new UnBlacklistCommand());
        loadedCommands.add(new UnBanCommand());
        loadedCommands.add(new UnMuteCommand());
        loadedCommands.add(new VPNCommand());
        loadedCommands.add(new HistoryCommand());
        loadedCommands.add(new GrantsCommand());
        loadedCommands.add(new HelpopCommand());
        loadedCommands.add(new LogsCommand());
        loadedCommands.add(new EditProfileCommand());
        loadedCommands.add(new ProfileCommand());
        loadedCommands.add(new RequestsCommand());
        loadedCommands.add(new ReportCommand());
        loadedCommands.add(new AdminChatCommand());
        loadedCommands.add(new StaffChatCommand());
        loadedCommands.add(new NameMCCommand());
        loadedCommands.add(new SeenCommand());
        loadedCommands.add(new NotificationCommand());
        loadedCommands.add(new NotificationsCommand());
        loadedCommands.add(new ServersCommand());
        loadedCommands.add(new ShopCommand());

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
