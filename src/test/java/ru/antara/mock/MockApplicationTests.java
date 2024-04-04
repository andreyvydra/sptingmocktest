package ru.antara.mock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MockApplicationTests {

    @Test
    void getData(@Autowired MockMvc mvc) throws Exception {
        MvcResult r = mvc.perform(
                        get("/api/mock/data")
                )
                .andExpect(request().asyncStarted())
                .andReturn();

        mvc.perform(asyncDispatch(r))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value("SUCCESS"));
    }

    @Test
    void submitData(@Autowired MockMvc mvc) throws Exception {
        MvcResult r = mvc.perform(post("/api/mock/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "id": 1,
                                    "name": "aboba",
                                    "cost": 1
                                }""")
                )
                .andExpect(request().asyncStarted())
                .andReturn();

        mvc.perform(asyncDispatch(r))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value("SUCCESS"));
    }

}
