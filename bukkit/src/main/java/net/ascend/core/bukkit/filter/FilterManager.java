package net.ascend.core.bukkit.filter;

import com.mongodb.client.model.Filters;
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.modules.ModuleLoader;
import lombok.Getter;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class FilterManager implements ModuleLoader {

    @Getter private List<Filter> filters;

    @Override
    public void onEnable() {
        filters = new ArrayList<>();

        registerAllFilters();
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onReload() {

    }

    public void registerAllFilters() {
        this.filters.clear();

        int size = 0;
        for (Document document : XenonBukkit.getInstance().getMongoModule().getFiltersCollection().find()) {
            String name = document.getString("name");
            String phrase = document.getString("name");
            Filter filter = new Filter(name, phrase);

            size = size + 1;
            this.filters.add(filter);
        }
    }

    public void removeFilter(Filter filter) {
        XenonBukkit.getInstance().getMongoModule().getFiltersCollection().deleteOne(Filters.eq("id", filter.getId().toString()));
        XenonBukkit.getInstance().getFilterManager().getFilters().remove(filter);
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
