package com.intesoft.syncworks.interfaces.web;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class WelcomeController {

    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code) {

            return "Welcome to SyncWorks";


    }
}
