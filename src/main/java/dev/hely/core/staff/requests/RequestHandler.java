package dev.hely.core.staff.requests;

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

import dev.hely.core.Oxygen;
import dev.hely.core.ranks.rank.Rank;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestHandler {

    @Getter
    private final Map<Integer, Request> requestsMap = new HashMap<>();

    public void onEnable() {
        requestsMap.clear();
        System.out.print("Attemtping to setup request list.");

        Oxygen.getInstance().getMongoModule().getRequestsCollection().find().into(new ArrayList<>()).forEach(document -> {
            Request request = new Request(document.getInteger("identifier"), "Unknown");
            request.setCreatorPlayerName(request.getCreatorPlayerName());
            System.out.print("------------------------------------------");
            System.out.print("Request Creator: " + request.getCreatorPlayerName());
            request.load();

            requestsMap.put(request.getIdentifier(), new Request(request.getIdentifier(), request.getCreatorPlayerName()));
            System.out.print("Successfully setup new player request.");
            System.out.print(" - Creator: " + request.getCreatorPlayerName());
            System.out.print(" - Identifier: #" + request.getIdentifier());
            System.out.print("------------------------------------------");
        });
    }

    public Request getRequest(int identifier) {
        return this.requestsMap.get(identifier) == null ? new Request(identifier, null) : this.requestsMap.get(identifier);
    }

    public Request getRequest(int identifier, String name) {
        if (this.requestsMap.containsKey(identifier)) return this.requestsMap.get(identifier);

        this.requestsMap.put(identifier, new Request(identifier, name));
        return this.requestsMap.get(identifier);
    }

    public List<Request> getRequests() {
        return (new ArrayList<>(requestsMap.values()));
    }

    public void createNewRequest(String creator, String message) {
        int min = (int) Math.ceil(999);
        int max = (int) Math.floor(9999);
        int identifier = (int) (Math.floor(Math.random() * (max - min + 1)) + min);
        Request request = new Request(identifier, creator);
        request.setMessage(message);
        request.setCreatorPlayerName(creator);
        request.setReportedPlayer("None");
        request.setClaimedStaffName("None");
        request.setClaimed(false);
        request.save();

        this.requestsMap.put(identifier, new Request(identifier, creator));

        System.out.print("------------------------------------------");
        System.out.print("Identifier: " + identifier);
        System.out.print("Creator: " + creator);
        System.out.print("------------------------------------------");
        System.out.print("Request List:");
        for (Request requests : getRequests()) {
            System.out.print("#" + requests.getIdentifier());
            System.out.print(" - Creator: " + requests.getCreatorPlayerName());
        }
        System.out.print("------------------------------------------");
    }

    public void createNewRequestReport(String creator, String message, String reportedPlayer) {
        int min = (int) Math.ceil(999);
        int max = (int) Math.floor(9999);
        int identifier = (int) (Math.floor(Math.random() * (max - min + 1)) + min);
        Request request = new Request(identifier, creator);
        request.setMessage(message);
        request.setCreatorPlayerName(creator);
        request.setReportedPlayer(reportedPlayer);
        request.setClaimedStaffName("None");
        request.setClaimed(false);
        request.save();

        this.requestsMap.put(identifier, new Request(identifier, creator));

        System.out.print("------------------------------------------");
        System.out.print("Identifier: " + identifier);
        System.out.print("Creator: " + creator);
        System.out.print("------------------------------------------");
        System.out.print("Request List:");
        for (Request requests : getRequests()) {
            System.out.print("#" + requests.getIdentifier());
            System.out.print(" - Creator: " + requests.getCreatorPlayerName());
        }
        System.out.print("------------------------------------------");
    }
}

