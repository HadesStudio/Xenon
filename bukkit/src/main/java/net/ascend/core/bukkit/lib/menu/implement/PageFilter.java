package net.ascend.core.bukkit.lib.menu.implement;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.function.Predicate;

@RequiredArgsConstructor
public class PageFilter<T> {

    @Getter private String name;
    @Getter @Setter private boolean enabled;
    private final Predicate<T> predicate;

    public PageFilter(String name, Predicate<T> predicate, boolean enabled) {
        this.name = name;
        this.predicate = predicate;
        this.enabled = enabled;
    }

    public boolean test(T t) {
        return enabled && predicate.test(t);
    }

    @Override
    public boolean equals(Object object) {
        if(object instanceof PageFilter) {
            PageFilter<?> pageFilter = (PageFilter<?>) object;

            if(pageFilter.getName().equals(this.name)) {
                return true;
            }
        }

        return false;
    }
}