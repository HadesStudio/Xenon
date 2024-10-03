package net.ascend.core.bukkit.server;

import net.ascend.core.bukkit.lib.CC;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServerState {

    ONLINE("Online", CC.translate("&aOnline")),
    OFFLINE("Offline", CC.translate("&cOffline")),
    WHITELISTED("Whitelisted", CC.translate("&dWhitelisted"));

    private final String name;
    private final String displayName;

}
