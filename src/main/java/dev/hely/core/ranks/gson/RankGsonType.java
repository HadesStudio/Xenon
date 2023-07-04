package dev.hely.core.ranks.gson;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.ranks.gson
//   Date: Tuesday, July 04, 2023 - 5:34 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import com.google.gson.reflect.TypeToken;
import dev.hely.core.ranks.grant.Grants;
import dev.hely.core.ranks.permissions.Permission;
import dev.hely.core.ranks.permissions.RankPermission;

import java.lang.reflect.Type;
import java.util.List;

public class RankGsonType {
    public static final Type GRANT = new TypeToken<List<Grants>>() {
    }.getType();
    public static final Type PERMISSION = new TypeToken<List<Permission>>() {
    }.getType();
    public static final Type RANK_PERMISSION = new TypeToken<List<RankPermission>>() {
    }.getType();
    public static final Type STRING_LIST = new TypeToken<List<String>>() {
    }.getType();
}
