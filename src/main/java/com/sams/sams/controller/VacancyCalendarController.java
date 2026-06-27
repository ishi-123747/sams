package com.sams.sams.controller;

import com.sams.sams.repository.ApartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VacancyCalendarController {

    @Autowired private ApartmentRepository apartmentRepository;

    @GetMapping("/vacancy-calendar")
    public String vacancyCalendar(Model model) {
        model.addAttribute("apartments", apartmentRepository.findAll());
        model.addAttribute("activePage", "calendar");
        return "vacancy-calendar";
    }
}