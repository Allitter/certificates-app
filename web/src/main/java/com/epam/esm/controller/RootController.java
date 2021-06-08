package com.epam.esm.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping
    public String getDefaultPage() {
        return "Welcome to Certificates app";
    }
}
