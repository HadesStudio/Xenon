package net.ascend.core.bukkit.staff.request;

import com.google.gson.reflect.TypeToken;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import net.ascend.core.bukkit.XenonBukkit;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Request {

    private RequestType requestType;
    private int identifier;
    private String message;

    private long dateCreated;
    private String creatorPlayerName;
    private String reportedPlayer = "";

    private boolean claimed;
    private String claimedStaffName = "";

    private boolean closed;
    private long dateClosed = 0L;
    private String closedStaffName = "";

    private List<String> staffNotes = new ArrayList<>();

    private XenonBukkit plugin = XenonBukkit.getInstance();

    public Request(Integer identifier, String creator, long creation) {
        this.identifier = identifier;
        this.creatorPlayerName = creator;
        this.dateCreated = creation;
    }

    public void save() {
        Document document = new Document();

        document.put("identifier", identifier);
        document.put("type", requestType.name());
        document.put("creatorPlayerName", creatorPlayerName);
        document.put("dateCreated", dateCreated);
        document.put("message", message);
        document.put("reportedPlayer", reportedPlayer);

        document.put("claimed", claimed);
        document.put("claimedStaffName", claimedStaffName);

        document.put("closed", closed);
        if (closed) {
            document.put("closedStaffName", closedStaffName);
            document.put("dateClosed", dateClosed);
        }

        Type staffNote = new TypeToken<List<String>>() {}.getType();
        document.put("staffNotes", XenonBukkit.PLAIN_GSON.toJson(this.staffNotes, staffNote));

        plugin.getMongoModule().getRequestsCollection().replaceOne(Filters.eq("identifier", identifier), document, new ReplaceOptions().upsert(true));
    }

    public void load() {
        Document document = plugin.getMongoModule().getRequestsCollection().find(Filters.eq("identifier", identifier)).first();

        if (document != null) {
            requestType = RequestType.valueOf(document.getString("type"));
            creatorPlayerName = document.getString("creatorPlayerName");
            dateCreated = document.getLong("dateCreated");
            reportedPlayer = document.getString("reportedPlayer");
            message = document.getString("message");

            closed = document.getBoolean("closed");
            closedStaffName = document.getString("closedStaffName");
            dateClosed = document.getLong("dateClosed");

            claimed = document.getBoolean("claimed");
            claimedStaffName = document.getString("claimedStaffName");

            Type requestStaffNotes = new TypeToken<List<String>>() {}.getType();
            staffNotes = XenonBukkit.PLAIN_GSON.fromJson(document.getString("staffNotes"), requestStaffNotes);
        }
    }
}
