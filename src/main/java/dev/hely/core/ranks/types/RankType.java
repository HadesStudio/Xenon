package dev.hely.core.ranks.types;

import dev.hely.core.utils.StringUtils;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum RankType {
    DEFAULT, STAFF, HIDDEN;

    public static String toMessage() {
        return StringUtils.getStringFromList(Stream.of(RankType.values()).map(RankType::toString).map(String::toLowerCase).map(StringUtils::convertFirstUpperCase).collect(Collectors.toList()));
    }
}