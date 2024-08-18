package com.example.user;

import com.example.user.entity.Users;
import com.example.user.repository.UserRepository;
import com.example.user.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testCreateUser() {
        Users user = new Users();
        user.setUsername("username");
        user.setPassword("password");
        user.setActive(true);

        when(userRepository.save(user)).thenReturn(user);

        Users createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals("username", createdUser.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testUpdateUser() {
        Long userId = 1L;
        Users existingUser = new Users();
        existingUser.setUsername("username");
        existingUser.setPassword("password");
        existingUser.setActive(true);

        Users updatedUser = new Users();
        updatedUser.setUsername("username");
        updatedUser.setPassword("newPassword");
        updatedUser.setActive(false);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(Users.class))).thenReturn(updatedUser);

        Users result = userService.updateUser(userId, updatedUser);

        assertEquals("newPassword", result.getPassword());
        assertFalse(result.isActive());
        verify(userRepository, times(2)).findById(userId);
        verify(userRepository, times(1)).save(any(Users.class));
    }

    @Test
    public void testDeleteUser() {
        Long userId = 1L;
        Users user = new Users();
        user.setId(userId);
        user.setUsername("username");
        user.setPassword("password");
        user.setActive(true);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(userId);

        userService.deleteUser(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }
}

