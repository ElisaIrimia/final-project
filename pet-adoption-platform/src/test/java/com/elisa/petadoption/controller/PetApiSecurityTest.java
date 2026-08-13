package com.elisa.petadoption.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PetApiSecurityTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void publicPetApiReturnsJsonCatalog() throws Exception {
        mockMvc.perform(get("/api/pets").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(3)))
                .andExpect(jsonPath("$[0].name").exists());
    }

    @Test
    @WithMockUser(roles = "USER")
    void userCannotCreatePetThroughAdminApi() throws Exception {
        String body = """
                {
                  "name": "Test",
                  "species": "Dog",
                  "breed": "Mixed",
                  "age": 2,
                  "story": "A valid story long enough for validation.",
                  "status": "AVAILABLE"
                }
                """;

        mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isForbidden());
    }
}
