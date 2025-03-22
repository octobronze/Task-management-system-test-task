package com.example.Task_management_system_test_task.services;

import com.example.Task_management_system_test_task.dtos.TaskCreateRequestDto;
import com.example.Task_management_system_test_task.enums.TaskPriorityEnum;
import com.example.Task_management_system_test_task.enums.TaskStatusEnum;
import com.example.Task_management_system_test_task.exceptions.BadRequestException;
import com.example.Task_management_system_test_task.repos.TaskRepository;
import com.example.Task_management_system_test_task.repos.UserRepository;
import com.example.Task_management_system_test_task.tables.Task;
import com.example.Task_management_system_test_task.tables.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.Task_management_system_test_task.consts.ExceptionMessagesConsts.USER_NOT_FOUND;

@Service
@AllArgsConstructor
public class TaskService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public void createTask(Integer creatorId, TaskCreateRequestDto requestDto) {
        User creator = userRepository.findById(creatorId).orElseThrow(() -> new BadRequestException(USER_NOT_FOUND));
        User implementer = userRepository.findById(requestDto.getImplementerId()).orElseThrow(() -> new BadRequestException(USER_NOT_FOUND));
        TaskStatusEnum status = TaskStatusEnum.getByIndex(requestDto.getTaskStatusIndex());
        TaskPriorityEnum priority = TaskPriorityEnum.getByIndex(requestDto.getPriorityIndex());

        Task task = new Task();

        task.setTitle(requestDto.getTitle());
        task.setDescription(requestDto.getDescription());
        task.setStatus(status);
        task.setPriority(priority);
        task.setCreator(creator);
        task.setImplementer(implementer);

        taskRepository.save(task);
    }
}
