package net.ascend.core.bukkit.rank.grant.type;

import net.ascend.core.bukkit.rank.grant.Grant;
import lombok.Getter;

import java.util.UUID;

@Getter
public class TagGrant extends Grant {

    private final String tagName;

    public TagGrant(UUID id, String tagName) {
        super(id);

        this.tagName = tagName;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof TagGrant) {
            TagGrant tagGrant = (TagGrant) o;

            return super.equals(o) && tagGrant.getTagName().equals(tagName);
        }

        return super.equals(o);
    }
}
