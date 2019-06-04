package com.yuzu.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
	

	    @GetMapping("/login")
	    public String login(Model model) {
	        return "login";
	    }

	    @GetMapping("/user")
	    public String userIndex() {
	        return "user/index";
	    }

     
}
