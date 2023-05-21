package com.example.myvers.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ErrorController {
    @GetMapping("/error/{errorCode}")
    public String error(@PathVariable String errorCode) {
        return "error/" + errorCode;
    }
}
