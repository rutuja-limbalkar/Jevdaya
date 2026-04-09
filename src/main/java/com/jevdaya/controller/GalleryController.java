package com.jevdaya.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.jevdaya.Entity.Gallery;
import com.jevdaya.service.GalleryService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/gallery")
public class GalleryController {

    @Autowired
    private GalleryService service;

    @PostMapping
    public Gallery save(@RequestBody Gallery gallery) {
        return service.save(gallery);
    }

    @GetMapping
    public List<Gallery> getAll() {
        return service.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}