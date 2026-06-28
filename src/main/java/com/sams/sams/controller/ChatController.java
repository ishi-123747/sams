package com.sams.sams.controller;

import com.sams.sams.model.ChatLog;
import com.sams.sams.repository.ChatLogRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ChatController {

    @Autowired private ChatLogRepository chatLogRepository;

    @PostMapping("/chatbot/ask")
    @ResponseBody
    public Map<String, String> ask(@RequestParam String question, HttpSession session) {
        String username = (String) session.getAttribute("username");
        String lower = question.toLowerCase();

        String category;
        String answer;

        if (containsAny(lower, "rent due", "when is rent", "payment due", "due date", "bill due")) {
            category = "Rent/Billing";
            answer = "Your rent/maintenance bill due date is shown on your 'My Bills' page. Most societies set it on the 1st, 5th, or 10th of every month — check your specific agreement under 'My Agreement' for the exact date.";
        } else if (containsAny(lower, "late fee", "penalty", "overdue")) {
            category = "Rent/Billing";
            answer = "Late fees apply if your bill isn't paid by the due date. Check your 'My Bills' page for the exact penalty amount, if applicable.";
        } else if (containsAny(lower, "book amenity", "book gym", "book pool", "book hall", "amenity booking")) {
            category = "Amenities";
            answer = "To book an amenity, go to 'Book Amenity' from the sidebar, select the facility, choose your date and time slot, then submit. Admin will approve or reject your request.";
        } else if (containsAny(lower, "visitor", "guest", "gate pass", "otp")) {
            category = "Visitors";
            answer = "To let a visitor in, go to 'My Visitors' and pre-approve them — you'll get a 4-digit OTP to share with your guest. Security verifies the OTP at the gate to let them in.";
        } else if (containsAny(lower, "complaint", "issue", "problem", "broken", "not working")) {
            category = "Complaints";
            answer = "You can raise a complaint from 'My Complaints' in the sidebar. Describe the issue and admin/maintenance staff will be notified to resolve it.";
        } else if (containsAny(lower, "emergency", "fire", "ambulance", "police", "help")) {
            category = "Emergency";
            answer = "For emergencies, check the 'Emergency' page for quick-dial numbers — Fire: 101, Ambulance: 108, Police: 100. You can tap any number to call directly.";
        } else if (containsAny(lower, "lost", "found", "missing item")) {
            category = "Lost & Found";
            answer = "Report a lost or found item from the 'Lost & Found' page. Fill in the item details and location — other residents can see it and respond.";
        } else if (containsAny(lower, "event", "rsvp", "function", "festival")) {
            category = "Events";
            answer = "Check the 'Events' page for upcoming society events and RSVP directly there. You'll also see how many neighbors are attending.";
        } else if (containsAny(lower, "deposit", "refund", "move out", "move in")) {
            category = "Deposit";
            answer = "Your security deposit details, including any move-out deductions and refund status, are tracked by the admin. Contact your admin office for specifics about your deposit.";
        } else if (containsAny(lower, "agreement", "lease", "rent agreement", "contract")) {
            category = "Agreement";
            answer = "Your rent agreement can be viewed and downloaded from 'My Agreement' in the sidebar. Click 'View / Print' to save it as a PDF.";
        } else if (containsAny(lower, "parking", "vehicle", "car", "bike")) {
            category = "Parking";
            answer = "Parking slot details are managed by the admin under the Parking section. Contact your admin office if you need a slot assigned or have a parking-related issue.";
        } else if (containsAny(lower, "feedback", "review", "rating")) {
            category = "Feedback";
            answer = "You can share your feedback about the society or any service from the 'My Feedback' page.";
        } else if (containsAny(lower, "staff", "watchman", "cleaner", "security guard")) {
            category = "Staff";
            answer = "For staff-related queries (watchman, cleaner schedules, etc.), please contact the admin office directly — staff details are managed by them.";
        } else if (containsAny(lower, "hello", "hi", "hey")) {
            category = "Greeting";
            answer = "Hello! I'm your SAMS assistant. Ask me about rent, amenities, visitors, complaints, events, or emergencies and I'll help.";
        } else if (containsAny(lower, "thank")) {
            category = "Greeting";
            answer = "You're welcome! Let me know if you need anything else.";
        } else {
            category = "Unmatched";
            answer = "I'm not sure about that one yet. For specific questions, please contact your admin office, or try asking about rent, amenities, visitors, complaints, events, or emergencies.";
        }

        ChatLog log = new ChatLog();
        log.setUsername(username != null ? username : "Guest");
        log.setQuestion(question);
        log.setMatchedCategory(category);
        log.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")));
        chatLogRepository.save(log);

        Map<String, String> response = new HashMap<>();
        response.put("answer", answer);
        response.put("category", category);
        return response;
    }

    private boolean containsAny(String text, String... keywords) {
        for (String k : keywords) {
            if (text.contains(k)) return true;
        }
        return false;
    }
}