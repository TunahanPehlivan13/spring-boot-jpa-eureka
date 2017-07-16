package com.app;

import com.app.service.ItemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RestApplication.class)
@WebAppConfiguration
public class RestApplicationTest {

    @Autowired
    private ItemService itemService;

    @Test
    public void contexLoads() throws Exception {
        assertNotNull(itemService);
    }
}