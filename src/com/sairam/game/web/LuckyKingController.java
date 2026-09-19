package com.sairam.game.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LuckyKingController {

@GetMapping("/api/status")
public String status() {
    return "Lucky King Web Application is running!";
}


}
