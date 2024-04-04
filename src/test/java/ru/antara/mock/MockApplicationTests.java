package ru.antara.mock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MockApplicationTests {

	@Autowired
	MockController mockController;

	@Test
	void getData(@Autowired MockMvc mvc) throws Exception {
		mvc.perform(get("/api/mock/data")).andExpect(status().isOk());
	}

	@Test
	void submitData(@Autowired MockMvc mvc) throws Exception {
		mvc.perform(post("/api/mock/submit").
				contentType(MediaType.APPLICATION_JSON).
				content("{\n" +
						"    \"id\": 1,\n" +
						"    \"name\": \"aboba\",\n" +
						"    \"cost\": 1\n" +
						"}")
		).andExpect(status().isOk()).
				andExpect(content().json("{\n" +
				"    \"status\": \"SUCCESS\",\n" +
				"    \"message\": \"Всё ок!\"\n" +
				"}"));
	}

}
