package com.app.service;


import com.app.RestApplication;
import com.app.model.Item;
import com.app.repository.ItemRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RestApplication.class)
@WebAppConfiguration
public class ItemServiceTest {

    private MockMvc mockMvc;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        itemRepository.deleteAll();
    }

    @After
    public void destroy() {
        itemRepository.deleteAll();
    }

    @Test
    public void shouldReturnOkWhenAddingNewItem() throws Exception {
        Item item = new Item();
        item.setNumber(1L);
        String itemAsJson = convert(item);

        mockMvc.perform(post("/save")
                .content(itemAsJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk());
    }

    @Test
    public void shouldReturnBadRequestWhenAddingNewItemIfNumberIsAlreadyExist() throws Exception {
        Item persistedItem = new Item();
        persistedItem.setNumber(1L);
        itemRepository.save(persistedItem);

        Item item = new Item();
        item.setNumber(1L);
        String itemAsJson = convert(persistedItem);

        mockMvc.perform(post("/save")
                .content(itemAsJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    public void shouldReturnOkWhenWhenRetrievingItemByNumber() throws Exception {
        Item persistedItem = new Item();
        persistedItem.setNumber(1L);
        itemRepository.save(persistedItem);

        MvcResult responseMvc = mockMvc.perform(get("/retrieve/1"))
                .andExpect(status()
                        .isOk())
                .andReturn();

        String response = responseMvc.getResponse().getContentAsString();
        JSONObject responseObj = new JSONObject(response);
        assertEquals(responseObj.get("number"), 1);
    }

    @Test
    public void shouldReturnNotFoundWhenWhenRetrievingItemIfNumberIsNotFound() throws Exception {
        mockMvc.perform(get("/retrieve/1"))
                .andExpect(status()
                        .isNotFound());
    }

    @Test
    public void shouldReturnOkWhenRetrievingMaxNumber() throws Exception {
        Item persistedItem1 = new Item();
        persistedItem1.setNumber(1L);
        itemRepository.save(persistedItem1);

        Item persistedItem2 = new Item();
        persistedItem2.setNumber(100L);
        itemRepository.save(persistedItem2);

        Item persistedItem3 = new Item();
        persistedItem3.setNumber(50L);
        itemRepository.save(persistedItem3);

        Item persistedItem4 = new Item();
        persistedItem4.setNumber(150L);
        itemRepository.save(persistedItem4);

        MvcResult responseMvc = mockMvc.perform(get("/retrieveMaxNumber"))
                .andExpect(status()
                        .isOk())
                .andReturn();

        String response = responseMvc.getResponse().getContentAsString();
        JSONObject responseObj = new JSONObject(response);
        assertEquals(responseObj.get("number"), 150);
    }

    @Test
    public void shouldReturnNotFoundWhenRetrievingMaxNumberIfHasNoAnyItem() throws Exception {
        mockMvc.perform(get("/retrieveMaxNumber"))
                .andExpect(status()
                        .isNotFound());
    }

    @Test
    public void shouldReturnOkWhenRetrievingMinNumber() throws Exception {
        Item persistedItem1 = new Item();
        persistedItem1.setNumber(1L);
        itemRepository.save(persistedItem1);

        Item persistedItem2 = new Item();
        persistedItem2.setNumber(100L);
        itemRepository.save(persistedItem2);

        Item persistedItem3 = new Item();
        persistedItem3.setNumber(50L);
        itemRepository.save(persistedItem3);

        Item persistedItem4 = new Item();
        persistedItem4.setNumber(150L);
        itemRepository.save(persistedItem4);

        MvcResult responseMvc = mockMvc.perform(get("/retrieveMinNumber"))
                .andExpect(status()
                        .isOk())
                .andReturn();

        String response = responseMvc.getResponse().getContentAsString();
        JSONObject responseObj = new JSONObject(response);
        assertEquals(responseObj.get("number"), 1);
    }

    @Test
    public void shouldReturnNotFoundWhenRetrievingMinNumberIfHasNoAnyItem() throws Exception {
        mockMvc.perform(get("/retrieveMinNumber"))
                .andExpect(status()
                        .isNotFound());
    }

    @Test
    public void shouldReturnOkWhenRemovingItemIfNumberIsSetted() throws Exception {
        Item persistedItem = new Item();
        persistedItem.setNumber(1L);
        itemRepository.save(persistedItem);

        mockMvc.perform(delete("/deleteOrRetrieve")
                .param("number", "1"))
                .andExpect(status()
                        .isOk());
    }

    @Test
    public void shouldReturnNotFoundWhenRemovingItemIfNumberIsSettedAndNumberIsNotFound() throws Exception {
        Item persistedItem = new Item();
        persistedItem.setNumber(1L);
        itemRepository.save(persistedItem);

        mockMvc.perform(delete("/deleteOrRetrieve")
                .param("number", "2"))
                .andExpect(status()
                        .isNotFound());
    }

    @Test
    public void shouldReturnOkWhenRetrievingAllItemsByOrderTypeIfNumberIsNotSetted() throws Exception {
        Item persistedItem1 = new Item();
        persistedItem1.setNumber(1L);
        itemRepository.save(persistedItem1);

        Item persistedItem2 = new Item();
        persistedItem2.setNumber(100L);
        itemRepository.save(persistedItem2);

        Item persistedItem3 = new Item();
        persistedItem3.setNumber(50L);
        itemRepository.save(persistedItem3);

        Item persistedItem4 = new Item();
        persistedItem4.setNumber(150L);
        itemRepository.save(persistedItem4);

        MvcResult responseMvc = mockMvc.perform(get("/deleteOrRetrieve").param("direction", "DESC"))
                .andExpect(status()
                        .isOk())
                .andReturn();

        String response = responseMvc.getResponse().getContentAsString();
        JSONArray jsonArr = new JSONArray(response);
        assertEquals(jsonArr.length(), 4);

        JSONObject jsonObject1 = (JSONObject) jsonArr.get(0);
        assertEquals(jsonObject1.get("number"), 150);

        JSONObject jsonObject2 = (JSONObject) jsonArr.get(1);
        assertEquals(jsonObject2.get("number"), 100);

        JSONObject jsonObject3 = (JSONObject) jsonArr.get(2);
        assertEquals(jsonObject3.get("number"), 50);

        JSONObject jsonObject4 = (JSONObject) jsonArr.get(3);
        assertEquals(jsonObject4.get("number"), 1);
    }

    private String convert(Item item) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(item);
    }
}
