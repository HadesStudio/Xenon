package dev.hely.core.bukkit.coins.logging;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.coins.logging
//   Date: Monday, August 07, 2023 - 6:36 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.coins.logging.types.CurrencyActions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class CurrencyLogging {
    private UUID uuid;
    private int afterCurrency;
    private int beforeCurrency;
    private long datePurchased;
    private CurrencyActions action;
    private String issuer;

    public CurrencyLogging(Document document) {
        this.uuid = UUID.fromString(document.getString("uuid"));
        this.afterCurrency = document.getInteger("afterCurrency");
        this.beforeCurrency = document.getInteger("beforeCurrency");
        this.datePurchased = document.getLong("datePurchased");
        this.action = CurrencyActions.valueOf(document.getString("action"));
        this.issuer = document.getString("issuer");
    }

    public int getDifference() {
        return afterCurrency - beforeCurrency;
    }

    public Document toDocument() {
        Document dataDocument = new Document();
        dataDocument.put("uuid", uuid.toString());
        dataDocument.put("afterCurrency", afterCurrency);
        dataDocument.put("beforeCurrency", beforeCurrency);
        dataDocument.put("datePurchased", datePurchased);
        dataDocument.put("action", action.toString());
        dataDocument.put("issuer", issuer);

        return dataDocument;
    }
}
