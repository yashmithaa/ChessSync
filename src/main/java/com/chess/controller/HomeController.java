package com.chess.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE)
    public String home() {
        return "Chess application is running. Launch the desktop Swing UI from mvn spring-boot:run, then use the auth dialog to register or log in.";
    }
}
// Last modified during: feat: Add REST API controllers and endpoints [minor]
