package net.ascend.core.bukkit.modules.database.mongo.serializer;

import org.bson.Document;

public interface DocumentSerializer<T> {
    Document toDocument(T source);

    T fromDocument(Document document);
}

