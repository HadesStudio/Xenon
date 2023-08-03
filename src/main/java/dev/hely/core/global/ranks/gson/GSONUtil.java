package dev.hely.core.global.ranks.gson;

import com.google.gson.reflect.TypeToken;
import dev.hely.core.global.ranks.rank.permission.PermissionData;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created By LeandroSSJ
 * Created on 04/07/2023
 * Protect Name OxygenGlobal
 */
public class GSONUtil {

    public static final Type LIST_STRING_TYPE = new TypeToken<List<String>>() {}.getType();
    public static final Type PERMISSION_DATA = new TypeToken<List<PermissionData>>() {}.getType();
}
