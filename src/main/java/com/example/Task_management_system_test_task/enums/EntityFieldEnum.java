package com.example.Task_management_system_test_task.enums;

public enum EntityFieldEnum {
    IMPLEMENTER("implementer"), ROLE("role"), COMMENTS("comments"), CREATOR("creator"), USER("user");

    private final String name;

    EntityFieldEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
