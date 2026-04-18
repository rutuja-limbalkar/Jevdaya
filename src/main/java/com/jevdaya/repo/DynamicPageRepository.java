//package com.jevdaya.repo;
//
//import java.util.Optional;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import com.jevdaya.Entity.DynamicPage;
//
//public interface DynamicPageRepository extends JpaRepository<DynamicPage, Long> {
//    Optional<DynamicPage> findBySlug(String slug);
//}