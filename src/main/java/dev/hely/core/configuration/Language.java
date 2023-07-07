package dev.hely.core.configuration;

import dev.hely.core.Oxygen;
import dev.hely.lib.configuration.Config;

import java.util.List;

/**
 * Created By LeandroSSJ
 * Created on 07/07/2023
 * Protect Name Oxygen
 */
public class Language {

    public static List<String> TAG_USAGE;
    public static String TAG_CREATE_USAGE;
    public static String TAG_CREATE_ALREADY_EXIST;
    public static String TAG_CREATE_CREATED;
    public static String TAG_REMOVE_USAGE;
    public static String TAG_DONT_EXISTS;
    public static String TAG_REMOVE_REMOVED;
    public static String TAG_SETPREFIX_USAGE;
    public static String TAG_SETPREFIX_UPDATED;
    public static String TAG_SETWEIGHT_USAGE;
    public static String TAG_SETWEIGHT_UPDATED;
    public static String TAG_IMPORT_IMPORTED;
    public static String TAG_EXPORT_EXPORTED;


    public static void load(){
        Config language = Oxygen.getInstance().getLanguageConfig();

        TAG_USAGE = language.getConfiguration().getStringList("tag.usage");
        TAG_DONT_EXISTS = language.getConfiguration().getString("tag.dont-exists");
        TAG_CREATE_USAGE = language.getConfiguration().getString("tag.create.usage");
        TAG_CREATE_ALREADY_EXIST = language.getConfiguration().getString("tag.create.already-exist");
        TAG_CREATE_CREATED = language.getConfiguration().getString("tag.create.created");
        TAG_REMOVE_USAGE = language.getConfiguration().getString("tag.remove.usage");
        TAG_REMOVE_REMOVED = language.getConfiguration().getString("tag.remove.removed");
        TAG_SETPREFIX_USAGE = language.getConfiguration().getString("tag.setprefix.usage");
        TAG_SETPREFIX_UPDATED = language.getConfiguration().getString("tag.setprefix.updated");
        TAG_SETWEIGHT_USAGE = language.getConfiguration().getString("tag.setweight.usage");
        TAG_SETWEIGHT_UPDATED = language.getConfiguration().getString("tag.setweight.updated");
        TAG_IMPORT_IMPORTED = language.getConfiguration().getString("tag.import.imported");
        TAG_EXPORT_EXPORTED = language.getConfiguration().getString("tag.export.exported");

    }
}
