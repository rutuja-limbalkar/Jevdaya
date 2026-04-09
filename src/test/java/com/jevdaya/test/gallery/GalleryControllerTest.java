package com.jevdaya.test.gallery;

import com.jevdaya.Entity.Gallery;
import com.jevdaya.controller.GalleryController;
import com.jevdaya.service.GalleryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GalleryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GalleryService galleryService;

    @InjectMocks
    private GalleryController galleryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(galleryController).build();
    }

    @Test
    void saveGallery_ShouldReturnSavedGallery() throws Exception {
        Gallery gallery = new Gallery(); // set necessary fields if needed
        when(galleryService.save(any(Gallery.class))).thenReturn(gallery);

        mockMvc.perform(post("/gallery")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))  // or proper JSON if using Jackson
                .andExpect(status().isOk());

        verify(galleryService, times(1)).save(any(Gallery.class));
    }

    @Test
    void getAllGalleries_ShouldReturnList() throws Exception {
        List<Gallery> galleries = Arrays.asList(new Gallery(), new Gallery());
        when(galleryService.getAll()).thenReturn(galleries);

        mockMvc.perform(get("/gallery"))
                .andExpect(status().isOk());

        verify(galleryService, times(1)).getAll();
    }

    @Test
    void deleteGallery_ShouldCallService() throws Exception {
        doNothing().when(galleryService).delete(anyLong());

        mockMvc.perform(delete("/gallery/1"))
                .andExpect(status().isOk());

        verify(galleryService, times(1)).delete(1L);
    }
}