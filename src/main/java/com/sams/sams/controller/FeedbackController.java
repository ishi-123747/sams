package com.sams.sams.controller;

import com.sams.sams.model.Feedback;
import com.sams.sams.model.Tenant;
import com.sams.sams.repository.FeedbackRepository;
import com.sams.sams.repository.TenantRepository;
import com.sams.sams.repository.UserRepository;
import com.sams.sams.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class FeedbackController {

    @Autowired private FeedbackRepository feedbackRepository;
    @Autowired private TenantRepository tenantRepository;
    @Autowired private UserRepository userRepository;

    // Admin view
    @GetMapping("/feedback")
    public String adminFeedback(Model model) {
        List<Feedback> all = feedbackRepository.findAll();
        double avgRating = all.stream().mapToInt(Feedback::getRating).average().orElse(0);
        model.addAttribute("feedbacks", all);
        model.addAttribute("totalCount", all.size());
        model.addAttribute("avgRating", String.format("%.1f", avgRating));
        model.addAttribute("fiveStars", all.stream().filter(f -> f.getRating() == 5).count());
        model.addAttribute("fourStars", all.stream().filter(f -> f.getRating() == 4).count());
        model.addAttribute("threeStars", all.stream().filter(f -> f.getRating() <= 3).count());
        model.addAttribute("activePage", "feedback");
        return "feedback";
    }

    // Tenant submit feedback
    @GetMapping("/my-feedback")
    public String myFeedback(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) return "redirect:/login";

        User user = userRepository.findByUsername(username).orElse(null);
        Tenant tenant = null;
        if (user != null && user.getTenantId() != null) {
            tenant = tenantRepository.findById(user.getTenantId()).orElse(null);
        }

        List<Feedback> myFeedbacks = tenant != null
                ? feedbackRepository.findByApartmentNumber(tenant.getApartmentNumber())
                : List.of();

        model.addAttribute("tenant", tenant);
        model.addAttribute("myFeedbacks", myFeedbacks);
        model.addAttribute("activePage", "feedback");
        return "my-feedback";
    }

    @PostMapping("/my-feedback/submit")
    public String submitFeedback(@RequestParam int rating,
                                 @RequestParam String comment,
                                 @RequestParam String category,
                                 HttpSession session) {
        String username = (String) session.getAttribute("username");
        User user = userRepository.findByUsername(username).orElse(null);
        Tenant tenant = null;
        if (user != null && user.getTenantId() != null) {
            tenant = tenantRepository.findById(user.getTenantId()).orElse(null);
        }

        if (tenant != null) {
            Feedback fb = new Feedback();
            fb.setTenantName(tenant.getName());
            fb.setApartmentNumber(tenant.getApartmentNumber());
            fb.setRating(rating);
            fb.setComment(comment);
            fb.setCategory(category);
            fb.setCreatedDate(LocalDate.now().toString());
            feedbackRepository.save(fb);
        }
        return "redirect:/my-feedback";
    }

    @PostMapping("/feedback/delete/{id}")
    public String deleteFeedback(@PathVariable Long id) {
        feedbackRepository.deleteById(id);
        return "redirect:/feedback";
    }
}