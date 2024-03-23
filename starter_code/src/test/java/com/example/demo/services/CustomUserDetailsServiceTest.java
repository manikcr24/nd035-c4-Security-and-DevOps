package com.example.demo.services;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetailsServiceTest {

    public CustomUserDetailsService customUserDetailsService;

    public UserRepository userRepository = Mockito.mock(UserRepository.class);


    @Before
    public void init() {
        this.customUserDetailsService = new CustomUserDetailsService();
        TestUtils.inject(customUserDetailsService, "userRepository", userRepository);
    }

    @Test
    public void load_user_details_test() {
        User manikanta = User.builder()
                .id(1)
                .username("manikanta")
                .password("hashedPass")
                .build();
        Mockito.when(userRepository.findByUsername("manikanta")).thenReturn(manikanta);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("manikanta");
        Assert.assertNotNull(userDetails);
        Assert.assertEquals("manikanta", userDetails.getUsername());
    }

}
