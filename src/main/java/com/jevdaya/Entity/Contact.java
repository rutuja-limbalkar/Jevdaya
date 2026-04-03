package com.jevdaya.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "contact_messages")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String subject;

    @Column(length = 2000)
    private String message;
}
