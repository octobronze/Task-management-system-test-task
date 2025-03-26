package com.example.Task_management_system_test_task.enums.entity_fetch_fields;

import lombok.Getter;

@Getter
public enum TaskFetchFields {
    IMPLEMENTER("implementer"), COMMENTS("comments");

    private final String field;

    TaskFetchFields(String field) {
        this.field = field;
    }
}
