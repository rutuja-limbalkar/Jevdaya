package com.jevdaya.serviceImpl;

import com.jevdaya.Entity.Contact;
import com.jevdaya.repo.ContactRepository;
import com.jevdaya.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Override
    public Contact saveMessage(Contact contact) {
        return contactRepository.save(contact);
    }
}