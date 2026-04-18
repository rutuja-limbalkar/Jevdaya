package com.jevdaya.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "gaushala_help")
@Data
public class GaushalaHelp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "gaushala_name")
    private String gaushalaName;

    @Column(name = "contact_person")
    private String contactPerson;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Column(name = "help_type")
    private String helpType;
}