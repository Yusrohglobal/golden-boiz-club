package com.goldenboiz.controller;

import com.goldenboiz.model.Member;
import com.goldenboiz.repository.MemberRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminController {

    private final MemberRepository memberRepository;

    public AdminController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // SEARCH ADDED HERE
    @GetMapping("/admin")
    public String showAdminDashboard(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        if (keyword != null && !keyword.isEmpty()) {
            model.addAttribute("members", memberRepository.findByFullNameContainingIgnoreCaseOrPhoneContainingOrEmailContaining(keyword, keyword, keyword));
        } else {
            model.addAttribute("members", memberRepository.findAll());
        }
        model.addAttribute("keyword", keyword);
        return "admin";
    }

    @GetMapping("/admin/approve/{id}")
    public String approveMember(@PathVariable Long id) {
        Member member = memberRepository.findById(id).orElseThrow();
        member.setStatus("APPROVED");
        memberRepository.save(member);
        return "redirect:/admin";
    }

    @GetMapping("/admin/reject/{id}")
    public String rejectMember(@PathVariable Long id) {
        Member member = memberRepository.findById(id).orElseThrow();
        member.setStatus("REJECTED");
        memberRepository.save(member);
        return "redirect:/admin";
    }

    @GetMapping("/admin/delete/{id}")
    public String deleteMember(@PathVariable Long id) {
        memberRepository.deleteById(id);
        return "redirect:/admin";
    }

    // NEW: Show edit form at bottom
    @GetMapping("/admin/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("member", memberRepository.findById(id).orElseThrow());
        model.addAttribute("members", memberRepository.findAll()); // still show table
        return "admin";
    }

    // NEW: Handle update
    @PostMapping("/admin/update")
    public String updateMember(@ModelAttribute Member member) {
        memberRepository.save(member);
        return "redirect:/admin";
    }
    @GetMapping("/admin/approvePayment/{id}")
public String approvePayment(@PathVariable Long id) {
    Member member = memberRepository.findById(id).orElse(null);
    if(member != null){
        member.setPaymentStatus("PAID");
        memberRepository.save(member);
    }
    return "redirect:/admin";
}
}