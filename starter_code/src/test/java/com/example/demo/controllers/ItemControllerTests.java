package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;

public class ItemControllerTests {

    public ItemController itemController;

    public ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void init() {
        itemController = new ItemController();
        TestUtils.inject(itemController, "itemRepository", itemRepository);

    }

    private Item item1;
    private Item item2;

    private List<Item> items;
    @Before
    public void initialiseData() {
        Item item1 = Item.builder()
                .id(1l)
                .name("iPhone")
                .price(new BigDecimal("100000"))
                .description("Apple iPhone")
                .build();

        Item item2 = Item.builder()
                .id(2l)
                .name("Mac")
                .price(new BigDecimal("150000"))
                .description("Apple MacBook Pro")
                .build();

        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);

        this.item1 = item1;
        this.item2 = item2;
        this.items = items;
    }

    @Test
    public void get_all_items_test() {
        Mockito.when(itemRepository.findAll()).thenReturn(this.items);
        ResponseEntity<List<Item>> itemsResponseEntity = itemController.getItems();

        Assert.assertEquals(200, itemsResponseEntity.getStatusCodeValue());
        List<Item> items = itemsResponseEntity.getBody();


        Assert.assertEquals(2, items.size());
        Assert.assertEquals(this.items.get(0), items.get(0));
        Assert.assertEquals(this.items.get(1), items.get(1));
    }

    @Test
    public void get_item_by_id_test() {
        Mockito.when(itemRepository.findById(1l)).thenReturn(Optional.ofNullable(this.item1));
        ResponseEntity<Item> itemResponseEntity = itemController.getItemById(1l);

        Assert.assertEquals(200, itemResponseEntity.getStatusCodeValue());
        Item item = itemResponseEntity.getBody();

        Assert.assertEquals(this.item1, item);
    }

    @Test
    public void get_items_by_name_test() {
        List<Item> items = new ArrayList<>();
        items.add(item1);
        Mockito.when(itemRepository.findByName("iPhone")).thenReturn(items);

        ResponseEntity<List<Item>> itemsResponseEntity = itemController.getItemsByName("iPhone");
        Assert.assertEquals(200, itemsResponseEntity.getStatusCodeValue());

        List<Item> responseItems = itemsResponseEntity.getBody();


        Assert.assertEquals(1, responseItems.size());
//        Assert.assertEquals(this.item1, responseItems.get(0));
        Assert.assertTrue(this.item1.equals(responseItems.get(0)));
        Assert.assertFalse(this.item2.equals(responseItems.get(0)));

    }
}
