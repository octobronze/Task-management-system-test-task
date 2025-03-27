package com.example.Task_management_system_test_task.unit_tests.services;

import com.example.Task_management_system_test_task.dtos.LoginRequestDto;
import com.example.Task_management_system_test_task.dtos.LoginResponseDto;
import com.example.Task_management_system_test_task.repos.TaskRepository;
import com.example.Task_management_system_test_task.repos.UserRepository;
import com.example.Task_management_system_test_task.security.JwtService;
import com.example.Task_management_system_test_task.security.UserPrincipal;
import com.example.Task_management_system_test_task.services.AuthService;
import com.example.Task_management_system_test_task.tables.Role;
import com.example.Task_management_system_test_task.tables.Task;
import com.example.Task_management_system_test_task.tables.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.stream.Stream;

class AuthServiceUnitTests {
	private final JwtService jwtService = Mockito.mock(JwtService.class);
	private final TaskRepository taskRepository = Mockito.mock(TaskRepository.class);
	private final UserRepository userRepository = Mockito.mock(UserRepository.class);
	private final PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);

	private final AuthService authService = new AuthService(jwtService, taskRepository, userRepository, passwordEncoder);

	private final static Long JWT_DURATION = 86400L;

	@BeforeEach
	public void setUpPropertyVariables() {
		ReflectionTestUtils.setField(authService, "jwtDuration", JWT_DURATION);
	}

	@ParameterizedTest
	@MethodSource("provideData_for_test_login_existingUser_success")
	public void test_login_existingUser_success(User user, LoginRequestDto requestDto, UserPrincipal userPrincipal, String jwtToken) {
		Mockito.when(userRepository.findOne(Mockito.any(Specification.class))).thenReturn(Optional.of(user));
		Mockito.when(passwordEncoder.matches(requestDto.getPassword(), user.getPassword())).thenReturn(true);
		Mockito.when(jwtService.generateTokenByUserPrincipal(Mockito.argThat(
				new UserPrincipalMatcher(userPrincipal, user.getRole().getName())
		))).thenReturn(jwtToken);

		LoginResponseDto responseDto = authService.login(requestDto);

		Mockito.verify(userRepository, Mockito.times(1)).findOne(Mockito.any(Specification.class));
		Mockito.verify(passwordEncoder, Mockito.times(1)).matches(requestDto.getPassword(), user.getPassword());
		Mockito.verify(jwtService, Mockito.times(1)).generateTokenByUserPrincipal(Mockito.argThat(
				new UserPrincipalMatcher(userPrincipal, user.getRole().getName())
		));

		Assertions.assertNotNull(responseDto.getToken());
		Assertions.assertNotNull(responseDto.getLifeTimeSec());
		Assertions.assertEquals(jwtToken, responseDto.getToken());
		Assertions.assertEquals(JWT_DURATION, responseDto.getLifeTimeSec());
	}

	@ParameterizedTest
	@MethodSource("provideData_for_test_hasAccessToCreateComment_admin_true")
	public void test_hasAccessToCreateComment_admin_true(UserPrincipal userPrincipal, Integer taskId) {
		boolean response = authService.hasAccessToCreateComment(userPrincipal, taskId);

		Assertions.assertTrue(response);
	}

	@ParameterizedTest
	@MethodSource("provideData_for_test_hasAccessToCreateComment_implementer_true")
	public void test_hasAccessToCreateComment_implementer_true(UserPrincipal userPrincipal, Integer taskId, Task task) {
		Mockito.when(taskRepository.findOne(Mockito.any(Specification.class))).thenReturn(Optional.ofNullable(task));

		boolean response = authService.hasAccessToCreateComment(userPrincipal, taskId);

		Mockito.verify(taskRepository, Mockito.times(1)).findOne(Mockito.any(Specification.class));

		Assertions.assertTrue(response);
	}

	@ParameterizedTest
	@MethodSource("provideData_for_test_hasAccessToCreateComment_notImplementer_false")
	public void test_hasAccessToCreateComment_notImplementer_false(UserPrincipal userPrincipal, Integer taskId, Task task) {
		Mockito.when(taskRepository.findOne(Mockito.any(Specification.class))).thenReturn(Optional.ofNullable(task));

		boolean response = authService.hasAccessToCreateComment(userPrincipal, taskId);

		Mockito.verify(taskRepository, Mockito.times(1)).findOne(Mockito.any(Specification.class));

		Assertions.assertFalse(response);
	}

	@ParameterizedTest
	@MethodSource("provideData_for_test_hasAccessToUpdateTask_implementer_true")
	public void test_hasAccessToUpdateTask_implementer_true(UserPrincipal userPrincipal, Integer taskId, Task task) {
		Mockito.when(taskRepository.findOne(Mockito.any(Specification.class))).thenReturn(Optional.ofNullable(task));

		boolean response = authService.hasAccessToUpdateTask(userPrincipal, taskId);

		Mockito.verify(taskRepository, Mockito.times(1)).findOne(Mockito.any(Specification.class));

		Assertions.assertTrue(response);
	}

	@ParameterizedTest
	@MethodSource("provideData_for_test_hasAccessToUpdateTask_notImplementer_false")
	public void test_hasAccessToUpdateTask_notImplementer_false(UserPrincipal userPrincipal, Integer taskId, Task task) {
		Mockito.when(taskRepository.findOne(Mockito.any(Specification.class))).thenReturn(Optional.ofNullable(task));

		boolean response = authService.hasAccessToUpdateTask(userPrincipal, taskId);

		Mockito.verify(taskRepository, Mockito.times(1)).findOne(Mockito.any(Specification.class));

		Assertions.assertFalse(response);
	}

	private static Stream<Arguments> provideData_for_test_hasAccessToCreateComment_implementer_true() {
		User user = new User();
		user.setId(1);
		user.setRole(new Role() {
			{
				setName("User");
			}
		});

		UserPrincipal userPrincipal = new UserPrincipal(user);

		Integer taskId = 1;
		Task task = new Task();
		task.setImplementer(user);
		task.setId(taskId);

		return Stream.of(
				Arguments.of(userPrincipal, taskId, task)
		);
	}

	private static Stream<Arguments> provideData_for_test_hasAccessToUpdateTask_implementer_true() {
		User user = new User();
		user.setId(1);
		user.setRole(new Role() {
			{
				setName("User");
			}
		});

		UserPrincipal userPrincipal = new UserPrincipal(user);

		Integer taskId = 1;
		Task task = new Task();
		task.setImplementer(user);
		task.setId(taskId);

		return Stream.of(
				Arguments.of(userPrincipal, taskId, task)
		);
	}

	private static Stream<Arguments> provideData_for_test_hasAccessToCreateComment_notImplementer_false() {
		User user = new User();
		user.setId(1);
		user.setRole(new Role() {
			{
				setName("User");
			}
		});

		UserPrincipal userPrincipal = new UserPrincipal(user);

		Integer taskId = 1;

		User implementer = new User();
		implementer.setId(2);

		Task task = new Task();
		task.setImplementer(implementer);
		task.setId(taskId);

		return Stream.of(
				Arguments.of(userPrincipal, taskId, task)
		);
	}

	private static Stream<Arguments> provideData_for_test_hasAccessToUpdateTask_notImplementer_false() {
		User user = new User();
		user.setId(1);
		user.setRole(new Role() {
			{
				setName("User");
			}
		});

		UserPrincipal userPrincipal = new UserPrincipal(user);

		Integer taskId = 1;

		User implementer = new User();
		implementer.setId(2);

		Task task = new Task();
		task.setImplementer(implementer);
		task.setId(taskId);

		return Stream.of(
				Arguments.of(userPrincipal, taskId, task)
		);
	}

	private static Stream<Arguments> provideData_for_test_hasAccessToCreateComment_admin_true() {
		User admin = new User();
		admin.setRole(new Role() {
			{
				setName("Admin");
			}
		});

		UserPrincipal adminPrincipal = new UserPrincipal(admin);

		Integer taskId = 1;

		return Stream.of(
				Arguments.of(adminPrincipal, taskId)
		);
	}

	private static Stream<Arguments> provideData_for_test_login_existingUser_success() {
		User user = new User();
		user.setId(1);
		user.setEmail("mail@mail.ru");
		user.setPassword("1234");
		user.setRole(new Role() {
			{
				setName("user");
			}
		});

		UserPrincipal userPrincipal = new UserPrincipal(user);

		LoginRequestDto loginRequestDto = new LoginRequestDto();
		loginRequestDto.setPassword("1234");

		String jwtToken = "asdlasldlasd";

		return Stream.of(
				Arguments.of(user, loginRequestDto, userPrincipal, jwtToken)
		);
	}

	private static class UserPrincipalMatcher implements ArgumentMatcher<UserPrincipal> {
		private final UserPrincipal left;

		public UserPrincipalMatcher(UserPrincipal left, String roleName) {
			this.left = new UserPrincipal();

			this.left.setId(left.getId());
			this.left.setUsername(left.getUsername());
			this.left.setPassword(left.getPassword());
			this.left.setAuthorities(roleName);
		}

		@Override
		public boolean matches(UserPrincipal right) {
			return left.getId().equals(right.getId())
					&& left.getUsername().equals(right.getUsername())
					&& left.getPassword().equals(right.getPassword())
					&& left.getAuthorities().equals(right.getAuthorities());
		}
	}
}
