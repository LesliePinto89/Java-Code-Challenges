package com.yuzu.controllers;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuzu.entities.Task;
import com.yuzu.entities.User;
import com.yuzu.repositories.TaskRepository;
import com.yuzu.repositories.UserRepository;
import com.yuzu.security.UserRegistrationDto;

@Controller
public class TaskController {

    @Autowired
    TaskRepository taskRepository;

    @GetMapping("/")
    public String showPage(Model model, @AuthenticationPrincipal UserDetails currentUser, @RequestParam(defaultValue = "0") int page) {
        model.addAttribute("data",
        		taskRepository.findAll(PageRequest.of(page, 4)));
        model.addAttribute("currentPage", page);
        
        if(currentUser !=null){
        //Can change to first name if desired rather than username
        User user = (User) userR.findByUsername(currentUser.getUsername());
        model.addAttribute("currentUser", user);}
        
        return "index";
    }

    
    @Autowired
    private UserRepository userR;
    
  
    
    
    
    @PostMapping("/save")
    public String save(Task task) {
    	taskRepository.save(task);
        return "redirect:/";
    }

    @GetMapping("/delete")
    public String deleteTask(long id) {
    	taskRepository.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/findOne")
    @ResponseBody
    public Task findOne(long id) {
        return taskRepository.findById(id).get();
    }
}
