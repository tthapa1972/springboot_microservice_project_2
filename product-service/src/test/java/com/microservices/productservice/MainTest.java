package com.microservices.productservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class MainTest {

    Main main;

    @BeforeEach
    public void setUp(){
        main = new Main();
    }
    @Test
    public void shouldCapitilizeInitialLetter(){
        List<String> list = new ArrayList<>();

        list.add("apple");
        list.add("banana");
        list.add("cherry");

        List<String> res = main.capitalizeInitialLetter(list);


        Assertions.assertNotNull(res);
        Assertions.assertTrue(res.size() > 0);
        Assertions.assertEquals(res.size(), list.size());
        for(int i = 0;i < res.size();i++) {
            String actual = res.get(i);
            String expected = list.get(i);
            Assertions.assertEquals(actual.charAt(0), Character.toUpperCase(expected.charAt(0)));
        }
    }
}
