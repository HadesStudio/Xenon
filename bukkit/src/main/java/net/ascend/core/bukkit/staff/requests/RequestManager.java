package net.ascend.core.bukkit.staff.requests;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.staff.requests
//   Date: Sunday, July 30, 2023 - 4:30 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.modules.ModuleLoader;
import net.ascend.core.bukkit.profile.packet.implement.StaffRequestClaimPacket;
import net.ascend.lib.CC;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class RequestManager implements ModuleLoader {

    private boolean enabled;

    @Getter private final Map<Integer, Request> requestsMap = new HashMap<>();

    @Override
    public void onEnable() {
        requestsMap.clear();

        XenonBukkit.getInstance().getMongoModule().getRequestsCollection().find().into(new ArrayList<>()).forEach(document -> {
            Request request = new Request(document.getInteger("identifier"), "Unknown", document.getString("dateCreated"));
            request.setCreatorPlayerName(request.getCreatorPlayerName());
            request.load();

            requestsMap.put(request.getIdentifier(), new Request(request.getIdentifier(), request.getCreatorPlayerName(), request.getDateCreated()));
        });
    }


    @Override
    public void onDisable() {
        this.enabled = false;

    }

    @Override
    public void onReload() {
    }

    public List<Request> getRequests() {
        return (new ArrayList<>(requestsMap.values()));
    }

    public void closeRequest(String staffMemberName, Request request) {
        Date date = new Date();
        String date_format_manual = date.getMonth() + "/" + date.getDay() + "/" + " - " + date.getHours() + ":" + date.getMinutes();

        request.setClosed(true);
        request.setDateClosed(date_format_manual);
        request.save();

        Player staffPlayer = Bukkit.getPlayer(staffMemberName).getPlayer();
        staffPlayer.sendMessage(CC.translate(Locale.REQUEST_CLOSED).replace("%player%", request.getCreatorPlayerName()));
        if (Bukkit.getPlayer(request.getCreatorPlayerName()).isOnline()) {
            Bukkit.getPlayer(request.getCreatorPlayerName()).sendMessage(CC.translate(Locale.REQUEST_PLAYER_ALERT_CLOSED).replace("%staff_member%", staffPlayer.getName()));
        }
        XenonBukkit.getInstance().getRedisModule().sendPacket(new StaffRequestClaimPacket(staffMemberName, request.getCreatorPlayerName()));
    }

    public void createNewRequest(String creator, String message) {
        int min = (int) Math.ceil(999);
        int max = (int) Math.floor(9999);
        int identifier = (int) (Math.floor(Math.random() * (max - min + 1)) + min);
        Date date = new Date();
        String date_format_manual = date.getMonth() + "/" + date.getDay() + "/" + " - " + date.getHours() + ":" + date.getMinutes();

        Request request = new Request(identifier, creator, date_format_manual);
        request.setMessage(message);
        request.setCreatorPlayerName(creator);
        request.setReportedPlayer("None");
        request.setClaimedStaffName("None");
        request.setClaimed(false);
        request.save();

        this.requestsMap.put(identifier, new Request(identifier, creator, date_format_manual));
    }

    public void createNewRequestReport(String creator, String message, String reportedPlayer) {
        int min = (int) Math.ceil(999);
        int max = (int) Math.floor(9999);
        int identifier = (int) (Math.floor(Math.random() * (max - min + 1)) + min);
        Date date = new Date();
        String date_format_manual = date.getMonth() + "/" + date.getDay() + "/" + " - " + date.getHours() + ":" + date.getMinutes();

        Request request = new Request(identifier, creator, date_format_manual);
        request.setMessage(message);
        request.setCreatorPlayerName(creator);
        request.setReportedPlayer(reportedPlayer);
        request.setClaimedStaffName("None");
        request.setClaimed(false);
        request.save();

        this.requestsMap.put(identifier, new Request(identifier, creator, date_format_manual));
    }

    public int generateOpenRequestInteger() {
        int open_requests = 0;

        for (Request requests : this.getRequests()) {
            if (!requests.isClosed()) {
                open_requests = open_requests + 1;
            }
        }

        return open_requests;
    }

    public int generateClosedRequestInteger() {
        int closed_requests = 0;

        for (Request requests : this.getRequests()) {
            if (requests.isClosed()) {
                closed_requests = closed_requests + 1;
            }
        }

        return closed_requests;
    }
}

