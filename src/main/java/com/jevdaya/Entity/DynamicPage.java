//package com.jevdaya.Entity;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.Table;
//import lombok.Data;
//
//@Entity
//@Data
//@Table(name = "dynamic_pages")
//public class DynamicPage {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(unique = true, nullable = false)
//    private String slug; // e.g., "about-us" or "events"
//
//    private String title;
//
//    @Column(columnDefinition = "TEXT") // Allows for very long HTML strings
//    private String content;
//}