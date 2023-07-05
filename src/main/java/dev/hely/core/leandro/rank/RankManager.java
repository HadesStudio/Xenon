package dev.hely.core.leandro.rank;

import dev.hely.core.Oxygen;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By LeandroSSJ
 * Created on 04/07/2023
 * Protect Name Oxygen
 */
public class RankManager {

    @Getter private List<Rank> ranks = new ArrayList<>();

    private Oxygen plugin;

    public RankManager(Oxygen plugin){
        this.plugin = plugin;

        setupRanks();
    }

    public void onDisable(){
        saveRanks();
    }

    public void setupRanks(){
        this.ranks.clear();

        this.plugin.getMongoManager().getRanksCollection().find().into(new ArrayList<>()).forEach(document -> {
            Rank rank = new Rank(document.getString("name"));
            rank.load();

            this.ranks.add(rank);
        });
    }

    public void saveRanks(){
        this.ranks.forEach(Rank::save);
    }

    public Rank getRank(String name) {
        return this.ranks.stream().filter(rank -> rank.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
