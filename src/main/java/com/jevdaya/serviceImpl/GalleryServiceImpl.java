package com.jevdaya.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jevdaya.Entity.Gallery;
import com.jevdaya.repo.GalleryRepository;
import com.jevdaya.service.GalleryService;

@Service
public class GalleryServiceImpl implements GalleryService {

    @Autowired
    private GalleryRepository repo;

    @Override
    public Gallery save(Gallery gallery) {
        gallery.setCreatedAt(LocalDateTime.now());
        return repo.save(gallery);
    }

    @Override
    public List<Gallery> getAll() {
        return repo.findAll();
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }
}