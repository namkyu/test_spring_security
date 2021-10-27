package com.example.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
public class TestController {

    @GetMapping("/denied")
    public String denied(@RequestParam String exception) {
        return exception;
    }

    @GetMapping("/admin/{name}")
    public String admin(@PathVariable String name) {
        return name;
    }

    @GetMapping("/user/{name}")
    public String user(@PathVariable String name) {
        return name;
    }

}
