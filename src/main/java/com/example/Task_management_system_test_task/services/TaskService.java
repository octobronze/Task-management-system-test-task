package com.example.Task_management_system_test_task.services;

import com.example.Task_management_system_test_task.dtos.TaskCreateRequestDto;
import com.example.Task_management_system_test_task.dtos.TaskGetResponseDto;
import com.example.Task_management_system_test_task.dtos.TaskUpdateByAdminRequestDto;
import com.example.Task_management_system_test_task.dtos.TaskUpdateRequestDto;
import com.example.Task_management_system_test_task.enums.TaskPriorityEnum;
import com.example.Task_management_system_test_task.enums.TaskStatusEnum;
import com.example.Task_management_system_test_task.exceptions.BadRequestException;
import com.example.Task_management_system_test_task.repos.TaskRepository;
import com.example.Task_management_system_test_task.repos.UserRepository;
import com.example.Task_management_system_test_task.tables.Task;
import com.example.Task_management_system_test_task.tables.User;
import com.example.Task_management_system_test_task.utils.DtoMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.Task_management_system_test_task.consts.ExceptionMessagesConsts.*;

@Service
@AllArgsConstructor
public class TaskService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public void createTask(Integer currentUserId, TaskCreateRequestDto requestDto) {
        User creator = userRepository.findById(currentUserId).orElseThrow(() -> new BadRequestException(USER_NOT_FOUND));
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

    public void deleteTask(Integer taskId) {
        taskRepository.deleteById(taskId);
    }

    public void updateTask(TaskUpdateByAdminRequestDto requestDto) {
        Task task = taskRepository.findById(requestDto.getId())
                .orElseThrow(() -> new BadRequestException(TASK_NOT_FOUND));
        TaskPriorityEnum priority = TaskPriorityEnum.getByIndex(requestDto.getPriorityIndex());
        User implementer = userRepository.findById(requestDto.getImplementerId()).orElseThrow(() -> new BadRequestException(USER_NOT_FOUND));

        setTaskWithDto(task, requestDto);

        task.setPriority(priority);
        task.setImplementer(implementer);

        taskRepository.save(task);
    }

    public void updateTask(TaskUpdateRequestDto requestDto, Integer currentUserId) {
        Task task = taskRepository.findById(requestDto.getId())
                .orElseThrow(() -> new BadRequestException(TASK_NOT_FOUND));

        setTaskWithDto(task, requestDto);

        if (!currentUserId.equals(task.getImplementer().getId())) {
            throw new AccessDeniedException(ACCESS_DENIED);
        }

        taskRepository.save(task);
    }

    private void setTaskWithDto(Task task, TaskUpdateRequestDto requestDto) {
        TaskStatusEnum status = TaskStatusEnum.getByIndex(requestDto.getStatusIndex());

        task.setStatus(status);
    }

    public List<TaskGetResponseDto> getTasksByCreatorId(Integer creatorId) {
        return taskRepository
                .findByCreatorIdWithComments(creatorId)
                .stream().map(DtoMapper::taskToTaskGetResponseDto).toList();
    }

    public List<TaskGetResponseDto> getTaskByImplementerId(Integer implementerId) {
        return taskRepository
                .findByImplementerIdWithComments(implementerId)
                .stream().map(DtoMapper::taskToTaskGetResponseDto).toList();
    }
}
