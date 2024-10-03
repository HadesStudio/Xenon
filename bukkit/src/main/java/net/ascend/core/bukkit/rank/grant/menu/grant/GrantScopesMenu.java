package net.ascend.core.bukkit.rank.grant.menu.grant;

import com.google.common.collect.Maps;
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.rank.grant.Grant;
import net.ascend.core.bukkit.rank.grant.menu.grant.scopes.GlobalRankButton;
import net.ascend.core.bukkit.rank.grant.menu.grant.scopes.SpecifyServersButton;
import net.ascend.core.bukkit.lib.menu.Menu;
import net.ascend.core.bukkit.lib.menu.button.Button;
import net.ascend.core.bukkit.lib.menu.button.implement.BackButton;
import net.ascend.core.bukkit.lib.menu.fill.FillerTemplate;
import lombok.RequiredArgsConstructor;
import net.ascend.core.bukkit.server.Server;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class GrantScopesMenu extends Menu {

    private final Menu backMenu;
    private final Profile profile;
    private final Grant grant;

    {
        setClickUpdate(true);
    }

    @Override
    public String getName() {
        return "Select Server Type";
    }

    @Override
    public String getTitle(Player player) {
        return "Select Server Type";
    }

    @Override
    public FillerTemplate getFillerTemplate() {
        return FillerTemplate.FILL;
    }

    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();
        buttons.put(0, new BackButton(backMenu));

        buttons.put(3, new GlobalRankButton(profile, grant));

        List<Server> serverList = new ArrayList<>(XenonBukkit.getInstance().getServerManager().getServers());
        buttons.put(5, new SpecifyServersButton(profile, grant, serverList));

        return buttons;
    }

    @Override
    public int getSize() {
        return 9;
    }
}

