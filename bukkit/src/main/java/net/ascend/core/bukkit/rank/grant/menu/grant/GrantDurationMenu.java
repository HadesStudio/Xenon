package net.ascend.core.bukkit.rank.grant.menu.grant;

import com.google.common.collect.Maps;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.rank.grant.Grant;
import net.ascend.core.bukkit.rank.grant.menu.grant.duration.PermanentDurationButton;
import net.ascend.core.bukkit.rank.grant.menu.grant.duration.TemporaryDurationButton;
import net.ascend.core.bukkit.lib.menu.Menu;
import net.ascend.core.bukkit.lib.menu.button.Button;
import net.ascend.core.bukkit.lib.menu.button.implement.BackButton;
import net.ascend.core.bukkit.lib.menu.fill.FillerTemplate;
import lombok.RequiredArgsConstructor;
import net.ascend.core.bukkit.server.Server;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class GrantDurationMenu extends Menu {

    private final Menu backMenu;
    private final Profile profile;
    private final Grant grant;
    private final List<Server> servers;

    @Override
    public String getName() {
        return "Select Duration";
    }

    @Override
    public String getTitle(Player player) {
        return "Select Duration";
    }

    @Override
    public FillerTemplate getFillerTemplate() {
        return FillerTemplate.FILL;
    }


    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();

        buttons.put(4, new BackButton(new GrantScopesMenu(backMenu, profile, grant)));

        buttons.put(11, new PermanentDurationButton(profile, grant, servers));
        buttons.put(15, new TemporaryDurationButton(profile, grant, servers));

        return buttons;
    }

    @Override
    public int getSize() {
        return 27;
    }
}