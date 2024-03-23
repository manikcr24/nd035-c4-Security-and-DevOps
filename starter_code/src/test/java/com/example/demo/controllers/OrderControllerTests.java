package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderControllerTests {


    public UserRepository userRepository = Mockito.mock(UserRepository.class);
    public OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
    private OrderController orderController;
    @Before
    public void init() {
        this.orderController = new OrderController();
        TestUtils.inject(orderController, "userRepository", userRepository);
        TestUtils.inject(orderController, "orderRepository", orderRepository);

    }

    private User user;
    private Cart cart;

    private List<Item> items;
    @Before
    public void initiatialiseData() {
        List<Item> items = new ArrayList<>();
        Item item = Item.builder().id(1l).price(new BigDecimal("10000")).name("Laptop").description("Computers").build();
        items.add(item);
        Cart cart = Cart.builder()
                .total(new BigDecimal("10000"))
                .id(1l)
                .items(items)
                .build();

        User manikanta = User.builder()
                .id(1)
                .username("manikanta")
                .cart(cart)
                .build();

        this.user = manikanta;
        this.cart = cart;
        this.items = items;
    }

    @Before
    public void initMocks() {
        Mockito.when(userRepository.findByUsername("manikanta")).thenReturn(this.user);
    }

    @Test
    public void submit_order_test() {
        ResponseEntity<UserOrder> userOrderResponseEntity = orderController.submit("manikanta");
        Assert.assertEquals(200, userOrderResponseEntity.getStatusCodeValue());
    }

    @Test
    public void submit_order_user_not_found_test() {
        Mockito.when(userRepository.findByUsername("manikanta")).thenReturn(null);
        ResponseEntity<UserOrder> userOrderResponseEntity = orderController.submit("manikanta");
        Assert.assertEquals(404, userOrderResponseEntity.getStatusCodeValue());
    }

    @Test
    public void get_order_history_test() {
        UserOrder userOrder = UserOrder.builder()
                .user(this.user)
                .id(1l)
                .total(new BigDecimal(10000))
                .items(this.items)
                .build();
        List<UserOrder> userOrders = new ArrayList<>();
        userOrders.add(userOrder);

        Mockito.when(orderRepository.findByUser(this.user)).thenReturn(userOrders);
        ResponseEntity<List<UserOrder>> ordersResponseEntity = orderController.getOrdersForUser("manikanta");
        Assert.assertEquals(200, ordersResponseEntity.getStatusCodeValue());

    }

    @Test
    public void get_order_history_user_not_found_test() {
        Mockito.when(userRepository.findByUsername("manikanta")).thenReturn(null);
        ResponseEntity<List<UserOrder>> ordersResponseEntity = orderController.getOrdersForUser("manikanta");
        Assert.assertEquals(404, ordersResponseEntity.getStatusCodeValue());

    }
}
