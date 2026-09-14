package com.goldenboiz.controller;

import com.goldenboiz.model.News;
import com.goldenboiz.model.Executive; // NEW
import com.goldenboiz.repository.NewsRepository;
import com.goldenboiz.repository.ExecutiveRepository; // NEW
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;

@Controller
public class NewsController {

    private final NewsRepository newsRepository;
    private final ExecutiveRepository executiveRepository; // NEW
    private final String UPLOAD_DIR = "src/main/resources/static/uploads/"; // Folder to save news images
    private final String EXEC_UPLOAD_DIR = "src/main/resources/static/uploads/execs/"; // NEW: Folder for exec photos

    // UPDATED CONSTRUCTOR
    public NewsController(NewsRepository newsRepository, ExecutiveRepository executiveRepository) {
        this.newsRepository = newsRepository;
        this.executiveRepository = executiveRepository; // NEW
    }

    // ================== NEWS SECTION ==================

    // PUBLIC: Show all news on /news page
    @GetMapping("/news")
    public String showNewsPage(Model model) {
        model.addAttribute("newsList", newsRepository.findAllByOrderByDatePostedDesc());
        return "news"; // news.html
    }

    // PUBLIC: Show single news detail
    @GetMapping("/news/{id}")
    public String showNewsDetail(@PathVariable Long id, Model model) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid news Id:" + id));
        model.addAttribute("news", news);
        return "news-detail"; // news-detail.html
    }

    // ADMIN: Show news form + list
    @GetMapping("/admin/news")
    public String showNewsForm(Model model) {
        model.addAttribute("news", new News());
        model.addAttribute("allNews", newsRepository.findAllByOrderByDatePostedDesc());
        return "admin-news"; // admin-news.html
    }

    // ADMIN: Save news with image upload
    @PostMapping("/admin/news/save")
    public String saveNews(@ModelAttribute News news, 
                           @RequestParam("imageFile") MultipartFile file) throws IOException {
        
        // Set date if new
        if(news.getId() == null) {
            news.setDatePosted(LocalDateTime.now());
        }
        
        // Handle file upload
        if (!file.isEmpty()) {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename().replaceAll(" ", "_");
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            news.setImageUrl("/uploads/" + fileName);
        }
        
        newsRepository.save(news);
        return "redirect:/admin/news";
    }

    // ADMIN: Delete news
    @GetMapping("/admin/news/delete/{id}")
    public String deleteNews(@PathVariable Long id) {
        newsRepository.deleteById(id);
        return "redirect:/admin/news";
    }
    
    // ================== EXECUTIVES SECTION ================== // NEW

    // ADMIN: Show executives form + list
    @GetMapping("/admin/executives")
    public String showExecutiveForm(Model model) {
        model.addAttribute("executive", new Executive());
        model.addAttribute("allExecutives", executiveRepository.findAllByOrderBySortOrderAsc());
        return "admin-executives"; // admin-executives.html
    }

    // ADMIN: Save executive with image upload
    @PostMapping("/admin/executives/save")
    public String saveExecutive(@ModelAttribute Executive executive, 
                                @RequestParam("imageFile") MultipartFile file) throws IOException {
        
        // Handle file upload
        if (!file.isEmpty()) {
            Path uploadPath = Paths.get(EXEC_UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename().replaceAll(" ", "_");
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            executive.setImageUrl("/uploads/execs/" + fileName);
        }
        
        executiveRepository.save(executive);
        return "redirect:/admin/executives";
    }

    // ADMIN: Delete executive
    @GetMapping("/admin/executives/delete/{id}")
    public String deleteExecutive(@PathVariable Long id) {
        executiveRepository.deleteById(id);
        return "redirect:/admin/executives";
    }
    
}