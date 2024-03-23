package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CartControllerTests {

    private UserRepository userRepository = Mockito.mock(UserRepository.class);
    private CartRepository cartRepository = Mockito.mock(CartRepository.class);
    private ItemRepository itemRepository = Mockito.mock(ItemRepository.class);

    public CartController cartController;

    @Before
    public void init() {
        this.cartController = new CartController();
        TestUtils.inject(cartController, "userRepository", userRepository);
        TestUtils.inject(cartController, "cartRepository", cartRepository);
        TestUtils.inject(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void add_item_to_cart_test() {
        ModifyCartRequest modifyCartRequest = ModifyCartRequest.builder()
                .itemId(1)
                .username("manikanta")
                .quantity(1)
                .build();

        Cart cart = new Cart();
        cart.setId(1l);
        List<Item> items = new ArrayList<>();
        items.add(Item.builder().id(1l).price(new BigDecimal("145934")).name("Laptop").description("Computers").build());
        cart.setItems(items);
        User manikanta = User.builder().id(1)
                            .cart(cart)
                            .username("manikanta").build();

        BigDecimal price = new BigDecimal("145934");
        Item item = Item.builder()
                .name("iPhone")
                .price(price)
                .description("Appple iPhone")
                .id(1l)
                .build();
        Mockito.when(userRepository.findByUsername("manikanta")).thenReturn(manikanta);
        Mockito.when(itemRepository.findById(modifyCartRequest.getItemId())).thenReturn(Optional.ofNullable(item));
        Mockito.when(cartRepository.save(Mockito.any())).thenReturn(cart);

        ResponseEntity<Cart> cartResponseEntity = cartController.addTocart(modifyCartRequest);
        Assert.assertEquals(200, cartResponseEntity.getStatusCodeValue());
        cart = cartResponseEntity.getBody();
        BigDecimal total = cart.getTotal();
        Assert.assertEquals(price, total);
    }

    @Test
    public void remove_item_from_cart_test() {
        ModifyCartRequest modifyCartRequest = ModifyCartRequest.builder()
                .itemId(1)
                .username("manikanta")
                .quantity(1)
                .build();

        User manikanta = User.builder().id(1)
                .username("manikanta").build();
        List<Item> items = new ArrayList<>();
        Item item = Item.builder().id(1l).price(new BigDecimal("10000")).name("Laptop").description("Computers").build();
        items.add(item);

        Cart cart = Cart.builder()
                .user(manikanta)
                .total(new BigDecimal("10000"))
                .id(1l)
                .items(items)
                .build();
        manikanta.setCart(cart);

        Mockito.when(userRepository.findByUsername("manikanta")).thenReturn(manikanta);
        Mockito.when(itemRepository.findById(modifyCartRequest.getItemId())).thenReturn(Optional.ofNullable(item));
        Mockito.when(cartRepository.save(Mockito.any())).thenReturn(cart);

        ResponseEntity<Cart> cartResponseEntity = cartController.removeFromcart(modifyCartRequest);
        Assert.assertEquals(200, cartResponseEntity.getStatusCodeValue());
        cart = cartResponseEntity.getBody();
        BigDecimal total = cart.getTotal();
        Assert.assertEquals(new BigDecimal(0), total);
    }
}
