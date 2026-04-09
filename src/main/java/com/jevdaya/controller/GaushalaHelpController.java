package com.jevdaya.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.jevdaya.Entity.GaushalaHelp;
import com.jevdaya.service.GaushalaHelpService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/gaushala")
public class GaushalaHelpController {

    @Autowired
    private GaushalaHelpService service;

    // CREATE - POST /api/gaushala
    @PostMapping
    public GaushalaHelp save(@RequestBody GaushalaHelp help) {
        System.out.println("Received Gaushala: " + help);
        return service.save(help);
    }

    // GET ALL - GET /api/gaushala
    @GetMapping
    public List<GaushalaHelp> getAll() {
        return service.getAll();
    }

    // UPDATE - PUT /api/gaushala/{id}
    @PutMapping("/{id}")
    public GaushalaHelp update(@PathVariable Long id, @RequestBody GaushalaHelp help) {
        return service.update(id, help);
    }

    // DELETE - DELETE /api/gaushala/{id}
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }
}