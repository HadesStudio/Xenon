package dev.hely.core.utils.command;

import dev.hely.core.modules.ModuleLoader;
import dev.hely.core.ranks.rank.commands.RankCommand;
import dev.hely.core.utils.command.utils.ClassWrapper;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.defaults.BukkitCommand;

public class CommandManager implements ModuleLoader {
    private CommandMap commandMap;
    public static final String MODULE_NAME = "Command Manager";
    private boolean enabled;

    public boolean isEnabled() {
        return this.enabled;
    }


    public void reload() {
    }

    public void enable() {
        commandMap = getCommandMap();
        setupCommand();
    }

    public void disable() {
        this.enabled = false;
    }

    public String getName() {
        return "Command Manager";
    }

    public void setupCommand() {
        registerCommand(new RankCommand());
    }


    private CommandMap getCommandMap() {
        return (CommandMap) new ClassWrapper(Bukkit.getServer()).getField("commandMap").get();
    }

    public void registerCommand(BukkitCommand command) {
        this.commandMap.register("Oxygen", command);
    }
}
