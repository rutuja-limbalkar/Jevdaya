package com.jevdaya.test;

import com.jevdaya.Entity.Contact;
import com.jevdaya.repo.ContactRepository;
import com.jevdaya.serviceImpl.ContactServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactServiceImplTest {

    @Mock
    private ContactRepository contactRepository;

    @InjectMocks
    private ContactServiceImpl contactService;

    @Test
    void shouldSaveContactMessageSuccessfully() {

        // Arrange
        Contact contact = new Contact();
        contact.setName("Rutuja");
        contact.setEmail("test@example.com");
        contact.setMessage("Hello");

        when(contactRepository.save(contact)).thenReturn(contact);

        // Act
        Contact savedContact = contactService.saveMessage(contact);

        // Assert
        assertNotNull(savedContact);
        assertEquals("Rutuja", savedContact.getName());

        verify(contactRepository, times(1)).save(contact);
    }
}