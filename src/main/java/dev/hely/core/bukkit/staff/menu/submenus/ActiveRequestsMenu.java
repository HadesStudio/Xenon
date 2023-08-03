package dev.hely.core.bukkit.staff.menu.submenus;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.staff.menu.submenus
//   Date: Sunday, July 30, 2023 - 6:41 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.global.profile.packet.implement.StaffRequestClaimPacket;
import dev.hely.core.bukkit.staff.requests.Request;
import dev.hely.lib.CC;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.button.Button;
import dev.hely.lib.menu.button.implement.InformationButton;
import dev.hely.lib.menu.button.implement.PageButton;
import dev.hely.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActiveRequestsMenu extends PaginatedMenu {

    {
        setClickUpdate(true);
    }

    @Override
    public String getName() {
        return OxygenBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("active-requests.title");
    }

    @Override
    public int getSize() {
        return 36;
    }

    @Override
    public String getTitle(Player player) {
        return OxygenBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("active-requests.title");
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (Request request : OxygenBukkit.getInstance().getRequestHandler().getRequests()) {
            if (!request.isClosed()) {
                buttons.put(buttons.size(), new Requests_Button(request));
            }
        }

        return buttons;
    }

    public Map<Integer, Button> getGlobalContent(Player player) {
        Map<Integer, Button> menuContent = new HashMap<>();

        menuContent.put(0, Button.getPlaceholder());
        menuContent.put(1, Button.getPlaceholder());
        menuContent.put(2, Button.getPlaceholder());

        menuContent.put(3, Button.getPlaceholder());
        menuContent.put(4, Button.getPlaceholder());
        menuContent.put(5, Button.getPlaceholder());

        menuContent.put(6, Button.getPlaceholder());
        menuContent.put(7, Button.getPlaceholder());
        menuContent.put(8, Button.getPlaceholder());

        menuContent.put(27, Button.getPlaceholder());
        menuContent.put(28, new PageButton(-1, this));
        menuContent.put(29, Button.getPlaceholder());

        menuContent.put(30, Button.getPlaceholder());
        menuContent.put(31, new InformationButton(this));
        menuContent.put(32, Button.getPlaceholder());

        menuContent.put(33, Button.getPlaceholder());
        menuContent.put(34, new PageButton(1, this));
        menuContent.put(35, Button.getPlaceholder());

        return menuContent;
    }

    @AllArgsConstructor
    public static class Requests_Button extends Button {
        Request request;

        public Material getMaterial() {
            if (OxygenBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("active-requests.buttons.requests.material") != null) {
                if (Material.getMaterial(OxygenBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("active-requests.buttons.requests.material")) != null) {
                    return Material.valueOf(OxygenBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("active-requests.buttons.requests.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'active-requests-button' is null, using a default material."));
                    return Material.PAPER;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'active-requests-button' is broken, using a default material."));
                return Material.PAPER;
            }
        }

        public List<String> getLore() {
            if (request.getReportedPlayer() == null) {
                if (request.isClaimed()) {
                    return OxygenBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getStringList("active-requests.buttons.requests.lores.requests.claimed-lore");
                } else {
                    return OxygenBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getStringList("active-requests.buttons.requests.lores.requests.default-lore");
                }
            } else {
                if (request.isClaimed()) {
                    return OxygenBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getStringList("active-requests.buttons.requests.lores.report.claimed-lore");
                } else {
                    return OxygenBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getStringList("active-requests.buttons.requests.lores.report.default-lore");
                }
            }
        }

        public short getData() {
            return (short) OxygenBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getInt("active-requests.buttons.requests.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(OxygenBukkit.getInstance().getRequestsMenuConfig().getConfiguration().getString("active-requests.buttons.requests.display-name")
                    .replace("%request_player%", request.getCreatorPlayerName())
                    .replace("%request_identifier%", String.valueOf(request.getIdentifier()))
            );

            for (String line : getLore()) {
                builder.addLore(line
                        .replace("%message%", request.getMessage())
                        .replace("%player_claimed%", request.getClaimedStaffName())
                        .replace("%notes_size%", String.valueOf(request.getStaffNotes().size()))
                        .replace("%reported_player%", request.getReportedPlayer())
                        .replace("%date_created%", request.getDateCreated())
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

                    player.sendMessage(CC.translate(Locale.REQUEST_CLAIMED).replace("%player%", request.getCreatorPlayerName()));
                    OxygenBukkit.getInstance().getRedisModule().sendPacket(new StaffRequestClaimPacket(player.getDisplayName(), request.getCreatorPlayerName()));
                }
            } else if (clickType.isCreativeAction()) {
                OxygenBukkit.getInstance().getRequestHandler().closeRequest(player.getName(), request);
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
