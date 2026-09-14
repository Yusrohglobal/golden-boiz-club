package com.goldenboiz.controller;

import com.goldenboiz.model.Member;
import com.goldenboiz.repository.MemberRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegistrationController {

    private final MemberRepository memberRepository;

    public RegistrationController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("member", new Member());
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(@ModelAttribute Member member, Model model) {
        // Set default statuses when someone registers
        member.setStatus("PENDING");
        member.setPaymentStatus("UNPAID");
        
        Member savedMember = memberRepository.save(member);
        
        // Redirect to payment page instead of showing success on same page
        return "redirect:/payment/" + savedMember.getId();
    }

    // ===== NEW: SHOW PAYMENT PAGE =====
    @GetMapping("/payment/{id}")
    public String showPaymentPage(@PathVariable Long id, Model model) {
        Member member = memberRepository.findById(id).orElse(null);
        if (member == null) {
            return "redirect:/"; // if id not found go home
        }
        model.addAttribute("member", member);
        return "payment";
    }

    // ===== NEW: SUBMIT BANK TRANSFER =====
    @PostMapping("/submitPayment/{id}")
    public String submitBankPayment(@PathVariable Long id, 
                                    @RequestParam String paymentMethod, 
                                    @RequestParam(required = false) String paymentReference) {
        Member member = memberRepository.findById(id).orElse(null);
        if(member != null){
            member.setPaymentMethod(paymentMethod);
            member.setPaymentReference(paymentReference);
            member.setPaymentStatus("PENDING"); // Wait for admin to verify
            memberRepository.save(member);
        }
        return "redirect:/?success=true";
    }

    // ===== NEW: VERIFY PAYSTACK PAYMENT =====
    @GetMapping("/verifyPayment/{id}")
    public String verifyPaystackPayment(@PathVariable Long id, @RequestParam String reference) {
        Member member = memberRepository.findById(id).orElse(null);
        if(member != null){
            member.setPaymentMethod("PAYSTACK");
            member.setPaymentReference(reference);
            member.setPaymentStatus("PAID"); // Auto verified
            memberRepository.save(member);
        }
        return "redirect:/?success=true";
    }
}