package net.ascend.core.bukkit.rank.grant.menu;

import com.google.common.collect.Maps;
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.rank.grant.Grant;
import net.ascend.core.bukkit.rank.grant.menu.buttons.scopes.BukkitPermissionButton;
import net.ascend.core.bukkit.rank.grant.menu.buttons.scopes.BungeePermissionButton;
import net.ascend.core.bukkit.rank.grant.menu.buttons.scopes.GlobalRankButton;
import net.ascend.core.bukkit.rank.grant.menu.buttons.scopes.SpecifyServersButton;
import net.ascend.core.bukkit.rank.grant.types.PermissionGrant;
import net.ascend.core.bukkit.lib.menu.Menu;
import net.ascend.core.bukkit.lib.menu.button.Button;
import net.ascend.core.bukkit.lib.menu.button.implement.BackButton;
import net.ascend.core.bukkit.lib.menu.fill.FillerTemplate;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.Map;

@RequiredArgsConstructor
public class GrantScopesMenu extends Menu {
    private final Menu backMenu;
    private final Profile profile;
    private final Grant grant;

    @Override
    public String getTitle(Player player) {
        return XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.select-server.title");
    }

    @Override
    public FillerTemplate getFillerTemplate() {
        return FillerTemplate.FILL;
    }

    @Override
    public String getName() {
        return XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.select-server.title");
    }

    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();

        buttons.put(4, new BackButton(backMenu));

        if (grant instanceof PermissionGrant) {
            PermissionGrant permissionGrant = (PermissionGrant) grant;

            buttons.put(11, new BungeePermissionButton(profile, grant, permissionGrant));
            buttons.put(13, new BukkitPermissionButton(profile, grant, permissionGrant));
        } else {
            buttons.put(11, new GlobalRankButton(profile, grant));
        }


        buttons.put(15, new SpecifyServersButton(profile, grant));

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 3;
    }
}

