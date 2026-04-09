package com.jevdaya.service;

import java.util.List;

import com.jevdaya.Entity.GaushalaHelp;

public interface GaushalaHelpService {

    GaushalaHelp save(GaushalaHelp help);

    List<GaushalaHelp> getAll();

    void deleteById(Long id);

    GaushalaHelp update(Long id, GaushalaHelp help);
}