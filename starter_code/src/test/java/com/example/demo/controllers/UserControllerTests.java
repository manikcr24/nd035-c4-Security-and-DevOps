package com.example.demo.controllers;


import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class UserControllerTests {

    public UserRepository userRepository = Mockito.mock(UserRepository.class);
    public CartRepository cartRepository = Mockito.mock(CartRepository.class);
    public BCryptPasswordEncoder bCryptPasswordEncoder = Mockito.mock(BCryptPasswordEncoder.class);


    public UserController userController;

    @Before
    public void initialise() {
        this.userController = new UserController();
        TestUtils.inject(userController, "userRepository", userRepository);
        TestUtils.inject(userController, "cartRepository", cartRepository);
        TestUtils.inject(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }

    @Test
    public void create_user_test() {
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(User.builder()
                .id(0)
                .username("manikanta").build());

        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .username("manikanta")
                .password("password1234")
                .confirmPassword("password1234")
                .build();

        ResponseEntity<User> userResponseEntity = userController.createUser(createUserRequest);
        Assert.assertEquals(200, userResponseEntity.getStatusCodeValue());
        Assert.assertNotNull(userResponseEntity);
        User body = userResponseEntity.getBody();
        Assert.assertEquals(0, body.getId());
        Assert.assertEquals("manikanta", body.getUsername());


    }
}
