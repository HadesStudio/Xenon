package dev.hely.core.bukkit.filter;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.filter
//   Date: Thursday, September 14, 2023 - 1:16 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import com.google.gson.reflect.TypeToken;
import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.modules.ModuleLoader;
import dev.hely.core.bukkit.profile.log.Log;
import dev.hely.core.bukkit.profile.log.types.LogActionTypes;
import lombok.Getter;
import org.bson.Document;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FilterManager implements ModuleLoader {
    @Getter private final List<Filter> filters = new ArrayList<>();

    @Override
    public void enable() {
        registerAllFilters();
    }

    @Override
    public void disable() {

    }

    @Override
    public void reload() {

    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public String getName() {
        return "Filter";
    }

    public void registerAllFilters() {
        this.filters.clear();
        int size = 0;
        for (Document document : OxygenBukkit.getInstance().getMongoModule().getFiltersCollection().find()) {
            String name = document.getString("name");
            String phrase = document.getString("name");
            Filter filter = new Filter(name, phrase);

            size = size + 1;
            this.filters.add(filter);
        }
    }

    public Filter getFilterByName(String name) {
        for (Filter filter : filters) {
            if (filter.getName().equalsIgnoreCase(name)) {
                return filter;
            }
        }
        return null;
    }

    public Filter checkMessage(String message) {
        Filter max = null;
        for (Filter filter : filters) {
            if (filter.isEnabled() && filter.matches(message)) {
                if (max == null) {
                    max = filter;
                    continue;
                }

                if (filter.compareTo(max) > 0)
                    max = filter;
            }
        }

        return max;
    }
}
