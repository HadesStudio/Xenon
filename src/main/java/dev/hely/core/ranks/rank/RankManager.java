package dev.hely.core.ranks.rank;

import dev.hely.core.Oxygen;
import dev.hely.core.managers.modules.ModuleLoader;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By LeandroSSJ
 * Created on 04/07/2023
 * Protect Name Oxygen
 */
public class RankManager implements ModuleLoader {
    private boolean enabled;

    @Getter private List<Rank> ranks = new ArrayList<>();
    private Oxygen plugin;

    @Override
    public void enable() {
        this.plugin = Oxygen.getInstance();
        setupRanks();

        this.enabled = true;
    }

    @Override
    public void disable() {
        saveRanks();
        this.enabled = false;
    }

    @Override
    public void reload() {
        saveRanks();
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getName() {
        return "Rank Manager";
    }

    public void setupRanks(){
        ranks.clear();

        plugin.getMongoManager().getRanksCollection().find().into(new ArrayList<>()).forEach(document -> {
            Rank rank = new Rank(document.getString("name"));
            rank.load();

            ranks.add(rank);
        });
    }

    public void saveRanks(){
        ranks.forEach(Rank::save);
    }

    public Rank getRank(String name) {
        return ranks.stream().filter(rank -> rank.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
