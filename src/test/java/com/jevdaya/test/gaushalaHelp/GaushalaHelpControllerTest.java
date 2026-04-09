package com.jevdaya.test.gaushalaHelp;

import com.fasterxml.jackson.databind.ObjectMapper;   // Use this instead of tools.jackson
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import com.jevdaya.JwtUtil;
import com.jevdaya.Entity.GaushalaHelp;
import com.jevdaya.controller.GaushalaHelpController;
import com.jevdaya.service.GaushalaHelpService;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GaushalaHelpController.class)
@AutoConfigureMockMvc(addFilters = false)
public class GaushalaHelpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GaushalaHelpService service;

    @MockitoBean
    private JwtUtil jwtUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // ==================== GET ALL ====================
    @Test
    void testGetAll() throws Exception {
        List<GaushalaHelp> list = new ArrayList<>();
        GaushalaHelp help = new GaushalaHelp();
        help.setGaushalaName("ABC Gaushala");
        help.setContactPerson("John Doe");
        help.setContactPhone("9876543210");
        help.setHelpType("चारा");
        list.add(help);

        when(service.getAll()).thenReturn(list);

        mockMvc.perform(get("/api/gaushala"))   // ← Fixed URL
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].gaushalaName").value("ABC Gaushala"));
    }

    // ==================== POST (Save) ====================
    @Test
    void testSave() throws Exception {
        GaushalaHelp input = new GaushalaHelp();
        input.setGaushalaName("ABC Gaushala");
        input.setContactPerson("John Doe");
        input.setContactPhone("9876543210");
        input.setHelpType("चारा");

        GaushalaHelp saved = new GaushalaHelp();
        saved.setId(1L);
        saved.setGaushalaName("ABC Gaushala");
        saved.setContactPerson("John Doe");
        saved.setContactPhone("9876543210");
        saved.setHelpType("चारा");

        when(service.save(any(GaushalaHelp.class))).thenReturn(saved);

        mockMvc.perform(post("/api/gaushala")          // ← Fixed URL
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gaushalaName").value("ABC Gaushala"))
                .andExpect(jsonPath("$.id").value(1));
    }

    // ==================== DELETE ====================
    @Test
    void testDelete() throws Exception {
        doNothing().when(service).deleteById(1L);

        mockMvc.perform(delete("/api/gaushala/1"))     // ← Fixed URL
                .andExpect(status().isOk());           // or .isNoContent() if you change controller
    }

    // ==================== UPDATE ====================
    @Test
    void testUpdate() throws Exception {
        GaushalaHelp input = new GaushalaHelp();
        input.setGaushalaName("Updated Gaushala");
        input.setContactPerson("Jane Doe");
        input.setContactPhone("9876543210");
        input.setHelpType("मेडिकल");

        GaushalaHelp updated = new GaushalaHelp();
        updated.setId(1L);
        updated.setGaushalaName("Updated Gaushala");
        updated.setContactPerson("Jane Doe");
        updated.setContactPhone("9876543210");
        updated.setHelpType("मेडिकल");

        when(service.update(eq(1L), any(GaushalaHelp.class))).thenReturn(updated);

        mockMvc.perform(put("/api/gaushala/1")         // ← Fixed URL
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gaushalaName").value("Updated Gaushala"));
    }
}