package com.jevdaya.controller;

import com.jevdaya.Entity.Contact;
import com.jevdaya.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = "*") // allow frontend
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping("/save")
    public String saveContact(@RequestBody Contact contact) {
        contactService.saveMessage(contact);
        return "Message saved successfully";
    }
}
