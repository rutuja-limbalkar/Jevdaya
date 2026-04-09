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

    private String gaushalaName;
    private String contactPerson;
    private String contactPhone;
    private String helpType;
}