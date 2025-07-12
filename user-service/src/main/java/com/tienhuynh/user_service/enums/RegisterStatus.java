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
    public static RegisterStatus fromString(String value) {
        return RegisterStatus.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}