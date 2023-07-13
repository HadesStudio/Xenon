package dev.hely.core.utils.command;

import dev.hely.core.commands.implement.staff.AdminChatCommand;
import dev.hely.core.commands.implement.staff.StaffChatCommand;
import dev.hely.core.modules.ModuleLoader;
import dev.hely.core.ranks.rank.commands.RankCommand;
import dev.hely.core.tag.command.TagCommand;
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

        registerCommand(new AdminChatCommand());
        registerCommand(new StaffChatCommand());
    }


    private CommandMap getCommandMap() {
        return (CommandMap) new ClassWrapper(Bukkit.getServer()).getField("commandMap").get();
    }

    public void registerCommand(BukkitCommand command) {
        this.commandMap.register("oxygen", command);
    }
}
