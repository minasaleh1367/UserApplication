package com.example.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.user.exception.UserNotFoundException;
import com.example.user.entity.Users;
import com.example.user.repository.UserRepository;
import com.example.user.service.UserService;

import java.util.Optional;

@SpringBootTest
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private Users testUser;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        testUser = new Users();
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setActive(true);
        testUser = userRepository.save(testUser);
    }

    @Test
    public void testCreateUser() {
        Users newUser = new Users();
        newUser.setUsername("newuser");
        newUser.setPassword("password");
        newUser.setActive(true);

        Users createdUser = userService.createUser(newUser);

        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getUsername()).isEqualTo("newuser");
    }

    @Test
    public void testGetUser() {
        Optional<Users> foundUser = userService.getUserById(testUser.getId());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("testuser");
    }

    @Test
    public void testUpdateUser() {
        testUser.setPassword("newpassword");
        testUser.setActive(false);

        Users updatedUser = userService.updateUser(testUser.getId(), testUser);

        assertThat(updatedUser.getPassword()).isEqualTo("newpassword");
        assertThat(updatedUser.isActive()).isFalse();
    }

    @Test
    public void testDeleteUser() {
        userService.deleteUser(testUser.getId());

        Optional<Users> deletedUser = userRepository.findById(testUser.getId());

        assertThat(deletedUser).isNotPresent();
    }

    @Test
    public void testDeleteUserNotFound() {
        Long nonExistentId = 999L;

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(nonExistentId));
    }
}
