package com.goalglo.services;

import com.goalglo.dto.UserDTO;
import com.goalglo.entities.Role;
import com.goalglo.entities.User;
import com.goalglo.repositories.RoleRepository;
import com.goalglo.repositories.UserRepository;
import com.goalglo.tokens.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private UserService userService;

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_PASSWORD = "testpass123";
    private static final String TEST_ENCODED_PASSWORD = "encoded_password_hash";
    private static final String TEST_JWT_TOKEN = "test.jwt.token";
    private static final int TOKEN_EXPIRATION = 3600;

    private User testUser;

   @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setEmail(TEST_EMAIL);
        testUser.setUsername(TEST_USERNAME);
        testUser.setPassword(TEST_ENCODED_PASSWORD);
        testUser.setEmailVerified(true);

        Role userRole = new Role();
        userRole.setName("TEST_ROLE");
        testUser.setRoles(Set.of(userRole));

      Set<String> testRoles = Set.of("TEST_ROLE");
    }

    @Nested
    @DisplayName("Login Tests")
    class LoginTests {

        @Test
        @DisplayName("Should login successfully with email")
        void shouldLoginSuccessfullyWithEmail() {
            when(userRepository.findByEmailOrUsername(TEST_EMAIL, TEST_EMAIL))
               .thenReturn(Optional.of(testUser));
            when(passwordEncoder.matches(TEST_PASSWORD, TEST_ENCODED_PASSWORD))
               .thenReturn(true);
            when(jwtTokenUtil.generateJwtToken(any(User.class), anySet(), eq(TOKEN_EXPIRATION)))
               .thenReturn(TEST_JWT_TOKEN);

            Optional<UserDTO> result = userService.loginUser(TEST_EMAIL, TEST_PASSWORD);

            assertThat(result).isPresent().hasValueSatisfying(userDTO ->
               assertThat(userDTO.getToken()).isEqualTo(TEST_JWT_TOKEN));
        }

        @Test
        @DisplayName("Should login successfully with username")
        void shouldLoginSuccessfullyWithUsername() {
            when(userRepository.findByEmailOrUsername(TEST_USERNAME, TEST_USERNAME))
               .thenReturn(Optional.of(testUser));
            when(passwordEncoder.matches(TEST_PASSWORD, TEST_ENCODED_PASSWORD))
               .thenReturn(true);
            when(jwtTokenUtil.generateJwtToken(any(User.class), anySet(), eq(TOKEN_EXPIRATION)))
               .thenReturn(TEST_JWT_TOKEN);

            Optional<UserDTO> result = userService.loginUser(TEST_USERNAME, TEST_PASSWORD);

            assertThat(result).isPresent().hasValueSatisfying(userDTO ->
               assertThat(userDTO.getToken()).isEqualTo(TEST_JWT_TOKEN));
        }

        @Test
        @DisplayName("Should fail login when email is not verified")
        void shouldFailLoginWhenEmailNotVerified() {
            testUser.setEmailVerified(false);
            when(userRepository.findByEmailOrUsername(TEST_EMAIL, TEST_EMAIL))
               .thenReturn(Optional.of(testUser));

            assertThatThrownBy(() -> userService.loginUser(TEST_EMAIL, TEST_PASSWORD))
               .isInstanceOf(RuntimeException.class)
               .hasMessage("Email not verified");
        }

        @Test
        @DisplayName("Should return empty when user not found")
        void shouldReturnEmptyWhenUserNotFound() {
            when(userRepository.findByEmailOrUsername(anyString(), anyString()))
               .thenReturn(Optional.empty());

            Optional<UserDTO> result = userService.loginUser("nonexistent@email.com", "anypassword");

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should return empty when password doesn't match")
        void shouldReturnEmptyWhenPasswordDoesntMatch() {
            when(userRepository.findByEmailOrUsername(TEST_EMAIL, TEST_EMAIL))
               .thenReturn(Optional.of(testUser));
            when(passwordEncoder.matches(anyString(), anyString()))
               .thenReturn(false);

            Optional<UserDTO> result = userService.loginUser(TEST_EMAIL, "wrongpassword");

            assertThat(result).isEmpty();
        }
    }
}