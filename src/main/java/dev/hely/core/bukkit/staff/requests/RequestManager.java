package dev.hely.core.bukkit.staff.requests;

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

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.modules.ModuleLoader;
import dev.hely.core.bukkit.profile.packet.implement.StaffRequestClaimPacket;
import dev.hely.lib.CC;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class RequestManager implements ModuleLoader {
    private OxygenBukkit plugin;
    private boolean enabled;

    @Getter
    private final Map<Integer, Request> requestsMap = new HashMap<>();

    @Override
    public void enable() {
        requestsMap.clear();
        OxygenBukkit.getInstance().debug_message("Attemtping to setup request list.");

        OxygenBukkit.getInstance().getMongoModule().getRequestsCollection().find().into(new ArrayList<>()).forEach(document -> {
            Request request = new Request(document.getInteger("identifier"), "Unknown", document.getString("dateCreated"));
            request.setCreatorPlayerName(request.getCreatorPlayerName());
            OxygenBukkit.getInstance().debug_message("------------------------------------------");
            OxygenBukkit.getInstance().debug_message("Request Creator: " + request.getCreatorPlayerName());
            request.load();

            requestsMap.put(request.getIdentifier(), new Request(request.getIdentifier(), request.getCreatorPlayerName(), request.getDateCreated()));
            OxygenBukkit.getInstance().debug_message("Successfully setup new player request.");
            OxygenBukkit.getInstance().debug_message(" - Creator: " + request.getCreatorPlayerName());
            OxygenBukkit.getInstance().debug_message(" - Identifier: #" + request.getIdentifier());
            OxygenBukkit.getInstance().debug_message(" - Date: " + request.getDateCreated());
            OxygenBukkit.getInstance().debug_message("------------------------------------------");
        });
    }


    @Override
    public void disable() {
        this.enabled = false;

    }

    @Override
    public void reload() {
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getName() {
        return "Request";
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
        OxygenBukkit.getInstance().getRedisModule().sendPacket(new StaffRequestClaimPacket(staffMemberName, request.getCreatorPlayerName()));
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

        OxygenBukkit.getInstance().debug_message("------------------------------------------");
        OxygenBukkit.getInstance().debug_message("Identifier: " + identifier);
        OxygenBukkit.getInstance().debug_message("Creator: " + creator);
        OxygenBukkit.getInstance().debug_message("Date: " + date_format_manual);
        OxygenBukkit.getInstance().debug_message("------------------------------------------");
        OxygenBukkit.getInstance().debug_message("Request List:");
        for (Request requests : getRequests()) {
            OxygenBukkit.getInstance().debug_message("#" + requests.getIdentifier());
            OxygenBukkit.getInstance().debug_message(" - Creator: " + requests.getCreatorPlayerName());
            OxygenBukkit.getInstance().debug_message(" - Date: " + requests.getDateCreated());
        }
        OxygenBukkit.getInstance().debug_message("------------------------------------------");
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

        OxygenBukkit.getInstance().debug_message("------------------------------------------");
        OxygenBukkit.getInstance().debug_message("Identifier: " + identifier);
        OxygenBukkit.getInstance().debug_message("Creator: " + creator);
        OxygenBukkit.getInstance().debug_message("Date: " + date_format_manual);

        OxygenBukkit.getInstance().debug_message("------------------------------------------");
        OxygenBukkit.getInstance().debug_message("Request List:");
        for (Request requests : getRequests()) {
            OxygenBukkit.getInstance().debug_message("#" + requests.getIdentifier());
            OxygenBukkit.getInstance().debug_message(" - Creator: " + requests.getCreatorPlayerName());
            OxygenBukkit.getInstance().debug_message(" - Date: " + requests.getDateCreated());
        }
        OxygenBukkit.getInstance().debug_message("------------------------------------------");
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

