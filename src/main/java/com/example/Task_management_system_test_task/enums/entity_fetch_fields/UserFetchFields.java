package com.example.Task_management_system_test_task.enums.entity_fetch_fields;

import lombok.Getter;

@Getter
public enum UserFetchFields {
    ROLE("role");

    private final String field;

    UserFetchFields(String field) {
        this.field = field;
    }

}
