package com.jevdaya.test.gallery;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.jevdaya.Entity.Gallery;
import com.jevdaya.repo.GalleryRepository;
import com.jevdaya.serviceImpl.GalleryServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class GalleryServiceTest {

    @Mock
    private GalleryRepository repo;

    @InjectMocks
    private GalleryServiceImpl service;

    private Gallery gallery;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        gallery = new Gallery();
        gallery.setId(1L);
        gallery.setTitle("Test Title");
        gallery.setDescription("Test Description");
        gallery.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testSaveGallery() {
        when(repo.save(any(Gallery.class))).thenReturn(gallery);

        Gallery saved = service.save(gallery);

        assertNotNull(saved);
        assertEquals("Test Title", saved.getTitle());
    }

    @Test
    void testGetAllGallery() {
        when(repo.findAll()).thenReturn(Arrays.asList(gallery));

        List<Gallery> list = service.getAll();

        assertEquals(1, list.size());
    }

    @Test
    void testDeleteGallery() {
        doNothing().when(repo).deleteById(1L);

        service.delete(1L);

        verify(repo, times(1)).deleteById(1L);
    }
}