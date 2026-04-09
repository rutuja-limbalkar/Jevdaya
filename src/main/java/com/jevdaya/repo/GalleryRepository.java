package com.jevdaya.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.jevdaya.Entity.Gallery;

public interface GalleryRepository extends JpaRepository<Gallery, Long> {
}