package com.jevdaya.test;import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {@Autowired
private MockMvc mockMvc;

@Test
void register_shouldWorkWithH2Database() throws Exception {
    mockMvc.perform(post("/api/users/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{}")
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
            .andExpect(status().isOk());
}

@Test
void getAllUsers_shouldReturnListFromH2() throws Exception {
    mockMvc.perform(get("/api/users/all"))
            .andExpect(status().isOk());
}}

