package com.jevdaya.test.gaushalaHelp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.jevdaya.Entity.GaushalaHelp;
import com.jevdaya.repo.GaushalaHelpRepo;
import com.jevdaya.serviceImpl.GaushalaHelpServiceImpl;

public class GaushalaHelpServiceImplTest {

    @Mock
    private GaushalaHelpRepo repo;

    @InjectMocks
    private GaushalaHelpServiceImpl service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // ✅ SAVE TEST
    @Test
    void testSave() {
        GaushalaHelp help = new GaushalaHelp();
        help.setGaushalaName("ABC");

        when(repo.save(help)).thenReturn(help);

        GaushalaHelp result = service.save(help);

        assertEquals("ABC", result.getGaushalaName());
    }

    // ✅ GET ALL TEST
    @Test
    void testGetAll() {
        List<GaushalaHelp> list = new ArrayList<>();
        list.add(new GaushalaHelp());

        when(repo.findAll()).thenReturn(list);

        List<GaushalaHelp> result = service.getAll();

        assertEquals(1, result.size());
    }

    // ✅ DELETE TEST
    @Test
    void testDelete() {
        Long id = 1L;

        doNothing().when(repo).deleteById(id);

        service.deleteById(id);

        verify(repo, times(1)).deleteById(id);
    }

    // ✅ UPDATE TEST
    @Test
    void testUpdate() {
        Long id = 1L;

        GaushalaHelp existing = new GaushalaHelp();
        existing.setId(id);
        existing.setGaushalaName("Old Name");

        GaushalaHelp updated = new GaushalaHelp();
        updated.setGaushalaName("New Name");
        updated.setContactPerson("Komal");
        updated.setContactPhone("1234567890");
        updated.setHelpType("Food");

        when(repo.findById(id)).thenReturn(Optional.of(existing));
        when(repo.save(any(GaushalaHelp.class))).thenReturn(existing);

        GaushalaHelp result = service.update(id, updated);

        assertEquals("New Name", result.getGaushalaName());
        assertEquals("Komal", result.getContactPerson());
    }
    @Test
    void testUpdate_NotFound() {

        Long id = 999L; // non-existing id

        GaushalaHelp help = new GaushalaHelp();
        help.setGaushalaName("Test");

        
        when(repo.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.update(id, help);
        });

        assertEquals("GaushalaHelp not found with id: 999", exception.getMessage());
    }
}