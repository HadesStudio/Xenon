package dev.hely.core.bungee.utils;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bungee.utils
//   Date: Tuesday, August 29, 2023 - 8:43 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.md_5.bungee.api.CommandSender;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class BungeePaginatedList<T> {
    private int resultsPerPage;

    public BungeePaginatedList() {
        this(9);
    }

    public BungeePaginatedList(final int resultsPerPage) {
        assert resultsPerPage > 0;
        this.resultsPerPage = resultsPerPage;
    }

    public void display(final CommandSender sender, final Collection<? extends T> results, final int page) {
        this.display(sender, new ArrayList<T>(results), page);
    }

    public void display(final CommandSender sender, final List<? extends T> results, final int page) {
        if (results.size() == 0) {
            sender.sendMessage(ChatColor.RED + "No entries were found.");
            return;
        }
        final int maxPages = results.size() / this.resultsPerPage + 1;
        if (page <= 0 || page > maxPages) {
            sender.sendMessage(ChatColor.RED + "Page '" + page + "' not found. (" + ChatColor.YELLOW + "1 - " + maxPages + ChatColor.RED + ")");
            return;
        }
        sender.sendMessage(this.getHeader(page, maxPages));
        for (int i = this.resultsPerPage * (page - 1); i < this.resultsPerPage * page && i < results.size(); ++i) {
            sender.sendMessage(this.format(results.get(i), i));
        }
    }

    public abstract String getHeader(final int p0, final int p1);

    public abstract String format(final T p0, final int p1);
}
