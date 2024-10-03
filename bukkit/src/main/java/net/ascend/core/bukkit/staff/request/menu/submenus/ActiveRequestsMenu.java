package net.ascend.core.bukkit.staff.request.menu.submenus;

import com.google.common.collect.Lists;
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.lib.menu.button.implement.BackButton;
import net.ascend.core.bukkit.lib.menu.button.implement.PageFilterButton;
import net.ascend.core.bukkit.lib.menu.implement.FilterablePaginatedMenu;
import net.ascend.core.bukkit.lib.menu.implement.PageFilter;
import net.ascend.core.bukkit.staff.request.packet.StaffRequestClaimPacket;
import net.ascend.core.bukkit.staff.request.Request;
import net.ascend.core.bukkit.lib.CC;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.button.Button;
import net.ascend.core.bukkit.lib.menu.button.implement.PageButton;
import lombok.AllArgsConstructor;
import net.ascend.core.bukkit.staff.request.RequestType;
import net.ascend.core.bukkit.staff.request.menu.RequestsMenu;
import net.ascend.core.bukkit.util.TimeUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class ActiveRequestsMenu extends FilterablePaginatedMenu<Request> {

    {
        setClickUpdate(true);
    }

    @Override
    public String getName() {
        return XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("active-requests-menu.title");
    }
    @Override
    public String getTitle(Player player) {
        return XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("active-requests-menu.title");
    }

    @Override
    public int getSize() {
        return 36;
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
        menuContent.put(31, new PageFilterButton<>(this));
        menuContent.put(32, Button.getPlaceholder());

        menuContent.put(33, Button.getPlaceholder());
        menuContent.put(34, Button.getPlaceholder());
        menuContent.put(35, new PageButton(1, this));

        return menuContent;
    }

    @Override
    public List<PageFilter<Request>> generateFilters() {
        List<PageFilter<Request>> filters = Lists.newArrayList();

        filters.add(new PageFilter<>("Filter: All Requests", (request -> request.getRequestType() != null), true));
        filters.add(new PageFilter<>("Filter: All Request", (request -> request.getRequestType() != RequestType.REQUEST), false));
        filters.add(new PageFilter<>("Filter: All Reports", (request -> request.getRequestType() != RequestType.REPORT), false));

        return filters;
    }

    @Override
    public Map<Integer, Button> getFilteredButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        List<Request> requests = XenonBukkit.getInstance().getRequestHandler().getRequests();
        List<Request> openRequests = requests.stream()
                .filter(request -> !request.isClosed())
                .collect(Collectors.toList());

        openRequests.sort(Comparator.comparingLong(Request::getDateCreated));

        loop:
        for (Request request : openRequests) {
            for (PageFilter<Request> filter : getFilters()) {
                if (filter.test(request)) {
                    buttons.put(buttons.size(), new Requests_Button(request));
                    continue loop;
                }
            }
        }

        return buttons;
    }

    @AllArgsConstructor
    public static class Requests_Button extends Button {

        private final Request request;

        public String getMaterial() {
            return XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("active-requests-menu.buttons.requests.material");
        }

        public List<String> getLore() {
            if (request.getRequestType() == RequestType.REQUEST) {
                if (request.isClaimed()) {
                    return XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getStringList("active-requests-menu.buttons.requests.lores.requests.claimed_lore");
                } else {
                    return XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getStringList("active-requests-menu.buttons.requests.lores.requests.default_lore");
                }
            } else {
                if (request.isClaimed()) {
                    return XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getStringList("active-requests-menu.buttons.requests.lores.report.claimed_lore");
                } else {
                    return XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getStringList("active-requests-menu.buttons.requests.lores.report.default_lore");
                }
            }
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getInt("active-requests-menu.buttons.requests.data");
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("active-requests-menu.buttons.requests.display_name")
                    .replace("%request_player%", request.getCreatorPlayerName())
                    .replace("%request_identifier%", String.valueOf(request.getIdentifier()))
            );

            for (String line : getLore()) {
                builder.addLore(line
                        .replace("%message%", request.getMessage())
                        .replace("%player_claimed%", request.getClaimedStaffName())
                        .replace("%notes_size%", String.valueOf(request.getStaffNotes().size()))
                        .replace("%reported_player%", request.getReportedPlayer())
                        .replace("%date_created%", TimeUtil.formatIntoCalendarString(new Date(request.getDateCreated())))
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType.isLeftClick()) {
                if (request.isClaimed()) {
                    if (request.getClaimedStaffName().equalsIgnoreCase(player.getName())) {
                        player.sendMessage(CC.translate(Locale.REQUEST_UNCLAIMED).replace("%player%", request.getCreatorPlayerName()));

                        request.setClaimed(false);
                        request.setClaimedStaffName("None");
                        request.save();
                    } else {
                        player.sendMessage(CC.translate(Locale.REQUEST_ALREADY_CLAIMED));
                    }
                } else {
                    request.setClaimed(true);
                    request.setClaimedStaffName(player.getName());
                    request.save();

                    player.sendMessage(CC.translate(Locale.REQUEST_CLAIMED).replace("%player%", request.getCreatorPlayerName()));
                    XenonBukkit.getInstance().getRedisModule().sendPacket(new StaffRequestClaimPacket(player.getName(), request.getCreatorPlayerName()));
                }
            } else if (clickType.isCreativeAction()) {
                XenonBukkit.getInstance().getRequestHandler().closeRequest(player, request);
            } else {
                new RequestNotesMenu(request).openMenu(player);
            }
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isLeftClick();
        }
    }
}
