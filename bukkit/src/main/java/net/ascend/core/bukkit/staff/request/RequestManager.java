package net.ascend.core.bukkit.staff.request;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.modules.ModuleLoader;
import net.ascend.core.bukkit.staff.request.packet.StaffRequestClaimPacket;
import net.ascend.core.bukkit.lib.CC;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
public class RequestManager implements ModuleLoader {

    private final Map<Integer, Request> requestsMap = new HashMap<>();

    @Override
    public void onEnable() {
        this.requestsMap.clear();

        XenonBukkit.getInstance().getMongoModule().getRequestsCollection().drop();
        for (Document document : XenonBukkit.getInstance().getMongoModule().getRequestsCollection().find()) {
            if (document != null) {
                Request request = new Request(document.getInteger("identifier"), document.getString("creatorPlayerName"), document.getLong("dateCreated"));
                request.load();

                this.requestsMap.put(request.getIdentifier(), request);
            }
        }
    }


    @Override
    public void onDisable() {
        for (Request request : this.getRequests()) {
            request.save();
        }
    }

    @Override
    public void onReload() {
    }

    public List<Request> getRequests() {
        return (new ArrayList<>(this.requestsMap.values()));
    }

    public void closeRequest(Player staff, Request request) {
        request.setClosed(true);
        request.setClosedStaffName(staff.getName());
        request.setDateClosed(System.currentTimeMillis());
        request.save();

        staff.sendMessage(CC.translate(Locale.REQUEST_CLOSED).replace("%player%", request.getCreatorPlayerName()));

        if (Bukkit.getPlayer(request.getCreatorPlayerName()).isOnline()) {
            Bukkit.getPlayer(request.getCreatorPlayerName()).sendMessage(CC.translate(Locale.REQUEST_PLAYER_ALERT_CLOSED).replace("%staff_member%", staff.getName()));
        }

        XenonBukkit.getInstance().getRedisModule().sendPacket(new StaffRequestClaimPacket(staff.getName(), request.getCreatorPlayerName()));
    }

    public void createNewRequest(String creator, String message) {
        int min = (int) Math.ceil(999);
        int max = (int) Math.floor(9999);
        int identifier = (int) (Math.floor(Math.random() * (max - min + 1)) + min);

        Request request = new Request(identifier, creator, System.currentTimeMillis());
        request.setRequestType(RequestType.REQUEST);
        request.setMessage(message);
        request.setCreatorPlayerName(creator);
        request.setClaimedStaffName("None");
        request.setClaimed(false);
        request.save();

        this.requestsMap.put(identifier, request);
    }

    public void createNewReport(String creator, String reportedPlayer, String message) {
        int min = (int) Math.ceil(999);
        int max = (int) Math.floor(9999);
        int identifier = (int) (Math.floor(Math.random() * (max - min + 1)) + min);

        Request request = new Request(identifier, creator, System.currentTimeMillis());
        request.setRequestType(RequestType.REPORT);
        request.setMessage(message);
        request.setClaimedStaffName("None");
        request.setReportedPlayer(reportedPlayer);
        request.setClaimed(false);
        request.save();

        this.requestsMap.put(identifier, request);
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

