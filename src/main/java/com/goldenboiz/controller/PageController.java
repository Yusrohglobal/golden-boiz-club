package com.goldenboiz.controller;

import com.goldenboiz.repository.ExecutiveRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    private final ExecutiveRepository executiveRepository;

    public PageController(ExecutiveRepository executiveRepository) {
        this.executiveRepository = executiveRepository;
    }

    @GetMapping("/about")
    public String about() {
        return "about"; // about.html
    }

    @GetMapping("/executives")
    public String executives(Model model) {
        model.addAttribute("executives", executiveRepository.findAllByOrderBySortOrderAsc());
        return "executives"; // executives.html
    }
}