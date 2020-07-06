package com.edozo.java.test.datastores;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.edozo.java.test.model.UpsertMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
public class MapControllerTest {

    @Autowired
    private MockMvc mockMvc;

    EasyRandom random = new EasyRandom();
    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void shouldReturnDefaultMessage() throws Exception {
        mockMvc
            .perform(get("/v1/maps"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("\"id\":122")))
            .andExpect(content().string(containsString("\"id\":123")))
            .andExpect(content().string(containsString("\"id\":124")))
            .andExpect(content().string(containsString("\"id\":125")));
    }

    @Test
    public void givenNonExistentId_whenFetched_thenStatus404Returned() throws Exception {
        mockMvc
            .perform(get("/v1/maps/999"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void givenNonExistentId_whenDeleted_thenStatus404Returned() throws Exception {
        mockMvc
            .perform(delete("/v1/maps/999"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void givenNewMap_whenDataIsNotComplete_thenStatus400Returned() throws Exception {
        UpsertMap upsertMap = random
            .nextObject(UpsertMap.class)
            .withAddress(null);

        mockMvc
            .perform(post("/v1/maps", upsertMap)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(upsertMap)))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(containsString("Address must be present")));
    }

    @Test
    public void givenNonExistentId_whenUpdated_thenStatus404Returned() throws Exception {
        UpsertMap upsertMap = random
            .nextObject(UpsertMap.class)
            .withUserId(10);

        mockMvc
            .perform(put("/v1/maps/999", upsertMap)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(upsertMap)))
            .andExpect(status().isNotFound());
    }
}
