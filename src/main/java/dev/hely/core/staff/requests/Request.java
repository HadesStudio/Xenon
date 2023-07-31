package dev.hely.core.staff.requests;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.staff.requests
//   Date: Sunday, July 30, 2023 - 4:24 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import com.google.gson.reflect.TypeToken;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import dev.hely.core.Oxygen;
import dev.hely.core.profile.note.Note;
import dev.hely.core.profile.permission.ProfilePermission;
import dev.hely.core.profile.profilesystem.impl.Comment;
import dev.hely.core.profile.profilesystem.impl.Message;
import dev.hely.core.profile.profilesystem.impl.Preferences;
import dev.hely.core.profile.profilesystem.impl.serverstats.Statistics;
import dev.hely.core.profile.profilesystem.impl.serverstats.StatisticsServer;
import dev.hely.core.profile.profilesystem.impl.social.SocialMediaType;
import dev.hely.core.profile.staff.StaffProfile;
import dev.hely.core.punishment.Punishment;
import dev.hely.core.ranks.grants.Grant;
import dev.hely.core.ranks.gson.GSONUtil;
import dev.hely.core.staff.requests.types.RequestType;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

import java.lang.reflect.Type;
import java.util.*;

@Getter
@Setter
public class Request {
    private int identifier;
    private boolean closed;
    private String message;

    private String dateCreated;
    private String dateClosed;

    private String creatorPlayerName;
    private RequestType requestType;

    private boolean claimed;
    private String claimedStaffName;

    private String closedStaffName;
    private String reportedPlayer;


    private List<String> staffNotes = new ArrayList<>();

    private Oxygen plugin = Oxygen.getInstance();


    public Request(Integer identifier, String creator) {
        this.identifier = identifier;
        this.creatorPlayerName = creator;

        load();
    }

    public void save() {
        save(false);
    }

    public void save(boolean leave) {
        Document document = new Document();
        document.put("identifier", identifier);
        document.put("closed", closed);
        document.put("dateCreated", dateCreated);
        document.put("dateClosed", dateClosed);
        document.put("creatorPlayerName", creatorPlayerName);
        document.put("requestType", String.valueOf(requestType));
        document.put("claimed", claimed);
        document.put("claimedStaffName", claimedStaffName);
        document.put("message", message);
        document.put("closedStaffName", closedStaffName);
        document.put("reportedPlayer", reportedPlayer);

        Type staffNotes = new TypeToken<List<Comment>>() {}.getType();
        document.put("staffNotes", Oxygen.PLAIN_GSON.toJson(this.staffNotes, staffNotes));

        plugin.getMongoModule().getRequestsCollection().replaceOne(Filters.eq("identifier", identifier), document, new ReplaceOptions().upsert(true));
        if (leave) plugin.getProfileManager().getProfileMap().remove(identifier);
    }

    public void load() {
        Document document = plugin.getMongoModule().getRequestsCollection().find(Filters.eq("identifier", identifier)).first();

        if (document != null) {
            if (creatorPlayerName == null) {
                creatorPlayerName = document.getString("creatorPlayerName");
            }

            reportedPlayer = document.getString("reportedPlayer");
            closed = document.getBoolean("closed");
            dateCreated = document.getString("dateCreated");
            creatorPlayerName = document.getString("creatorPlayerName");
            message = document.getString("message");
            closedStaffName = document.getString("closedStaffName");

            claimed = document.getBoolean("claimed");
            claimedStaffName = document.getString("claimedStaffName");

            Type requestStaffNotes = new TypeToken<List<String>>() {}.getType();
            staffNotes = Oxygen.PLAIN_GSON.fromJson(document.getString("staffNotes"), requestStaffNotes);
        }
    }
}
