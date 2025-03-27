package com.example.Task_management_system_test_task.unit_tests.services;


import com.example.Task_management_system_test_task.dtos.*;
import com.example.Task_management_system_test_task.enums.TaskPriorityEnum;
import com.example.Task_management_system_test_task.enums.TaskStatusEnum;
import com.example.Task_management_system_test_task.repos.TaskRepository;
import com.example.Task_management_system_test_task.repos.UserRepository;
import com.example.Task_management_system_test_task.services.TaskService;
import com.example.Task_management_system_test_task.tables.Comment;
import com.example.Task_management_system_test_task.tables.Task;
import com.example.Task_management_system_test_task.tables.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;
import java.util.stream.Stream;

public class TaskServiceUnitTests {
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final TaskRepository taskRepository = Mockito.mock(TaskRepository.class);

    private final TaskService taskService = new TaskService(userRepository, taskRepository);

    private static final Integer PRIORITY_INDEX = 0;
    private static final Integer STATUS_INDEX = 0;
    private final static Integer IMPLEMENTER_ID = 2;
    private final static Integer TASK_ID = 1;
    private final static String TASK_TITLE = "title";
    private static final String TASK_DESCRIPTION = "description";
    private static final Integer CURRENT_USER_ID = 1;
    private static final Integer CREATOR_ID = 1;
    private static final Integer PAGE_SIZE = 1;
    private static final Integer PAGE_INDEX = 0;
    private static final String COMMENT_VALUE = "comment";
    private static final Integer COMMENT_ID = 1;

    private static Task TASK;
    private static Task TASK_NO_IMPLEMENTER;
    private static TaskUpdateByAdminRequestDto TASK_UPDATE_BY_ADMIN_REQUEST_DTO;
    private static TaskUpdateByAdminRequestDto TASK_UPDATE_BY_ADMIN_REQUEST_DTO_NO_IMPLEMENTER;
    private static TaskCreateRequestDto TASK_CREATE_REQUEST_DTO;
    private static TaskUpdateRequestDto TASK_UPDATE_REQUEST_DTO;
    private static User IMPLEMENTER;
    private static User CREATOR;
    private static List<Task> TASKS;
    private static TaskFilterRequestDto TASK_FILTER_REQUEST_DTO;
    private static Comment COMMENT;
    private static Set<Comment> COMMENTS;

    @BeforeAll
    public static void initBeforeAll() {
        init();
    }

    @BeforeEach
    public void initBeforeEach() {
        init();
    }

    private static void init() {
        TASK_UPDATE_BY_ADMIN_REQUEST_DTO = new TaskUpdateByAdminRequestDto();
        TASK_UPDATE_BY_ADMIN_REQUEST_DTO.setId(TASK_ID);
        TASK_UPDATE_BY_ADMIN_REQUEST_DTO.setPriorityIndex(PRIORITY_INDEX);
        TASK_UPDATE_BY_ADMIN_REQUEST_DTO.setStatusIndex(STATUS_INDEX);
        TASK_UPDATE_BY_ADMIN_REQUEST_DTO.setImplementerId(IMPLEMENTER_ID);

        TASK_UPDATE_BY_ADMIN_REQUEST_DTO_NO_IMPLEMENTER = new TaskUpdateByAdminRequestDto();
        TASK_UPDATE_BY_ADMIN_REQUEST_DTO_NO_IMPLEMENTER.setId(TASK_ID);
        TASK_UPDATE_BY_ADMIN_REQUEST_DTO_NO_IMPLEMENTER.setPriorityIndex(PRIORITY_INDEX);
        TASK_UPDATE_BY_ADMIN_REQUEST_DTO_NO_IMPLEMENTER.setStatusIndex(STATUS_INDEX);

        TASK_UPDATE_REQUEST_DTO = new TaskUpdateRequestDto();
        TASK_UPDATE_REQUEST_DTO.setId(TASK_ID);
        TASK_UPDATE_REQUEST_DTO.setStatusIndex(STATUS_INDEX);

        TASK_CREATE_REQUEST_DTO = new TaskCreateRequestDto();
        TASK_CREATE_REQUEST_DTO.setDescription(TASK_DESCRIPTION);
        TASK_CREATE_REQUEST_DTO.setTitle(TASK_TITLE);
        TASK_CREATE_REQUEST_DTO.setImplementerId(IMPLEMENTER_ID);
        TASK_CREATE_REQUEST_DTO.setPriorityIndex(PRIORITY_INDEX);

        IMPLEMENTER = new User();
        IMPLEMENTER.setId(IMPLEMENTER_ID);

        CREATOR = new User();
        CREATOR.setId(CREATOR_ID);

        COMMENT = new Comment();
        COMMENT.setId(COMMENT_ID);
        COMMENT.setTask(TASK);
        COMMENT.setValue(COMMENT_VALUE);
        COMMENT.setUser(CREATOR);

        COMMENTS = new HashSet<>() {
            {
                add(COMMENT);
            }
        };

        TASK = new Task();
        TASK.setPriority(TaskPriorityEnum.getByIndex(PRIORITY_INDEX));
        TASK.setStatus(TaskStatusEnum.getByIndex(STATUS_INDEX));
        TASK.setImplementer(IMPLEMENTER);
        TASK.setCreator(CREATOR);
        TASK.setTitle(TASK_TITLE);
        TASK.setDescription(TASK_DESCRIPTION);
        TASK.setPriority(TaskPriorityEnum.getByIndex(PRIORITY_INDEX));
        TASK.setStatus(TaskStatusEnum.getByIndex(STATUS_INDEX));
        TASK.setComments(COMMENTS);

        TASK_NO_IMPLEMENTER = new Task();
        TASK_NO_IMPLEMENTER.setPriority(TaskPriorityEnum.getByIndex(PRIORITY_INDEX));
        TASK_NO_IMPLEMENTER.setStatus(TaskStatusEnum.getByIndex(STATUS_INDEX));

        TASKS = new ArrayList<>();
        TASKS.add(TASK);

        TASK_FILTER_REQUEST_DTO = new TaskFilterRequestDto();
        TASK_FILTER_REQUEST_DTO.setCreatorId(CREATOR_ID);
        TASK_FILTER_REQUEST_DTO.setImplementerId(IMPLEMENTER_ID);
        TASK_FILTER_REQUEST_DTO.setPageIndex(PAGE_INDEX);
        TASK_FILTER_REQUEST_DTO.setPageSize(PAGE_SIZE);
    }

    @ParameterizedTest
    @MethodSource("provideData_for_test_createTask_validParams_success")
    public void test_createTask_validParams_success(Integer currentUserId, TaskCreateRequestDto requestDto, User creator, User implementer, Task task) {
        Mockito.when(userRepository.findById(currentUserId)).thenReturn(Optional.ofNullable(creator));
        Mockito.when(userRepository.findById(requestDto.getImplementerId())).thenReturn(Optional.ofNullable(implementer));
        Mockito.when(taskRepository.save(Mockito.argThat(new TaskMatcher(task)))).thenReturn(task);

        taskService.createTask(currentUserId, requestDto);

        Mockito.verify(userRepository, Mockito.times(1)).findById(currentUserId);
        Mockito.verify(userRepository, Mockito.times(1)).findById(requestDto.getImplementerId());
        Mockito.verify(taskRepository, Mockito.times(1)).save(Mockito.argThat(new TaskMatcher(task)));
    }

    @Test
    public void test_deleteTask_existingTask_success() {
        taskService.deleteTask(TASK_ID);

        Mockito.verify(taskRepository, Mockito.times(1)).deleteById(TASK_ID);
    }

    @ParameterizedTest
    @MethodSource("provideData_for_test_updateTask_admin_success")
    public void test_updateTask_byAdmin_success(TaskUpdateByAdminRequestDto requestDto, Task task, User implementer) {
        Mockito.when(taskRepository.findOne(Mockito.any(Specification.class))).thenReturn(Optional.ofNullable(task));
        Mockito.when(userRepository.findById(requestDto.getImplementerId())).thenReturn(Optional.ofNullable(implementer));
        Mockito.when(taskRepository.save(Mockito.argThat(new TaskMatcher(task)))).thenReturn(task);

        taskService.updateTask(requestDto);

        Mockito.verify(taskRepository, Mockito.times(1)).findOne(Mockito.any(Specification.class));
        if (requestDto.getImplementerId() != null) {
            Mockito.verify(userRepository, Mockito.times(1)).findById(requestDto.getImplementerId());
        }
        Mockito.verify(taskRepository, Mockito.times(1)).save(Mockito.argThat(new TaskMatcher(task)));
    }

    @ParameterizedTest
    @MethodSource("provideData_for_test_updateTask_byUser_success")
    public void test_updateTask_byUser_success(TaskUpdateRequestDto requestDto, Task task) {
        Mockito.when(taskRepository.findById(requestDto.getId())).thenReturn(Optional.ofNullable(task));
        Mockito.when(taskRepository.save(Mockito.argThat(new TaskMatcher(task)))).thenReturn(task);

        taskService.updateTask(requestDto);

        Mockito.verify(taskRepository, Mockito.times(1)).findById(requestDto.getId());
        Mockito.verify(taskRepository, Mockito.times(1)).save(Mockito.argThat(new TaskMatcher(task)));
    }

    @ParameterizedTest
    @MethodSource("provideData_for_test_getTasksWithFilters_validParams_success")
    public void test_getTasksWithFilters_validParams_success(TaskFilterRequestDto taskFilterRequestDto, List<Task> tasks) {
        Mockito.when(taskRepository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class)))
                .thenReturn(toPage(tasks, PAGE_INDEX, PAGE_SIZE));

        Page<TaskGetResponseDto> response = taskService.getTasksWithFilters(taskFilterRequestDto);

        Mockito.verify(taskRepository, Mockito.times(1)).findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class));

        TaskGetResponseDto responseDto = response.getContent().get(0);
        Task task = tasks.get(0);

        Assertions.assertEquals(responseDto.getId(), task.getId());
        Assertions.assertEquals(responseDto.getCreatorId(), task.getCreator().getId());
        Assertions.assertEquals(responseDto.getImplementerId(), task.getImplementer().getId());
        Assertions.assertEquals(responseDto.getTitle(), task.getTitle());
        Assertions.assertEquals(responseDto.getDescription(), task.getDescription());
        Assertions.assertEquals(responseDto.getPriorityIndex(), task.getPriority().ordinal());
        Assertions.assertEquals(responseDto.getStatusIndex(), task.getStatus().ordinal());

        if (responseDto.getComments() != null) {
            Map<Integer, Comment> commentByIdMap = new HashMap<>();
            for (var comment : task.getComments()) {
                commentByIdMap.put(comment.getId(), comment);
            }

            for(var commentDto : responseDto.getComments()) {
                Assertions.assertTrue(commentByIdMap.containsKey(commentDto.getId()));

                Comment commentFromMap = commentByIdMap.get(commentDto.getId());

                Assertions.assertEquals(commentDto.getId(), commentFromMap.getId());
                Assertions.assertEquals(commentDto.getValue(), commentFromMap.getValue());
                Assertions.assertEquals(commentDto.getUserId(), commentFromMap.getUser().getId());
            }
        }
    }

    private static Stream<Arguments> provideData_for_test_getTasksWithFilters_validParams_success() {
        return Stream.of(
                Arguments.of(TASK_FILTER_REQUEST_DTO, TASKS)
        );
    }


    private static Stream<Arguments> provideData_for_test_updateTask_byUser_success() {
        return Stream.of(
                Arguments.of(TASK_UPDATE_REQUEST_DTO, TASK)
        );
    }

    private static Stream<Arguments> provideData_for_test_updateTask_admin_success() {
        return Stream.of(
                Arguments.of(
                        TASK_UPDATE_BY_ADMIN_REQUEST_DTO, TASK, IMPLEMENTER,
                        TASK_UPDATE_BY_ADMIN_REQUEST_DTO_NO_IMPLEMENTER, TASK_NO_IMPLEMENTER, null
                )
        );
    }

    private static Stream<Arguments> provideData_for_test_createTask_validParams_success() {

        return Stream.of(
                Arguments.of(CURRENT_USER_ID, TASK_CREATE_REQUEST_DTO, CREATOR, IMPLEMENTER, TASK)
        );
    }

    private static class TaskMatcher implements ArgumentMatcher<Task> {
        private final Task left;

        public TaskMatcher(Task left) {
            this.left = new Task();

            this.left.setPriority(left.getPriority());
            this.left.setStatus(left.getStatus());
            this.left.setCreator(left.getCreator());
            this.left.setImplementer(left.getImplementer());
            this.left.setTitle(left.getTitle());
            this.left.setDescription(left.getDescription());
        }

        @Override
        public boolean matches(Task right) {
            return left.getPriority().equals(right.getPriority())
                    && left.getStatus().equals(right.getStatus())
                    && left.getCreator().getId().equals(right.getCreator().getId())
                    && left.getImplementer().getId().equals(right.getImplementer().getId())
                    && left.getTitle().equals(right.getTitle())
                    && left.getDescription().equals(right.getDescription());
        }
    }

    private static <T> Page<T> toPage(List<T> list, Integer pageIndex, Integer pageSize) {
        return new PageImpl<>(list.subList(pageIndex * pageSize, Math.min(list.size(), (pageIndex + 1) * pageSize)));
    }
}
