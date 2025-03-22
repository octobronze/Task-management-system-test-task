package com.example.Task_management_system_test_task.enums;

import com.example.Task_management_system_test_task.exceptions.BadRequestException;

public enum TaskStatusEnum {
    WAITING, IN_PROGRESS, DONE;

    public static TaskStatusEnum getByIndex(Integer index) {
        if (index < 0 || index >= TaskStatusEnum.values().length) {
            throw new BadRequestException("Invalid task status index");
        }
        return TaskStatusEnum.values()[index];
    }
}
