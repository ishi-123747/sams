package com.sams.sams.controller;

import com.sams.sams.model.*;
import com.sams.sams.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class EventController {

    @Autowired private EventRepository eventRepository;
    @Autowired private EventRsvpRepository eventRsvpRepository;
    @Autowired private TenantRepository tenantRepository;
    @Autowired private UserRepository userRepository;

    // Admin - manage events
    @GetMapping("/events")
    public String events(Model model) {
        List<Event> all = eventRepository.findAll();
        model.addAttribute("events", all);
        model.addAttribute("totalCount", all.size());
        model.addAttribute("upcomingCount", eventRepository.countByStatus("Upcoming"));

        Map<Long, Long> goingMap = new HashMap<>();
        Map<Long, Long> maybeMap = new HashMap<>();
        Map<Long, Long> notGoingMap = new HashMap<>();
        for (Event e : all) {
            goingMap.put(e.getId(), eventRsvpRepository.countByEventIdAndResponse(e.getId(), "Going"));
            maybeMap.put(e.getId(), eventRsvpRepository.countByEventIdAndResponse(e.getId(), "Maybe"));
            notGoingMap.put(e.getId(), eventRsvpRepository.countByEventIdAndResponse(e.getId(), "NotGoing"));
        }
        model.addAttribute("goingMap", goingMap);
        model.addAttribute("maybeMap", maybeMap);
        model.addAttribute("notGoingMap", notGoingMap);
        model.addAttribute("activePage", "events-admin");
        return "events";
    }

    @GetMapping("/events/rsvps/{id}")
    public String viewRsvps(@PathVariable Long id, Model model) {
        Event event = eventRepository.findById(id).orElse(null);
        List<EventRsvp> rsvps = eventRsvpRepository.findByEventId(id);
        model.addAttribute("event", event);
        model.addAttribute("rsvps", rsvps);
        model.addAttribute("activePage", "events-admin");
        return "event-rsvps";
    }

    @PostMapping("/events/add")
    public String addEvent(@ModelAttribute Event event) {
        event.setStatus("Upcoming");
        event.setCreatedDate(LocalDate.now().toString());
        eventRepository.save(event);
        return "redirect:/events";
    }

    @PostMapping("/events/complete/{id}")
    public String completeEvent(@PathVariable Long id) {
        Event e = eventRepository.findById(id).orElse(null);
        if (e != null) { e.setStatus("Completed"); eventRepository.save(e); }
        return "redirect:/events";
    }

    @PostMapping("/events/cancel/{id}")
    public String cancelEvent(@PathVariable Long id) {
        Event e = eventRepository.findById(id).orElse(null);
        if (e != null) { e.setStatus("Cancelled"); eventRepository.save(e); }
        return "redirect:/events";
    }

    @PostMapping("/events/delete/{id}")
    public String deleteEvent(@PathVariable Long id) {
        eventRsvpRepository.findByEventId(id).forEach(r -> eventRsvpRepository.deleteById(r.getId()));
        eventRepository.deleteById(id);
        return "redirect:/events";
    }

    // Tenant - view events and RSVP
    @GetMapping("/my-events")
    public String myEvents(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) return "redirect:/login";

        User user = userRepository.findByUsername(username).orElse(null);
        Tenant tenant = null;
        if (user != null && user.getTenantId() != null)
            tenant = tenantRepository.findById(user.getTenantId()).orElse(null);

        List<Event> upcoming = eventRepository.findByStatusOrderByEventDateAsc("Upcoming");
        model.addAttribute("events", upcoming);
        model.addAttribute("tenant", tenant);

        Map<Long, String> myResponses = new HashMap<>();
        if (tenant != null) {
            for (Event e : upcoming) {
                eventRsvpRepository.findByEventIdAndApartmentNumber(e.getId(), tenant.getApartmentNumber())
                        .ifPresent(r -> myResponses.put(e.getId(), r.getResponse()));
            }
        }
        model.addAttribute("myResponses", myResponses);
        model.addAttribute("activePage", "events-tenant");
        return "my-events";
    }

    @PostMapping("/my-events/rsvp")
    public String rsvp(@RequestParam Long eventId,
                       @RequestParam String response,
                       @RequestParam(defaultValue = "0") int guestCount,
                       HttpSession session) {
        String username = (String) session.getAttribute("username");
        User user = userRepository.findByUsername(username).orElse(null);
        Tenant tenant = null;
        if (user != null && user.getTenantId() != null)
            tenant = tenantRepository.findById(user.getTenantId()).orElse(null);

        if (tenant != null) {
            EventRsvp existing = eventRsvpRepository
                    .findByEventIdAndApartmentNumber(eventId, tenant.getApartmentNumber())
                    .orElse(new EventRsvp());

            existing.setEventId(eventId);
            existing.setTenantName(tenant.getName());
            existing.setApartmentNumber(tenant.getApartmentNumber());
            existing.setResponse(response);
            existing.setGuestCount(guestCount);
            existing.setRespondedDate(LocalDate.now().toString());
            eventRsvpRepository.save(existing);
        }
        return "redirect:/my-events";
    }
}