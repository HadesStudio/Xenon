package dev.hely.core.utils.json;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.utils.json
//   Date: Tuesday, July 04, 2023 - 5:42 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonBuilder {

    private JsonObject json = new JsonObject();

    public JsonBuilder addProperty(String property, String value) {
        this.json.addProperty(property, value);
        return this;
    }

    public JsonBuilder add(String property, JsonElement value) {
        this.json.add(property, value);
        return this;
    }

    public JsonBuilder addProperty(String property, Number value) {
        this.json.addProperty(property, value);
        return this;
    }

    public JsonBuilder addProperty(String property, Boolean value) {
        this.json.addProperty(property, value);
        return this;
    }

    public JsonBuilder addProperty(String property, Character value) {
        this.json.addProperty(property, value);
        return this;
    }

    public JsonBuilder addProperty(String property, JsonObject value) {
        this.json.add(property, value);
        return this;
    }

    public JsonObject get() {
        return this.json;
    }

}
