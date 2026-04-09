package com.jevdaya.serviceImpl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jevdaya.Entity.GaushalaHelp;
import com.jevdaya.repo.GaushalaHelpRepo;
import com.jevdaya.service.GaushalaHelpService;


@Service
public class GaushalaHelpServiceImpl implements GaushalaHelpService {

    @Autowired
    private GaushalaHelpRepo repo;

    @Override
    public GaushalaHelp save(GaushalaHelp help) {
        return repo.save(help);
    }

    @Override
    public List<GaushalaHelp> getAll() {
        return repo.findAll();
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

   
    @Override
    public GaushalaHelp update(Long id, GaushalaHelp help) {

        GaushalaHelp existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("GaushalaHelp not found with id: " + id));

        
        existing.setGaushalaName(help.getGaushalaName());
        existing.setContactPerson(help.getContactPerson());
        existing.setContactPhone(help.getContactPhone());
        existing.setHelpType(help.getHelpType());

        return repo.save(existing);
    }
}
