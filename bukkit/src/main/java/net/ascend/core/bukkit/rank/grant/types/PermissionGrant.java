package net.ascend.core.bukkit.rank.grant.types;

import net.ascend.core.bukkit.rank.grant.Grant;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PermissionGrant extends Grant {
    private final String permission;
    private boolean bungeecord;

    public PermissionGrant(UUID uuid, String permission, boolean bungeecord) {
        super(uuid);

        this.permission = permission;
        this.bungeecord = bungeecord;
    }
}

