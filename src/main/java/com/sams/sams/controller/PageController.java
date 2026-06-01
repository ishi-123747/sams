package com.sams.sams.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/reports")
    public String reports() {
        return "reports";
    }

    @GetMapping("/settings")
    public String settings() {
        return "settings";
    }
}