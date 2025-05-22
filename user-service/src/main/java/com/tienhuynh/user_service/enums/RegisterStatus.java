package com.tienhuynh.user_service.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RegisterStatus {
    PENDING_VERIFICATION,
    VERIFIED,
    EXPIRED,
    FAILED,
    BLACKLISTED;

    @JsonCreator
    public static Role fromString(String value) {
        return Role.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}