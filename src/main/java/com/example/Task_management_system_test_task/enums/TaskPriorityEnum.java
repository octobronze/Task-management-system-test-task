package com.example.Task_management_system_test_task.enums;

import com.example.Task_management_system_test_task.exceptions.BadRequestException;

public enum TaskPriorityEnum {
    LOW, MEDIUM, HIGH;

    public static TaskPriorityEnum getByIndex(Integer index) {
        if (index < 0 || index >= TaskPriorityEnum.values().length) {
            throw new BadRequestException("Invalid task priority index");
        }
        return TaskPriorityEnum.values()[index];
    }
}
