package com.yuzu.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yuzu.service.SecurityService;
import com.yuzu.entities.User;
import com.yuzu.repositories.UserRepository;
import com.yuzu.security.UserRegistrationDto;
import com.yuzu.service.UserService;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private SecurityService securityService;

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        return "registration";
    }

    
    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") @Valid UserRegistrationDto userDto, 
                                      BindingResult result){

        User existing = userService.findByUsername(userDto.getUsername());
        if (existing != null){
            result.rejectValue("username", null, "There is already an account registered with that name");
        }

        if (result.hasErrors()){
            return "registration";
        }

        userService.save(userDto);
       
        
        securityService.autoLogin(userDto.getUsername(), userDto.getConfirmPassword());

        return "redirect:/?success";
        
        
        
    }
}
