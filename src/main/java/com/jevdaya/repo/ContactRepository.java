package com.jevdaya.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jevdaya.Entity.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {
}