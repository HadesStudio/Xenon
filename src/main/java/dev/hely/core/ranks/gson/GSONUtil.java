package dev.hely.core.ranks.gson;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created By LeandroSSJ
 * Created on 04/07/2023
 * Protect Name Oxygen
 */
public class GSONUtil {

    public static final Type listStringType = new TypeToken<List<String>>() {}.getType();

}
