package net.ascend.core.bukkit.staff.request.menu.submenus;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.lib.menu.button.implement.BackButton;
import net.ascend.core.bukkit.staff.request.Request;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.button.Button;
import net.ascend.core.bukkit.lib.menu.button.implement.InformationButton;
import net.ascend.core.bukkit.lib.menu.button.implement.PageButton;
import net.ascend.core.bukkit.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import net.ascend.core.bukkit.staff.request.RequestType;
import net.ascend.core.bukkit.staff.request.menu.RequestsMenu;
import net.ascend.core.bukkit.util.TimeUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClosedRequestsMenu extends PaginatedMenu {

    {
        setClickUpdate(true);
    }

    @Override
    public String getName() {
        return XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("closed-requests-menu.title");
    }

    @Override
    public String getTitle(Player player) {
        return XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("closed-requests-menu.title");
    }

    @Override
    public int getSize() {
        return 36;
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (Request request : XenonBukkit.getInstance().getRequestHandler().getRequests()) {
            if (request.isClosed()) {
                buttons.put(buttons.size(), new Requests_Button(request));
            }
        }

        return buttons;
    }

    public Map<Integer, Button> getGlobalContent(Player player) {
        Map<Integer, Button> menuContent = new HashMap<>();

        menuContent.put(0, new BackButton(new RequestsMenu()));
        menuContent.put(1, Button.getPlaceholder());
        menuContent.put(2, Button.getPlaceholder());

        menuContent.put(3, Button.getPlaceholder());
        menuContent.put(4, Button.getPlaceholder());
        menuContent.put(5, Button.getPlaceholder());

        menuContent.put(6, Button.getPlaceholder());
        menuContent.put(7, Button.getPlaceholder());
        menuContent.put(8, Button.getPlaceholder());

        menuContent.put(27, new PageButton(-1, this));
        menuContent.put(28, Button.getPlaceholder());
        menuContent.put(29, Button.getPlaceholder());

        menuContent.put(30, Button.getPlaceholder());
        menuContent.put(31, new InformationButton(this));
        menuContent.put(32, Button.getPlaceholder());

        menuContent.put(33, Button.getPlaceholder());
        menuContent.put(34, Button.getPlaceholder());
        menuContent.put(35, new PageButton(1, this));

        return menuContent;
    }

    @AllArgsConstructor
    public static class Requests_Button extends Button {

        private final Request request;

        public String getMaterial() {
            return XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("closed-requests-menu.buttons.requests.material");
        }

        public List<String> getLore() {
            if (request.getRequestType() == RequestType.REQUEST) {
                if (request.isClaimed()) {
                    return XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getStringList("closed-requests-menu.buttons.requests.lores.requests.claimed_lore");
                } else {
                    return XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getStringList("closed-requests-menu.buttons.requests.lores.requests.default_lore");
                }
            } else {
                if (request.isClaimed()) {
                    return XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getStringList("closed-requests-menu.buttons.requests.lores.report.claimed_lore");
                } else {
                    return XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getStringList("closed-requests-menu.buttons.requests.lores.report.default_lore");
                }
            }
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getInt("closed-requests-menu.buttons.requests.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("closed-requests-menu.buttons.requests.display_name")
                    .replace("%request_player%", request.getCreatorPlayerName())
                    .replace("%request_identifier%", String.valueOf(request.getIdentifier()))
            );

            for (String line : getLore()) {
                builder.addLore(line
                        .replace("%message%", request.getMessage())
                        .replace("%player_claimed%", request.getClaimedStaffName())
                        .replace("%notes_size%", String.valueOf(request.getStaffNotes().size()))
                        .replace("%date_closed%", TimeUtil.formatIntoCalendarString(new Date(request.getDateClosed())))
                        .replace("%reported_player%", request.getReportedPlayer())
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType.isLeftClick()) {
                request.setClosed(false);
                request.save();
            }
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isLeftClick();
        }
    }
}
