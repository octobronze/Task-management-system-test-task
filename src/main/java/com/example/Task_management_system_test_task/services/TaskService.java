package com.example.Task_management_system_test_task.services;

import com.example.Task_management_system_test_task.dtos.*;
import com.example.Task_management_system_test_task.enums.TaskPriorityEnum;
import com.example.Task_management_system_test_task.enums.TaskStatusEnum;
import com.example.Task_management_system_test_task.exceptions.BadRequestException;
import com.example.Task_management_system_test_task.repos.TaskRepository;
import com.example.Task_management_system_test_task.repos.UserRepository;
import com.example.Task_management_system_test_task.specifications.TaskSpecification;
import com.example.Task_management_system_test_task.tables.Task;
import com.example.Task_management_system_test_task.tables.User;
import com.example.Task_management_system_test_task.utils.DtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import static com.example.Task_management_system_test_task.consts.ExceptionMessagesConsts.*;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public void createTask(Integer currentUserId, TaskCreateRequestDto requestDto) {
        User creator = userRepository.findById(currentUserId).orElseThrow(() -> new BadRequestException(USER_NOT_FOUND));
        User implementer = userRepository.findById(requestDto.getImplementerId()).orElseThrow(() -> new BadRequestException(USER_NOT_FOUND));
        TaskPriorityEnum priority = TaskPriorityEnum.getByIndex(requestDto.getPriorityIndex());

        Task task = new Task();

        task.setTitle(requestDto.getTitle());
        task.setDescription(requestDto.getDescription());
        task.setStatus(TaskStatusEnum.WAITING);
        task.setPriority(priority);
        task.setCreator(creator);
        task.setImplementer(implementer);

        taskRepository.save(task);
    }

    public void deleteTask(Integer taskId) {
        taskRepository.deleteById(taskId);
    }

    public void updateTask(TaskUpdateByAdminRequestDto requestDto) {
        Task task = taskRepository.findById(requestDto.getId())
                .orElseThrow(() -> new BadRequestException(TASK_NOT_FOUND));

        if (requestDto.getPriorityIndex() != null) {
            TaskPriorityEnum priority = TaskPriorityEnum.getByIndex(requestDto.getPriorityIndex());
            task.setPriority(priority);
        }
        if (requestDto.getImplementerId() != null) {
            User implementer = userRepository.findById(requestDto.getImplementerId()).orElseThrow(() -> new BadRequestException(USER_NOT_FOUND));
            task.setImplementer(implementer);
        }

        setTaskWithDto(task, requestDto);

        taskRepository.save(task);
    }

    public void updateTask(TaskUpdateRequestDto requestDto) {
        Task task = taskRepository.findById(requestDto.getId())
                .orElseThrow(() -> new BadRequestException(TASK_NOT_FOUND));

        setTaskWithDto(task, requestDto);

        taskRepository.save(task);
    }

    private void setTaskWithDto(Task task, TaskUpdateRequestDto requestDto) {
        if (requestDto.getStatusIndex() != null) {
            TaskStatusEnum status = TaskStatusEnum.getByIndex(requestDto.getStatusIndex());
            task.setStatus(status);
        }
    }

    public Page<TaskGetResponseDto> getTasksWithFilters(TaskFilterRequestDto requestDto) {
        return taskRepository.findAllWithComments(
                new TaskSpecification(requestDto),
                PageRequest.of(requestDto.getPageIndex(), requestDto.getPageSize())

        ).map(DtoMapper::taskToTaskGetResponseDto);
    }
}
