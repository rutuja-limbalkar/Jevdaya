package com.jevdaya.service;

import java.util.List;
import com.jevdaya.Entity.Gallery;

public interface GalleryService {
    Gallery save(Gallery gallery);
    List<Gallery> getAll();
    void delete(Long id);
}