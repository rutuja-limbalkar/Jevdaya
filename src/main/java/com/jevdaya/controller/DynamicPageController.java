//package com.jevdaya.controller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.jevdaya.Entity.DynamicPage;
//import com.jevdaya.repo.DynamicPageRepository;
//
//@RestController
//@RequestMapping("/api/pages")
//@CrossOrigin("*")
//public class DynamicPageController {
//
//    @Autowired
//    private DynamicPageRepository repository;
//
//    // This matches your fetch(".../api/pages/active")
//    @GetMapping("/active")
//    public List<DynamicPage> getActivePages() {
//        return repository.findAll(); 
//    }
//
//    @GetMapping("/{slug}")
//    public ResponseEntity<DynamicPage> getPage(@PathVariable("slug") String slug) {
//        return repository.findBySlug(slug)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @PostMapping
//    public DynamicPage savePage(@RequestBody DynamicPage page) {
//        return repository.save(page);
//    }
//    
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deletePage(@PathVariable Long id) {
//        repository.deleteById(id);
//        return ResponseEntity.ok().build();
//    }
//
//    // UPDATE: http://localhost:8080/api/pages/1
//    @PutMapping("/{id}")
//    public ResponseEntity<DynamicPage> updatePage(@PathVariable Long id, @RequestBody DynamicPage updatedPage) {
//        return repository.findById(id)
//                .map(page -> {
//                    page.setTitle(updatedPage.getTitle());
//                    page.setSlug(updatedPage.getSlug());
//                    page.setContent(updatedPage.getContent());
//                    return ResponseEntity.ok(repository.save(page));
//                })
//                .orElse(ResponseEntity.notFound().build());
//    }
//}